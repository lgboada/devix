import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tipo-producto.test-samples';

import { TipoProductoFormService } from './tipo-producto-form.service';

describe('TipoProducto Form Service', () => {
  let service: TipoProductoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoProductoFormService);
  });

  describe('Service methods', () => {
    describe('createTipoProductoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTipoProductoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            nombre: expect.any(Object),
          }),
        );
      });

      it('passing ITipoProducto should create a new form with FormGroup', () => {
        const formGroup = service.createTipoProductoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            nombre: expect.any(Object),
          }),
        );
      });
    });

    describe('getTipoProducto', () => {
      it('should return NewTipoProducto for default TipoProducto initial value', () => {
        const formGroup = service.createTipoProductoFormGroup(sampleWithNewData);

        const tipoProducto = service.getTipoProducto(formGroup) as any;

        expect(tipoProducto).toMatchObject(sampleWithNewData);
      });

      it('should return NewTipoProducto for empty TipoProducto initial value', () => {
        const formGroup = service.createTipoProductoFormGroup();

        const tipoProducto = service.getTipoProducto(formGroup) as any;

        expect(tipoProducto).toMatchObject({});
      });

      it('should return ITipoProducto', () => {
        const formGroup = service.createTipoProductoFormGroup(sampleWithRequiredData);

        const tipoProducto = service.getTipoProducto(formGroup) as any;

        expect(tipoProducto).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITipoProducto should not enable id FormControl', () => {
        const formGroup = service.createTipoProductoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTipoProducto should disable id FormControl', () => {
        const formGroup = service.createTipoProductoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
