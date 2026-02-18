import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../factura.test-samples';

import { FacturaFormService } from './factura-form.service';

describe('Factura Form Service', () => {
  let service: FacturaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FacturaFormService);
  });

  describe('Service methods', () => {
    describe('createFacturaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFacturaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            serie: expect.any(Object),
            noFisico: expect.any(Object),
            fecha: expect.any(Object),
            subtotal: expect.any(Object),
            impuesto: expect.any(Object),
            impuestoCero: expect.any(Object),
            descuento: expect.any(Object),
            total: expect.any(Object),
            porcentajeImpuesto: expect.any(Object),
            cedula: expect.any(Object),
            direccion: expect.any(Object),
            email: expect.any(Object),
            estado: expect.any(Object),
            centro: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });

      it('passing IFactura should create a new form with FormGroup', () => {
        const formGroup = service.createFacturaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            serie: expect.any(Object),
            noFisico: expect.any(Object),
            fecha: expect.any(Object),
            subtotal: expect.any(Object),
            impuesto: expect.any(Object),
            impuestoCero: expect.any(Object),
            descuento: expect.any(Object),
            total: expect.any(Object),
            porcentajeImpuesto: expect.any(Object),
            cedula: expect.any(Object),
            direccion: expect.any(Object),
            email: expect.any(Object),
            estado: expect.any(Object),
            centro: expect.any(Object),
            cliente: expect.any(Object),
          }),
        );
      });
    });

    describe('getFactura', () => {
      it('should return NewFactura for default Factura initial value', () => {
        const formGroup = service.createFacturaFormGroup(sampleWithNewData);

        const factura = service.getFactura(formGroup) as any;

        expect(factura).toMatchObject(sampleWithNewData);
      });

      it('should return NewFactura for empty Factura initial value', () => {
        const formGroup = service.createFacturaFormGroup();

        const factura = service.getFactura(formGroup) as any;

        expect(factura).toMatchObject({});
      });

      it('should return IFactura', () => {
        const formGroup = service.createFacturaFormGroup(sampleWithRequiredData);

        const factura = service.getFactura(formGroup) as any;

        expect(factura).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFactura should not enable id FormControl', () => {
        const formGroup = service.createFacturaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFactura should disable id FormControl', () => {
        const formGroup = service.createFacturaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
