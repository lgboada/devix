import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IModelo } from 'app/entities/modelo/modelo.model';
import { ModeloService } from 'app/entities/modelo/service/modelo.service';
import { ITipoProducto } from 'app/entities/tipo-producto/tipo-producto.model';
import { TipoProductoService } from 'app/entities/tipo-producto/service/tipo-producto.service';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import { ProductoService } from '../service/producto.service';
import { IProducto } from '../producto.model';
import { ProductoFormGroup, ProductoFormService } from './producto-form.service';

@Component({
  selector: 'jhi-producto-update',
  templateUrl: './producto-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductoUpdateComponent implements OnInit {
  isSaving = false;
  producto: IProducto | null = null;

  modelosSharedCollection: IModelo[] = [];
  tipoProductosSharedCollection: ITipoProducto[] = [];
  proveedorsSharedCollection: IProveedor[] = [];

  protected productoService = inject(ProductoService);
  protected productoFormService = inject(ProductoFormService);
  protected modeloService = inject(ModeloService);
  protected tipoProductoService = inject(TipoProductoService);
  protected proveedorService = inject(ProveedorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductoFormGroup = this.productoFormService.createProductoFormGroup();

  compareModelo = (o1: IModelo | null, o2: IModelo | null): boolean => this.modeloService.compareModelo(o1, o2);

  compareTipoProducto = (o1: ITipoProducto | null, o2: ITipoProducto | null): boolean =>
    this.tipoProductoService.compareTipoProducto(o1, o2);

  compareProveedor = (o1: IProveedor | null, o2: IProveedor | null): boolean => this.proveedorService.compareProveedor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.producto = producto;
      if (producto) {
        this.updateForm(producto);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const producto = this.productoFormService.getProducto(this.editForm);
    if (producto.id !== null) {
      this.subscribeToSaveResponse(this.productoService.update(producto));
    } else {
      this.subscribeToSaveResponse(this.productoService.create(producto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProducto>>): void {
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

  protected updateForm(producto: IProducto): void {
    this.producto = producto;
    this.productoFormService.resetForm(this.editForm, producto);

    this.modelosSharedCollection = this.modeloService.addModeloToCollectionIfMissing<IModelo>(
      this.modelosSharedCollection,
      producto.modelo,
    );
    this.tipoProductosSharedCollection = this.tipoProductoService.addTipoProductoToCollectionIfMissing<ITipoProducto>(
      this.tipoProductosSharedCollection,
      producto.tipoProducto,
    );
    this.proveedorsSharedCollection = this.proveedorService.addProveedorToCollectionIfMissing<IProveedor>(
      this.proveedorsSharedCollection,
      producto.proveedor,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.modeloService
      .query()
      .pipe(map((res: HttpResponse<IModelo[]>) => res.body ?? []))
      .pipe(map((modelos: IModelo[]) => this.modeloService.addModeloToCollectionIfMissing<IModelo>(modelos, this.producto?.modelo)))
      .subscribe((modelos: IModelo[]) => (this.modelosSharedCollection = modelos));

    this.tipoProductoService
      .query()
      .pipe(map((res: HttpResponse<ITipoProducto[]>) => res.body ?? []))
      .pipe(
        map((tipoProductos: ITipoProducto[]) =>
          this.tipoProductoService.addTipoProductoToCollectionIfMissing<ITipoProducto>(tipoProductos, this.producto?.tipoProducto),
        ),
      )
      .subscribe((tipoProductos: ITipoProducto[]) => (this.tipoProductosSharedCollection = tipoProductos));

    this.proveedorService
      .query()
      .pipe(map((res: HttpResponse<IProveedor[]>) => res.body ?? []))
      .pipe(
        map((proveedors: IProveedor[]) =>
          this.proveedorService.addProveedorToCollectionIfMissing<IProveedor>(proveedors, this.producto?.proveedor),
        ),
      )
      .subscribe((proveedors: IProveedor[]) => (this.proveedorsSharedCollection = proveedors));
  }
}
