import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TipoDireccionService } from '../service/tipo-direccion.service';
import { ITipoDireccion } from '../tipo-direccion.model';
import { TipoDireccionFormService } from './tipo-direccion-form.service';

import { TipoDireccionUpdateComponent } from './tipo-direccion-update.component';

describe('TipoDireccion Management Update Component', () => {
  let comp: TipoDireccionUpdateComponent;
  let fixture: ComponentFixture<TipoDireccionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoDireccionFormService: TipoDireccionFormService;
  let tipoDireccionService: TipoDireccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TipoDireccionUpdateComponent],
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
      .overrideTemplate(TipoDireccionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoDireccionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoDireccionFormService = TestBed.inject(TipoDireccionFormService);
    tipoDireccionService = TestBed.inject(TipoDireccionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const tipoDireccion: ITipoDireccion = { id: 18715 };

      activatedRoute.data = of({ tipoDireccion });
      comp.ngOnInit();

      expect(comp.tipoDireccion).toEqual(tipoDireccion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoDireccion>>();
      const tipoDireccion = { id: 22481 };
      jest.spyOn(tipoDireccionFormService, 'getTipoDireccion').mockReturnValue(tipoDireccion);
      jest.spyOn(tipoDireccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoDireccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoDireccion }));
      saveSubject.complete();

      // THEN
      expect(tipoDireccionFormService.getTipoDireccion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoDireccionService.update).toHaveBeenCalledWith(expect.objectContaining(tipoDireccion));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoDireccion>>();
      const tipoDireccion = { id: 22481 };
      jest.spyOn(tipoDireccionFormService, 'getTipoDireccion').mockReturnValue({ id: null });
      jest.spyOn(tipoDireccionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoDireccion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoDireccion }));
      saveSubject.complete();

      // THEN
      expect(tipoDireccionFormService.getTipoDireccion).toHaveBeenCalled();
      expect(tipoDireccionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoDireccion>>();
      const tipoDireccion = { id: 22481 };
      jest.spyOn(tipoDireccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoDireccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoDireccionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
