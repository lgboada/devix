import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICompania } from 'app/entities/compania/compania.model';
import { CompaniaService } from 'app/entities/compania/service/compania.service';
import { ICentro } from 'app/entities/centro/centro.model';
import { CentroService } from 'app/entities/centro/service/centro.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { UsuarioCentroService } from '../service/usuario-centro.service';
import { IUsuarioCentro } from '../usuario-centro.model';
import { UsuarioCentroFormGroup, UsuarioCentroFormService } from './usuario-centro-form.service';

@Component({
  selector: 'jhi-usuario-centro-update',
  templateUrl: './usuario-centro-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UsuarioCentroUpdateComponent implements OnInit {
  isSaving = false;
  usuarioCentro: IUsuarioCentro | null = null;

  /** Id de fila en `compania` (unico); el valor de negocio `noCia` se deriva al guardar. */
  selectedCompaniaId: number | null = null;
  /** Varias filas comparten el mismo noCia en BD: hay que corregir datos o elegir por nombre. */
  noCiaDuplicadoEnLista = false;

  companiasSharedCollection: ICompania[] = [];
  centrosSharedCollection: ICentro[] = [];
  usersSharedCollection: IUser[] = [];

  protected usuarioCentroService = inject(UsuarioCentroService);
  protected usuarioCentroFormService = inject(UsuarioCentroFormService);
  protected companiaService = inject(CompaniaService);
  protected centroService = inject(CentroService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UsuarioCentroFormGroup = this.usuarioCentroFormService.createUsuarioCentroFormGroup();

  compareCentro = (o1: ICentro | null, o2: ICentro | null): boolean => this.centroService.compareCentro(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioCentro }) => {
      this.usuarioCentro = usuarioCentro;
      if (usuarioCentro) {
        this.updateForm(usuarioCentro);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  onCompaniaIdChange(companiaId: number | null): void {
    this.selectedCompaniaId = companiaId;
    const c = companiaId != null ? this.companiasSharedCollection.find(x => x.id === companiaId) : undefined;
    this.editForm.patchValue({ noCia: c?.noCia ?? null });
    this.loadCentrosForNoCia(c?.noCia);
  }

  save(): void {
    this.isSaving = true;
    const usuarioCentro = this.usuarioCentroFormService.getUsuarioCentro(this.editForm);
    if (usuarioCentro.id !== null) {
      this.subscribeToSaveResponse(this.usuarioCentroService.update(usuarioCentro));
    } else {
      this.subscribeToSaveResponse(this.usuarioCentroService.create(usuarioCentro));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuarioCentro>>): void {
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

  protected updateForm(usuarioCentro: IUsuarioCentro): void {
    this.usuarioCentro = usuarioCentro;
    this.usuarioCentroFormService.resetForm(this.editForm, usuarioCentro);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, usuarioCentro.user);
  }

  protected loadRelationshipsOptions(): void {
    // GET /api/companias sin filtro por noCia de contexto: CompanyCriteriaEnforcementAspect omite CompaniaCriteria.
    this.companiaService
      .query({ size: 1000 })
      .pipe(map((res: HttpResponse<ICompania[]>) => res.body ?? []))
      .subscribe((companias: ICompania[]) => {
        this.companiasSharedCollection = companias;
        this.syncSelectedCompaniaFromNoCia();
      });

    this.loadCentrosForNoCia(this.editForm.controls.noCia.value);

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.usuarioCentro?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  /**
   * Carga centros del noCia elegido. El backend solo aplica noCia.equals distinto al contexto si el usuario es ADMIN.
   */
  private loadCentrosForNoCia(noCia: number | null | undefined): void {
    if (noCia === null || noCia === undefined) {
      this.centrosSharedCollection = [];
      return;
    }
    this.centroService
      .query({ size: 1000, 'noCia.equals': noCia })
      .pipe(map((res: HttpResponse<ICentro[]>) => res.body ?? []))
      .pipe(
        map((centros: ICentro[]) =>
          this.centroService.addCentroToCollectionIfMissing<ICentro>(centros, this.editForm.controls.centro.value),
        ),
      )
      .subscribe((centros: ICentro[]) => {
        this.centrosSharedCollection = centros;
        const currentCentro = this.editForm.controls.centro.value;
        if (currentCentro && !centros.some(c => c.id === currentCentro.id)) {
          this.editForm.controls.centro.setValue(null);
        }
      });
  }

  private syncSelectedCompaniaFromNoCia(): void {
    const noCia = this.editForm.controls.noCia.value;
    if (noCia === null || noCia === undefined) {
      this.selectedCompaniaId = null;
      this.noCiaDuplicadoEnLista = false;
      return;
    }
    const matches = this.companiasSharedCollection.filter(c => c.noCia === noCia);
    if (matches.length === 0) {
      this.selectedCompaniaId = null;
      this.noCiaDuplicadoEnLista = false;
    } else if (matches.length === 1) {
      this.selectedCompaniaId = matches[0].id ?? null;
      this.noCiaDuplicadoEnLista = false;
    } else {
      this.selectedCompaniaId = null;
      this.noCiaDuplicadoEnLista = true;
    }
  }
}
