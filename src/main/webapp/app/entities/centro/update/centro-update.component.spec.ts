import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICompania } from 'app/entities/compania/compania.model';
import { CompaniaService } from 'app/entities/compania/service/compania.service';
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
  let companiaService: CompaniaService;

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
    companiaService = TestBed.inject(CompaniaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Compania query and add missing value', () => {
      const centro: ICentro = { id: 27658 };
      const compania: ICompania = { id: 14846 };
      centro.compania = compania;

      const companiaCollection: ICompania[] = [{ id: 14846 }];
      jest.spyOn(companiaService, 'query').mockReturnValue(of(new HttpResponse({ body: companiaCollection })));
      const additionalCompanias = [compania];
      const expectedCollection: ICompania[] = [...additionalCompanias, ...companiaCollection];
      jest.spyOn(companiaService, 'addCompaniaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ centro });
      comp.ngOnInit();

      expect(companiaService.query).toHaveBeenCalled();
      expect(companiaService.addCompaniaToCollectionIfMissing).toHaveBeenCalledWith(
        companiaCollection,
        ...additionalCompanias.map(expect.objectContaining),
      );
      expect(comp.companiasSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const centro: ICentro = { id: 27658 };
      const compania: ICompania = { id: 14846 };
      centro.compania = compania;

      activatedRoute.data = of({ centro });
      comp.ngOnInit();

      expect(comp.companiasSharedCollection).toContainEqual(compania);
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

  describe('Compare relationships', () => {
    describe('compareCompania', () => {
      it('should forward to companiaService', () => {
        const entity = { id: 14846 };
        const entity2 = { id: 12296 };
        jest.spyOn(companiaService, 'compareCompania');
        comp.compareCompania(entity, entity2);
        expect(companiaService.compareCompania).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
