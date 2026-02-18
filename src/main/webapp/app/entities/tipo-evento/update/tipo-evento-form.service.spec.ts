import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tipo-evento.test-samples';

import { TipoEventoFormService } from './tipo-evento-form.service';

describe('TipoEvento Form Service', () => {
  let service: TipoEventoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoEventoFormService);
  });

  describe('Service methods', () => {
    describe('createTipoEventoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTipoEventoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            nombre: expect.any(Object),
          }),
        );
      });

      it('passing ITipoEvento should create a new form with FormGroup', () => {
        const formGroup = service.createTipoEventoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            nombre: expect.any(Object),
          }),
        );
      });
    });

    describe('getTipoEvento', () => {
      it('should return NewTipoEvento for default TipoEvento initial value', () => {
        const formGroup = service.createTipoEventoFormGroup(sampleWithNewData);

        const tipoEvento = service.getTipoEvento(formGroup) as any;

        expect(tipoEvento).toMatchObject(sampleWithNewData);
      });

      it('should return NewTipoEvento for empty TipoEvento initial value', () => {
        const formGroup = service.createTipoEventoFormGroup();

        const tipoEvento = service.getTipoEvento(formGroup) as any;

        expect(tipoEvento).toMatchObject({});
      });

      it('should return ITipoEvento', () => {
        const formGroup = service.createTipoEventoFormGroup(sampleWithRequiredData);

        const tipoEvento = service.getTipoEvento(formGroup) as any;

        expect(tipoEvento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITipoEvento should not enable id FormControl', () => {
        const formGroup = service.createTipoEventoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTipoEvento should disable id FormControl', () => {
        const formGroup = service.createTipoEventoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
