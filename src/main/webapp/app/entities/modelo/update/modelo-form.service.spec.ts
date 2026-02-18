import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../modelo.test-samples';

import { ModeloFormService } from './modelo-form.service';

describe('Modelo Form Service', () => {
  let service: ModeloFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModeloFormService);
  });

  describe('Service methods', () => {
    describe('createModeloFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createModeloFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            nombre: expect.any(Object),
            pathImagen: expect.any(Object),
            marca: expect.any(Object),
          }),
        );
      });

      it('passing IModelo should create a new form with FormGroup', () => {
        const formGroup = service.createModeloFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            nombre: expect.any(Object),
            pathImagen: expect.any(Object),
            marca: expect.any(Object),
          }),
        );
      });
    });

    describe('getModelo', () => {
      it('should return NewModelo for default Modelo initial value', () => {
        const formGroup = service.createModeloFormGroup(sampleWithNewData);

        const modelo = service.getModelo(formGroup) as any;

        expect(modelo).toMatchObject(sampleWithNewData);
      });

      it('should return NewModelo for empty Modelo initial value', () => {
        const formGroup = service.createModeloFormGroup();

        const modelo = service.getModelo(formGroup) as any;

        expect(modelo).toMatchObject({});
      });

      it('should return IModelo', () => {
        const formGroup = service.createModeloFormGroup(sampleWithRequiredData);

        const modelo = service.getModelo(formGroup) as any;

        expect(modelo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IModelo should not enable id FormControl', () => {
        const formGroup = service.createModeloFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewModelo should disable id FormControl', () => {
        const formGroup = service.createModeloFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
