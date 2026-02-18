import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';
import { EmpleadoFormGroup, EmpleadoFormService } from './empleado-form.service';

@Component({
  selector: 'jhi-empleado-update',
  templateUrl: './empleado-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmpleadoUpdateComponent implements OnInit {
  isSaving = false;
  empleado: IEmpleado | null = null;

  protected empleadoService = inject(EmpleadoService);
  protected empleadoFormService = inject(EmpleadoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmpleadoFormGroup = this.empleadoFormService.createEmpleadoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleado }) => {
      this.empleado = empleado;
      if (empleado) {
        this.updateForm(empleado);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleado = this.empleadoFormService.getEmpleado(this.editForm);
    if (empleado.id !== null) {
      this.subscribeToSaveResponse(this.empleadoService.update(empleado));
    } else {
      this.subscribeToSaveResponse(this.empleadoService.create(empleado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleado>>): void {
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

  protected updateForm(empleado: IEmpleado): void {
    this.empleado = empleado;
    this.empleadoFormService.resetForm(this.editForm, empleado);
  }
}
