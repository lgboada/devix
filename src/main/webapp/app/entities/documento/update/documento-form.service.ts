import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumento, NewDocumento } from '../documento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumento for edit and NewDocumentoFormGroupInput for create.
 */
type DocumentoFormGroupInput = IDocumento | PartialWithRequiredKeyOf<NewDocumento>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumento | NewDocumento> = Omit<T, 'fechaCreacion' | 'fechaVencimiento'> & {
  fechaCreacion?: string | null;
  fechaVencimiento?: string | null;
};

type DocumentoFormRawValue = FormValueOf<IDocumento>;

type NewDocumentoFormRawValue = FormValueOf<NewDocumento>;

type DocumentoFormDefaults = Pick<NewDocumento, 'id' | 'fechaCreacion' | 'fechaVencimiento'>;

type DocumentoFormGroupContent = {
  id: FormControl<DocumentoFormRawValue['id'] | NewDocumento['id']>;
  noCia: FormControl<DocumentoFormRawValue['noCia']>;
  tipo: FormControl<DocumentoFormRawValue['tipo']>;
  observacion: FormControl<DocumentoFormRawValue['observacion']>;
  fechaCreacion: FormControl<DocumentoFormRawValue['fechaCreacion']>;
  fechaVencimiento: FormControl<DocumentoFormRawValue['fechaVencimiento']>;
  path: FormControl<DocumentoFormRawValue['path']>;
  cliente: FormControl<DocumentoFormRawValue['cliente']>;
  evento: FormControl<DocumentoFormRawValue['evento']>;
};

export type DocumentoFormGroup = FormGroup<DocumentoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentoFormService {
  createDocumentoFormGroup(documento: DocumentoFormGroupInput = { id: null }): DocumentoFormGroup {
    const documentoRawValue = this.convertDocumentoToDocumentoRawValue({
      ...this.getFormDefaults(),
      ...documento,
    });
    return new FormGroup<DocumentoFormGroupContent>({
      id: new FormControl(
        { value: documentoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(documentoRawValue.noCia, {
        validators: [Validators.required],
      }),
      tipo: new FormControl(documentoRawValue.tipo),
      observacion: new FormControl(documentoRawValue.observacion),
      fechaCreacion: new FormControl(documentoRawValue.fechaCreacion),
      fechaVencimiento: new FormControl(documentoRawValue.fechaVencimiento),
      path: new FormControl(documentoRawValue.path, {
        validators: [Validators.required],
      }),
      cliente: new FormControl(documentoRawValue.cliente),
      evento: new FormControl(documentoRawValue.evento),
    });
  }

  getDocumento(form: DocumentoFormGroup): IDocumento | NewDocumento {
    return this.convertDocumentoRawValueToDocumento(form.getRawValue() as DocumentoFormRawValue | NewDocumentoFormRawValue);
  }

  resetForm(form: DocumentoFormGroup, documento: DocumentoFormGroupInput): void {
    const documentoRawValue = this.convertDocumentoToDocumentoRawValue({ ...this.getFormDefaults(), ...documento });
    form.reset(
      {
        ...documentoRawValue,
        id: { value: documentoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
      fechaVencimiento: currentTime,
    };
  }

  private convertDocumentoRawValueToDocumento(rawDocumento: DocumentoFormRawValue | NewDocumentoFormRawValue): IDocumento | NewDocumento {
    return {
      ...rawDocumento,
      fechaCreacion: dayjs(rawDocumento.fechaCreacion, DATE_TIME_FORMAT),
      fechaVencimiento: dayjs(rawDocumento.fechaVencimiento, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentoToDocumentoRawValue(
    documento: IDocumento | (Partial<NewDocumento> & DocumentoFormDefaults),
  ): DocumentoFormRawValue | PartialWithRequiredKeyOf<NewDocumentoFormRawValue> {
    return {
      ...documento,
      fechaCreacion: documento.fechaCreacion ? documento.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
      fechaVencimiento: documento.fechaVencimiento ? documento.fechaVencimiento.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
