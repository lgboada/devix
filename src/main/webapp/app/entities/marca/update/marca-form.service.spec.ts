import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../marca.test-samples';

import { MarcaFormService } from './marca-form.service';

describe('Marca Form Service', () => {
  let service: MarcaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MarcaFormService);
  });

  describe('Service methods', () => {
    describe('createMarcaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMarcaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            nombre: expect.any(Object),
            pathImagen: expect.any(Object),
          }),
        );
      });

      it('passing IMarca should create a new form with FormGroup', () => {
        const formGroup = service.createMarcaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            nombre: expect.any(Object),
            pathImagen: expect.any(Object),
          }),
        );
      });
    });

    describe('getMarca', () => {
      it('should return NewMarca for default Marca initial value', () => {
        const formGroup = service.createMarcaFormGroup(sampleWithNewData);

        const marca = service.getMarca(formGroup) as any;

        expect(marca).toMatchObject(sampleWithNewData);
      });

      it('should return NewMarca for empty Marca initial value', () => {
        const formGroup = service.createMarcaFormGroup();

        const marca = service.getMarca(formGroup) as any;

        expect(marca).toMatchObject({});
      });

      it('should return IMarca', () => {
        const formGroup = service.createMarcaFormGroup(sampleWithRequiredData);

        const marca = service.getMarca(formGroup) as any;

        expect(marca).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMarca should not enable id FormControl', () => {
        const formGroup = service.createMarcaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMarca should disable id FormControl', () => {
        const formGroup = service.createMarcaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
