import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tipo-cliente.test-samples';

import { TipoClienteFormService } from './tipo-cliente-form.service';

describe('TipoCliente Form Service', () => {
  let service: TipoClienteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoClienteFormService);
  });

  describe('Service methods', () => {
    describe('createTipoClienteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTipoClienteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            descripcion: expect.any(Object),
          }),
        );
      });

      it('passing ITipoCliente should create a new form with FormGroup', () => {
        const formGroup = service.createTipoClienteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            noCia: expect.any(Object),
            descripcion: expect.any(Object),
          }),
        );
      });
    });

    describe('getTipoCliente', () => {
      it('should return NewTipoCliente for default TipoCliente initial value', () => {
        const formGroup = service.createTipoClienteFormGroup(sampleWithNewData);

        const tipoCliente = service.getTipoCliente(formGroup) as any;

        expect(tipoCliente).toMatchObject(sampleWithNewData);
      });

      it('should return NewTipoCliente for empty TipoCliente initial value', () => {
        const formGroup = service.createTipoClienteFormGroup();

        const tipoCliente = service.getTipoCliente(formGroup) as any;

        expect(tipoCliente).toMatchObject({});
      });

      it('should return ITipoCliente', () => {
        const formGroup = service.createTipoClienteFormGroup(sampleWithRequiredData);

        const tipoCliente = service.getTipoCliente(formGroup) as any;

        expect(tipoCliente).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITipoCliente should not enable id FormControl', () => {
        const formGroup = service.createTipoClienteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTipoCliente should disable id FormControl', () => {
        const formGroup = service.createTipoClienteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
