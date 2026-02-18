import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoDireccion } from '../tipo-direccion.model';
import { TipoDireccionService } from '../service/tipo-direccion.service';
import { TipoDireccionFormGroup, TipoDireccionFormService } from './tipo-direccion-form.service';

@Component({
  selector: 'jhi-tipo-direccion-update',
  templateUrl: './tipo-direccion-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TipoDireccionUpdateComponent implements OnInit {
  isSaving = false;
  tipoDireccion: ITipoDireccion | null = null;

  protected tipoDireccionService = inject(TipoDireccionService);
  protected tipoDireccionFormService = inject(TipoDireccionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TipoDireccionFormGroup = this.tipoDireccionFormService.createTipoDireccionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoDireccion }) => {
      this.tipoDireccion = tipoDireccion;
      if (tipoDireccion) {
        this.updateForm(tipoDireccion);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoDireccion = this.tipoDireccionFormService.getTipoDireccion(this.editForm);
    if (tipoDireccion.id !== null) {
      this.subscribeToSaveResponse(this.tipoDireccionService.update(tipoDireccion));
    } else {
      this.subscribeToSaveResponse(this.tipoDireccionService.create(tipoDireccion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoDireccion>>): void {
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

  protected updateForm(tipoDireccion: ITipoDireccion): void {
    this.tipoDireccion = tipoDireccion;
    this.tipoDireccionFormService.resetForm(this.editForm, tipoDireccion);
  }
}
