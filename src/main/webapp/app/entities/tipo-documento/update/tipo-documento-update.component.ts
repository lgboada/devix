import { Component, OnInit, effect, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';

import { ITipoDocumento } from '../tipo-documento.model';
import { TipoDocumentoService } from '../service/tipo-documento.service';
import { TipoDocumentoFormGroup, TipoDocumentoFormService } from './tipo-documento-form.service';

@Component({
  selector: 'jhi-tipo-documento-update',
  templateUrl: './tipo-documento-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TipoDocumentoUpdateComponent implements OnInit {
  isSaving = false;
  isEditing = false;
  tipoDocumento: ITipoDocumento | null = null;

  protected tipoDocumentoService = inject(TipoDocumentoService);
  protected tipoDocumentoFormService = inject(TipoDocumentoFormService);
  protected activeCompanyService = inject(ActiveCompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  editForm: TipoDocumentoFormGroup = this.tipoDocumentoFormService.createTipoDocumentoFormGroup();

  private readonly syncNoCia = effect(() => {
    const noCia = this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
    if (noCia === null) return;
    const ctrl = this.editForm.get('noCia');
    if (!ctrl?.value) {
      ctrl?.setValue(noCia);
    }
  });

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoDocumento }) => {
      this.tipoDocumento = tipoDocumento;
      if (tipoDocumento) {
        this.isEditing = true;
        this.tipoDocumentoFormService.resetForm(this.editForm, tipoDocumento);
        // En edición el código no se puede cambiar
        this.editForm.get('tipoDocumento')?.disable();
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dto = this.tipoDocumentoFormService.getTipoDocumento(this.editForm);
    const obs: Observable<HttpResponse<ITipoDocumento>> = this.isEditing
      ? this.tipoDocumentoService.update(dto)
      : this.tipoDocumentoService.create(dto);

    obs.pipe(finalize(() => (this.isSaving = false))).subscribe({
      next: () => this.previousState(),
      error: () => {},
    });
  }
}
