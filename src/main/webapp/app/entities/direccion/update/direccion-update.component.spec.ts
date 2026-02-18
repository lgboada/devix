import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITipoDireccion } from 'app/entities/tipo-direccion/tipo-direccion.model';
import { TipoDireccionService } from 'app/entities/tipo-direccion/service/tipo-direccion.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IDireccion } from '../direccion.model';
import { DireccionService } from '../service/direccion.service';
import { DireccionFormService } from './direccion-form.service';

import { DireccionUpdateComponent } from './direccion-update.component';

describe('Direccion Management Update Component', () => {
  let comp: DireccionUpdateComponent;
  let fixture: ComponentFixture<DireccionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let direccionFormService: DireccionFormService;
  let direccionService: DireccionService;
  let tipoDireccionService: TipoDireccionService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DireccionUpdateComponent],
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
      .overrideTemplate(DireccionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DireccionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    direccionFormService = TestBed.inject(DireccionFormService);
    direccionService = TestBed.inject(DireccionService);
    tipoDireccionService = TestBed.inject(TipoDireccionService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TipoDireccion query and add missing value', () => {
      const direccion: IDireccion = { id: 766 };
      const tipoDireccion: ITipoDireccion = { id: 22481 };
      direccion.tipoDireccion = tipoDireccion;

      const tipoDireccionCollection: ITipoDireccion[] = [{ id: 22481 }];
      jest.spyOn(tipoDireccionService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoDireccionCollection })));
      const additionalTipoDireccions = [tipoDireccion];
      const expectedCollection: ITipoDireccion[] = [...additionalTipoDireccions, ...tipoDireccionCollection];
      jest.spyOn(tipoDireccionService, 'addTipoDireccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      expect(tipoDireccionService.query).toHaveBeenCalled();
      expect(tipoDireccionService.addTipoDireccionToCollectionIfMissing).toHaveBeenCalledWith(
        tipoDireccionCollection,
        ...additionalTipoDireccions.map(expect.objectContaining),
      );
      expect(comp.tipoDireccionsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Cliente query and add missing value', () => {
      const direccion: IDireccion = { id: 766 };
      const cliente: ICliente = { id: 13484 };
      direccion.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 13484 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const direccion: IDireccion = { id: 766 };
      const tipoDireccion: ITipoDireccion = { id: 22481 };
      direccion.tipoDireccion = tipoDireccion;
      const cliente: ICliente = { id: 13484 };
      direccion.cliente = cliente;

      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      expect(comp.tipoDireccionsSharedCollection).toContainEqual(tipoDireccion);
      expect(comp.clientesSharedCollection).toContainEqual(cliente);
      expect(comp.direccion).toEqual(direccion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDireccion>>();
      const direccion = { id: 31929 };
      jest.spyOn(direccionFormService, 'getDireccion').mockReturnValue(direccion);
      jest.spyOn(direccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: direccion }));
      saveSubject.complete();

      // THEN
      expect(direccionFormService.getDireccion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(direccionService.update).toHaveBeenCalledWith(expect.objectContaining(direccion));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDireccion>>();
      const direccion = { id: 31929 };
      jest.spyOn(direccionFormService, 'getDireccion').mockReturnValue({ id: null });
      jest.spyOn(direccionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: direccion }));
      saveSubject.complete();

      // THEN
      expect(direccionFormService.getDireccion).toHaveBeenCalled();
      expect(direccionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDireccion>>();
      const direccion = { id: 31929 };
      jest.spyOn(direccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(direccionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTipoDireccion', () => {
      it('should forward to tipoDireccionService', () => {
        const entity = { id: 22481 };
        const entity2 = { id: 18715 };
        jest.spyOn(tipoDireccionService, 'compareTipoDireccion');
        comp.compareTipoDireccion(entity, entity2);
        expect(tipoDireccionService.compareTipoDireccion).toHaveBeenCalledWith(entity, entity2);
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
