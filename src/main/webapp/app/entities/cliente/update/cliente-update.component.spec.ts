import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICiudad } from 'app/entities/ciudad/ciudad.model';
import { CiudadService } from 'app/entities/ciudad/service/ciudad.service';
import { ProvinciaService } from 'app/entities/provincia/service/provincia.service';
import { ICliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { ClienteFormService } from './cliente-form.service';

import { ClienteUpdateComponent } from './cliente-update.component';

describe('Cliente Management Update Component', () => {
  let comp: ClienteUpdateComponent;
  let fixture: ComponentFixture<ClienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let clienteFormService: ClienteFormService;
  let clienteService: ClienteService;
  let ciudadService: CiudadService;
  let provinciaService: ProvinciaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ClienteUpdateComponent],
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
      .overrideTemplate(ClienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    clienteFormService = TestBed.inject(ClienteFormService);
    clienteService = TestBed.inject(ClienteService);
    ciudadService = TestBed.inject(CiudadService);
    provinciaService = TestBed.inject(ProvinciaService);
    jest.spyOn(provinciaService, 'query').mockReturnValue(of(new HttpResponse({ body: [] })));
    jest.spyOn(ciudadService, 'query').mockReturnValue(of(new HttpResponse({ body: [] })));
    jest.spyOn(ciudadService, 'find').mockReturnValue(of(new HttpResponse({ body: null })));

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Ciudad find when cliente has ciudad selected', () => {
      const cliente: ICliente = { id: 20795 };
      const ciudad: ICiudad = { id: 13640 };
      cliente.ciudad = ciudad;

      jest.spyOn(ciudadService, 'find').mockReturnValue(of(new HttpResponse({ body: ciudad })));
      jest.spyOn(ciudadService, 'query').mockReturnValue(of(new HttpResponse({ body: [] })));

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(ciudadService.find).toHaveBeenCalledWith(ciudad.id);
    });

    it('should update editForm', () => {
      const cliente: ICliente = { id: 20795 };
      cliente.tipoCliente = 'VIP';
      const ciudad: ICiudad = { id: 13640 };
      cliente.ciudad = ciudad;

      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      expect(comp.cliente).toEqual(cliente);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICliente>>();
      const cliente = { id: 13484 };
      jest.spyOn(clienteFormService, 'getCliente').mockReturnValue(cliente);
      jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliente }));
      saveSubject.complete();

      // THEN
      expect(clienteFormService.getCliente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(clienteService.update).toHaveBeenCalledWith(expect.objectContaining(cliente));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICliente>>();
      const cliente = { id: 13484 };
      jest.spyOn(clienteFormService, 'getCliente').mockReturnValue({ id: null });
      jest.spyOn(clienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliente }));
      saveSubject.complete();

      // THEN
      expect(clienteFormService.getCliente).toHaveBeenCalled();
      expect(clienteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICliente>>();
      const cliente = { id: 13484 };
      jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(clienteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCiudad', () => {
      it('should forward to ciudadService', () => {
        const entity = { id: 13640 };
        const entity2 = { id: 32223 };
        jest.spyOn(ciudadService, 'compareCiudad');
        comp.compareCiudad(entity, entity2);
        expect(ciudadService.compareCiudad).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
