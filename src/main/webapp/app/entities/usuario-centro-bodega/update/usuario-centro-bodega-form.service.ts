import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUsuarioCentroBodega, NewUsuarioCentroBodega } from '../usuario-centro-bodega.model';

type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

type UCBFormGroupInput = IUsuarioCentroBodega | PartialWithRequiredKeyOf<NewUsuarioCentroBodega>;

type UCBFormDefaults = Pick<NewUsuarioCentroBodega, 'id' | 'principal'>;

type UCBFormGroupContent = {
  id: FormControl<IUsuarioCentroBodega['id'] | NewUsuarioCentroBodega['id']>;
  noCia: FormControl<IUsuarioCentroBodega['noCia']>;
  principal: FormControl<IUsuarioCentroBodega['principal']>;
  bodega: FormControl<IUsuarioCentroBodega['bodega']>;
};

export type UCBFormGroup = FormGroup<UCBFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UsuarioCentroBodegaFormService {
  createFormGroup(ucb: UCBFormGroupInput = { id: null }): UCBFormGroup {
    const raw = { ...this.getDefaults(), ...ucb };
    return new FormGroup<UCBFormGroupContent>({
      id: new FormControl({ value: raw.id, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
      noCia: new FormControl(raw.noCia),
      principal: new FormControl(raw.principal ?? false, { nonNullable: true, validators: [Validators.required] }),
      bodega: new FormControl(raw.bodega, { validators: [Validators.required] }),
    });
  }

  getUCB(form: UCBFormGroup): IUsuarioCentroBodega | NewUsuarioCentroBodega {
    return form.getRawValue() as IUsuarioCentroBodega | NewUsuarioCentroBodega;
  }

  resetForm(form: UCBFormGroup, ucb: UCBFormGroupInput): void {
    const raw = { ...this.getDefaults(), ...ucb };
    form.reset({ ...raw, id: { value: raw.id, disabled: true } } as any);
  }

  private getDefaults(): UCBFormDefaults {
    return { id: null, principal: false };
  }
}
