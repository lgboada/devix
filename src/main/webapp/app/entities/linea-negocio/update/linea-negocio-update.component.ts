import { Component, OnInit, effect, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';

import { ILineaNegocio } from '../linea-negocio.model';
import { LineaNegocioService } from '../service/linea-negocio.service';
import { LineaNegocioFormGroup, LineaNegocioFormService } from './linea-negocio-form.service';

@Component({
  selector: 'jhi-linea-negocio-update',
  templateUrl: './linea-negocio-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LineaNegocioUpdateComponent implements OnInit {
  isSaving = false;
  isEditing = false;
  lineaNegocio: ILineaNegocio | null = null;

  protected lineaNegocioService = inject(LineaNegocioService);
  protected lineaNegocioFormService = inject(LineaNegocioFormService);
  protected activeCompanyService = inject(ActiveCompanyService);
  protected activatedRoute = inject(ActivatedRoute);

  editForm: LineaNegocioFormGroup = this.lineaNegocioFormService.createLineaNegocioFormGroup();

  private readonly syncNoCia = effect(() => {
    const noCia = this.activeCompanyService.trackActiveCompany()()?.noCia ?? null;
    if (noCia === null) return;
    const ctrl = this.editForm.get('noCia');
    if (!ctrl?.value) {
      ctrl?.setValue(noCia);
    }
  });

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lineaNegocio }) => {
      this.lineaNegocio = lineaNegocio;
      if (lineaNegocio) {
        this.isEditing = true;
        this.lineaNegocioFormService.resetForm(this.editForm, lineaNegocio);
        // En edición el código no se puede cambiar (es parte de la PK)
        this.editForm.get('lineaNo')?.disable();
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dto = this.lineaNegocioFormService.getLineaNegocio(this.editForm);
    const obs: Observable<HttpResponse<ILineaNegocio>> = this.isEditing
      ? this.lineaNegocioService.update(dto)
      : this.lineaNegocioService.create(dto);

    obs.pipe(finalize(() => (this.isSaving = false))).subscribe({
      next: () => this.previousState(),
      error: () => {},
    });
  }
}
