import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDireccion, NewDireccion } from '../direccion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDireccion for edit and NewDireccionFormGroupInput for create.
 */
type DireccionFormGroupInput = IDireccion | PartialWithRequiredKeyOf<NewDireccion>;

type DireccionFormDefaults = Pick<NewDireccion, 'id'>;

type DireccionFormGroupContent = {
  id: FormControl<IDireccion['id'] | NewDireccion['id']>;
  noCia: FormControl<IDireccion['noCia']>;
  descripcion: FormControl<IDireccion['descripcion']>;
  pais: FormControl<IDireccion['pais']>;
  provincia: FormControl<IDireccion['provincia']>;
  tipoDireccion: FormControl<IDireccion['tipoDireccion']>;
  cliente: FormControl<IDireccion['cliente']>;
};

export type DireccionFormGroup = FormGroup<DireccionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DireccionFormService {
  createDireccionFormGroup(direccion: DireccionFormGroupInput = { id: null }): DireccionFormGroup {
    const direccionRawValue = {
      ...this.getFormDefaults(),
      ...direccion,
    };
    return new FormGroup<DireccionFormGroupContent>({
      id: new FormControl(
        { value: direccionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(direccionRawValue.noCia, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(direccionRawValue.descripcion, {
        validators: [Validators.required],
      }),
      pais: new FormControl(direccionRawValue.pais),
      provincia: new FormControl(direccionRawValue.provincia),
      tipoDireccion: new FormControl(direccionRawValue.tipoDireccion),
      cliente: new FormControl(direccionRawValue.cliente),
    });
  }

  getDireccion(form: DireccionFormGroup): IDireccion | NewDireccion {
    return form.getRawValue() as IDireccion | NewDireccion;
  }

  resetForm(form: DireccionFormGroup, direccion: DireccionFormGroupInput): void {
    const direccionRawValue = { ...this.getFormDefaults(), ...direccion };
    form.reset(
      {
        ...direccionRawValue,
        id: { value: direccionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DireccionFormDefaults {
    return {
      id: null,
    };
  }
}
