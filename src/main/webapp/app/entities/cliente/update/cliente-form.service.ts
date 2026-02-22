import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICliente, NewCliente } from '../cliente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICliente for edit and NewClienteFormGroupInput for create.
 */
type ClienteFormGroupInput = ICliente | PartialWithRequiredKeyOf<NewCliente>;

type ClienteFormDefaults = Pick<NewCliente, 'id'>;

type ClienteFormGroupContent = {
  id: FormControl<ICliente['id'] | NewCliente['id']>;
  noCia: FormControl<ICliente['noCia']>;
  dni: FormControl<ICliente['dni']>;
  nombres: FormControl<ICliente['nombres']>;
  apellidos: FormControl<ICliente['apellidos']>;
  nombreComercial: FormControl<ICliente['nombreComercial']>;
  email: FormControl<ICliente['email']>;
  telefono1: FormControl<ICliente['telefono1']>;
  telefono2: FormControl<ICliente['telefono2']>;
  fechaNacimiento: FormControl<ICliente['fechaNacimiento']>;
  sexo: FormControl<ICliente['sexo']>;
  estadoCivil: FormControl<ICliente['estadoCivil']>;
  tipoSangre: FormControl<ICliente['tipoSangre']>;
  pathImagen: FormControl<ICliente['pathImagen']>;
  tipoCliente: FormControl<ICliente['tipoCliente']>;
  ciudad: FormControl<ICliente['ciudad']>;
};

export type ClienteFormGroup = FormGroup<ClienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClienteFormService {
  createClienteFormGroup(cliente: ClienteFormGroupInput = { id: null }): ClienteFormGroup {
    const clienteRawValue = {
      ...this.getFormDefaults(),
      ...cliente,
    };
    return new FormGroup<ClienteFormGroupContent>({
      id: new FormControl(
        { value: clienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(clienteRawValue.noCia, {
        validators: [Validators.required],
      }),
      dni: new FormControl(clienteRawValue.dni, {
        validators: [Validators.required],
      }),
      nombres: new FormControl(clienteRawValue.nombres, {
        validators: [Validators.required],
      }),
      apellidos: new FormControl(clienteRawValue.apellidos, {
        validators: [Validators.required],
      }),
      nombreComercial: new FormControl(clienteRawValue.nombreComercial),
      email: new FormControl(clienteRawValue.email, {
        validators: [Validators.required, Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}')],
      }),
      telefono1: new FormControl(clienteRawValue.telefono1),
      telefono2: new FormControl(clienteRawValue.telefono2),
      fechaNacimiento: new FormControl(clienteRawValue.fechaNacimiento, {
        validators: [Validators.required],
      }),
      sexo: new FormControl(clienteRawValue.sexo, {
        validators: [Validators.required],
      }),
      estadoCivil: new FormControl(clienteRawValue.estadoCivil, {
        validators: [Validators.required],
      }),
      tipoSangre: new FormControl(clienteRawValue.tipoSangre, {
        validators: [Validators.required],
      }),
      pathImagen: new FormControl(clienteRawValue.pathImagen, {
        validators: [Validators.required],
      }),
      tipoCliente: new FormControl(clienteRawValue.tipoCliente),
      ciudad: new FormControl(clienteRawValue.ciudad),
    });
  }

  getCliente(form: ClienteFormGroup): ICliente | NewCliente {
    return form.getRawValue() as ICliente | NewCliente;
  }

  resetForm(form: ClienteFormGroup, cliente: ClienteFormGroupInput): void {
    const clienteRawValue = { ...this.getFormDefaults(), ...cliente };
    form.reset(
      {
        ...clienteRawValue,
        id: { value: clienteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClienteFormDefaults {
    return {
      id: null,
    };
  }
}
