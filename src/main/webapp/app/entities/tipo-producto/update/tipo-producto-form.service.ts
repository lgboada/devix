import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITipoProducto, NewTipoProducto } from '../tipo-producto.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoProducto for edit and NewTipoProductoFormGroupInput for create.
 */
type TipoProductoFormGroupInput = ITipoProducto | PartialWithRequiredKeyOf<NewTipoProducto>;

type TipoProductoFormDefaults = Pick<NewTipoProducto, 'id'>;

type TipoProductoFormGroupContent = {
  id: FormControl<ITipoProducto['id'] | NewTipoProducto['id']>;
  noCia: FormControl<ITipoProducto['noCia']>;
  nombre: FormControl<ITipoProducto['nombre']>;
};

export type TipoProductoFormGroup = FormGroup<TipoProductoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoProductoFormService {
  createTipoProductoFormGroup(tipoProducto: TipoProductoFormGroupInput = { id: null }): TipoProductoFormGroup {
    const tipoProductoRawValue = {
      ...this.getFormDefaults(),
      ...tipoProducto,
    };
    return new FormGroup<TipoProductoFormGroupContent>({
      id: new FormControl(
        { value: tipoProductoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(tipoProductoRawValue.noCia, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(tipoProductoRawValue.nombre, {
        validators: [Validators.required],
      }),
    });
  }

  getTipoProducto(form: TipoProductoFormGroup): ITipoProducto | NewTipoProducto {
    return form.getRawValue() as ITipoProducto | NewTipoProducto;
  }

  resetForm(form: TipoProductoFormGroup, tipoProducto: TipoProductoFormGroupInput): void {
    const tipoProductoRawValue = { ...this.getFormDefaults(), ...tipoProducto };
    form.reset(
      {
        ...tipoProductoRawValue,
        id: { value: tipoProductoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoProductoFormDefaults {
    return {
      id: null,
    };
  }
}
