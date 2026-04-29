import { Component, OnInit, effect, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActiveCompanyService } from 'app/core/auth/active-company.service';

import { ICentro } from 'app/entities/centro/centro.model';
import { IBodega, NewBodega } from '../bodega.model';
import { BodegaService } from '../service/bodega.service';
import { BodegaFormGroup, BodegaFormService } from './bodega-form.service';

@Component({
  selector: 'jhi-bodega-update',
  templateUrl: './bodega-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BodegaUpdateComponent implements OnInit {
  isSaving = false;
  centro: ICentro | null = null;
  bodega: IBodega | null = null;

  protected bodegaService = inject(BodegaService);
  protected bodegaFormService = inject(BodegaFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected router = inject(Router);
  protected activeCompanyService = inject(ActiveCompanyService);

  editForm: BodegaFormGroup = this.bodegaFormService.createBodegaFormGroup();

  // eslint-disable-next-line @typescript-eslint/member-ordering
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
    this.activatedRoute.data.subscribe(({ centro, bodega }) => {
      this.centro = centro ?? null;
      this.bodega = bodega ?? null;
      if (this.bodega) {
        this.bodegaFormService.resetForm(this.editForm, this.bodega);
      } else {
        this.bodegaFormService.resetForm(this.editForm, { id: null, activa: true });
      }
      this.ensureNoCiaFromSession();
    });
  }

  previousState(): void {
    if (this.centro?.id) {
      this.router.navigate(['/centro', this.centro.id, 'bodegas']);
    } else {
      window.history.back();
    }
  }

  save(): void {
    this.ensureNoCiaFromSession();
    if (!this.centro?.id) {
      return;
    }
    this.isSaving = true;
    const formValue = this.bodegaFormService.getBodega(this.editForm);
    if (formValue.id !== null) {
      const payload: IBodega = { ...(formValue as IBodega), centro: { id: this.centro.id } };
      this.subscribeToSaveResponse(this.bodegaService.update(payload));
    } else {
      const payload: NewBodega = { ...(formValue as NewBodega), centro: { id: this.centro.id } };
      this.subscribeToSaveResponse(this.bodegaService.create(payload));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBodega>>): void {
    result.subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  private ensureNoCiaFromSession(): void {
    const sessionNoCia = this.activeCompanyService.trackActiveCompany()()?.noCia;
    if (sessionNoCia != null) {
      const noCiaControl = this.editForm.get('noCia');
      if (noCiaControl?.value === null || noCiaControl?.value === undefined) {
        noCiaControl?.setValue(sessionNoCia);
      }
    }
  }
}
