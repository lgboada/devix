import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoCatalogo } from 'app/entities/tipo-catalogo/tipo-catalogo.model';
import { TipoCatalogoService } from 'app/entities/tipo-catalogo/service/tipo-catalogo.service';
import { ICatalogo } from '../catalogo.model';
import { CatalogoService } from '../service/catalogo.service';
import { CatalogoFormGroup, CatalogoFormService } from './catalogo-form.service';

@Component({
  selector: 'jhi-catalogo-update',
  templateUrl: './catalogo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CatalogoUpdateComponent implements OnInit {
  isSaving = false;
  catalogo: ICatalogo | null = null;

  tipoCatalogosSharedCollection: ITipoCatalogo[] = [];

  protected catalogoService = inject(CatalogoService);
  protected catalogoFormService = inject(CatalogoFormService);
  protected tipoCatalogoService = inject(TipoCatalogoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CatalogoFormGroup = this.catalogoFormService.createCatalogoFormGroup();

  compareTipoCatalogo = (o1: ITipoCatalogo | null, o2: ITipoCatalogo | null): boolean =>
    this.tipoCatalogoService.compareTipoCatalogo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogo }) => {
      this.catalogo = catalogo;
      if (catalogo) {
        this.updateForm(catalogo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogo = this.catalogoFormService.getCatalogo(this.editForm);
    if (catalogo.id !== null) {
      this.subscribeToSaveResponse(this.catalogoService.update(catalogo));
    } else {
      this.subscribeToSaveResponse(this.catalogoService.create(catalogo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogo>>): void {
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

  protected updateForm(catalogo: ICatalogo): void {
    this.catalogo = catalogo;
    this.catalogoFormService.resetForm(this.editForm, catalogo);

    this.tipoCatalogosSharedCollection = this.tipoCatalogoService.addTipoCatalogoToCollectionIfMissing<ITipoCatalogo>(
      this.tipoCatalogosSharedCollection,
      catalogo.tipoCatalogo,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tipoCatalogoService
      .query()
      .pipe(map((res: HttpResponse<ITipoCatalogo[]>) => res.body ?? []))
      .pipe(
        map((tipoCatalogos: ITipoCatalogo[]) =>
          this.tipoCatalogoService.addTipoCatalogoToCollectionIfMissing<ITipoCatalogo>(tipoCatalogos, this.catalogo?.tipoCatalogo),
        ),
      )
      .subscribe((tipoCatalogos: ITipoCatalogo[]) => (this.tipoCatalogosSharedCollection = tipoCatalogos));
  }
}
