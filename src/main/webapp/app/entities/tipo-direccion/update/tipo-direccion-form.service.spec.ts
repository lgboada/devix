import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tipo-direccion.test-samples';

import { TipoDireccionFormService } from './tipo-direccion-form.service';

describe('TipoDireccion Form Service', () => {
  let service: TipoDireccionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoDireccionFormService);
  });

  describe('Service methods', () => {
    describe('createTipoDireccionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTipoDireccionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            descripcion: expect.any(Object),
          }),
        );
      });

      it('passing ITipoDireccion should create a new form with FormGroup', () => {
        const formGroup = service.createTipoDireccionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            descripcion: expect.any(Object),
          }),
        );
      });
    });

    describe('getTipoDireccion', () => {
      it('should return NewTipoDireccion for default TipoDireccion initial value', () => {
        const formGroup = service.createTipoDireccionFormGroup(sampleWithNewData);

        const tipoDireccion = service.getTipoDireccion(formGroup) as any;

        expect(tipoDireccion).toMatchObject(sampleWithNewData);
      });

      it('should return NewTipoDireccion for empty TipoDireccion initial value', () => {
        const formGroup = service.createTipoDireccionFormGroup();

        const tipoDireccion = service.getTipoDireccion(formGroup) as any;

        expect(tipoDireccion).toMatchObject({});
      });

      it('should return ITipoDireccion', () => {
        const formGroup = service.createTipoDireccionFormGroup(sampleWithRequiredData);

        const tipoDireccion = service.getTipoDireccion(formGroup) as any;

        expect(tipoDireccion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITipoDireccion should not enable id FormControl', () => {
        const formGroup = service.createTipoDireccionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTipoDireccion should disable id FormControl', () => {
        const formGroup = service.createTipoDireccionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
