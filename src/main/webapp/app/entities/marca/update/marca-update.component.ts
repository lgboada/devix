import { Component, OnInit, effect, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FileService } from 'app/shared/service/file.service';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';

import { IMarca } from '../marca.model';
import { MarcaService } from '../service/marca.service';
import { MarcaFormGroup, MarcaFormService } from './marca-form.service';

@Component({
  selector: 'jhi-marca-update',
  templateUrl: './marca-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MarcaUpdateComponent implements OnInit {
  isSaving = false;
  isUploading = false;
  marca: IMarca | null = null;
  uploadError: string | null = null;

  protected marcaService = inject(MarcaService);
  protected marcaFormService = inject(MarcaFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected fileService = inject(FileService);
  protected activeCompanyService = inject(ActiveCompanyService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MarcaFormGroup = this.marcaFormService.createMarcaFormGroup();
  private readonly syncNoCiaFromSession = effect(
    () => {
      const sessionNoCia = this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
      if (sessionNoCia === null) {
        return;
      }
      const noCiaControl = this.editForm.get('noCia');
      const currentNoCia = noCiaControl?.value;
      if (currentNoCia === null || currentNoCia === undefined) {
        noCiaControl?.setValue(sessionNoCia);
      }
    },
    { allowSignalWrites: true },
  );

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marca }) => {
      this.marca = marca;
      if (marca) {
        this.updateForm(marca);
      }
      this.ensureNoCiaFromSession();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.ensureNoCiaFromSession();
    this.isSaving = true;
    const marca = this.marcaFormService.getMarca(this.editForm);
    if (marca.id !== null) {
      this.subscribeToSaveResponse(this.marcaService.update(marca));
    } else {
      this.subscribeToSaveResponse(this.marcaService.create(marca));
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

  private ensureNoCiaFromSession(): void {
    const noCiaControl = this.editForm.get('noCia');
    const currentNoCia = noCiaControl?.value;
    if (currentNoCia !== null && currentNoCia !== undefined) {
      return;
    }
    const sessionNoCia = this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
    if (sessionNoCia !== null) {
      noCiaControl?.setValue(sessionNoCia);
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMarca>>): void {
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

  protected updateForm(marca: IMarca): void {
    this.marca = marca;
    this.marcaFormService.resetForm(this.editForm, marca);
  }
}
