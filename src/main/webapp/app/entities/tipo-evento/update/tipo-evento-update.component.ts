import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoEvento } from '../tipo-evento.model';
import { TipoEventoService } from '../service/tipo-evento.service';
import { TipoEventoFormGroup, TipoEventoFormService } from './tipo-evento-form.service';

@Component({
  selector: 'jhi-tipo-evento-update',
  templateUrl: './tipo-evento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TipoEventoUpdateComponent implements OnInit {
  isSaving = false;
  tipoEvento: ITipoEvento | null = null;

  protected tipoEventoService = inject(TipoEventoService);
  protected tipoEventoFormService = inject(TipoEventoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TipoEventoFormGroup = this.tipoEventoFormService.createTipoEventoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoEvento }) => {
      this.tipoEvento = tipoEvento;
      if (tipoEvento) {
        this.updateForm(tipoEvento);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoEvento = this.tipoEventoFormService.getTipoEvento(this.editForm);
    if (tipoEvento.id !== null) {
      this.subscribeToSaveResponse(this.tipoEventoService.update(tipoEvento));
    } else {
      this.subscribeToSaveResponse(this.tipoEventoService.create(tipoEvento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoEvento>>): void {
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

  protected updateForm(tipoEvento: ITipoEvento): void {
    this.tipoEvento = tipoEvento;
    this.tipoEventoFormService.resetForm(this.editForm, tipoEvento);
  }
}
