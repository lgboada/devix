import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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

  centrosSharedCollection: ICentro[] = [];
  usersSharedCollection: IUser[] = [];

  protected usuarioCentroService = inject(UsuarioCentroService);
  protected usuarioCentroFormService = inject(UsuarioCentroFormService);
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

    this.centrosSharedCollection = this.centroService.addCentroToCollectionIfMissing<ICentro>(
      this.centrosSharedCollection,
      usuarioCentro.centro,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, usuarioCentro.user);
  }

  protected loadRelationshipsOptions(): void {
    this.centroService
      .query()
      .pipe(map((res: HttpResponse<ICentro[]>) => res.body ?? []))
      .pipe(map((centros: ICentro[]) => this.centroService.addCentroToCollectionIfMissing<ICentro>(centros, this.usuarioCentro?.centro)))
      .subscribe((centros: ICentro[]) => (this.centrosSharedCollection = centros));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.usuarioCentro?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
