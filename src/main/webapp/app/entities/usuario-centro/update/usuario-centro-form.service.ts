import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUsuarioCentro, NewUsuarioCentro } from '../usuario-centro.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUsuarioCentro for edit and NewUsuarioCentroFormGroupInput for create.
 */
type UsuarioCentroFormGroupInput = IUsuarioCentro | PartialWithRequiredKeyOf<NewUsuarioCentro>;

type UsuarioCentroFormDefaults = Pick<NewUsuarioCentro, 'id' | 'principal'>;

type UsuarioCentroFormGroupContent = {
  id: FormControl<IUsuarioCentro['id'] | NewUsuarioCentro['id']>;
  noCia: FormControl<IUsuarioCentro['noCia']>;
  principal: FormControl<IUsuarioCentro['principal']>;
  centro: FormControl<IUsuarioCentro['centro']>;
  user: FormControl<IUsuarioCentro['user']>;
};

export type UsuarioCentroFormGroup = FormGroup<UsuarioCentroFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UsuarioCentroFormService {
  createUsuarioCentroFormGroup(usuarioCentro: UsuarioCentroFormGroupInput = { id: null }): UsuarioCentroFormGroup {
    const usuarioCentroRawValue = {
      ...this.getFormDefaults(),
      ...usuarioCentro,
    };
    return new FormGroup<UsuarioCentroFormGroupContent>({
      id: new FormControl(
        { value: usuarioCentroRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(usuarioCentroRawValue.noCia, {
        validators: [Validators.required],
      }),
      principal: new FormControl(usuarioCentroRawValue.principal, {
        validators: [Validators.required],
      }),
      centro: new FormControl(usuarioCentroRawValue.centro),
      user: new FormControl(usuarioCentroRawValue.user),
    });
  }

  getUsuarioCentro(form: UsuarioCentroFormGroup): IUsuarioCentro | NewUsuarioCentro {
    return form.getRawValue() as IUsuarioCentro | NewUsuarioCentro;
  }

  resetForm(form: UsuarioCentroFormGroup, usuarioCentro: UsuarioCentroFormGroupInput): void {
    const usuarioCentroRawValue = { ...this.getFormDefaults(), ...usuarioCentro };
    form.reset(
      {
        ...usuarioCentroRawValue,
        id: { value: usuarioCentroRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UsuarioCentroFormDefaults {
    return {
      id: null,
      principal: false,
    };
  }
}
