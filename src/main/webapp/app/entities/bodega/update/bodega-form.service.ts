import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBodega, NewBodega } from '../bodega.model';

type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

type BodegaFormGroupInput = IBodega | PartialWithRequiredKeyOf<NewBodega>;

type BodegaFormDefaults = Pick<NewBodega, 'id' | 'activa'>;

type BodegaFormGroupContent = {
  id: FormControl<IBodega['id'] | NewBodega['id']>;
  noCia: FormControl<IBodega['noCia']>;
  codigo: FormControl<IBodega['codigo']>;
  nombre: FormControl<IBodega['nombre']>;
  activa: FormControl<IBodega['activa']>;
};

export type BodegaFormGroup = FormGroup<BodegaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BodegaFormService {
  createBodegaFormGroup(bodega: BodegaFormGroupInput = { id: null }): BodegaFormGroup {
    const raw = {
      ...this.getFormDefaults(),
      ...bodega,
    };
    return new FormGroup<BodegaFormGroupContent>({
      id: new FormControl({ value: raw.id, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
      noCia: new FormControl(raw.noCia, { validators: [Validators.required] }),
      codigo: new FormControl(raw.codigo, { validators: [Validators.required, Validators.maxLength(50)] }),
      nombre: new FormControl(raw.nombre, { validators: [Validators.required, Validators.maxLength(255)] }),
      activa: new FormControl(raw.activa ?? true, { nonNullable: true, validators: [Validators.required] }),
    });
  }

  getBodega(form: BodegaFormGroup): IBodega | NewBodega {
    return form.getRawValue() as IBodega | NewBodega;
  }

  resetForm(form: BodegaFormGroup, bodega: BodegaFormGroupInput): void {
    const raw = { ...this.getFormDefaults(), ...bodega };
    form.reset({
      ...raw,
      id: { value: raw.id, disabled: true },
    } as any);
  }

  private getFormDefaults(): BodegaFormDefaults {
    return {
      id: null,
      activa: true,
    };
  }
}
