import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FileService } from 'app/shared/service/file.service';

import { IProveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';
import { ProveedorFormGroup, ProveedorFormService } from './proveedor-form.service';

@Component({
  selector: 'jhi-proveedor-update',
  templateUrl: './proveedor-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProveedorUpdateComponent implements OnInit {
  isSaving = false;
  isUploading = false;
  proveedor: IProveedor | null = null;
  uploadError: string | null = null;

  protected proveedorService = inject(ProveedorService);
  protected proveedorFormService = inject(ProveedorFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected fileService = inject(FileService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProveedorFormGroup = this.proveedorFormService.createProveedorFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proveedor }) => {
      this.proveedor = proveedor;
      if (proveedor) {
        this.updateForm(proveedor);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proveedor = this.proveedorFormService.getProveedor(this.editForm);
    if (proveedor.id !== null) {
      this.subscribeToSaveResponse(this.proveedorService.update(proveedor));
    } else {
      this.subscribeToSaveResponse(this.proveedorService.create(proveedor));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProveedor>>): void {
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

  protected updateForm(proveedor: IProveedor): void {
    this.proveedor = proveedor;
    this.proveedorFormService.resetForm(this.editForm, proveedor);
  }
}
