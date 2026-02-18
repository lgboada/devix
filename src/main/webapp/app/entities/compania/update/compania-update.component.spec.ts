import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CompaniaService } from '../service/compania.service';
import { ICompania } from '../compania.model';
import { CompaniaFormService } from './compania-form.service';

import { CompaniaUpdateComponent } from './compania-update.component';

describe('Compania Management Update Component', () => {
  let comp: CompaniaUpdateComponent;
  let fixture: ComponentFixture<CompaniaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let companiaFormService: CompaniaFormService;
  let companiaService: CompaniaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CompaniaUpdateComponent],
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
      .overrideTemplate(CompaniaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompaniaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    companiaFormService = TestBed.inject(CompaniaFormService);
    companiaService = TestBed.inject(CompaniaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const compania: ICompania = { id: 12296 };

      activatedRoute.data = of({ compania });
      comp.ngOnInit();

      expect(comp.compania).toEqual(compania);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompania>>();
      const compania = { id: 14846 };
      jest.spyOn(companiaFormService, 'getCompania').mockReturnValue(compania);
      jest.spyOn(companiaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compania });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compania }));
      saveSubject.complete();

      // THEN
      expect(companiaFormService.getCompania).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(companiaService.update).toHaveBeenCalledWith(expect.objectContaining(compania));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompania>>();
      const compania = { id: 14846 };
      jest.spyOn(companiaFormService, 'getCompania').mockReturnValue({ id: null });
      jest.spyOn(companiaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compania: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compania }));
      saveSubject.complete();

      // THEN
      expect(companiaFormService.getCompania).toHaveBeenCalled();
      expect(companiaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompania>>();
      const compania = { id: 14846 };
      jest.spyOn(companiaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compania });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(companiaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
