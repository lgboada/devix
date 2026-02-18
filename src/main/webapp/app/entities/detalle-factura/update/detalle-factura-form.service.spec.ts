import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../detalle-factura.test-samples';

import { DetalleFacturaFormService } from './detalle-factura-form.service';

describe('DetalleFactura Form Service', () => {
  let service: DetalleFacturaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetalleFacturaFormService);
  });

  describe('Service methods', () => {
    describe('createDetalleFacturaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDetalleFacturaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            cantidad: expect.any(Object),
            precioUnitario: expect.any(Object),
            subtotal: expect.any(Object),
            descuento: expect.any(Object),
            impuesto: expect.any(Object),
            total: expect.any(Object),
            factura: expect.any(Object),
            producto: expect.any(Object),
          }),
        );
      });

      it('passing IDetalleFactura should create a new form with FormGroup', () => {
        const formGroup = service.createDetalleFacturaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            cantidad: expect.any(Object),
            precioUnitario: expect.any(Object),
            subtotal: expect.any(Object),
            descuento: expect.any(Object),
            impuesto: expect.any(Object),
            total: expect.any(Object),
            factura: expect.any(Object),
            producto: expect.any(Object),
          }),
        );
      });
    });

    describe('getDetalleFactura', () => {
      it('should return NewDetalleFactura for default DetalleFactura initial value', () => {
        const formGroup = service.createDetalleFacturaFormGroup(sampleWithNewData);

        const detalleFactura = service.getDetalleFactura(formGroup) as any;

        expect(detalleFactura).toMatchObject(sampleWithNewData);
      });

      it('should return NewDetalleFactura for empty DetalleFactura initial value', () => {
        const formGroup = service.createDetalleFacturaFormGroup();

        const detalleFactura = service.getDetalleFactura(formGroup) as any;

        expect(detalleFactura).toMatchObject({});
      });

      it('should return IDetalleFactura', () => {
        const formGroup = service.createDetalleFacturaFormGroup(sampleWithRequiredData);

        const detalleFactura = service.getDetalleFactura(formGroup) as any;

        expect(detalleFactura).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDetalleFactura should not enable id FormControl', () => {
        const formGroup = service.createDetalleFacturaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDetalleFactura should disable id FormControl', () => {
        const formGroup = service.createDetalleFacturaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
