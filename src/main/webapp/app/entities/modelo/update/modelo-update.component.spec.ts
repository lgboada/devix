import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMarca } from 'app/entities/marca/marca.model';
import { MarcaService } from 'app/entities/marca/service/marca.service';
import { ModeloService } from '../service/modelo.service';
import { IModelo } from '../modelo.model';
import { ModeloFormService } from './modelo-form.service';

import { ModeloUpdateComponent } from './modelo-update.component';

describe('Modelo Management Update Component', () => {
  let comp: ModeloUpdateComponent;
  let fixture: ComponentFixture<ModeloUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let modeloFormService: ModeloFormService;
  let modeloService: ModeloService;
  let marcaService: MarcaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ModeloUpdateComponent],
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
      .overrideTemplate(ModeloUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ModeloUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    modeloFormService = TestBed.inject(ModeloFormService);
    modeloService = TestBed.inject(ModeloService);
    marcaService = TestBed.inject(MarcaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Marca query and add missing value', () => {
      const modelo: IModelo = { id: 14716 };
      const marca: IMarca = { id: 9264 };
      modelo.marca = marca;

      const marcaCollection: IMarca[] = [{ id: 9264 }];
      jest.spyOn(marcaService, 'query').mockReturnValue(of(new HttpResponse({ body: marcaCollection })));
      const additionalMarcas = [marca];
      const expectedCollection: IMarca[] = [...additionalMarcas, ...marcaCollection];
      jest.spyOn(marcaService, 'addMarcaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ modelo });
      comp.ngOnInit();

      expect(marcaService.query).toHaveBeenCalled();
      expect(marcaService.addMarcaToCollectionIfMissing).toHaveBeenCalledWith(
        marcaCollection,
        ...additionalMarcas.map(expect.objectContaining),
      );
      expect(comp.marcasSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const modelo: IModelo = { id: 14716 };
      const marca: IMarca = { id: 9264 };
      modelo.marca = marca;

      activatedRoute.data = of({ modelo });
      comp.ngOnInit();

      expect(comp.marcasSharedCollection).toContainEqual(marca);
      expect(comp.modelo).toEqual(modelo);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModelo>>();
      const modelo = { id: 11658 };
      jest.spyOn(modeloFormService, 'getModelo').mockReturnValue(modelo);
      jest.spyOn(modeloService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ modelo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: modelo }));
      saveSubject.complete();

      // THEN
      expect(modeloFormService.getModelo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(modeloService.update).toHaveBeenCalledWith(expect.objectContaining(modelo));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModelo>>();
      const modelo = { id: 11658 };
      jest.spyOn(modeloFormService, 'getModelo').mockReturnValue({ id: null });
      jest.spyOn(modeloService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ modelo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: modelo }));
      saveSubject.complete();

      // THEN
      expect(modeloFormService.getModelo).toHaveBeenCalled();
      expect(modeloService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IModelo>>();
      const modelo = { id: 11658 };
      jest.spyOn(modeloService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ modelo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(modeloService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMarca', () => {
      it('should forward to marcaService', () => {
        const entity = { id: 9264 };
        const entity2 = { id: 13647 };
        jest.spyOn(marcaService, 'compareMarca');
        comp.compareMarca(entity, entity2);
        expect(marcaService.compareMarca).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
