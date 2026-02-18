import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICatalogo, NewCatalogo } from '../catalogo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICatalogo for edit and NewCatalogoFormGroupInput for create.
 */
type CatalogoFormGroupInput = ICatalogo | PartialWithRequiredKeyOf<NewCatalogo>;

type CatalogoFormDefaults = Pick<NewCatalogo, 'id'>;

type CatalogoFormGroupContent = {
  id: FormControl<ICatalogo['id'] | NewCatalogo['id']>;
  noCia: FormControl<ICatalogo['noCia']>;
  descripcion1: FormControl<ICatalogo['descripcion1']>;
  descripcion2: FormControl<ICatalogo['descripcion2']>;
  estado: FormControl<ICatalogo['estado']>;
  orden: FormControl<ICatalogo['orden']>;
  texto1: FormControl<ICatalogo['texto1']>;
  texto2: FormControl<ICatalogo['texto2']>;
  tipoCatalogo: FormControl<ICatalogo['tipoCatalogo']>;
};

export type CatalogoFormGroup = FormGroup<CatalogoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CatalogoFormService {
  createCatalogoFormGroup(catalogo: CatalogoFormGroupInput = { id: null }): CatalogoFormGroup {
    const catalogoRawValue = {
      ...this.getFormDefaults(),
      ...catalogo,
    };
    return new FormGroup<CatalogoFormGroupContent>({
      id: new FormControl(
        { value: catalogoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(catalogoRawValue.noCia, {
        validators: [Validators.required],
      }),
      descripcion1: new FormControl(catalogoRawValue.descripcion1, {
        validators: [Validators.required],
      }),
      descripcion2: new FormControl(catalogoRawValue.descripcion2),
      estado: new FormControl(catalogoRawValue.estado),
      orden: new FormControl(catalogoRawValue.orden),
      texto1: new FormControl(catalogoRawValue.texto1),
      texto2: new FormControl(catalogoRawValue.texto2),
      tipoCatalogo: new FormControl(catalogoRawValue.tipoCatalogo),
    });
  }

  getCatalogo(form: CatalogoFormGroup): ICatalogo | NewCatalogo {
    return form.getRawValue() as ICatalogo | NewCatalogo;
  }

  resetForm(form: CatalogoFormGroup, catalogo: CatalogoFormGroupInput): void {
    const catalogoRawValue = { ...this.getFormDefaults(), ...catalogo };
    form.reset(
      {
        ...catalogoRawValue,
        id: { value: catalogoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CatalogoFormDefaults {
    return {
      id: null,
    };
  }
}
