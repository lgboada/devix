import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../evento.test-samples';

import { EventoFormService } from './evento-form.service';

describe('Evento Form Service', () => {
  let service: EventoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventoFormService);
  });

  describe('Service methods', () => {
    describe('createEventoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEventoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            descripcion: expect.any(Object),
            fecha: expect.any(Object),
            estado: expect.any(Object),
            motivoConsulta: expect.any(Object),
            tratamiento: expect.any(Object),
            indicaciones: expect.any(Object),
            diagnostico1: expect.any(Object),
            diagnostico2: expect.any(Object),
            diagnostico3: expect.any(Object),
            diagnostico4: expect.any(Object),
            diagnostico5: expect.any(Object),
            diagnostico6: expect.any(Object),
            diagnostico7: expect.any(Object),
            observacion: expect.any(Object),
            tipoEvento: expect.any(Object),
            centro: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });

      it('passing IEvento should create a new form with FormGroup', () => {
        const formGroup = service.createEventoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            descripcion: expect.any(Object),
            fecha: expect.any(Object),
            estado: expect.any(Object),
            motivoConsulta: expect.any(Object),
            tratamiento: expect.any(Object),
            indicaciones: expect.any(Object),
            diagnostico1: expect.any(Object),
            diagnostico2: expect.any(Object),
            diagnostico3: expect.any(Object),
            diagnostico4: expect.any(Object),
            diagnostico5: expect.any(Object),
            diagnostico6: expect.any(Object),
            diagnostico7: expect.any(Object),
            observacion: expect.any(Object),
            tipoEvento: expect.any(Object),
            centro: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });
    });

    describe('getEvento', () => {
      it('should return NewEvento for default Evento initial value', () => {
        const formGroup = service.createEventoFormGroup(sampleWithNewData);

        const evento = service.getEvento(formGroup) as any;

        expect(evento).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvento for empty Evento initial value', () => {
        const formGroup = service.createEventoFormGroup();

        const evento = service.getEvento(formGroup) as any;

        expect(evento).toMatchObject({});
      });

      it('should return IEvento', () => {
        const formGroup = service.createEventoFormGroup(sampleWithRequiredData);

        const evento = service.getEvento(formGroup) as any;

        expect(evento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvento should not enable id FormControl', () => {
        const formGroup = service.createEventoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvento should disable id FormControl', () => {
        const formGroup = service.createEventoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
