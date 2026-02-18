import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompania } from 'app/entities/compania/compania.model';
import { CompaniaService } from 'app/entities/compania/service/compania.service';
import { ICentro } from '../centro.model';
import { CentroService } from '../service/centro.service';
import { CentroFormGroup, CentroFormService } from './centro-form.service';

@Component({
  selector: 'jhi-centro-update',
  templateUrl: './centro-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CentroUpdateComponent implements OnInit {
  isSaving = false;
  centro: ICentro | null = null;

  companiasSharedCollection: ICompania[] = [];

  protected centroService = inject(CentroService);
  protected centroFormService = inject(CentroFormService);
  protected companiaService = inject(CompaniaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CentroFormGroup = this.centroFormService.createCentroFormGroup();

  compareCompania = (o1: ICompania | null, o2: ICompania | null): boolean => this.companiaService.compareCompania(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ centro }) => {
      this.centro = centro;
      if (centro) {
        this.updateForm(centro);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const centro = this.centroFormService.getCentro(this.editForm);
    if (centro.id !== null) {
      this.subscribeToSaveResponse(this.centroService.update(centro));
    } else {
      this.subscribeToSaveResponse(this.centroService.create(centro));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICentro>>): void {
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

  protected updateForm(centro: ICentro): void {
    this.centro = centro;
    this.centroFormService.resetForm(this.editForm, centro);

    this.companiasSharedCollection = this.companiaService.addCompaniaToCollectionIfMissing<ICompania>(
      this.companiasSharedCollection,
      centro.compania,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.companiaService
      .query()
      .pipe(map((res: HttpResponse<ICompania[]>) => res.body ?? []))
      .pipe(
        map((companias: ICompania[]) => this.companiaService.addCompaniaToCollectionIfMissing<ICompania>(companias, this.centro?.compania)),
      )
      .subscribe((companias: ICompania[]) => (this.companiasSharedCollection = companias));
  }
}
