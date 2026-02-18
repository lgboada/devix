import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICentro, NewCentro } from '../centro.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICentro for edit and NewCentroFormGroupInput for create.
 */
type CentroFormGroupInput = ICentro | PartialWithRequiredKeyOf<NewCentro>;

type CentroFormDefaults = Pick<NewCentro, 'id'>;

type CentroFormGroupContent = {
  id: FormControl<ICentro['id'] | NewCentro['id']>;
  noCia: FormControl<ICentro['noCia']>;
  descripcion: FormControl<ICentro['descripcion']>;
  compania: FormControl<ICentro['compania']>;
};

export type CentroFormGroup = FormGroup<CentroFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CentroFormService {
  createCentroFormGroup(centro: CentroFormGroupInput = { id: null }): CentroFormGroup {
    const centroRawValue = {
      ...this.getFormDefaults(),
      ...centro,
    };
    return new FormGroup<CentroFormGroupContent>({
      id: new FormControl(
        { value: centroRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(centroRawValue.noCia, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(centroRawValue.descripcion, {
        validators: [Validators.required],
      }),
      compania: new FormControl(centroRawValue.compania),
    });
  }

  getCentro(form: CentroFormGroup): ICentro | NewCentro {
    return form.getRawValue() as ICentro | NewCentro;
  }

  resetForm(form: CentroFormGroup, centro: CentroFormGroupInput): void {
    const centroRawValue = { ...this.getFormDefaults(), ...centro };
    form.reset(
      {
        ...centroRawValue,
        id: { value: centroRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CentroFormDefaults {
    return {
      id: null,
    };
  }
}
