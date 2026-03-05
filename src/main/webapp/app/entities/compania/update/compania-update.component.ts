import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ActiveCompanyService } from 'app/core/auth/active-company.service';
import SharedModule from 'app/shared/shared.module';
import { FileService } from 'app/shared/service/file.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompania } from '../compania.model';
import { CompaniaService } from '../service/compania.service';
import { CompaniaFormGroup, CompaniaFormService } from './compania-form.service';

@Component({
  selector: 'jhi-compania-update',
  templateUrl: './compania-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CompaniaUpdateComponent implements OnInit {
  isSaving = false;
  isUploading = false;
  compania: ICompania | null = null;
  uploadError: string | null = null;

  protected companiaService = inject(CompaniaService);
  protected companiaFormService = inject(CompaniaFormService);
  protected activeCompanyService = inject(ActiveCompanyService);
  protected fileService = inject(FileService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CompaniaFormGroup = this.companiaFormService.createCompaniaFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compania }) => {
      this.compania = compania;
      if (compania) {
        this.updateForm(compania);
      } else {
        const activeCompanyNoCia = this.getActiveCompanyNoCia();
        if (activeCompanyNoCia !== null) {
          this.editForm.patchValue({ noCia: activeCompanyNoCia });
        }
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compania = this.companiaFormService.getCompania(this.editForm);
    const activeCompanyNoCia = this.getActiveCompanyNoCia();
    if (activeCompanyNoCia !== null) {
      compania.noCia = activeCompanyNoCia;
    }
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
          this.editForm.patchValue({ pathImage: storedFilename });
          this.editForm.get('pathImage')?.markAsDirty();
          this.editForm.get('pathImage')?.markAsTouched();
        },
        error: () => {
          this.uploadError = 'No se pudo subir la imagen. Intenta nuevamente.';
        },
      });
  }

  getCurrentImageUrl(): string | null {
    const currentPathImage = this.editForm.get('pathImage')?.value;
    return currentPathImage ? this.fileService.getFileUrl(currentPathImage) : null;
  }

  private getActiveCompanyNoCia(): number | null {
    return this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
  }
}
