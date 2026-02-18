import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDetalleFactura, NewDetalleFactura } from '../detalle-factura.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDetalleFactura for edit and NewDetalleFacturaFormGroupInput for create.
 */
type DetalleFacturaFormGroupInput = IDetalleFactura | PartialWithRequiredKeyOf<NewDetalleFactura>;

type DetalleFacturaFormDefaults = Pick<NewDetalleFactura, 'id'>;

type DetalleFacturaFormGroupContent = {
  id: FormControl<IDetalleFactura['id'] | NewDetalleFactura['id']>;
  noCia: FormControl<IDetalleFactura['noCia']>;
  cantidad: FormControl<IDetalleFactura['cantidad']>;
  precioUnitario: FormControl<IDetalleFactura['precioUnitario']>;
  subtotal: FormControl<IDetalleFactura['subtotal']>;
  descuento: FormControl<IDetalleFactura['descuento']>;
  impuesto: FormControl<IDetalleFactura['impuesto']>;
  total: FormControl<IDetalleFactura['total']>;
  factura: FormControl<IDetalleFactura['factura']>;
  producto: FormControl<IDetalleFactura['producto']>;
};

export type DetalleFacturaFormGroup = FormGroup<DetalleFacturaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DetalleFacturaFormService {
  createDetalleFacturaFormGroup(detalleFactura: DetalleFacturaFormGroupInput = { id: null }): DetalleFacturaFormGroup {
    const detalleFacturaRawValue = {
      ...this.getFormDefaults(),
      ...detalleFactura,
    };
    return new FormGroup<DetalleFacturaFormGroupContent>({
      id: new FormControl(
        { value: detalleFacturaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(detalleFacturaRawValue.noCia, {
        validators: [Validators.required],
      }),
      cantidad: new FormControl(detalleFacturaRawValue.cantidad, {
        validators: [Validators.required, Validators.min(1)],
      }),
      precioUnitario: new FormControl(detalleFacturaRawValue.precioUnitario, {
        validators: [Validators.required, Validators.min(0)],
      }),
      subtotal: new FormControl(detalleFacturaRawValue.subtotal, {
        validators: [Validators.required, Validators.min(0)],
      }),
      descuento: new FormControl(detalleFacturaRawValue.descuento, {
        validators: [Validators.required, Validators.min(0)],
      }),
      impuesto: new FormControl(detalleFacturaRawValue.impuesto, {
        validators: [Validators.required, Validators.min(0)],
      }),
      total: new FormControl(detalleFacturaRawValue.total, {
        validators: [Validators.required, Validators.min(0)],
      }),
      factura: new FormControl(detalleFacturaRawValue.factura),
      producto: new FormControl(detalleFacturaRawValue.producto),
    });
  }

  getDetalleFactura(form: DetalleFacturaFormGroup): IDetalleFactura | NewDetalleFactura {
    return form.getRawValue() as IDetalleFactura | NewDetalleFactura;
  }

  resetForm(form: DetalleFacturaFormGroup, detalleFactura: DetalleFacturaFormGroupInput): void {
    const detalleFacturaRawValue = { ...this.getFormDefaults(), ...detalleFactura };
    form.reset(
      {
        ...detalleFacturaRawValue,
        id: { value: detalleFacturaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DetalleFacturaFormDefaults {
    return {
      id: null,
    };
  }
}
