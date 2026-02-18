import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IModelo } from 'app/entities/modelo/modelo.model';
import { ModeloService } from 'app/entities/modelo/service/modelo.service';
import { ITipoProducto } from 'app/entities/tipo-producto/tipo-producto.model';
import { TipoProductoService } from 'app/entities/tipo-producto/service/tipo-producto.service';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import { IProducto } from '../producto.model';
import { ProductoService } from '../service/producto.service';
import { ProductoFormService } from './producto-form.service';

import { ProductoUpdateComponent } from './producto-update.component';

describe('Producto Management Update Component', () => {
  let comp: ProductoUpdateComponent;
  let fixture: ComponentFixture<ProductoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productoFormService: ProductoFormService;
  let productoService: ProductoService;
  let modeloService: ModeloService;
  let tipoProductoService: TipoProductoService;
  let proveedorService: ProveedorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductoUpdateComponent],
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
      .overrideTemplate(ProductoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productoFormService = TestBed.inject(ProductoFormService);
    productoService = TestBed.inject(ProductoService);
    modeloService = TestBed.inject(ModeloService);
    tipoProductoService = TestBed.inject(TipoProductoService);
    proveedorService = TestBed.inject(ProveedorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Modelo query and add missing value', () => {
      const producto: IProducto = { id: 15581 };
      const modelo: IModelo = { id: 11658 };
      producto.modelo = modelo;

      const modeloCollection: IModelo[] = [{ id: 11658 }];
      jest.spyOn(modeloService, 'query').mockReturnValue(of(new HttpResponse({ body: modeloCollection })));
      const additionalModelos = [modelo];
      const expectedCollection: IModelo[] = [...additionalModelos, ...modeloCollection];
      jest.spyOn(modeloService, 'addModeloToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(modeloService.query).toHaveBeenCalled();
      expect(modeloService.addModeloToCollectionIfMissing).toHaveBeenCalledWith(
        modeloCollection,
        ...additionalModelos.map(expect.objectContaining),
      );
      expect(comp.modelosSharedCollection).toEqual(expectedCollection);
    });

    it('should call TipoProducto query and add missing value', () => {
      const producto: IProducto = { id: 15581 };
      const tipoProducto: ITipoProducto = { id: 6329 };
      producto.tipoProducto = tipoProducto;

      const tipoProductoCollection: ITipoProducto[] = [{ id: 6329 }];
      jest.spyOn(tipoProductoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoProductoCollection })));
      const additionalTipoProductos = [tipoProducto];
      const expectedCollection: ITipoProducto[] = [...additionalTipoProductos, ...tipoProductoCollection];
      jest.spyOn(tipoProductoService, 'addTipoProductoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(tipoProductoService.query).toHaveBeenCalled();
      expect(tipoProductoService.addTipoProductoToCollectionIfMissing).toHaveBeenCalledWith(
        tipoProductoCollection,
        ...additionalTipoProductos.map(expect.objectContaining),
      );
      expect(comp.tipoProductosSharedCollection).toEqual(expectedCollection);
    });

    it('should call Proveedor query and add missing value', () => {
      const producto: IProducto = { id: 15581 };
      const proveedor: IProveedor = { id: 9668 };
      producto.proveedor = proveedor;

      const proveedorCollection: IProveedor[] = [{ id: 9668 }];
      jest.spyOn(proveedorService, 'query').mockReturnValue(of(new HttpResponse({ body: proveedorCollection })));
      const additionalProveedors = [proveedor];
      const expectedCollection: IProveedor[] = [...additionalProveedors, ...proveedorCollection];
      jest.spyOn(proveedorService, 'addProveedorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(proveedorService.query).toHaveBeenCalled();
      expect(proveedorService.addProveedorToCollectionIfMissing).toHaveBeenCalledWith(
        proveedorCollection,
        ...additionalProveedors.map(expect.objectContaining),
      );
      expect(comp.proveedorsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const producto: IProducto = { id: 15581 };
      const modelo: IModelo = { id: 11658 };
      producto.modelo = modelo;
      const tipoProducto: ITipoProducto = { id: 6329 };
      producto.tipoProducto = tipoProducto;
      const proveedor: IProveedor = { id: 9668 };
      producto.proveedor = proveedor;

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(comp.modelosSharedCollection).toContainEqual(modelo);
      expect(comp.tipoProductosSharedCollection).toContainEqual(tipoProducto);
      expect(comp.proveedorsSharedCollection).toContainEqual(proveedor);
      expect(comp.producto).toEqual(producto);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProducto>>();
      const producto = { id: 1896 };
      jest.spyOn(productoFormService, 'getProducto').mockReturnValue(producto);
      jest.spyOn(productoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: producto }));
      saveSubject.complete();

      // THEN
      expect(productoFormService.getProducto).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productoService.update).toHaveBeenCalledWith(expect.objectContaining(producto));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProducto>>();
      const producto = { id: 1896 };
      jest.spyOn(productoFormService, 'getProducto').mockReturnValue({ id: null });
      jest.spyOn(productoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: producto }));
      saveSubject.complete();

      // THEN
      expect(productoFormService.getProducto).toHaveBeenCalled();
      expect(productoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProducto>>();
      const producto = { id: 1896 };
      jest.spyOn(productoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareModelo', () => {
      it('should forward to modeloService', () => {
        const entity = { id: 11658 };
        const entity2 = { id: 14716 };
        jest.spyOn(modeloService, 'compareModelo');
        comp.compareModelo(entity, entity2);
        expect(modeloService.compareModelo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTipoProducto', () => {
      it('should forward to tipoProductoService', () => {
        const entity = { id: 6329 };
        const entity2 = { id: 23322 };
        jest.spyOn(tipoProductoService, 'compareTipoProducto');
        comp.compareTipoProducto(entity, entity2);
        expect(tipoProductoService.compareTipoProducto).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProveedor', () => {
      it('should forward to proveedorService', () => {
        const entity = { id: 9668 };
        const entity2 = { id: 23574 };
        jest.spyOn(proveedorService, 'compareProveedor');
        comp.compareProveedor(entity, entity2);
        expect(proveedorService.compareProveedor).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
