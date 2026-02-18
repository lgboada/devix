import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoDireccion } from 'app/entities/tipo-direccion/tipo-direccion.model';
import { TipoDireccionService } from 'app/entities/tipo-direccion/service/tipo-direccion.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { DireccionService } from '../service/direccion.service';
import { IDireccion } from '../direccion.model';
import { DireccionFormGroup, DireccionFormService } from './direccion-form.service';

@Component({
  selector: 'jhi-direccion-update',
  templateUrl: './direccion-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DireccionUpdateComponent implements OnInit {
  isSaving = false;
  direccion: IDireccion | null = null;

  tipoDireccionsSharedCollection: ITipoDireccion[] = [];
  clientesSharedCollection: ICliente[] = [];

  protected direccionService = inject(DireccionService);
  protected direccionFormService = inject(DireccionFormService);
  protected tipoDireccionService = inject(TipoDireccionService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DireccionFormGroup = this.direccionFormService.createDireccionFormGroup();

  compareTipoDireccion = (o1: ITipoDireccion | null, o2: ITipoDireccion | null): boolean =>
    this.tipoDireccionService.compareTipoDireccion(o1, o2);

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ direccion }) => {
      this.direccion = direccion;
      if (direccion) {
        this.updateForm(direccion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const direccion = this.direccionFormService.getDireccion(this.editForm);
    if (direccion.id !== null) {
      this.subscribeToSaveResponse(this.direccionService.update(direccion));
    } else {
      this.subscribeToSaveResponse(this.direccionService.create(direccion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDireccion>>): void {
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

  protected updateForm(direccion: IDireccion): void {
    this.direccion = direccion;
    this.direccionFormService.resetForm(this.editForm, direccion);

    this.tipoDireccionsSharedCollection = this.tipoDireccionService.addTipoDireccionToCollectionIfMissing<ITipoDireccion>(
      this.tipoDireccionsSharedCollection,
      direccion.tipoDireccion,
    );
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      direccion.cliente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tipoDireccionService
      .query()
      .pipe(map((res: HttpResponse<ITipoDireccion[]>) => res.body ?? []))
      .pipe(
        map((tipoDireccions: ITipoDireccion[]) =>
          this.tipoDireccionService.addTipoDireccionToCollectionIfMissing<ITipoDireccion>(tipoDireccions, this.direccion?.tipoDireccion),
        ),
      )
      .subscribe((tipoDireccions: ITipoDireccion[]) => (this.tipoDireccionsSharedCollection = tipoDireccions));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.direccion?.cliente)))
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
}
