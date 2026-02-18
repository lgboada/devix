import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoCliente } from '../tipo-cliente.model';
import { TipoClienteService } from '../service/tipo-cliente.service';
import { TipoClienteFormGroup, TipoClienteFormService } from './tipo-cliente-form.service';

@Component({
  selector: 'jhi-tipo-cliente-update',
  templateUrl: './tipo-cliente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TipoClienteUpdateComponent implements OnInit {
  isSaving = false;
  tipoCliente: ITipoCliente | null = null;

  protected tipoClienteService = inject(TipoClienteService);
  protected tipoClienteFormService = inject(TipoClienteFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TipoClienteFormGroup = this.tipoClienteFormService.createTipoClienteFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoCliente }) => {
      this.tipoCliente = tipoCliente;
      if (tipoCliente) {
        this.updateForm(tipoCliente);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoCliente = this.tipoClienteFormService.getTipoCliente(this.editForm);
    if (tipoCliente.id !== null) {
      this.subscribeToSaveResponse(this.tipoClienteService.update(tipoCliente));
    } else {
      this.subscribeToSaveResponse(this.tipoClienteService.create(tipoCliente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoCliente>>): void {
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

  protected updateForm(tipoCliente: ITipoCliente): void {
    this.tipoCliente = tipoCliente;
    this.tipoClienteFormService.resetForm(this.editForm, tipoCliente);
  }
}
