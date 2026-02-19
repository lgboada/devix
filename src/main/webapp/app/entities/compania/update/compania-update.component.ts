import { Component, OnInit, OnDestroy, inject, ViewChild, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompania } from '../compania.model';
import { CompaniaService } from '../service/compania.service';
import { CompaniaFormGroup, CompaniaFormService } from './compania-form.service';
import { FileService } from 'app/shared/service/file.service';

@Component({
  selector: 'jhi-compania-update',
  templateUrl: './compania-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CompaniaUpdateComponent implements OnInit, OnDestroy {
  isSaving = false;
  isUploading = false;
  uploadError: string | null = null;
  uploadSuccess = false;
  compania: ICompania | null = null;
  selectedFile: File | null = null; // Archivo seleccionado que se subirá al guardar
  imagePreviewUrl: string | null = null; // URL de previsualización de la imagen

  @ViewChild('fileInput', { static: false }) fileInput!: ElementRef<HTMLInputElement>;

  protected companiaService = inject(CompaniaService);
  protected companiaFormService = inject(CompaniaFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected fileService = inject(FileService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CompaniaFormGroup = this.companiaFormService.createCompaniaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compania }) => {
      this.compania = compania;
      if (compania) {
        this.updateForm(compania);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    // Si hay un archivo seleccionado, subirlo primero antes de guardar la compañía
    if (this.selectedFile) {
      this.uploadFileAndSave();
    } else {
      // Si no hay archivo, guardar directamente
      this.saveCompania();
    }
  }

  private uploadFileAndSave(): void {
    this.isSaving = true;
    this.isUploading = true;
    this.uploadError = null;

    this.fileService.upload(this.selectedFile!).subscribe({
      next: response => {
        if (response.body?.filename) {
          // Actualizar el campo pathImage con el nombre del archivo retornado
          this.editForm.patchValue({ pathImage: response.body.filename });
          // Ahora guardar la compañía con el pathImage actualizado
          this.saveCompania();
        } else if (response.body?.error) {
          this.isSaving = false;
          this.isUploading = false;
          this.uploadError = response.body.error;
        } else {
          this.isSaving = false;
          this.isUploading = false;
          this.uploadError = 'No se recibió el nombre del archivo del servidor';
        }
      },
      error: err => {
        this.isSaving = false;
        this.isUploading = false;
        this.uploadError = err.error?.error || err.error?.detail || err.message || 'Error al subir el archivo';
        console.error('Error al subir archivo:', err);
      },
    });
  }

  private saveCompania(): void {
    this.isSaving = true;
    const compania = this.companiaFormService.getCompania(this.editForm);
    if (compania.id !== null) {
      this.subscribeToSaveResponse(this.companiaService.update(compania));
    } else {
      this.subscribeToSaveResponse(this.companiaService.create(compania));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompania>>): void {
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

  protected updateForm(compania: ICompania): void {
    this.compania = compania;
    this.companiaFormService.resetForm(this.editForm, compania);
    // Si hay una imagen guardada, intentar mostrarla
    if (compania.pathImage) {
      this.loadExistingImage(compania.pathImage);
    }
  }

  private loadExistingImage(pathImage: string): void {
    // Construir la URL para acceder a la imagen guardada desde el endpoint GET
    this.imagePreviewUrl = this.fileService.getFileUrl(pathImage);
  }

  triggerFileSelect(): void {
    this.fileInput?.nativeElement?.click();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      this.uploadError = null;
      this.uploadSuccess = false;

      // Limpiar la URL de previsualización anterior si existe
      if (this.imagePreviewUrl && this.imagePreviewUrl.startsWith('blob:')) {
        URL.revokeObjectURL(this.imagePreviewUrl);
      }

      // Crear URL de previsualización para la imagen seleccionada
      if (this.selectedFile.type.startsWith('image/')) {
        this.imagePreviewUrl = URL.createObjectURL(this.selectedFile);
      } else {
        this.imagePreviewUrl = null;
      }

      // Mostrar el nombre del archivo seleccionado en el campo pathImage (solo visual)
      // El archivo se subirá cuando se guarde la compañía
      const fileName = this.selectedFile.name;
      this.editForm.patchValue({ pathImage: fileName });
    }
  }

  ngOnDestroy(): void {
    // Limpiar la URL de previsualización para liberar memoria
    if (this.imagePreviewUrl && this.imagePreviewUrl.startsWith('blob:')) {
      URL.revokeObjectURL(this.imagePreviewUrl);
    }
  }
}
