import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMarca } from '../marca.model';
import { MarcaService } from '../service/marca.service';
import { MarcaFormGroup, MarcaFormService } from './marca-form.service';

@Component({
  selector: 'jhi-marca-update',
  templateUrl: './marca-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MarcaUpdateComponent implements OnInit {
  isSaving = false;
  marca: IMarca | null = null;

  protected marcaService = inject(MarcaService);
  protected marcaFormService = inject(MarcaFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MarcaFormGroup = this.marcaFormService.createMarcaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marca }) => {
      this.marca = marca;
      if (marca) {
        this.updateForm(marca);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const marca = this.marcaFormService.getMarca(this.editForm);
    if (marca.id !== null) {
      this.subscribeToSaveResponse(this.marcaService.update(marca));
    } else {
      this.subscribeToSaveResponse(this.marcaService.create(marca));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMarca>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(marca: IMarca): void {
    this.marca = marca;
    this.marcaFormService.resetForm(this.editForm, marca);
  }
}
