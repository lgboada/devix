import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoEvento } from 'app/entities/tipo-evento/tipo-evento.model';
import { TipoEventoService } from 'app/entities/tipo-evento/service/tipo-evento.service';
import { ICentro } from 'app/entities/centro/centro.model';
import { CentroService } from 'app/entities/centro/service/centro.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { EventoService } from '../service/evento.service';
import { IEvento } from '../evento.model';
import { EventoFormGroup, EventoFormService } from './evento-form.service';

@Component({
  selector: 'jhi-evento-update',
  templateUrl: './evento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EventoUpdateComponent implements OnInit {
  isSaving = false;
  evento: IEvento | null = null;

  tipoEventosSharedCollection: ITipoEvento[] = [];
  centrosSharedCollection: ICentro[] = [];
  clientesSharedCollection: ICliente[] = [];

  protected eventoService = inject(EventoService);
  protected eventoFormService = inject(EventoFormService);
  protected tipoEventoService = inject(TipoEventoService);
  protected centroService = inject(CentroService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EventoFormGroup = this.eventoFormService.createEventoFormGroup();

  compareTipoEvento = (o1: ITipoEvento | null, o2: ITipoEvento | null): boolean => this.tipoEventoService.compareTipoEvento(o1, o2);

  compareCentro = (o1: ICentro | null, o2: ICentro | null): boolean => this.centroService.compareCentro(o1, o2);

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evento }) => {
      this.evento = evento;
      if (evento) {
        this.updateForm(evento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evento = this.eventoFormService.getEvento(this.editForm);
    if (evento.id !== null) {
      this.subscribeToSaveResponse(this.eventoService.update(evento));
    } else {
      this.subscribeToSaveResponse(this.eventoService.create(evento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvento>>): void {
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

  protected updateForm(evento: IEvento): void {
    this.evento = evento;
    this.eventoFormService.resetForm(this.editForm, evento);

    this.tipoEventosSharedCollection = this.tipoEventoService.addTipoEventoToCollectionIfMissing<ITipoEvento>(
      this.tipoEventosSharedCollection,
      evento.tipoEvento,
    );
    this.centrosSharedCollection = this.centroService.addCentroToCollectionIfMissing<ICentro>(this.centrosSharedCollection, evento.centro);
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      evento.cliente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tipoEventoService
      .query()
      .pipe(map((res: HttpResponse<ITipoEvento[]>) => res.body ?? []))
      .pipe(
        map((tipoEventos: ITipoEvento[]) =>
          this.tipoEventoService.addTipoEventoToCollectionIfMissing<ITipoEvento>(tipoEventos, this.evento?.tipoEvento),
        ),
      )
      .subscribe((tipoEventos: ITipoEvento[]) => (this.tipoEventosSharedCollection = tipoEventos));

    this.centroService
      .query()
      .pipe(map((res: HttpResponse<ICentro[]>) => res.body ?? []))
      .pipe(map((centros: ICentro[]) => this.centroService.addCentroToCollectionIfMissing<ICentro>(centros, this.evento?.centro)))
      .subscribe((centros: ICentro[]) => (this.centrosSharedCollection = centros));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.evento?.cliente)))
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
}
