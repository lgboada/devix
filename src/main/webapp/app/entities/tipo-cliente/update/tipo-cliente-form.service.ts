import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITipoCliente, NewTipoCliente } from '../tipo-cliente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoCliente for edit and NewTipoClienteFormGroupInput for create.
 */
type TipoClienteFormGroupInput = ITipoCliente | PartialWithRequiredKeyOf<NewTipoCliente>;

type TipoClienteFormDefaults = Pick<NewTipoCliente, 'id'>;

type TipoClienteFormGroupContent = {
  id: FormControl<ITipoCliente['id'] | NewTipoCliente['id']>;
  noCia: FormControl<ITipoCliente['noCia']>;
  descripcion: FormControl<ITipoCliente['descripcion']>;
};

export type TipoClienteFormGroup = FormGroup<TipoClienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoClienteFormService {
  createTipoClienteFormGroup(tipoCliente: TipoClienteFormGroupInput = { id: null }): TipoClienteFormGroup {
    const tipoClienteRawValue = {
      ...this.getFormDefaults(),
      ...tipoCliente,
    };
    return new FormGroup<TipoClienteFormGroupContent>({
      id: new FormControl(
        { value: tipoClienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(tipoClienteRawValue.noCia, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(tipoClienteRawValue.descripcion, {
        validators: [Validators.required],
      }),
    });
  }

  getTipoCliente(form: TipoClienteFormGroup): ITipoCliente | NewTipoCliente {
    return form.getRawValue() as ITipoCliente | NewTipoCliente;
  }

  resetForm(form: TipoClienteFormGroup, tipoCliente: TipoClienteFormGroupInput): void {
    const tipoClienteRawValue = { ...this.getFormDefaults(), ...tipoCliente };
    form.reset(
      {
        ...tipoClienteRawValue,
        id: { value: tipoClienteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoClienteFormDefaults {
    return {
      id: null,
    };
  }
}
