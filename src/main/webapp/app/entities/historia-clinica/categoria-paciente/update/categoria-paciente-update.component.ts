import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';

import SharedModule from 'app/shared/shared.module';
import { ICategoriaPaciente, NewCategoriaPaciente } from '../categoria-paciente.model';
import { CategoriaPacienteService } from '../service/categoria-paciente.service';

type CategoriaPacienteFormGroup = FormGroup<{
  id: FormControl<ICategoriaPaciente['id'] | NewCategoriaPaciente['id']>;
  noCia: FormControl<ICategoriaPaciente['noCia']>;
  nombre: FormControl<ICategoriaPaciente['nombre']>;
}>;

@Component({
  selector: 'jhi-categoria-paciente-update',
  templateUrl: './categoria-paciente-update.component.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class CategoriaPacienteUpdateComponent implements OnInit {
  isSaving = false;
  categoriaPaciente: ICategoriaPaciente | null = null;

  protected categoriaPacienteService = inject(CategoriaPacienteService);
  protected activatedRoute = inject(ActivatedRoute);

  editForm: CategoriaPacienteFormGroup = new FormGroup({
    id: new FormControl({ value: null as ICategoriaPaciente['id'] | null, disabled: true }),
    noCia: new FormControl<ICategoriaPaciente['noCia']>(null, { validators: [Validators.required] }),
    nombre: new FormControl<ICategoriaPaciente['nombre']>(null, { validators: [Validators.required, Validators.maxLength(60)] }),
  });

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaPaciente }) => {
      this.categoriaPaciente = categoriaPaciente;
      if (categoriaPaciente) {
        this.editForm.patchValue({ id: categoriaPaciente.id, noCia: categoriaPaciente.noCia, nombre: categoriaPaciente.nombre });
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const value = this.editForm.getRawValue();
    if (value.id !== null) {
      this.subscribeToSaveResponse(this.categoriaPacienteService.update(value as ICategoriaPaciente));
    } else {
      this.subscribeToSaveResponse(this.categoriaPacienteService.create(value as NewCategoriaPaciente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoriaPaciente>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // handled by alert component
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
