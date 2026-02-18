import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFactura } from 'app/entities/factura/factura.model';
import { FacturaService } from 'app/entities/factura/service/factura.service';
import { IProducto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { DetalleFacturaService } from '../service/detalle-factura.service';
import { IDetalleFactura } from '../detalle-factura.model';
import { DetalleFacturaFormGroup, DetalleFacturaFormService } from './detalle-factura-form.service';

@Component({
  selector: 'jhi-detalle-factura-update',
  templateUrl: './detalle-factura-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DetalleFacturaUpdateComponent implements OnInit {
  isSaving = false;
  detalleFactura: IDetalleFactura | null = null;

  facturasSharedCollection: IFactura[] = [];
  productosSharedCollection: IProducto[] = [];

  protected detalleFacturaService = inject(DetalleFacturaService);
  protected detalleFacturaFormService = inject(DetalleFacturaFormService);
  protected facturaService = inject(FacturaService);
  protected productoService = inject(ProductoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DetalleFacturaFormGroup = this.detalleFacturaFormService.createDetalleFacturaFormGroup();

  compareFactura = (o1: IFactura | null, o2: IFactura | null): boolean => this.facturaService.compareFactura(o1, o2);

  compareProducto = (o1: IProducto | null, o2: IProducto | null): boolean => this.productoService.compareProducto(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalleFactura }) => {
      this.detalleFactura = detalleFactura;
      if (detalleFactura) {
        this.updateForm(detalleFactura);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detalleFactura = this.detalleFacturaFormService.getDetalleFactura(this.editForm);
    if (detalleFactura.id !== null) {
      this.subscribeToSaveResponse(this.detalleFacturaService.update(detalleFactura));
    } else {
      this.subscribeToSaveResponse(this.detalleFacturaService.create(detalleFactura));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetalleFactura>>): void {
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

  protected updateForm(detalleFactura: IDetalleFactura): void {
    this.detalleFactura = detalleFactura;
    this.detalleFacturaFormService.resetForm(this.editForm, detalleFactura);

    this.facturasSharedCollection = this.facturaService.addFacturaToCollectionIfMissing<IFactura>(
      this.facturasSharedCollection,
      detalleFactura.factura,
    );
    this.productosSharedCollection = this.productoService.addProductoToCollectionIfMissing<IProducto>(
      this.productosSharedCollection,
      detalleFactura.producto,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.facturaService
      .query()
      .pipe(map((res: HttpResponse<IFactura[]>) => res.body ?? []))
      .pipe(
        map((facturas: IFactura[]) =>
          this.facturaService.addFacturaToCollectionIfMissing<IFactura>(facturas, this.detalleFactura?.factura),
        ),
      )
      .subscribe((facturas: IFactura[]) => (this.facturasSharedCollection = facturas));

    this.productoService
      .query()
      .pipe(map((res: HttpResponse<IProducto[]>) => res.body ?? []))
      .pipe(
        map((productos: IProducto[]) =>
          this.productoService.addProductoToCollectionIfMissing<IProducto>(productos, this.detalleFactura?.producto),
        ),
      )
      .subscribe((productos: IProducto[]) => (this.productosSharedCollection = productos));
  }
}
