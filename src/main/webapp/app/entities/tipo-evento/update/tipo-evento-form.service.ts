import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITipoEvento, NewTipoEvento } from '../tipo-evento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoEvento for edit and NewTipoEventoFormGroupInput for create.
 */
type TipoEventoFormGroupInput = ITipoEvento | PartialWithRequiredKeyOf<NewTipoEvento>;

type TipoEventoFormDefaults = Pick<NewTipoEvento, 'id'>;

type TipoEventoFormGroupContent = {
  id: FormControl<ITipoEvento['id'] | NewTipoEvento['id']>;
  noCia: FormControl<ITipoEvento['noCia']>;
  nombre: FormControl<ITipoEvento['nombre']>;
};

export type TipoEventoFormGroup = FormGroup<TipoEventoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoEventoFormService {
  createTipoEventoFormGroup(tipoEvento: TipoEventoFormGroupInput = { id: null }): TipoEventoFormGroup {
    const tipoEventoRawValue = {
      ...this.getFormDefaults(),
      ...tipoEvento,
    };
    return new FormGroup<TipoEventoFormGroupContent>({
      id: new FormControl(
        { value: tipoEventoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(tipoEventoRawValue.noCia, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(tipoEventoRawValue.nombre, {
        validators: [Validators.required],
      }),
    });
  }

  getTipoEvento(form: TipoEventoFormGroup): ITipoEvento | NewTipoEvento {
    return form.getRawValue() as ITipoEvento | NewTipoEvento;
  }

  resetForm(form: TipoEventoFormGroup, tipoEvento: TipoEventoFormGroupInput): void {
    const tipoEventoRawValue = { ...this.getFormDefaults(), ...tipoEvento };
    form.reset(
      {
        ...tipoEventoRawValue,
        id: { value: tipoEventoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoEventoFormDefaults {
    return {
      id: null,
    };
  }
}
