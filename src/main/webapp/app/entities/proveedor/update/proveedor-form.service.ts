import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProveedor, NewProveedor } from '../proveedor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProveedor for edit and NewProveedorFormGroupInput for create.
 */
type ProveedorFormGroupInput = IProveedor | PartialWithRequiredKeyOf<NewProveedor>;

type ProveedorFormDefaults = Pick<NewProveedor, 'id'>;

type ProveedorFormGroupContent = {
  id: FormControl<IProveedor['id'] | NewProveedor['id']>;
  noCia: FormControl<IProveedor['noCia']>;
  dni: FormControl<IProveedor['dni']>;
  nombre: FormControl<IProveedor['nombre']>;
  contacto: FormControl<IProveedor['contacto']>;
  email: FormControl<IProveedor['email']>;
  pathImagen: FormControl<IProveedor['pathImagen']>;
  telefono: FormControl<IProveedor['telefono']>;
};

export type ProveedorFormGroup = FormGroup<ProveedorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProveedorFormService {
  createProveedorFormGroup(proveedor: ProveedorFormGroupInput = { id: null }): ProveedorFormGroup {
    const proveedorRawValue = {
      ...this.getFormDefaults(),
      ...proveedor,
    };
    return new FormGroup<ProveedorFormGroupContent>({
      id: new FormControl(
        { value: proveedorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(proveedorRawValue.noCia, {
        validators: [Validators.required],
      }),
      dni: new FormControl(proveedorRawValue.dni, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(proveedorRawValue.nombre, {
        validators: [Validators.required],
      }),
      contacto: new FormControl(proveedorRawValue.contacto),
      email: new FormControl(proveedorRawValue.email, {
        validators: [Validators.required, Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}')],
      }),
      pathImagen: new FormControl(proveedorRawValue.pathImagen, {
        validators: [Validators.required],
      }),
      telefono: new FormControl(proveedorRawValue.telefono, {
        validators: [Validators.required],
      }),
    });
  }

  getProveedor(form: ProveedorFormGroup): IProveedor | NewProveedor {
    return form.getRawValue() as IProveedor | NewProveedor;
  }

  resetForm(form: ProveedorFormGroup, proveedor: ProveedorFormGroupInput): void {
    const proveedorRawValue = { ...this.getFormDefaults(), ...proveedor };
    form.reset(
      {
        ...proveedorRawValue,
        id: { value: proveedorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProveedorFormDefaults {
    return {
      id: null,
    };
  }
}
