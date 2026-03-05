import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICentro } from '../centro.model';
import { CentroService } from '../service/centro.service';
import { CentroFormGroup, CentroFormService } from './centro-form.service';

@Component({
  selector: 'jhi-centro-update',
  templateUrl: './centro-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CentroUpdateComponent implements OnInit {
  isSaving = false;
  centro: ICentro | null = null;

  protected centroService = inject(CentroService);
  protected centroFormService = inject(CentroFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CentroFormGroup = this.centroFormService.createCentroFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ centro }) => {
      this.centro = centro;
      if (centro) {
        this.updateForm(centro);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const centro = this.centroFormService.getCentro(this.editForm);
    if (centro.id !== null) {
      this.subscribeToSaveResponse(this.centroService.update(centro));
    } else {
      this.subscribeToSaveResponse(this.centroService.create(centro));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICentro>>): void {
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

  protected updateForm(centro: ICentro): void {
    this.centro = centro;
    this.centroFormService.resetForm(this.editForm, centro);
  }
}
