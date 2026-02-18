import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICentro } from 'app/entities/centro/centro.model';
import { CentroService } from 'app/entities/centro/service/centro.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { FacturaService } from '../service/factura.service';
import { IFactura } from '../factura.model';
import { FacturaFormGroup, FacturaFormService } from './factura-form.service';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FacturaUpdateComponent implements OnInit {
  isSaving = false;
  factura: IFactura | null = null;

  centrosSharedCollection: ICentro[] = [];
  clientesSharedCollection: ICliente[] = [];

  protected facturaService = inject(FacturaService);
  protected facturaFormService = inject(FacturaFormService);
  protected centroService = inject(CentroService);
  protected clienteService = inject(ClienteService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacturaFormGroup = this.facturaFormService.createFacturaFormGroup();

  compareCentro = (o1: ICentro | null, o2: ICentro | null): boolean => this.centroService.compareCentro(o1, o2);

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      this.factura = factura;
      if (factura) {
        this.updateForm(factura);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factura = this.facturaFormService.getFactura(this.editForm);
    if (factura.id !== null) {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    } else {
      this.subscribeToSaveResponse(this.facturaService.create(factura));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>): void {
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

  protected updateForm(factura: IFactura): void {
    this.factura = factura;
    this.facturaFormService.resetForm(this.editForm, factura);

    this.centrosSharedCollection = this.centroService.addCentroToCollectionIfMissing<ICentro>(this.centrosSharedCollection, factura.centro);
    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      factura.cliente,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.centroService
      .query()
      .pipe(map((res: HttpResponse<ICentro[]>) => res.body ?? []))
      .pipe(map((centros: ICentro[]) => this.centroService.addCentroToCollectionIfMissing<ICentro>(centros, this.factura?.centro)))
      .subscribe((centros: ICentro[]) => (this.centrosSharedCollection = centros));

    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.factura?.cliente)))
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
}
