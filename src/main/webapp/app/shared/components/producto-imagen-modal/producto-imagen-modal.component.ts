import { Component, Input, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { FileService } from 'app/shared/service/file.service';
import { IProductoImagen } from 'app/entities/producto-imagen/producto-imagen.model';

export interface IProductoImagenModalResult {
  id: number | null;
  pathImagen: string;
  orden: number;
  principal: boolean;
  visible: boolean;
}

@Component({
  selector: 'jhi-producto-imagen-modal',
  templateUrl: './producto-imagen-modal.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProductoImagenModalComponent implements OnInit {
  @Input() imagen: IProductoImagen | null = null;

  pathImagen = '';
  orden = 0;
  principal = false;
  visible = true;
  isUploading = false;
  errorMsg: string | null = null;

  protected readonly activeModal = inject(NgbActiveModal);
  protected readonly fileService = inject(FileService);

  ngOnInit(): void {
    if (!this.imagen) {
      return;
    }
    this.pathImagen = this.imagen.pathImagen ?? '';
    this.orden = this.imagen.orden ?? 0;
    this.principal = this.imagen.principal ?? false;
    this.visible = this.imagen.visible ?? true;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const selectedFile = input.files?.[0];
    if (!selectedFile) {
      return;
    }

    this.errorMsg = null;
    this.isUploading = true;
    this.fileService
      .upload(selectedFile)
      .pipe(finalize(() => (this.isUploading = false)))
      .subscribe({
        next: response => {
          const storedFilename = response.body?.filename;
          if (!storedFilename) {
            this.errorMsg = 'No se pudo obtener el nombre del archivo subido.';
            return;
          }
          this.pathImagen = storedFilename;
        },
        error: () => {
          this.errorMsg = 'No se pudo subir la imagen. Intenta nuevamente.';
        },
      });
  }

  getCurrentImageUrl(): string | null {
    return this.pathImagen ? this.fileService.getFileUrl(this.pathImagen) : null;
  }

  guardar(): void {
    this.errorMsg = null;
    if (!this.pathImagen.trim()) {
      this.errorMsg = 'La imagen es obligatoria.';
      return;
    }
    const result: IProductoImagenModalResult = {
      id: this.imagen?.id ?? null,
      pathImagen: this.pathImagen.trim(),
      orden: Number.isFinite(this.orden) ? this.orden : 0,
      principal: this.principal,
      visible: this.visible,
    };
    this.activeModal.close(result);
  }

  cancelar(): void {
    this.activeModal.dismiss('cancel');
  }
}
