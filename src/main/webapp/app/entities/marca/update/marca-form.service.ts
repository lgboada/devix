import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMarca, NewMarca } from '../marca.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMarca for edit and NewMarcaFormGroupInput for create.
 */
type MarcaFormGroupInput = IMarca | PartialWithRequiredKeyOf<NewMarca>;

type MarcaFormDefaults = Pick<NewMarca, 'id'>;

type MarcaFormGroupContent = {
  id: FormControl<IMarca['id'] | NewMarca['id']>;
  noCia: FormControl<IMarca['noCia']>;
  nombre: FormControl<IMarca['nombre']>;
  pathImagen: FormControl<IMarca['pathImagen']>;
};

export type MarcaFormGroup = FormGroup<MarcaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MarcaFormService {
  createMarcaFormGroup(marca: MarcaFormGroupInput = { id: null }): MarcaFormGroup {
    const marcaRawValue = {
      ...this.getFormDefaults(),
      ...marca,
    };
    return new FormGroup<MarcaFormGroupContent>({
      id: new FormControl(
        { value: marcaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(marcaRawValue.noCia, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(marcaRawValue.nombre, {
        validators: [Validators.required],
      }),
      pathImagen: new FormControl(marcaRawValue.pathImagen, {
        validators: [Validators.required],
      }),
    });
  }

  getMarca(form: MarcaFormGroup): IMarca | NewMarca {
    return form.getRawValue() as IMarca | NewMarca;
  }

  resetForm(form: MarcaFormGroup, marca: MarcaFormGroupInput): void {
    const marcaRawValue = { ...this.getFormDefaults(), ...marca };
    form.reset(
      {
        ...marcaRawValue,
        id: { value: marcaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MarcaFormDefaults {
    return {
      id: null,
    };
  }
}
