import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TipoClienteService } from '../service/tipo-cliente.service';
import { ITipoCliente } from '../tipo-cliente.model';
import { TipoClienteFormService } from './tipo-cliente-form.service';

import { TipoClienteUpdateComponent } from './tipo-cliente-update.component';

describe('TipoCliente Management Update Component', () => {
  let comp: TipoClienteUpdateComponent;
  let fixture: ComponentFixture<TipoClienteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoClienteFormService: TipoClienteFormService;
  let tipoClienteService: TipoClienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TipoClienteUpdateComponent],
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
      .overrideTemplate(TipoClienteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoClienteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoClienteFormService = TestBed.inject(TipoClienteFormService);
    tipoClienteService = TestBed.inject(TipoClienteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const tipoCliente: ITipoCliente = { id: 25421 };

      activatedRoute.data = of({ tipoCliente });
      comp.ngOnInit();

      expect(comp.tipoCliente).toEqual(tipoCliente);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoCliente>>();
      const tipoCliente = { id: 28838 };
      jest.spyOn(tipoClienteFormService, 'getTipoCliente').mockReturnValue(tipoCliente);
      jest.spyOn(tipoClienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoCliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoCliente }));
      saveSubject.complete();

      // THEN
      expect(tipoClienteFormService.getTipoCliente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoClienteService.update).toHaveBeenCalledWith(expect.objectContaining(tipoCliente));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoCliente>>();
      const tipoCliente = { id: 28838 };
      jest.spyOn(tipoClienteFormService, 'getTipoCliente').mockReturnValue({ id: null });
      jest.spyOn(tipoClienteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoCliente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoCliente }));
      saveSubject.complete();

      // THEN
      expect(tipoClienteFormService.getTipoCliente).toHaveBeenCalled();
      expect(tipoClienteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoCliente>>();
      const tipoCliente = { id: 28838 };
      jest.spyOn(tipoClienteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoCliente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoClienteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
