import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TipoCatalogoService } from '../service/tipo-catalogo.service';
import { ITipoCatalogo } from '../tipo-catalogo.model';
import { TipoCatalogoFormService } from './tipo-catalogo-form.service';

import { TipoCatalogoUpdateComponent } from './tipo-catalogo-update.component';

describe('TipoCatalogo Management Update Component', () => {
  let comp: TipoCatalogoUpdateComponent;
  let fixture: ComponentFixture<TipoCatalogoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoCatalogoFormService: TipoCatalogoFormService;
  let tipoCatalogoService: TipoCatalogoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TipoCatalogoUpdateComponent],
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
      .overrideTemplate(TipoCatalogoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoCatalogoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoCatalogoFormService = TestBed.inject(TipoCatalogoFormService);
    tipoCatalogoService = TestBed.inject(TipoCatalogoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const tipoCatalogo: ITipoCatalogo = { id: 27572 };

      activatedRoute.data = of({ tipoCatalogo });
      comp.ngOnInit();

      expect(comp.tipoCatalogo).toEqual(tipoCatalogo);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoCatalogo>>();
      const tipoCatalogo = { id: 26545 };
      jest.spyOn(tipoCatalogoFormService, 'getTipoCatalogo').mockReturnValue(tipoCatalogo);
      jest.spyOn(tipoCatalogoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoCatalogo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoCatalogo }));
      saveSubject.complete();

      // THEN
      expect(tipoCatalogoFormService.getTipoCatalogo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoCatalogoService.update).toHaveBeenCalledWith(expect.objectContaining(tipoCatalogo));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoCatalogo>>();
      const tipoCatalogo = { id: 26545 };
      jest.spyOn(tipoCatalogoFormService, 'getTipoCatalogo').mockReturnValue({ id: null });
      jest.spyOn(tipoCatalogoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoCatalogo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoCatalogo }));
      saveSubject.complete();

      // THEN
      expect(tipoCatalogoFormService.getTipoCatalogo).toHaveBeenCalled();
      expect(tipoCatalogoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoCatalogo>>();
      const tipoCatalogo = { id: 26545 };
      jest.spyOn(tipoCatalogoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoCatalogo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoCatalogoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
