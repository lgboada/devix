import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../compania.test-samples';

import { CompaniaFormService } from './compania-form.service';

describe('Compania Form Service', () => {
  let service: CompaniaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompaniaFormService);
  });

  describe('Service methods', () => {
    describe('createCompaniaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompaniaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            dni: expect.any(Object),
            nombre: expect.any(Object),
            direccion: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
            pathImage: expect.any(Object),
          }),
        );
      });

      it('passing ICompania should create a new form with FormGroup', () => {
        const formGroup = service.createCompaniaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            dni: expect.any(Object),
            nombre: expect.any(Object),
            direccion: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
            pathImage: expect.any(Object),
          }),
        );
      });
    });

    describe('getCompania', () => {
      it('should return NewCompania for default Compania initial value', () => {
        const formGroup = service.createCompaniaFormGroup(sampleWithNewData);

        const compania = service.getCompania(formGroup) as any;

        expect(compania).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompania for empty Compania initial value', () => {
        const formGroup = service.createCompaniaFormGroup();

        const compania = service.getCompania(formGroup) as any;

        expect(compania).toMatchObject({});
      });

      it('should return ICompania', () => {
        const formGroup = service.createCompaniaFormGroup(sampleWithRequiredData);

        const compania = service.getCompania(formGroup) as any;

        expect(compania).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompania should not enable id FormControl', () => {
        const formGroup = service.createCompaniaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompania should disable id FormControl', () => {
        const formGroup = service.createCompaniaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
