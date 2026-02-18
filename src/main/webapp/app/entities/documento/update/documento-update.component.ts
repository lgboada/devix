import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IEvento } from 'app/entities/evento/evento.model';
import { EventoService } from 'app/entities/evento/service/evento.service';
import { DocumentoService } from '../service/documento.service';
import { IDocumento } from '../documento.model';
import { DocumentoFormGroup, DocumentoFormService } from './documento-form.service';

@Component({
  selector: 'jhi-documento-update',
  templateUrl: './documento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DocumentoUpdateComponent implements OnInit {
  isSaving = false;
  documento: IDocumento | null = null;

  clientesSharedCollection: ICliente[] = [];
  eventosSharedCollection: IEvento[] = [];

  protected documentoService = inject(DocumentoService);
  protected documentoFormService = inject(DocumentoFormService);
  protected clienteService = inject(ClienteService);
  protected eventoService = inject(EventoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DocumentoFormGroup = this.documentoFormService.createDocumentoFormGroup();

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  compareEvento = (o1: IEvento | null, o2: IEvento | null): boolean => this.eventoService.compareEvento(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documento }) => {
      this.documento = documento;
      if (documento) {
        this.updateForm(documento);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documento = this.documentoFormService.getDocumento(this.editForm);
    if (documento.id !== null) {
      this.subscribeToSaveResponse(this.documentoService.update(documento));
    } else {
      this.subscribeToSaveResponse(this.documentoService.create(documento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumento>>): void {
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

  protected updateForm(documento: IDocumento): void {
    this.documento = documento;
    this.documentoFormService.resetForm(this.editForm, documento);

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      documento.cliente,
    );
    this.eventosSharedCollection = this.eventoService.addEventoToCollectionIfMissing<IEvento>(
      this.eventosSharedCollection,
      documento.evento,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.documento?.cliente)))
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.eventoService
      .query()
      .pipe(map((res: HttpResponse<IEvento[]>) => res.body ?? []))
      .pipe(map((eventos: IEvento[]) => this.eventoService.addEventoToCollectionIfMissing<IEvento>(eventos, this.documento?.evento)))
      .subscribe((eventos: IEvento[]) => (this.eventosSharedCollection = eventos));
  }
}
