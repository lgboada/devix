import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICentro } from 'app/entities/centro/centro.model';
import { CentroService } from 'app/entities/centro/service/centro.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IFactura } from '../factura.model';
import { FacturaService } from '../service/factura.service';
import { FacturaFormService } from './factura-form.service';

import { FacturaUpdateComponent } from './factura-update.component';

describe('Factura Management Update Component', () => {
  let comp: FacturaUpdateComponent;
  let fixture: ComponentFixture<FacturaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let facturaFormService: FacturaFormService;
  let facturaService: FacturaService;
  let centroService: CentroService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FacturaUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FacturaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FacturaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    facturaFormService = TestBed.inject(FacturaFormService);
    facturaService = TestBed.inject(FacturaService);
    centroService = TestBed.inject(CentroService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Centro query and add missing value', () => {
      const factura: IFactura = { id: 8873 };
      const centro: ICentro = { id: 10065 };
      factura.centro = centro;

      const centroCollection: ICentro[] = [{ id: 10065 }];
      jest.spyOn(centroService, 'query').mockReturnValue(of(new HttpResponse({ body: centroCollection })));
      const additionalCentros = [centro];
      const expectedCollection: ICentro[] = [...additionalCentros, ...centroCollection];
      jest.spyOn(centroService, 'addCentroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(centroService.query).toHaveBeenCalled();
      expect(centroService.addCentroToCollectionIfMissing).toHaveBeenCalledWith(
        centroCollection,
        ...additionalCentros.map(expect.objectContaining),
      );
      expect(comp.centrosSharedCollection).toEqual(expectedCollection);
    });

    it('should call Cliente query and add missing value', () => {
      const factura: IFactura = { id: 8873 };
      const cliente: ICliente = { id: 13484 };
      factura.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 13484 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const factura: IFactura = { id: 8873 };
      const centro: ICentro = { id: 10065 };
      factura.centro = centro;
      const cliente: ICliente = { id: 13484 };
      factura.cliente = cliente;

      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      expect(comp.centrosSharedCollection).toContainEqual(centro);
      expect(comp.clientesSharedCollection).toContainEqual(cliente);
      expect(comp.factura).toEqual(factura);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactura>>();
      const factura = { id: 30162 };
      jest.spyOn(facturaFormService, 'getFactura').mockReturnValue(factura);
      jest.spyOn(facturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factura }));
      saveSubject.complete();

      // THEN
      expect(facturaFormService.getFactura).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(facturaService.update).toHaveBeenCalledWith(expect.objectContaining(factura));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactura>>();
      const factura = { id: 30162 };
      jest.spyOn(facturaFormService, 'getFactura').mockReturnValue({ id: null });
      jest.spyOn(facturaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factura }));
      saveSubject.complete();

      // THEN
      expect(facturaFormService.getFactura).toHaveBeenCalled();
      expect(facturaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactura>>();
      const factura = { id: 30162 };
      jest.spyOn(facturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(facturaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCentro', () => {
      it('should forward to centroService', () => {
        const entity = { id: 10065 };
        const entity2 = { id: 27658 };
        jest.spyOn(centroService, 'compareCentro');
        comp.compareCentro(entity, entity2);
        expect(centroService.compareCentro).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCliente', () => {
      it('should forward to clienteService', () => {
        const entity = { id: 13484 };
        const entity2 = { id: 20795 };
        jest.spyOn(clienteService, 'compareCliente');
        comp.compareCliente(entity, entity2);
        expect(clienteService.compareCliente).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
