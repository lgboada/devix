import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PaisService } from '../service/pais.service';
import { IPais } from '../pais.model';
import { PaisFormService } from './pais-form.service';

import { PaisUpdateComponent } from './pais-update.component';

describe('Pais Management Update Component', () => {
  let comp: PaisUpdateComponent;
  let fixture: ComponentFixture<PaisUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paisFormService: PaisFormService;
  let paisService: PaisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PaisUpdateComponent],
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
      .overrideTemplate(PaisUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaisUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paisFormService = TestBed.inject(PaisFormService);
    paisService = TestBed.inject(PaisService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const pais: IPais = { id: 16774 };

      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      expect(comp.pais).toEqual(pais);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPais>>();
      const pais = { id: 17868 };
      jest.spyOn(paisFormService, 'getPais').mockReturnValue(pais);
      jest.spyOn(paisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pais }));
      saveSubject.complete();

      // THEN
      expect(paisFormService.getPais).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paisService.update).toHaveBeenCalledWith(expect.objectContaining(pais));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPais>>();
      const pais = { id: 17868 };
      jest.spyOn(paisFormService, 'getPais').mockReturnValue({ id: null });
      jest.spyOn(paisService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pais: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pais }));
      saveSubject.complete();

      // THEN
      expect(paisFormService.getPais).toHaveBeenCalled();
      expect(paisService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPais>>();
      const pais = { id: 17868 };
      jest.spyOn(paisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pais });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paisService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
