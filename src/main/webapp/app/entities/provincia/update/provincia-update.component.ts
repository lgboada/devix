import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';
import { IProvincia } from '../provincia.model';
import { ProvinciaService } from '../service/provincia.service';
import { ProvinciaFormGroup, ProvinciaFormService } from './provincia-form.service';

@Component({
  selector: 'jhi-provincia-update',
  templateUrl: './provincia-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProvinciaUpdateComponent implements OnInit {
  isSaving = false;
  provincia: IProvincia | null = null;

  paisSharedCollection: IPais[] = [];

  protected provinciaService = inject(ProvinciaService);
  protected provinciaFormService = inject(ProvinciaFormService);
  protected paisService = inject(PaisService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProvinciaFormGroup = this.provinciaFormService.createProvinciaFormGroup();

  comparePais = (o1: IPais | null, o2: IPais | null): boolean => this.paisService.comparePais(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ provincia }) => {
      this.provincia = provincia;
      if (provincia) {
        this.updateForm(provincia);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const provincia = this.provinciaFormService.getProvincia(this.editForm);
    if (provincia.id !== null) {
      this.subscribeToSaveResponse(this.provinciaService.update(provincia));
    } else {
      this.subscribeToSaveResponse(this.provinciaService.create(provincia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvincia>>): void {
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

  protected updateForm(provincia: IProvincia): void {
    this.provincia = provincia;
    this.provinciaFormService.resetForm(this.editForm, provincia);

    this.paisSharedCollection = this.paisService.addPaisToCollectionIfMissing<IPais>(this.paisSharedCollection, provincia.pais);
  }

  protected loadRelationshipsOptions(): void {
    this.paisService
      .query()
      .pipe(map((res: HttpResponse<IPais[]>) => res.body ?? []))
      .pipe(map((pais: IPais[]) => this.paisService.addPaisToCollectionIfMissing<IPais>(pais, this.provincia?.pais)))
      .subscribe((pais: IPais[]) => (this.paisSharedCollection = pais));
  }
}
