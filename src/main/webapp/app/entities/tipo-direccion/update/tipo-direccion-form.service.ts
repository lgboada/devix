import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITipoDireccion, NewTipoDireccion } from '../tipo-direccion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoDireccion for edit and NewTipoDireccionFormGroupInput for create.
 */
type TipoDireccionFormGroupInput = ITipoDireccion | PartialWithRequiredKeyOf<NewTipoDireccion>;

type TipoDireccionFormDefaults = Pick<NewTipoDireccion, 'id'>;

type TipoDireccionFormGroupContent = {
  id: FormControl<ITipoDireccion['id'] | NewTipoDireccion['id']>;
  noCia: FormControl<ITipoDireccion['noCia']>;
  descripcion: FormControl<ITipoDireccion['descripcion']>;
};

export type TipoDireccionFormGroup = FormGroup<TipoDireccionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoDireccionFormService {
  createTipoDireccionFormGroup(tipoDireccion: TipoDireccionFormGroupInput = { id: null }): TipoDireccionFormGroup {
    const tipoDireccionRawValue = {
      ...this.getFormDefaults(),
      ...tipoDireccion,
    };
    return new FormGroup<TipoDireccionFormGroupContent>({
      id: new FormControl(
        { value: tipoDireccionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(tipoDireccionRawValue.noCia, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(tipoDireccionRawValue.descripcion, {
        validators: [Validators.required],
      }),
    });
  }

  getTipoDireccion(form: TipoDireccionFormGroup): ITipoDireccion | NewTipoDireccion {
    return form.getRawValue() as ITipoDireccion | NewTipoDireccion;
  }

  resetForm(form: TipoDireccionFormGroup, tipoDireccion: TipoDireccionFormGroupInput): void {
    const tipoDireccionRawValue = { ...this.getFormDefaults(), ...tipoDireccion };
    form.reset(
      {
        ...tipoDireccionRawValue,
        id: { value: tipoDireccionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoDireccionFormDefaults {
    return {
      id: null,
    };
  }
}
