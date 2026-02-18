import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IFactura } from 'app/entities/factura/factura.model';
import { FacturaService } from 'app/entities/factura/service/factura.service';
import { IProducto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { IDetalleFactura } from '../detalle-factura.model';
import { DetalleFacturaService } from '../service/detalle-factura.service';
import { DetalleFacturaFormService } from './detalle-factura-form.service';

import { DetalleFacturaUpdateComponent } from './detalle-factura-update.component';

describe('DetalleFactura Management Update Component', () => {
  let comp: DetalleFacturaUpdateComponent;
  let fixture: ComponentFixture<DetalleFacturaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detalleFacturaFormService: DetalleFacturaFormService;
  let detalleFacturaService: DetalleFacturaService;
  let facturaService: FacturaService;
  let productoService: ProductoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DetalleFacturaUpdateComponent],
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
      .overrideTemplate(DetalleFacturaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetalleFacturaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detalleFacturaFormService = TestBed.inject(DetalleFacturaFormService);
    detalleFacturaService = TestBed.inject(DetalleFacturaService);
    facturaService = TestBed.inject(FacturaService);
    productoService = TestBed.inject(ProductoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Factura query and add missing value', () => {
      const detalleFactura: IDetalleFactura = { id: 18345 };
      const factura: IFactura = { id: 30162 };
      detalleFactura.factura = factura;

      const facturaCollection: IFactura[] = [{ id: 30162 }];
      jest.spyOn(facturaService, 'query').mockReturnValue(of(new HttpResponse({ body: facturaCollection })));
      const additionalFacturas = [factura];
      const expectedCollection: IFactura[] = [...additionalFacturas, ...facturaCollection];
      jest.spyOn(facturaService, 'addFacturaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      expect(facturaService.query).toHaveBeenCalled();
      expect(facturaService.addFacturaToCollectionIfMissing).toHaveBeenCalledWith(
        facturaCollection,
        ...additionalFacturas.map(expect.objectContaining),
      );
      expect(comp.facturasSharedCollection).toEqual(expectedCollection);
    });

    it('should call Producto query and add missing value', () => {
      const detalleFactura: IDetalleFactura = { id: 18345 };
      const producto: IProducto = { id: 1896 };
      detalleFactura.producto = producto;

      const productoCollection: IProducto[] = [{ id: 1896 }];
      jest.spyOn(productoService, 'query').mockReturnValue(of(new HttpResponse({ body: productoCollection })));
      const additionalProductos = [producto];
      const expectedCollection: IProducto[] = [...additionalProductos, ...productoCollection];
      jest.spyOn(productoService, 'addProductoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      expect(productoService.query).toHaveBeenCalled();
      expect(productoService.addProductoToCollectionIfMissing).toHaveBeenCalledWith(
        productoCollection,
        ...additionalProductos.map(expect.objectContaining),
      );
      expect(comp.productosSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const detalleFactura: IDetalleFactura = { id: 18345 };
      const factura: IFactura = { id: 30162 };
      detalleFactura.factura = factura;
      const producto: IProducto = { id: 1896 };
      detalleFactura.producto = producto;

      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      expect(comp.facturasSharedCollection).toContainEqual(factura);
      expect(comp.productosSharedCollection).toContainEqual(producto);
      expect(comp.detalleFactura).toEqual(detalleFactura);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleFactura>>();
      const detalleFactura = { id: 26550 };
      jest.spyOn(detalleFacturaFormService, 'getDetalleFactura').mockReturnValue(detalleFactura);
      jest.spyOn(detalleFacturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleFactura }));
      saveSubject.complete();

      // THEN
      expect(detalleFacturaFormService.getDetalleFactura).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(detalleFacturaService.update).toHaveBeenCalledWith(expect.objectContaining(detalleFactura));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleFactura>>();
      const detalleFactura = { id: 26550 };
      jest.spyOn(detalleFacturaFormService, 'getDetalleFactura').mockReturnValue({ id: null });
      jest.spyOn(detalleFacturaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleFactura: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleFactura }));
      saveSubject.complete();

      // THEN
      expect(detalleFacturaFormService.getDetalleFactura).toHaveBeenCalled();
      expect(detalleFacturaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleFactura>>();
      const detalleFactura = { id: 26550 };
      jest.spyOn(detalleFacturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleFactura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detalleFacturaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFactura', () => {
      it('should forward to facturaService', () => {
        const entity = { id: 30162 };
        const entity2 = { id: 8873 };
        jest.spyOn(facturaService, 'compareFactura');
        comp.compareFactura(entity, entity2);
        expect(facturaService.compareFactura).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProducto', () => {
      it('should forward to productoService', () => {
        const entity = { id: 1896 };
        const entity2 = { id: 15581 };
        jest.spyOn(productoService, 'compareProducto');
        comp.compareProducto(entity, entity2);
        expect(productoService.compareProducto).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
