import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IModelo, NewModelo } from '../modelo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IModelo for edit and NewModeloFormGroupInput for create.
 */
type ModeloFormGroupInput = IModelo | PartialWithRequiredKeyOf<NewModelo>;

type ModeloFormDefaults = Pick<NewModelo, 'id'>;

type ModeloFormGroupContent = {
  id: FormControl<IModelo['id'] | NewModelo['id']>;
  noCia: FormControl<IModelo['noCia']>;
  nombre: FormControl<IModelo['nombre']>;
  pathImagen: FormControl<IModelo['pathImagen']>;
  marca: FormControl<IModelo['marca']>;
};

export type ModeloFormGroup = FormGroup<ModeloFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ModeloFormService {
  createModeloFormGroup(modelo: ModeloFormGroupInput = { id: null }): ModeloFormGroup {
    const modeloRawValue = {
      ...this.getFormDefaults(),
      ...modelo,
    };
    return new FormGroup<ModeloFormGroupContent>({
      id: new FormControl(
        { value: modeloRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(modeloRawValue.noCia, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(modeloRawValue.nombre, {
        validators: [Validators.required],
      }),
      pathImagen: new FormControl(modeloRawValue.pathImagen, {
        validators: [Validators.required],
      }),
      marca: new FormControl(modeloRawValue.marca),
    });
  }

  getModelo(form: ModeloFormGroup): IModelo | NewModelo {
    return form.getRawValue() as IModelo | NewModelo;
  }

  resetForm(form: ModeloFormGroup, modelo: ModeloFormGroupInput): void {
    const modeloRawValue = { ...this.getFormDefaults(), ...modelo };
    form.reset(
      {
        ...modeloRawValue,
        id: { value: modeloRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ModeloFormDefaults {
    return {
      id: null,
    };
  }
}
