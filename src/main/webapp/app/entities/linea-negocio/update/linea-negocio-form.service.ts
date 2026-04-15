import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILineaNegocio } from '../linea-negocio.model';

type LineaNegocioFormGroupContent = {
  noCia: FormControl<ILineaNegocio['noCia'] | null>;
  lineaNo: FormControl<ILineaNegocio['lineaNo']>;
  descripcion: FormControl<ILineaNegocio['descripcion']>;
};

export type LineaNegocioFormGroup = FormGroup<LineaNegocioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LineaNegocioFormService {
  createLineaNegocioFormGroup(lineaNegocio: Partial<ILineaNegocio> = {}): LineaNegocioFormGroup {
    return new FormGroup<LineaNegocioFormGroupContent>({
      noCia: new FormControl(lineaNegocio.noCia ?? null, { nonNullable: false, validators: [Validators.required] }),
      lineaNo: new FormControl(lineaNegocio.lineaNo ?? '', {
        nonNullable: true,
        validators: [Validators.required, Validators.maxLength(3)],
      }),
      descripcion: new FormControl(lineaNegocio.descripcion ?? null, { validators: [Validators.maxLength(60)] }),
    });
  }

  getLineaNegocio(form: LineaNegocioFormGroup): ILineaNegocio {
    return form.getRawValue() as ILineaNegocio;
  }

  resetForm(form: LineaNegocioFormGroup, lineaNegocio: Partial<ILineaNegocio>): void {
    form.reset({ ...lineaNegocio } as any);
  }
}
