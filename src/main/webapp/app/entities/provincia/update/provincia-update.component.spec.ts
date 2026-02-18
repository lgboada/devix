import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';
import { ProvinciaService } from '../service/provincia.service';
import { IProvincia } from '../provincia.model';
import { ProvinciaFormService } from './provincia-form.service';

import { ProvinciaUpdateComponent } from './provincia-update.component';

describe('Provincia Management Update Component', () => {
  let comp: ProvinciaUpdateComponent;
  let fixture: ComponentFixture<ProvinciaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let provinciaFormService: ProvinciaFormService;
  let provinciaService: ProvinciaService;
  let paisService: PaisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProvinciaUpdateComponent],
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
      .overrideTemplate(ProvinciaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProvinciaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    provinciaFormService = TestBed.inject(ProvinciaFormService);
    provinciaService = TestBed.inject(ProvinciaService);
    paisService = TestBed.inject(PaisService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Pais query and add missing value', () => {
      const provincia: IProvincia = { id: 17585 };
      const pais: IPais = { id: 17868 };
      provincia.pais = pais;

      const paisCollection: IPais[] = [{ id: 17868 }];
      jest.spyOn(paisService, 'query').mockReturnValue(of(new HttpResponse({ body: paisCollection })));
      const additionalPais = [pais];
      const expectedCollection: IPais[] = [...additionalPais, ...paisCollection];
      jest.spyOn(paisService, 'addPaisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ provincia });
      comp.ngOnInit();

      expect(paisService.query).toHaveBeenCalled();
      expect(paisService.addPaisToCollectionIfMissing).toHaveBeenCalledWith(paisCollection, ...additionalPais.map(expect.objectContaining));
      expect(comp.paisSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const provincia: IProvincia = { id: 17585 };
      const pais: IPais = { id: 17868 };
      provincia.pais = pais;

      activatedRoute.data = of({ provincia });
      comp.ngOnInit();

      expect(comp.paisSharedCollection).toContainEqual(pais);
      expect(comp.provincia).toEqual(provincia);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvincia>>();
      const provincia = { id: 18523 };
      jest.spyOn(provinciaFormService, 'getProvincia').mockReturnValue(provincia);
      jest.spyOn(provinciaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ provincia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: provincia }));
      saveSubject.complete();

      // THEN
      expect(provinciaFormService.getProvincia).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(provinciaService.update).toHaveBeenCalledWith(expect.objectContaining(provincia));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvincia>>();
      const provincia = { id: 18523 };
      jest.spyOn(provinciaFormService, 'getProvincia').mockReturnValue({ id: null });
      jest.spyOn(provinciaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ provincia: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: provincia }));
      saveSubject.complete();

      // THEN
      expect(provinciaFormService.getProvincia).toHaveBeenCalled();
      expect(provinciaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvincia>>();
      const provincia = { id: 18523 };
      jest.spyOn(provinciaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ provincia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(provinciaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePais', () => {
      it('should forward to paisService', () => {
        const entity = { id: 17868 };
        const entity2 = { id: 16774 };
        jest.spyOn(paisService, 'comparePais');
        comp.comparePais(entity, entity2);
        expect(paisService.comparePais).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
