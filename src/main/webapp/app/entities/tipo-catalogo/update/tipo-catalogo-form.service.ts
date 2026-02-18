import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITipoCatalogo, NewTipoCatalogo } from '../tipo-catalogo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoCatalogo for edit and NewTipoCatalogoFormGroupInput for create.
 */
type TipoCatalogoFormGroupInput = ITipoCatalogo | PartialWithRequiredKeyOf<NewTipoCatalogo>;

type TipoCatalogoFormDefaults = Pick<NewTipoCatalogo, 'id'>;

type TipoCatalogoFormGroupContent = {
  id: FormControl<ITipoCatalogo['id'] | NewTipoCatalogo['id']>;
  noCia: FormControl<ITipoCatalogo['noCia']>;
  descripcion: FormControl<ITipoCatalogo['descripcion']>;
  categoria: FormControl<ITipoCatalogo['categoria']>;
};

export type TipoCatalogoFormGroup = FormGroup<TipoCatalogoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoCatalogoFormService {
  createTipoCatalogoFormGroup(tipoCatalogo: TipoCatalogoFormGroupInput = { id: null }): TipoCatalogoFormGroup {
    const tipoCatalogoRawValue = {
      ...this.getFormDefaults(),
      ...tipoCatalogo,
    };
    return new FormGroup<TipoCatalogoFormGroupContent>({
      id: new FormControl(
        { value: tipoCatalogoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(tipoCatalogoRawValue.noCia, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(tipoCatalogoRawValue.descripcion, {
        validators: [Validators.required],
      }),
      categoria: new FormControl(tipoCatalogoRawValue.categoria, {
        validators: [Validators.required],
      }),
    });
  }

  getTipoCatalogo(form: TipoCatalogoFormGroup): ITipoCatalogo | NewTipoCatalogo {
    return form.getRawValue() as ITipoCatalogo | NewTipoCatalogo;
  }

  resetForm(form: TipoCatalogoFormGroup, tipoCatalogo: TipoCatalogoFormGroupInput): void {
    const tipoCatalogoRawValue = { ...this.getFormDefaults(), ...tipoCatalogo };
    form.reset(
      {
        ...tipoCatalogoRawValue,
        id: { value: tipoCatalogoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoCatalogoFormDefaults {
    return {
      id: null,
    };
  }
}
