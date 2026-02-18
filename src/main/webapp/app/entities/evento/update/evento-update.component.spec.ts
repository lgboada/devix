import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITipoEvento } from 'app/entities/tipo-evento/tipo-evento.model';
import { TipoEventoService } from 'app/entities/tipo-evento/service/tipo-evento.service';
import { ICentro } from 'app/entities/centro/centro.model';
import { CentroService } from 'app/entities/centro/service/centro.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IEvento } from '../evento.model';
import { EventoService } from '../service/evento.service';
import { EventoFormService } from './evento-form.service';

import { EventoUpdateComponent } from './evento-update.component';

describe('Evento Management Update Component', () => {
  let comp: EventoUpdateComponent;
  let fixture: ComponentFixture<EventoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventoFormService: EventoFormService;
  let eventoService: EventoService;
  let tipoEventoService: TipoEventoService;
  let centroService: CentroService;
  let clienteService: ClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EventoUpdateComponent],
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
      .overrideTemplate(EventoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventoFormService = TestBed.inject(EventoFormService);
    eventoService = TestBed.inject(EventoService);
    tipoEventoService = TestBed.inject(TipoEventoService);
    centroService = TestBed.inject(CentroService);
    clienteService = TestBed.inject(ClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TipoEvento query and add missing value', () => {
      const evento: IEvento = { id: 12252 };
      const tipoEvento: ITipoEvento = { id: 26103 };
      evento.tipoEvento = tipoEvento;

      const tipoEventoCollection: ITipoEvento[] = [{ id: 26103 }];
      jest.spyOn(tipoEventoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoEventoCollection })));
      const additionalTipoEventos = [tipoEvento];
      const expectedCollection: ITipoEvento[] = [...additionalTipoEventos, ...tipoEventoCollection];
      jest.spyOn(tipoEventoService, 'addTipoEventoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(tipoEventoService.query).toHaveBeenCalled();
      expect(tipoEventoService.addTipoEventoToCollectionIfMissing).toHaveBeenCalledWith(
        tipoEventoCollection,
        ...additionalTipoEventos.map(expect.objectContaining),
      );
      expect(comp.tipoEventosSharedCollection).toEqual(expectedCollection);
    });

    it('should call Centro query and add missing value', () => {
      const evento: IEvento = { id: 12252 };
      const centro: ICentro = { id: 10065 };
      evento.centro = centro;

      const centroCollection: ICentro[] = [{ id: 10065 }];
      jest.spyOn(centroService, 'query').mockReturnValue(of(new HttpResponse({ body: centroCollection })));
      const additionalCentros = [centro];
      const expectedCollection: ICentro[] = [...additionalCentros, ...centroCollection];
      jest.spyOn(centroService, 'addCentroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(centroService.query).toHaveBeenCalled();
      expect(centroService.addCentroToCollectionIfMissing).toHaveBeenCalledWith(
        centroCollection,
        ...additionalCentros.map(expect.objectContaining),
      );
      expect(comp.centrosSharedCollection).toEqual(expectedCollection);
    });

    it('should call Cliente query and add missing value', () => {
      const evento: IEvento = { id: 12252 };
      const cliente: ICliente = { id: 13484 };
      evento.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 13484 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const evento: IEvento = { id: 12252 };
      const tipoEvento: ITipoEvento = { id: 26103 };
      evento.tipoEvento = tipoEvento;
      const centro: ICentro = { id: 10065 };
      evento.centro = centro;
      const cliente: ICliente = { id: 13484 };
      evento.cliente = cliente;

      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      expect(comp.tipoEventosSharedCollection).toContainEqual(tipoEvento);
      expect(comp.centrosSharedCollection).toContainEqual(centro);
      expect(comp.clientesSharedCollection).toContainEqual(cliente);
      expect(comp.evento).toEqual(evento);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvento>>();
      const evento = { id: 11280 };
      jest.spyOn(eventoFormService, 'getEvento').mockReturnValue(evento);
      jest.spyOn(eventoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evento }));
      saveSubject.complete();

      // THEN
      expect(eventoFormService.getEvento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventoService.update).toHaveBeenCalledWith(expect.objectContaining(evento));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvento>>();
      const evento = { id: 11280 };
      jest.spyOn(eventoFormService, 'getEvento').mockReturnValue({ id: null });
      jest.spyOn(eventoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evento }));
      saveSubject.complete();

      // THEN
      expect(eventoFormService.getEvento).toHaveBeenCalled();
      expect(eventoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvento>>();
      const evento = { id: 11280 };
      jest.spyOn(eventoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTipoEvento', () => {
      it('should forward to tipoEventoService', () => {
        const entity = { id: 26103 };
        const entity2 = { id: 19774 };
        jest.spyOn(tipoEventoService, 'compareTipoEvento');
        comp.compareTipoEvento(entity, entity2);
        expect(tipoEventoService.compareTipoEvento).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
