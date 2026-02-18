import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TipoEventoService } from '../service/tipo-evento.service';
import { ITipoEvento } from '../tipo-evento.model';
import { TipoEventoFormService } from './tipo-evento-form.service';

import { TipoEventoUpdateComponent } from './tipo-evento-update.component';

describe('TipoEvento Management Update Component', () => {
  let comp: TipoEventoUpdateComponent;
  let fixture: ComponentFixture<TipoEventoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoEventoFormService: TipoEventoFormService;
  let tipoEventoService: TipoEventoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TipoEventoUpdateComponent],
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
      .overrideTemplate(TipoEventoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoEventoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoEventoFormService = TestBed.inject(TipoEventoFormService);
    tipoEventoService = TestBed.inject(TipoEventoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const tipoEvento: ITipoEvento = { id: 19774 };

      activatedRoute.data = of({ tipoEvento });
      comp.ngOnInit();

      expect(comp.tipoEvento).toEqual(tipoEvento);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoEvento>>();
      const tipoEvento = { id: 26103 };
      jest.spyOn(tipoEventoFormService, 'getTipoEvento').mockReturnValue(tipoEvento);
      jest.spyOn(tipoEventoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoEvento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoEvento }));
      saveSubject.complete();

      // THEN
      expect(tipoEventoFormService.getTipoEvento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoEventoService.update).toHaveBeenCalledWith(expect.objectContaining(tipoEvento));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoEvento>>();
      const tipoEvento = { id: 26103 };
      jest.spyOn(tipoEventoFormService, 'getTipoEvento').mockReturnValue({ id: null });
      jest.spyOn(tipoEventoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoEvento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoEvento }));
      saveSubject.complete();

      // THEN
      expect(tipoEventoFormService.getTipoEvento).toHaveBeenCalled();
      expect(tipoEventoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoEvento>>();
      const tipoEvento = { id: 26103 };
      jest.spyOn(tipoEventoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoEvento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoEventoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
