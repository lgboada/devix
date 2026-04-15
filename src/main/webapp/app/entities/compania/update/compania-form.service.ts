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

type CompaniaFormDefaults = Pick<NewCompania, 'id' | 'activa' | 'obligadoContabilidad' | 'ambienteSri'>;

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
  establecimiento: FormControl<ICompania['establecimiento']>;
  contribuyenteEspecial: FormControl<ICompania['contribuyenteEspecial']>;
  obligadoContabilidad: FormControl<ICompania['obligadoContabilidad']>;
  ambienteSri: FormControl<ICompania['ambienteSri']>;
  pathCertificado: FormControl<ICompania['pathCertificado']>;
  claveCertificado: FormControl<ICompania['claveCertificado']>;
  pathFileServer: FormControl<ICompania['pathFileServer']>;
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
      establecimiento: new FormControl(companiaRawValue.establecimiento, {
        validators: [Validators.maxLength(3)],
      }),
      contribuyenteEspecial: new FormControl(companiaRawValue.contribuyenteEspecial, {
        validators: [Validators.maxLength(10)],
      }),
      obligadoContabilidad: new FormControl(companiaRawValue.obligadoContabilidad),
      ambienteSri: new FormControl(companiaRawValue.ambienteSri),
      pathCertificado: new FormControl(companiaRawValue.pathCertificado),
      claveCertificado: new FormControl(companiaRawValue.claveCertificado),
      pathFileServer: new FormControl(companiaRawValue.pathFileServer),
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
      obligadoContabilidad: false,
      ambienteSri: 1,
    };
  }
}
