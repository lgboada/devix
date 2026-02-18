import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFactura, NewFactura } from '../factura.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactura for edit and NewFacturaFormGroupInput for create.
 */
type FacturaFormGroupInput = IFactura | PartialWithRequiredKeyOf<NewFactura>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFactura | NewFactura> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

type FacturaFormRawValue = FormValueOf<IFactura>;

type NewFacturaFormRawValue = FormValueOf<NewFactura>;

type FacturaFormDefaults = Pick<NewFactura, 'id' | 'fecha'>;

type FacturaFormGroupContent = {
  id: FormControl<FacturaFormRawValue['id'] | NewFactura['id']>;
  noCia: FormControl<FacturaFormRawValue['noCia']>;
  serie: FormControl<FacturaFormRawValue['serie']>;
  noFisico: FormControl<FacturaFormRawValue['noFisico']>;
  fecha: FormControl<FacturaFormRawValue['fecha']>;
  subtotal: FormControl<FacturaFormRawValue['subtotal']>;
  impuesto: FormControl<FacturaFormRawValue['impuesto']>;
  impuestoCero: FormControl<FacturaFormRawValue['impuestoCero']>;
  descuento: FormControl<FacturaFormRawValue['descuento']>;
  total: FormControl<FacturaFormRawValue['total']>;
  porcentajeImpuesto: FormControl<FacturaFormRawValue['porcentajeImpuesto']>;
  cedula: FormControl<FacturaFormRawValue['cedula']>;
  direccion: FormControl<FacturaFormRawValue['direccion']>;
  email: FormControl<FacturaFormRawValue['email']>;
  estado: FormControl<FacturaFormRawValue['estado']>;
  centro: FormControl<FacturaFormRawValue['centro']>;
  cliente: FormControl<FacturaFormRawValue['cliente']>;
};

export type FacturaFormGroup = FormGroup<FacturaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FacturaFormService {
  createFacturaFormGroup(factura: FacturaFormGroupInput = { id: null }): FacturaFormGroup {
    const facturaRawValue = this.convertFacturaToFacturaRawValue({
      ...this.getFormDefaults(),
      ...factura,
    });
    return new FormGroup<FacturaFormGroupContent>({
      id: new FormControl(
        { value: facturaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(facturaRawValue.noCia, {
        validators: [Validators.required],
      }),
      serie: new FormControl(facturaRawValue.serie, {
        validators: [Validators.required],
      }),
      noFisico: new FormControl(facturaRawValue.noFisico, {
        validators: [Validators.required],
      }),
      fecha: new FormControl(facturaRawValue.fecha, {
        validators: [Validators.required],
      }),
      subtotal: new FormControl(facturaRawValue.subtotal, {
        validators: [Validators.required, Validators.min(0)],
      }),
      impuesto: new FormControl(facturaRawValue.impuesto, {
        validators: [Validators.required, Validators.min(0)],
      }),
      impuestoCero: new FormControl(facturaRawValue.impuestoCero, {
        validators: [Validators.required, Validators.min(0)],
      }),
      descuento: new FormControl(facturaRawValue.descuento, {
        validators: [Validators.required, Validators.min(0)],
      }),
      total: new FormControl(facturaRawValue.total, {
        validators: [Validators.required, Validators.min(0)],
      }),
      porcentajeImpuesto: new FormControl(facturaRawValue.porcentajeImpuesto, {
        validators: [Validators.required, Validators.min(0)],
      }),
      cedula: new FormControl(facturaRawValue.cedula, {
        validators: [Validators.required],
      }),
      direccion: new FormControl(facturaRawValue.direccion, {
        validators: [Validators.required],
      }),
      email: new FormControl(facturaRawValue.email, {
        validators: [Validators.required],
      }),
      estado: new FormControl(facturaRawValue.estado, {
        validators: [Validators.required],
      }),
      centro: new FormControl(facturaRawValue.centro),
      cliente: new FormControl(facturaRawValue.cliente),
    });
  }

  getFactura(form: FacturaFormGroup): IFactura | NewFactura {
    return this.convertFacturaRawValueToFactura(form.getRawValue() as FacturaFormRawValue | NewFacturaFormRawValue);
  }

  resetForm(form: FacturaFormGroup, factura: FacturaFormGroupInput): void {
    const facturaRawValue = this.convertFacturaToFacturaRawValue({ ...this.getFormDefaults(), ...factura });
    form.reset(
      {
        ...facturaRawValue,
        id: { value: facturaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FacturaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fecha: currentTime,
    };
  }

  private convertFacturaRawValueToFactura(rawFactura: FacturaFormRawValue | NewFacturaFormRawValue): IFactura | NewFactura {
    return {
      ...rawFactura,
      fecha: dayjs(rawFactura.fecha, DATE_TIME_FORMAT),
    };
  }

  private convertFacturaToFacturaRawValue(
    factura: IFactura | (Partial<NewFactura> & FacturaFormDefaults),
  ): FacturaFormRawValue | PartialWithRequiredKeyOf<NewFacturaFormRawValue> {
    return {
      ...factura,
      fecha: factura.fecha ? factura.fecha.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
