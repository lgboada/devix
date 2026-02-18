import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProvincia } from 'app/entities/provincia/provincia.model';
import { ProvinciaService } from 'app/entities/provincia/service/provincia.service';
import { CiudadService } from '../service/ciudad.service';
import { ICiudad } from '../ciudad.model';
import { CiudadFormService } from './ciudad-form.service';

import { CiudadUpdateComponent } from './ciudad-update.component';

describe('Ciudad Management Update Component', () => {
  let comp: CiudadUpdateComponent;
  let fixture: ComponentFixture<CiudadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ciudadFormService: CiudadFormService;
  let ciudadService: CiudadService;
  let provinciaService: ProvinciaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CiudadUpdateComponent],
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
      .overrideTemplate(CiudadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CiudadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ciudadFormService = TestBed.inject(CiudadFormService);
    ciudadService = TestBed.inject(CiudadService);
    provinciaService = TestBed.inject(ProvinciaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Provincia query and add missing value', () => {
      const ciudad: ICiudad = { id: 32223 };
      const provincia: IProvincia = { id: 18523 };
      ciudad.provincia = provincia;

      const provinciaCollection: IProvincia[] = [{ id: 18523 }];
      jest.spyOn(provinciaService, 'query').mockReturnValue(of(new HttpResponse({ body: provinciaCollection })));
      const additionalProvincias = [provincia];
      const expectedCollection: IProvincia[] = [...additionalProvincias, ...provinciaCollection];
      jest.spyOn(provinciaService, 'addProvinciaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ciudad });
      comp.ngOnInit();

      expect(provinciaService.query).toHaveBeenCalled();
      expect(provinciaService.addProvinciaToCollectionIfMissing).toHaveBeenCalledWith(
        provinciaCollection,
        ...additionalProvincias.map(expect.objectContaining),
      );
      expect(comp.provinciasSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const ciudad: ICiudad = { id: 32223 };
      const provincia: IProvincia = { id: 18523 };
      ciudad.provincia = provincia;

      activatedRoute.data = of({ ciudad });
      comp.ngOnInit();

      expect(comp.provinciasSharedCollection).toContainEqual(provincia);
      expect(comp.ciudad).toEqual(ciudad);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICiudad>>();
      const ciudad = { id: 13640 };
      jest.spyOn(ciudadFormService, 'getCiudad').mockReturnValue(ciudad);
      jest.spyOn(ciudadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ciudad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ciudad }));
      saveSubject.complete();

      // THEN
      expect(ciudadFormService.getCiudad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ciudadService.update).toHaveBeenCalledWith(expect.objectContaining(ciudad));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICiudad>>();
      const ciudad = { id: 13640 };
      jest.spyOn(ciudadFormService, 'getCiudad').mockReturnValue({ id: null });
      jest.spyOn(ciudadService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ciudad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ciudad }));
      saveSubject.complete();

      // THEN
      expect(ciudadFormService.getCiudad).toHaveBeenCalled();
      expect(ciudadService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICiudad>>();
      const ciudad = { id: 13640 };
      jest.spyOn(ciudadService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ciudad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ciudadService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProvincia', () => {
      it('should forward to provinciaService', () => {
        const entity = { id: 18523 };
        const entity2 = { id: 17585 };
        jest.spyOn(provinciaService, 'compareProvincia');
        comp.compareProvincia(entity, entity2);
        expect(provinciaService.compareProvincia).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
