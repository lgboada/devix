import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMarca } from 'app/entities/marca/marca.model';
import { MarcaService } from 'app/entities/marca/service/marca.service';
import { IModelo } from '../modelo.model';
import { ModeloService } from '../service/modelo.service';
import { ModeloFormGroup, ModeloFormService } from './modelo-form.service';

@Component({
  selector: 'jhi-modelo-update',
  templateUrl: './modelo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ModeloUpdateComponent implements OnInit {
  isSaving = false;
  modelo: IModelo | null = null;

  marcasSharedCollection: IMarca[] = [];

  protected modeloService = inject(ModeloService);
  protected modeloFormService = inject(ModeloFormService);
  protected marcaService = inject(MarcaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ModeloFormGroup = this.modeloFormService.createModeloFormGroup();

  compareMarca = (o1: IMarca | null, o2: IMarca | null): boolean => this.marcaService.compareMarca(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ modelo }) => {
      this.modelo = modelo;
      if (modelo) {
        this.updateForm(modelo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const modelo = this.modeloFormService.getModelo(this.editForm);
    if (modelo.id !== null) {
      this.subscribeToSaveResponse(this.modeloService.update(modelo));
    } else {
      this.subscribeToSaveResponse(this.modeloService.create(modelo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IModelo>>): void {
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

  protected updateForm(modelo: IModelo): void {
    this.modelo = modelo;
    this.modeloFormService.resetForm(this.editForm, modelo);

    this.marcasSharedCollection = this.marcaService.addMarcaToCollectionIfMissing<IMarca>(this.marcasSharedCollection, modelo.marca);
  }

  protected loadRelationshipsOptions(): void {
    this.marcaService
      .query()
      .pipe(map((res: HttpResponse<IMarca[]>) => res.body ?? []))
      .pipe(map((marcas: IMarca[]) => this.marcaService.addMarcaToCollectionIfMissing<IMarca>(marcas, this.modelo?.marca)))
      .subscribe((marcas: IMarca[]) => (this.marcasSharedCollection = marcas));
  }
}
