import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICompania, NewCompania } from '../compania.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompania for edit and NewCompaniaFormGroupInput for create.
 */
type CompaniaFormGroupInput = ICompania | PartialWithRequiredKeyOf<NewCompania>;

type CompaniaFormDefaults = Pick<NewCompania, 'id' | 'activa'>;

type CompaniaFormGroupContent = {
  id: FormControl<ICompania['id'] | NewCompania['id']>;
  noCia: FormControl<ICompania['noCia']>;
  dni: FormControl<ICompania['dni']>;
  nombre: FormControl<ICompania['nombre']>;
  direccion: FormControl<ICompania['direccion']>;
  email: FormControl<ICompania['email']>;
  telefono: FormControl<ICompania['telefono']>;
  pathImage: FormControl<ICompania['pathImage']>;
  activa: FormControl<ICompania['activa']>;
};

export type CompaniaFormGroup = FormGroup<CompaniaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompaniaFormService {
  createCompaniaFormGroup(compania: CompaniaFormGroupInput = { id: null }): CompaniaFormGroup {
    const companiaRawValue = {
      ...this.getFormDefaults(),
      ...compania,
    };
    return new FormGroup<CompaniaFormGroupContent>({
      id: new FormControl(
        { value: companiaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(companiaRawValue.noCia, {
        validators: [Validators.required],
      }),
      dni: new FormControl(companiaRawValue.dni, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(companiaRawValue.nombre, {
        validators: [Validators.required],
      }),
      direccion: new FormControl(companiaRawValue.direccion, {
        validators: [Validators.required],
      }),
      email: new FormControl(companiaRawValue.email, {
        validators: [Validators.required, Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}')],
      }),
      telefono: new FormControl(companiaRawValue.telefono, {
        validators: [Validators.required],
      }),
      pathImage: new FormControl(companiaRawValue.pathImage, {
        validators: [Validators.required],
      }),
      activa: new FormControl(companiaRawValue.activa, {
        validators: [Validators.required],
      }),
    });
  }

  getCompania(form: CompaniaFormGroup): ICompania | NewCompania {
    return form.getRawValue() as ICompania | NewCompania;
  }

  resetForm(form: CompaniaFormGroup, compania: CompaniaFormGroupInput): void {
    const companiaRawValue = { ...this.getFormDefaults(), ...compania };
    form.reset(
      {
        ...companiaRawValue,
        id: { value: companiaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CompaniaFormDefaults {
    return {
      id: null,
      activa: false,
    };
  }
}
