import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CentroService } from '../service/centro.service';
import { ICentro } from '../centro.model';
import { CentroFormService } from './centro-form.service';

import { CentroUpdateComponent } from './centro-update.component';

describe('Centro Management Update Component', () => {
  let comp: CentroUpdateComponent;
  let fixture: ComponentFixture<CentroUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let centroFormService: CentroFormService;
  let centroService: CentroService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CentroUpdateComponent],
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
      .overrideTemplate(CentroUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CentroUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    centroFormService = TestBed.inject(CentroFormService);
    centroService = TestBed.inject(CentroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const centro: ICentro = { id: 27658 };

      activatedRoute.data = of({ centro });
      comp.ngOnInit();

      expect(comp.centro).toEqual(centro);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICentro>>();
      const centro = { id: 10065 };
      jest.spyOn(centroFormService, 'getCentro').mockReturnValue(centro);
      jest.spyOn(centroService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ centro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: centro }));
      saveSubject.complete();

      // THEN
      expect(centroFormService.getCentro).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(centroService.update).toHaveBeenCalledWith(expect.objectContaining(centro));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICentro>>();
      const centro = { id: 10065 };
      jest.spyOn(centroFormService, 'getCentro').mockReturnValue({ id: null });
      jest.spyOn(centroService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ centro: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: centro }));
      saveSubject.complete();

      // THEN
      expect(centroFormService.getCentro).toHaveBeenCalled();
      expect(centroService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICentro>>();
      const centro = { id: 10065 };
      jest.spyOn(centroService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ centro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(centroService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
