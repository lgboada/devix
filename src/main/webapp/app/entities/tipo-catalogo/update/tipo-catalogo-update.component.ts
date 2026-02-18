import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoCatalogo } from '../tipo-catalogo.model';
import { TipoCatalogoService } from '../service/tipo-catalogo.service';
import { TipoCatalogoFormGroup, TipoCatalogoFormService } from './tipo-catalogo-form.service';

@Component({
  selector: 'jhi-tipo-catalogo-update',
  templateUrl: './tipo-catalogo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TipoCatalogoUpdateComponent implements OnInit {
  isSaving = false;
  tipoCatalogo: ITipoCatalogo | null = null;

  protected tipoCatalogoService = inject(TipoCatalogoService);
  protected tipoCatalogoFormService = inject(TipoCatalogoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TipoCatalogoFormGroup = this.tipoCatalogoFormService.createTipoCatalogoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoCatalogo }) => {
      this.tipoCatalogo = tipoCatalogo;
      if (tipoCatalogo) {
        this.updateForm(tipoCatalogo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoCatalogo = this.tipoCatalogoFormService.getTipoCatalogo(this.editForm);
    if (tipoCatalogo.id !== null) {
      this.subscribeToSaveResponse(this.tipoCatalogoService.update(tipoCatalogo));
    } else {
      this.subscribeToSaveResponse(this.tipoCatalogoService.create(tipoCatalogo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoCatalogo>>): void {
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

  protected updateForm(tipoCatalogo: ITipoCatalogo): void {
    this.tipoCatalogo = tipoCatalogo;
    this.tipoCatalogoFormService.resetForm(this.editForm, tipoCatalogo);
  }
}
