import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITipoDocumento } from '../tipo-documento.model';

type TipoDocumentoFormGroupContent = {
  noCia: FormControl<ITipoDocumento['noCia'] | null>;
  tipoDocumento: FormControl<ITipoDocumento['tipoDocumento']>;
  descripcion: FormControl<ITipoDocumento['descripcion']>;
  indice: FormControl<ITipoDocumento['indice']>;
  codigoSri: FormControl<ITipoDocumento['codigoSri']>;
};

export type TipoDocumentoFormGroup = FormGroup<TipoDocumentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoDocumentoFormService {
  createTipoDocumentoFormGroup(tipoDocumento: Partial<ITipoDocumento> = {}): TipoDocumentoFormGroup {
    return new FormGroup<TipoDocumentoFormGroupContent>({
      noCia: new FormControl(tipoDocumento.noCia ?? null, { nonNullable: false, validators: [Validators.required] }),
      tipoDocumento: new FormControl(tipoDocumento.tipoDocumento ?? '', {
        nonNullable: true,
        validators: [Validators.required, Validators.maxLength(3)],
      }),
      descripcion: new FormControl(tipoDocumento.descripcion ?? null, { validators: [Validators.maxLength(60)] }),
      indice: new FormControl(tipoDocumento.indice ?? null, { validators: [Validators.maxLength(2)] }),
      codigoSri: new FormControl(tipoDocumento.codigoSri ?? null, { validators: [Validators.maxLength(2)] }),
    });
  }

  getTipoDocumento(form: TipoDocumentoFormGroup): ITipoDocumento {
    return form.getRawValue() as ITipoDocumento;
  }

  resetForm(form: TipoDocumentoFormGroup, tipoDocumento: Partial<ITipoDocumento>): void {
    form.reset({ ...tipoDocumento } as any);
  }
}
