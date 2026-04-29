import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUsuarioCentro } from 'app/entities/usuario-centro/usuario-centro.model';
import { IBodega } from 'app/entities/bodega/bodega.model';
import { BodegaService } from 'app/entities/bodega/service/bodega.service';
import { IUsuarioCentroBodega, NewUsuarioCentroBodega } from '../usuario-centro-bodega.model';
import { UsuarioCentroBodegaService } from '../service/usuario-centro-bodega.service';
import { UCBFormGroup, UsuarioCentroBodegaFormService } from './usuario-centro-bodega-form.service';

@Component({
  selector: 'jhi-usuario-centro-bodega-update',
  templateUrl: './usuario-centro-bodega-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UsuarioCentroBodegaUpdateComponent implements OnInit {
  isSaving = false;
  usuarioCentro: IUsuarioCentro | null = null;
  ucb: IUsuarioCentroBodega | null = null;
  bodegasDisponibles: IBodega[] = [];

  protected readonly service = inject(UsuarioCentroBodegaService);
  protected readonly formService = inject(UsuarioCentroBodegaFormService);
  protected readonly bodegaService = inject(BodegaService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly router = inject(Router);

  editForm: UCBFormGroup = this.formService.createFormGroup();

  compareBodega = (o1: IBodega | null, o2: IBodega | null): boolean => this.bodegaService.compareBodega(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioCentro, ucb }) => {
      this.usuarioCentro = usuarioCentro ?? null;
      this.ucb = ucb ?? null;

      if (this.ucb) {
        this.formService.resetForm(this.editForm, this.ucb);
      }

      const centroId = this.usuarioCentro?.centro?.id;
      if (centroId) {
        this.bodegaService
          .query({ 'centroId.equals': centroId, 'activa.equals': true, sort: ['codigo,asc'], size: 1000 })
          .pipe(map((res: HttpResponse<IBodega[]>) => res.body ?? []))
          .subscribe(bodegas => (this.bodegasDisponibles = bodegas));
      }
    });
  }

  previousState(): void {
    if (this.usuarioCentro?.id) {
      this.router.navigate(['/usuario-centro', this.usuarioCentro.id, 'bodegas']);
    } else {
      window.history.back();
    }
  }

  save(): void {
    if (!this.usuarioCentro?.centro?.id || !this.usuarioCentro?.user?.id || !this.usuarioCentro?.noCia) return;
    this.isSaving = true;
    const formValue = this.formService.getUCB(this.editForm);
    const payload = {
      ...formValue,
      noCia: this.usuarioCentro.noCia,
      centro: { id: this.usuarioCentro.centro.id },
      user: { id: this.usuarioCentro.user.id },
    };
    if (payload.id !== null) {
      this.subscribeToSave(this.service.update(payload as IUsuarioCentroBodega));
    } else {
      this.subscribeToSave(this.service.create(payload as NewUsuarioCentroBodega));
    }
  }

  private subscribeToSave(result: Observable<HttpResponse<IUsuarioCentroBodega>>): void {
    result.subscribe({
      next: () => {
        this.isSaving = false;
        this.previousState();
      },
      error: () => {
        this.isSaving = false;
      },
    });
  }
}
