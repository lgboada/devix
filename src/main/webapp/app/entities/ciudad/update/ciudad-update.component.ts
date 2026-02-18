import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProvincia } from 'app/entities/provincia/provincia.model';
import { ProvinciaService } from 'app/entities/provincia/service/provincia.service';
import { ICiudad } from '../ciudad.model';
import { CiudadService } from '../service/ciudad.service';
import { CiudadFormGroup, CiudadFormService } from './ciudad-form.service';

@Component({
  selector: 'jhi-ciudad-update',
  templateUrl: './ciudad-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CiudadUpdateComponent implements OnInit {
  isSaving = false;
  ciudad: ICiudad | null = null;

  provinciasSharedCollection: IProvincia[] = [];

  protected ciudadService = inject(CiudadService);
  protected ciudadFormService = inject(CiudadFormService);
  protected provinciaService = inject(ProvinciaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CiudadFormGroup = this.ciudadFormService.createCiudadFormGroup();

  compareProvincia = (o1: IProvincia | null, o2: IProvincia | null): boolean => this.provinciaService.compareProvincia(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ciudad }) => {
      this.ciudad = ciudad;
      if (ciudad) {
        this.updateForm(ciudad);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ciudad = this.ciudadFormService.getCiudad(this.editForm);
    if (ciudad.id !== null) {
      this.subscribeToSaveResponse(this.ciudadService.update(ciudad));
    } else {
      this.subscribeToSaveResponse(this.ciudadService.create(ciudad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICiudad>>): void {
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

  protected updateForm(ciudad: ICiudad): void {
    this.ciudad = ciudad;
    this.ciudadFormService.resetForm(this.editForm, ciudad);

    this.provinciasSharedCollection = this.provinciaService.addProvinciaToCollectionIfMissing<IProvincia>(
      this.provinciasSharedCollection,
      ciudad.provincia,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.provinciaService
      .query()
      .pipe(map((res: HttpResponse<IProvincia[]>) => res.body ?? []))
      .pipe(
        map((provincias: IProvincia[]) =>
          this.provinciaService.addProvinciaToCollectionIfMissing<IProvincia>(provincias, this.ciudad?.provincia),
        ),
      )
      .subscribe((provincias: IProvincia[]) => (this.provinciasSharedCollection = provincias));
  }
}
