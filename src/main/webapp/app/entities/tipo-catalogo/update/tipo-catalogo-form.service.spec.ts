import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tipo-catalogo.test-samples';

import { TipoCatalogoFormService } from './tipo-catalogo-form.service';

describe('TipoCatalogo Form Service', () => {
  let service: TipoCatalogoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoCatalogoFormService);
  });

  describe('Service methods', () => {
    describe('createTipoCatalogoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTipoCatalogoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            descripcion: expect.any(Object),
            categoria: expect.any(Object),
          }),
        );
      });

      it('passing ITipoCatalogo should create a new form with FormGroup', () => {
        const formGroup = service.createTipoCatalogoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            descripcion: expect.any(Object),
            categoria: expect.any(Object),
          }),
        );
      });
    });

    describe('getTipoCatalogo', () => {
      it('should return NewTipoCatalogo for default TipoCatalogo initial value', () => {
        const formGroup = service.createTipoCatalogoFormGroup(sampleWithNewData);

        const tipoCatalogo = service.getTipoCatalogo(formGroup) as any;

        expect(tipoCatalogo).toMatchObject(sampleWithNewData);
      });

      it('should return NewTipoCatalogo for empty TipoCatalogo initial value', () => {
        const formGroup = service.createTipoCatalogoFormGroup();

        const tipoCatalogo = service.getTipoCatalogo(formGroup) as any;

        expect(tipoCatalogo).toMatchObject({});
      });

      it('should return ITipoCatalogo', () => {
        const formGroup = service.createTipoCatalogoFormGroup(sampleWithRequiredData);

        const tipoCatalogo = service.getTipoCatalogo(formGroup) as any;

        expect(tipoCatalogo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITipoCatalogo should not enable id FormControl', () => {
        const formGroup = service.createTipoCatalogoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTipoCatalogo should disable id FormControl', () => {
        const formGroup = service.createTipoCatalogoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
