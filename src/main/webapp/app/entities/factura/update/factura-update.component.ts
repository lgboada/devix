import { Component, OnInit, effect, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable, forkJoin } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICentro } from 'app/entities/centro/centro.model';
import { CentroService } from 'app/entities/centro/service/centro.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IProducto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { IDetalleFactura, NewDetalleFactura } from 'app/entities/detalle-factura/detalle-factura.model';
import { DetalleFacturaService } from 'app/entities/detalle-factura/service/detalle-factura.service';
import { DireccionService } from 'app/entities/direccion/service/direccion.service';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import { ILineaNegocio } from 'app/entities/linea-negocio/linea-negocio.model';
import { LineaNegocioService } from 'app/entities/linea-negocio/service/linea-negocio.service';
import { ITipoDocumento } from 'app/entities/tipo-documento/tipo-documento.model';
import { TipoDocumentoService } from 'app/entities/tipo-documento/service/tipo-documento.service';
import { FacturaService } from '../service/factura.service';
import { IFactura, NewFactura } from '../factura.model';
import { FacturaFormGroup, FacturaFormService } from './factura-form.service';

interface DetalleItem {
  id?: number;
  producto: IProducto | null;
  cantidad: number;
  precioUnitario: number;
  descuento: number;
  conIva: boolean;
  subtotal: number;
  impuesto: number;
  total: number;
}

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
  productosCollection: IProducto[] = [];
  lineaNegociosCollection: ILineaNegocio[] = [];
  tipoDocumentosCollection: ITipoDocumento[] = [];

  detalles: DetalleItem[] = [];
  detallesEliminados: number[] = [];
  clienteCompleto: ICliente | null = null;

  protected facturaService = inject(FacturaService);
  protected facturaFormService = inject(FacturaFormService);
  protected centroService = inject(CentroService);
  protected clienteService = inject(ClienteService);
  protected productoService = inject(ProductoService);
  protected detalleFacturaService = inject(DetalleFacturaService);
  protected direccionService = inject(DireccionService);
  protected activeCompanyService = inject(ActiveCompanyService);
  protected lineaNegocioService = inject(LineaNegocioService);
  protected tipoDocumentoService = inject(TipoDocumentoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FacturaFormGroup = this.facturaFormService.createFacturaFormGroup();

  private readonly syncNoCiaFromSession = effect(() => {
    const sessionNoCia = this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
    if (sessionNoCia === null) return;
    const noCiaControl = this.editForm.get('noCia');
    if (noCiaControl?.value === null || noCiaControl?.value === undefined || noCiaControl?.value === 0) {
      noCiaControl?.setValue(sessionNoCia);
    }
  });

  compareCentro = (o1: ICentro | null, o2: ICentro | null): boolean => this.centroService.compareCentro(o1, o2);
  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  // --- Getters de totales calculados ---

  get porcentajeImpuesto(): number {
    return this.editForm.get('porcentajeImpuesto')?.value ?? 0;
  }

  get subtotalFactura(): number {
    return this.detalles.reduce((acc, d) => acc + d.subtotal, 0);
  }

  get descuentoFactura(): number {
    return this.detalles.reduce((acc, d) => acc + d.descuento, 0);
  }

  get netoFactura(): number {
    return this.subtotalFactura - this.descuentoFactura;
  }

  get impuestoFactura(): number {
    return this.detalles.reduce((acc, d) => acc + d.impuesto, 0);
  }

  get impuestoCeroFactura(): number {
    return this.detalles.filter(d => !d.conIva).reduce((acc, d) => acc + d.subtotal, 0);
  }

  get totalFactura(): number {
    return this.netoFactura + this.impuestoFactura;
  }

  // --- Helpers ---

  clienteNombre(c: ICliente): string {
    if (c.nombreComercial) return c.nombreComercial;
    return `${c.nombres ?? ''} ${c.apellidos ?? ''}`.trim();
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      this.factura = factura;
      if (factura) {
        this.updateForm(factura);
        this.cargarDetallesExistentes(factura.id);
      } else {
        // Defaults para nueva factura
        this.editForm.patchValue({ estado: 'PROFORMA', porcentajeImpuesto: 15 });
        this.agregarDetalle();
      }
      this.loadRelationshipsOptions();
    });
  }

  // --- Gestión de líneas de detalle ---

  agregarDetalle(): void {
    this.detalles.push({
      producto: null,
      cantidad: 1,
      precioUnitario: 0,
      descuento: 0,
      conIva: true,
      subtotal: 0,
      impuesto: 0,
      total: 0,
    });
  }

  agregarDetalleEn(index: number): void {
    this.detalles.splice(index + 1, 0, {
      producto: null,
      cantidad: 1,
      precioUnitario: 0,
      descuento: 0,
      conIva: true,
      subtotal: 0,
      impuesto: 0,
      total: 0,
    });
  }

  eliminarDetalle(index: number): void {
    const d = this.detalles[index];
    if (d.id) {
      this.detallesEliminados.push(d.id);
    }
    this.detalles.splice(index, 1);
  }

  onProductoChange(index: number): void {
    const d = this.detalles[index];
    d.precioUnitario = d.producto?.precio ?? 0;
    this.recalcularDetalle(index);
  }

  recalcularDetalle(index: number): void {
    const d = this.detalles[index];
    d.subtotal = d.cantidad * d.precioUnitario;
    d.impuesto = d.conIva ? d.subtotal * (this.porcentajeImpuesto / 100) : 0;
    d.total = d.subtotal + d.impuesto - d.descuento;
  }

  recalcularTodos(): void {
    this.detalles.forEach((_, i) => this.recalcularDetalle(i));
  }

  // --- Auto-fill del cliente ---

  onClienteChange(): void {
    const clienteControl = this.editForm.get('cliente')?.value as ICliente | null;
    if (!clienteControl) {
      this.clienteCompleto = null;
      return;
    }

    const found = this.clientesSharedCollection.find(c => c.id === clienteControl.id) ?? null;
    this.clienteCompleto = found;

    if (found) {
      this.editForm.patchValue({
        cedula: found.dni ?? '',
        email: found.email ?? '',
        telefono: found.telefono1 ?? '',
      });

      this.direccionService
        .query({ 'clienteId.equals': found.id, sort: ['id,asc'] })
        .pipe(map((res: HttpResponse<any[]>) => res.body ?? []))
        .subscribe((dirs: any[]) => {
          const dir = dirs[0];
          if (dir?.descripcion) {
            this.editForm.patchValue({ direccion: dir.descripcion });
          }
        });
    }
  }

  // --- Guardar ---

  previousState(): void {
    window.history.back();
  }

  save(): void {
    if (this.detalles.length === 0) return;
    this.isSaving = true;

    this.editForm.patchValue({
      subtotal: this.subtotalFactura,
      impuesto: this.impuestoFactura,
      impuestoCero: this.impuestoCeroFactura,
      descuento: this.descuentoFactura,
      total: this.totalFactura,
    });

    const factura = this.facturaFormService.getFactura(this.editForm);
    const obs: Observable<HttpResponse<IFactura>> = factura.id
      ? this.facturaService.update(factura as IFactura)
      : this.facturaService.create(factura as unknown as NewFactura);

    obs.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: (res: HttpResponse<IFactura>) => this.guardarDetalles(res.body!.id),
      error: () => this.onSaveError(),
    });
  }

  private guardarDetalles(facturaId: number): void {
    const noCia = this.editForm.get('noCia')?.value;

    const creates = this.detalles
      .filter(d => !d.id)
      .map(d => this.detalleFacturaService.create(this.toNewDetalleFactura(d, facturaId, noCia)));

    const updates = this.detalles
      .filter(d => !!d.id)
      .map(d => this.detalleFacturaService.update(this.toExistingDetalleFactura(d, facturaId, noCia)));

    const deletes = this.detallesEliminados.map(id => this.detalleFacturaService.delete(id));

    const all = [...creates, ...updates, ...deletes];
    if (all.length === 0) {
      this.onSaveSuccess();
      return;
    }

    forkJoin(all).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  private toNewDetalleFactura(d: DetalleItem, facturaId: number, noCia: any): NewDetalleFactura {
    return {
      id: null,
      noCia,
      cantidad: d.cantidad,
      precioUnitario: d.precioUnitario,
      subtotal: d.subtotal,
      descuento: d.descuento,
      impuesto: d.impuesto,
      total: d.total,
      factura: { id: facturaId },
      producto: d.producto ? { id: d.producto.id } : null,
    };
  }

  private toExistingDetalleFactura(d: DetalleItem, facturaId: number, noCia: any): IDetalleFactura {
    return {
      id: d.id!,
      noCia,
      cantidad: d.cantidad,
      precioUnitario: d.precioUnitario,
      subtotal: d.subtotal,
      descuento: d.descuento,
      impuesto: d.impuesto,
      total: d.total,
      factura: { id: facturaId },
      producto: d.producto ? { id: d.producto.id } : null,
    };
  }

  private cargarDetallesExistentes(facturaId: number): void {
    this.detalleFacturaService
      .query({ 'facturaId.equals': facturaId })
      .pipe(map((res: HttpResponse<IDetalleFactura[]>) => res.body ?? []))
      .subscribe((detalles: IDetalleFactura[]) => {
        this.detalles = detalles.map(
          d =>
            ({
              id: d.id,
              producto: null, // se resuelve tras cargar productos
              productoId: d.producto?.id,
              cantidad: d.cantidad ?? 1,
              precioUnitario: d.precioUnitario ?? 0,
              descuento: d.descuento ?? 0,
              conIva: (d.impuesto ?? 0) > 0,
              subtotal: d.subtotal ?? 0,
              impuesto: d.impuesto ?? 0,
              total: d.total ?? 0,
            }) as DetalleItem & { productoId?: number },
        );

        // Enlazar productos una vez cargados
        this.resolverProductosDetalles();
      });
  }

  private resolverProductosDetalles(): void {
    this.detalles.forEach(d => {
      const pId = (d as any).productoId;
      if (pId) {
        d.producto = this.productosCollection.find(p => p.id === pId) ?? null;
      }
    });
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

    this.productoService
      .query()
      .pipe(map((res: HttpResponse<IProducto[]>) => res.body ?? []))
      .subscribe((productos: IProducto[]) => {
        this.productosCollection = productos;
        // Si ya cargaron detalles, resolver los productos
        this.resolverProductosDetalles();
      });

    const noCia = this.activeCompanyService.trackActiveCompany()()?.noCia;
    if (noCia) {
      this.lineaNegocioService
        .query({ noCia })
        .pipe(map((res: HttpResponse<ILineaNegocio[]>) => res.body ?? []))
        .subscribe((items: ILineaNegocio[]) => (this.lineaNegociosCollection = items));

      this.tipoDocumentoService
        .query({ noCia })
        .pipe(map((res: HttpResponse<ITipoDocumento[]>) => res.body ?? []))
        .subscribe((items: ITipoDocumento[]) => (this.tipoDocumentosCollection = items));
    }
  }
}
