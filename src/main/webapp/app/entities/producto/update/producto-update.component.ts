import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FileService } from 'app/shared/service/file.service';
import { IMarca } from 'app/entities/marca/marca.model';
import { MarcaService } from 'app/entities/marca/service/marca.service';

import { IModelo } from 'app/entities/modelo/modelo.model';
import { ModeloService } from 'app/entities/modelo/service/modelo.service';
import { ITipoProducto } from 'app/entities/tipo-producto/tipo-producto.model';
import { TipoProductoService } from 'app/entities/tipo-producto/service/tipo-producto.service';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import { ProveedorSearchModalComponent } from 'app/shared/components/proveedor-search-modal/proveedor-search-modal.component';
import { IProductoImagen, NewProductoImagen } from 'app/entities/producto-imagen/producto-imagen.model';
import { ProductoImagenService } from 'app/entities/producto-imagen/service/producto-imagen.service';
import {
  IProductoImagenModalResult,
  ProductoImagenModalComponent,
} from 'app/shared/components/producto-imagen-modal/producto-imagen-modal.component';
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
  isUploading = false;
  producto: IProducto | null = null;
  uploadError: string | null = null;
  selectedMarcaId: number | null = null;
  isLoadingImagenes = false;
  imagenesProducto: IProductoImagen[] = [];

  marcasSharedCollection: IMarca[] = [];
  modelosSharedCollection: IModelo[] = [];
  tipoProductosSharedCollection: ITipoProducto[] = [];
  proveedorsSharedCollection: IProveedor[] = [];

  protected productoService = inject(ProductoService);
  protected productoFormService = inject(ProductoFormService);
  protected marcaService = inject(MarcaService);
  protected modeloService = inject(ModeloService);
  protected tipoProductoService = inject(TipoProductoService);
  protected proveedorService = inject(ProveedorService);
  protected productoImagenService = inject(ProductoImagenService);
  protected activatedRoute = inject(ActivatedRoute);
  protected fileService = inject(FileService);
  protected modalService = inject(NgbModal);

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
      this.initializeMarcaAndModeloSelection();
      this.loadImagenesProducto();
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

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const selectedFile = input.files?.[0];
    if (!selectedFile) {
      return;
    }

    this.uploadError = null;
    this.isUploading = true;

    this.fileService
      .upload(selectedFile)
      .pipe(finalize(() => (this.isUploading = false)))
      .subscribe({
        next: response => {
          const storedFilename = response.body?.filename;
          if (!storedFilename) {
            this.uploadError = 'No se pudo obtener el nombre del archivo subido.';
            return;
          }
          this.editForm.patchValue({ pathImagen: storedFilename });
          this.editForm.get('pathImagen')?.markAsDirty();
          this.editForm.get('pathImagen')?.markAsTouched();
        },
        error: () => {
          this.uploadError = 'No se pudo subir la imagen. Intenta nuevamente.';
        },
      });
  }

  getCurrentImageUrl(): string | null {
    const currentPathImagen = this.editForm.get('pathImagen')?.value;
    return currentPathImagen ? this.fileService.getFileUrl(currentPathImagen) : null;
  }

  onMarcaChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedMarcaId = value ? Number(value) : null;
    this.editForm.patchValue({ modelo: null });
    this.loadModelosByMarca(this.selectedMarcaId, null);
  }

  abrirModalProveedor(): void {
    const modalRef = this.modalService.open(ProveedorSearchModalComponent, { size: 'xl', backdrop: 'static' });
    modalRef.componentInstance.selectedProveedorId = this.editForm.get('proveedor')?.value?.id ?? null;
    modalRef.componentInstance.title = 'Buscar proveedor';

    modalRef.closed.subscribe((selectedProveedor: IProveedor | undefined) => {
      if (!selectedProveedor) {
        return;
      }
      this.editForm.patchValue({ proveedor: selectedProveedor });
      this.editForm.get('proveedor')?.markAsDirty();
      this.editForm.get('proveedor')?.markAsTouched();
      this.proveedorsSharedCollection = this.proveedorService.addProveedorToCollectionIfMissing<IProveedor>(
        this.proveedorsSharedCollection,
        selectedProveedor,
      );
    });
  }

  limpiarProveedorSeleccionado(): void {
    this.editForm.patchValue({ proveedor: null });
    this.editForm.get('proveedor')?.markAsDirty();
    this.editForm.get('proveedor')?.markAsTouched();
  }

  abrirModalImagen(imagen: IProductoImagen | null = null): void {
    if (!this.producto?.id) {
      return;
    }
    const modalRef = this.modalService.open(ProductoImagenModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.imagen = imagen;

    modalRef.closed.subscribe((result: IProductoImagenModalResult | undefined) => {
      if (!result || !this.producto?.id) {
        return;
      }

      if (result.id === null) {
        const { id: _ignoredId, ...newImagenValues } = result;
        const payload: NewProductoImagen = { id: null, ...newImagenValues, productoId: this.producto.id };
        this.productoImagenService.create(this.producto.id, payload).subscribe(() => this.loadImagenesProducto());
        return;
      }

      const payload: IProductoImagen = { ...result, productoId: this.producto.id, id: result.id };
      this.productoImagenService.update(this.producto.id, payload).subscribe(() => this.loadImagenesProducto());
    });
  }

  eliminarImagen(imagen: IProductoImagen): void {
    if (!this.producto?.id || !imagen.id) {
      return;
    }
    if (!window.confirm('Deseas eliminar esta imagen?')) {
      return;
    }
    this.productoImagenService.delete(this.producto.id, imagen.id).subscribe(() => this.loadImagenesProducto());
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
    this.marcaService
      .query()
      .pipe(map((res: HttpResponse<IMarca[]>) => res.body ?? []))
      .subscribe((marcas: IMarca[]) => (this.marcasSharedCollection = marcas));

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

  private initializeMarcaAndModeloSelection(): void {
    const selectedModelo = this.editForm.get('modelo')?.value;
    if (!selectedModelo?.id) {
      this.selectedMarcaId = null;
      this.modelosSharedCollection = [];
      return;
    }

    this.modeloService.find(selectedModelo.id).subscribe({
      next: response => {
        this.selectedMarcaId = response.body?.marca?.id ?? null;
        this.loadModelosByMarca(this.selectedMarcaId, selectedModelo);
      },
      error: () => {
        this.selectedMarcaId = null;
        this.modelosSharedCollection = this.modeloService.addModeloToCollectionIfMissing<IModelo>([], selectedModelo);
      },
    });
  }

  private loadModelosByMarca(marcaId: number | null, selectedModelo: IModelo | null): void {
    if (marcaId === null) {
      this.modelosSharedCollection = selectedModelo ? [selectedModelo] : [];
      return;
    }

    this.modeloService
      .query({ 'marcaId.equals': marcaId })
      .pipe(map((res: HttpResponse<IModelo[]>) => res.body ?? []))
      .pipe(map((modelos: IModelo[]) => this.modeloService.addModeloToCollectionIfMissing<IModelo>(modelos, selectedModelo)))
      .subscribe((modelos: IModelo[]) => (this.modelosSharedCollection = modelos));
  }

  private loadImagenesProducto(): void {
    if (!this.producto?.id) {
      this.imagenesProducto = [];
      return;
    }
    this.isLoadingImagenes = true;
    this.productoImagenService
      .queryByProducto(this.producto.id)
      .pipe(
        map((res: HttpResponse<IProductoImagen[]>) => res.body ?? []),
        finalize(() => (this.isLoadingImagenes = false)),
      )
      .subscribe({
        next: imagenes => (this.imagenesProducto = imagenes),
        error: () => (this.imagenesProducto = []),
      });
  }
}
