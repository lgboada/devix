import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoProducto } from '../tipo-producto.model';
import { TipoProductoService } from '../service/tipo-producto.service';
import { TipoProductoFormGroup, TipoProductoFormService } from './tipo-producto-form.service';

@Component({
  selector: 'jhi-tipo-producto-update',
  templateUrl: './tipo-producto-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TipoProductoUpdateComponent implements OnInit {
  isSaving = false;
  tipoProducto: ITipoProducto | null = null;

  protected tipoProductoService = inject(TipoProductoService);
  protected tipoProductoFormService = inject(TipoProductoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TipoProductoFormGroup = this.tipoProductoFormService.createTipoProductoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoProducto }) => {
      this.tipoProducto = tipoProducto;
      if (tipoProducto) {
        this.updateForm(tipoProducto);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoProducto = this.tipoProductoFormService.getTipoProducto(this.editForm);
    if (tipoProducto.id !== null) {
      this.subscribeToSaveResponse(this.tipoProductoService.update(tipoProducto));
    } else {
      this.subscribeToSaveResponse(this.tipoProductoService.create(tipoProducto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoProducto>>): void {
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

  protected updateForm(tipoProducto: ITipoProducto): void {
    this.tipoProducto = tipoProducto;
    this.tipoProductoFormService.resetForm(this.editForm, tipoProducto);
  }
}
