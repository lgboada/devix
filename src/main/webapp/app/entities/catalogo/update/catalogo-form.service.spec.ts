import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../catalogo.test-samples';

import { CatalogoFormService } from './catalogo-form.service';

describe('Catalogo Form Service', () => {
  let service: CatalogoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CatalogoFormService);
  });

  describe('Service methods', () => {
    describe('createCatalogoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCatalogoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            descripcion1: expect.any(Object),
            descripcion2: expect.any(Object),
            estado: expect.any(Object),
            orden: expect.any(Object),
            texto1: expect.any(Object),
            texto2: expect.any(Object),
            tipoCatalogo: expect.any(Object),
          }),
        );
      });

      it('passing ICatalogo should create a new form with FormGroup', () => {
        const formGroup = service.createCatalogoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            descripcion1: expect.any(Object),
            descripcion2: expect.any(Object),
            estado: expect.any(Object),
            orden: expect.any(Object),
            texto1: expect.any(Object),
            texto2: expect.any(Object),
            tipoCatalogo: expect.any(Object),
          }),
        );
      });
    });

    describe('getCatalogo', () => {
      it('should return NewCatalogo for default Catalogo initial value', () => {
        const formGroup = service.createCatalogoFormGroup(sampleWithNewData);

        const catalogo = service.getCatalogo(formGroup) as any;

        expect(catalogo).toMatchObject(sampleWithNewData);
      });

      it('should return NewCatalogo for empty Catalogo initial value', () => {
        const formGroup = service.createCatalogoFormGroup();

        const catalogo = service.getCatalogo(formGroup) as any;

        expect(catalogo).toMatchObject({});
      });

      it('should return ICatalogo', () => {
        const formGroup = service.createCatalogoFormGroup(sampleWithRequiredData);

        const catalogo = service.getCatalogo(formGroup) as any;

        expect(catalogo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICatalogo should not enable id FormControl', () => {
        const formGroup = service.createCatalogoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCatalogo should disable id FormControl', () => {
        const formGroup = service.createCatalogoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
