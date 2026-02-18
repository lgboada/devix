import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompania } from '../compania.model';
import { CompaniaService } from '../service/compania.service';
import { CompaniaFormGroup, CompaniaFormService } from './compania-form.service';

@Component({
  selector: 'jhi-compania-update',
  templateUrl: './compania-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CompaniaUpdateComponent implements OnInit {
  isSaving = false;
  compania: ICompania | null = null;

  protected companiaService = inject(CompaniaService);
  protected companiaFormService = inject(CompaniaFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CompaniaFormGroup = this.companiaFormService.createCompaniaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compania }) => {
      this.compania = compania;
      if (compania) {
        this.updateForm(compania);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compania = this.companiaFormService.getCompania(this.editForm);
    if (compania.id !== null) {
      this.subscribeToSaveResponse(this.companiaService.update(compania));
    } else {
      this.subscribeToSaveResponse(this.companiaService.create(compania));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompania>>): void {
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

  protected updateForm(compania: ICompania): void {
    this.compania = compania;
    this.companiaFormService.resetForm(this.editForm, compania);
  }
}
