import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoCliente } from 'app/entities/tipo-cliente/tipo-cliente.model';
import { TipoClienteService } from 'app/entities/tipo-cliente/service/tipo-cliente.service';
import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { CiudadService } from 'app/entities/ciudad/service/ciudad.service';
import { ClienteService } from '../service/cliente.service';
import { ICliente } from '../cliente.model';
import { ClienteFormGroup, ClienteFormService } from './cliente-form.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;
  cliente: ICliente | null = null;

  tipoClientesSharedCollection: ITipoCliente[] = [];
  ciudadsSharedCollection: ICiudad[] = [];

  protected clienteService = inject(ClienteService);
  protected clienteFormService = inject(ClienteFormService);
  protected tipoClienteService = inject(TipoClienteService);
  protected ciudadService = inject(CiudadService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ClienteFormGroup = this.clienteFormService.createClienteFormGroup();

  compareTipoCliente = (o1: ITipoCliente | null, o2: ITipoCliente | null): boolean => this.tipoClienteService.compareTipoCliente(o1, o2);

  compareCiudad = (o1: ICiudad | null, o2: ICiudad | null): boolean => this.ciudadService.compareCiudad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.cliente = cliente;
      if (cliente) {
        this.updateForm(cliente);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.clienteFormService.getCliente(this.editForm);
    if (cliente.id !== null) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.cliente = cliente;
    this.clienteFormService.resetForm(this.editForm, cliente);

    this.tipoClientesSharedCollection = this.tipoClienteService.addTipoClienteToCollectionIfMissing<ITipoCliente>(
      this.tipoClientesSharedCollection,
      cliente.tipoCliente,
    );
    this.ciudadsSharedCollection = this.ciudadService.addCiudadToCollectionIfMissing<ICiudad>(this.ciudadsSharedCollection, cliente.ciudad);
  }

  protected loadRelationshipsOptions(): void {
    this.tipoClienteService
      .query()
      .pipe(map((res: HttpResponse<ITipoCliente[]>) => res.body ?? []))
      .pipe(
        map((tipoClientes: ITipoCliente[]) =>
          this.tipoClienteService.addTipoClienteToCollectionIfMissing<ITipoCliente>(tipoClientes, this.cliente?.tipoCliente),
        ),
      )
      .subscribe((tipoClientes: ITipoCliente[]) => (this.tipoClientesSharedCollection = tipoClientes));

    this.ciudadService
      .query()
      .pipe(map((res: HttpResponse<ICiudad[]>) => res.body ?? []))
      .pipe(map((ciudads: ICiudad[]) => this.ciudadService.addCiudadToCollectionIfMissing<ICiudad>(ciudads, this.cliente?.ciudad)))
      .subscribe((ciudads: ICiudad[]) => (this.ciudadsSharedCollection = ciudads));
  }
}
