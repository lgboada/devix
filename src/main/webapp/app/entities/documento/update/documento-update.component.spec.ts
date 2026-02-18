import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IEvento } from 'app/entities/evento/evento.model';
import { EventoService } from 'app/entities/evento/service/evento.service';
import { IDocumento } from '../documento.model';
import { DocumentoService } from '../service/documento.service';
import { DocumentoFormService } from './documento-form.service';

import { DocumentoUpdateComponent } from './documento-update.component';

describe('Documento Management Update Component', () => {
  let comp: DocumentoUpdateComponent;
  let fixture: ComponentFixture<DocumentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentoFormService: DocumentoFormService;
  let documentoService: DocumentoService;
  let clienteService: ClienteService;
  let eventoService: EventoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentoUpdateComponent],
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
      .overrideTemplate(DocumentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentoFormService = TestBed.inject(DocumentoFormService);
    documentoService = TestBed.inject(DocumentoService);
    clienteService = TestBed.inject(ClienteService);
    eventoService = TestBed.inject(EventoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Cliente query and add missing value', () => {
      const documento: IDocumento = { id: 880 };
      const cliente: ICliente = { id: 13484 };
      documento.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 13484 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documento });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining),
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Evento query and add missing value', () => {
      const documento: IDocumento = { id: 880 };
      const evento: IEvento = { id: 11280 };
      documento.evento = evento;

      const eventoCollection: IEvento[] = [{ id: 11280 }];
      jest.spyOn(eventoService, 'query').mockReturnValue(of(new HttpResponse({ body: eventoCollection })));
      const additionalEventos = [evento];
      const expectedCollection: IEvento[] = [...additionalEventos, ...eventoCollection];
      jest.spyOn(eventoService, 'addEventoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documento });
      comp.ngOnInit();

      expect(eventoService.query).toHaveBeenCalled();
      expect(eventoService.addEventoToCollectionIfMissing).toHaveBeenCalledWith(
        eventoCollection,
        ...additionalEventos.map(expect.objectContaining),
      );
      expect(comp.eventosSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const documento: IDocumento = { id: 880 };
      const cliente: ICliente = { id: 13484 };
      documento.cliente = cliente;
      const evento: IEvento = { id: 11280 };
      documento.evento = evento;

      activatedRoute.data = of({ documento });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection).toContainEqual(cliente);
      expect(comp.eventosSharedCollection).toContainEqual(evento);
      expect(comp.documento).toEqual(documento);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumento>>();
      const documento = { id: 19765 };
      jest.spyOn(documentoFormService, 'getDocumento').mockReturnValue(documento);
      jest.spyOn(documentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documento }));
      saveSubject.complete();

      // THEN
      expect(documentoFormService.getDocumento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentoService.update).toHaveBeenCalledWith(expect.objectContaining(documento));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumento>>();
      const documento = { id: 19765 };
      jest.spyOn(documentoFormService, 'getDocumento').mockReturnValue({ id: null });
      jest.spyOn(documentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documento }));
      saveSubject.complete();

      // THEN
      expect(documentoFormService.getDocumento).toHaveBeenCalled();
      expect(documentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumento>>();
      const documento = { id: 19765 };
      jest.spyOn(documentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCliente', () => {
      it('should forward to clienteService', () => {
        const entity = { id: 13484 };
        const entity2 = { id: 20795 };
        jest.spyOn(clienteService, 'compareCliente');
        comp.compareCliente(entity, entity2);
        expect(clienteService.compareCliente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEvento', () => {
      it('should forward to eventoService', () => {
        const entity = { id: 11280 };
        const entity2 = { id: 12252 };
        jest.spyOn(eventoService, 'compareEvento');
        comp.compareEvento(entity, entity2);
        expect(eventoService.compareEvento).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
