import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEvento, NewEvento } from '../evento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvento for edit and NewEventoFormGroupInput for create.
 */
type EventoFormGroupInput = IEvento | PartialWithRequiredKeyOf<NewEvento>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEvento | NewEvento> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

type EventoFormRawValue = FormValueOf<IEvento>;

type NewEventoFormRawValue = FormValueOf<NewEvento>;

type EventoFormDefaults = Pick<NewEvento, 'id' | 'fecha'>;

type EventoFormGroupContent = {
  id: FormControl<EventoFormRawValue['id'] | NewEvento['id']>;
  noCia: FormControl<EventoFormRawValue['noCia']>;
  descripcion: FormControl<EventoFormRawValue['descripcion']>;
  fecha: FormControl<EventoFormRawValue['fecha']>;
  estado: FormControl<EventoFormRawValue['estado']>;
  motivoConsulta: FormControl<EventoFormRawValue['motivoConsulta']>;
  tratamiento: FormControl<EventoFormRawValue['tratamiento']>;
  indicaciones: FormControl<EventoFormRawValue['indicaciones']>;
  diagnostico1: FormControl<EventoFormRawValue['diagnostico1']>;
  diagnostico2: FormControl<EventoFormRawValue['diagnostico2']>;
  diagnostico3: FormControl<EventoFormRawValue['diagnostico3']>;
  diagnostico4: FormControl<EventoFormRawValue['diagnostico4']>;
  diagnostico5: FormControl<EventoFormRawValue['diagnostico5']>;
  diagnostico6: FormControl<EventoFormRawValue['diagnostico6']>;
  diagnostico7: FormControl<EventoFormRawValue['diagnostico7']>;
  observacion: FormControl<EventoFormRawValue['observacion']>;
  tipoEvento: FormControl<EventoFormRawValue['tipoEvento']>;
  centro: FormControl<EventoFormRawValue['centro']>;
  cliente: FormControl<EventoFormRawValue['cliente']>;
};

export type EventoFormGroup = FormGroup<EventoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventoFormService {
  createEventoFormGroup(evento: EventoFormGroupInput = { id: null }): EventoFormGroup {
    const eventoRawValue = this.convertEventoToEventoRawValue({
      ...this.getFormDefaults(),
      ...evento,
    });
    return new FormGroup<EventoFormGroupContent>({
      id: new FormControl(
        { value: eventoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      noCia: new FormControl(eventoRawValue.noCia, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(eventoRawValue.descripcion, {
        validators: [Validators.required],
      }),
      fecha: new FormControl(eventoRawValue.fecha, {
        validators: [Validators.required],
      }),
      estado: new FormControl(eventoRawValue.estado),
      motivoConsulta: new FormControl(eventoRawValue.motivoConsulta),
      tratamiento: new FormControl(eventoRawValue.tratamiento),
      indicaciones: new FormControl(eventoRawValue.indicaciones),
      diagnostico1: new FormControl(eventoRawValue.diagnostico1),
      diagnostico2: new FormControl(eventoRawValue.diagnostico2),
      diagnostico3: new FormControl(eventoRawValue.diagnostico3),
      diagnostico4: new FormControl(eventoRawValue.diagnostico4),
      diagnostico5: new FormControl(eventoRawValue.diagnostico5),
      diagnostico6: new FormControl(eventoRawValue.diagnostico6),
      diagnostico7: new FormControl(eventoRawValue.diagnostico7),
      observacion: new FormControl(eventoRawValue.observacion),
      tipoEvento: new FormControl(eventoRawValue.tipoEvento),
      centro: new FormControl(eventoRawValue.centro),
      cliente: new FormControl(eventoRawValue.cliente),
    });
  }

  getEvento(form: EventoFormGroup): IEvento | NewEvento {
    return this.convertEventoRawValueToEvento(form.getRawValue() as EventoFormRawValue | NewEventoFormRawValue);
  }

  resetForm(form: EventoFormGroup, evento: EventoFormGroupInput): void {
    const eventoRawValue = this.convertEventoToEventoRawValue({ ...this.getFormDefaults(), ...evento });
    form.reset(
      {
        ...eventoRawValue,
        id: { value: eventoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EventoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fecha: currentTime,
    };
  }

  private convertEventoRawValueToEvento(rawEvento: EventoFormRawValue | NewEventoFormRawValue): IEvento | NewEvento {
    return {
      ...rawEvento,
      fecha: dayjs(rawEvento.fecha, DATE_TIME_FORMAT),
    };
  }

  private convertEventoToEventoRawValue(
    evento: IEvento | (Partial<NewEvento> & EventoFormDefaults),
  ): EventoFormRawValue | PartialWithRequiredKeyOf<NewEventoFormRawValue> {
    return {
      ...evento,
      fecha: evento.fecha ? evento.fecha.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
