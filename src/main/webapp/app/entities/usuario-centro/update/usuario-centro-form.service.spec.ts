import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../usuario-centro.test-samples';

import { UsuarioCentroFormService } from './usuario-centro-form.service';

describe('UsuarioCentro Form Service', () => {
  let service: UsuarioCentroFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsuarioCentroFormService);
  });

  describe('Service methods', () => {
    describe('createUsuarioCentroFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUsuarioCentroFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            principal: expect.any(Object),
            centro: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IUsuarioCentro should create a new form with FormGroup', () => {
        const formGroup = service.createUsuarioCentroFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            principal: expect.any(Object),
            centro: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getUsuarioCentro', () => {
      it('should return NewUsuarioCentro for default UsuarioCentro initial value', () => {
        const formGroup = service.createUsuarioCentroFormGroup(sampleWithNewData);

        const usuarioCentro = service.getUsuarioCentro(formGroup) as any;

        expect(usuarioCentro).toMatchObject(sampleWithNewData);
      });

      it('should return NewUsuarioCentro for empty UsuarioCentro initial value', () => {
        const formGroup = service.createUsuarioCentroFormGroup();

        const usuarioCentro = service.getUsuarioCentro(formGroup) as any;

        expect(usuarioCentro).toMatchObject({});
      });

      it('should return IUsuarioCentro', () => {
        const formGroup = service.createUsuarioCentroFormGroup(sampleWithRequiredData);

        const usuarioCentro = service.getUsuarioCentro(formGroup) as any;

        expect(usuarioCentro).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUsuarioCentro should not enable id FormControl', () => {
        const formGroup = service.createUsuarioCentroFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUsuarioCentro should disable id FormControl', () => {
        const formGroup = service.createUsuarioCentroFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
