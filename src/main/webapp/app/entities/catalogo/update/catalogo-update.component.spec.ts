import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITipoCatalogo } from 'app/entities/tipo-catalogo/tipo-catalogo.model';
import { TipoCatalogoService } from 'app/entities/tipo-catalogo/service/tipo-catalogo.service';
import { CatalogoService } from '../service/catalogo.service';
import { ICatalogo } from '../catalogo.model';
import { CatalogoFormService } from './catalogo-form.service';

import { CatalogoUpdateComponent } from './catalogo-update.component';

describe('Catalogo Management Update Component', () => {
  let comp: CatalogoUpdateComponent;
  let fixture: ComponentFixture<CatalogoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let catalogoFormService: CatalogoFormService;
  let catalogoService: CatalogoService;
  let tipoCatalogoService: TipoCatalogoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CatalogoUpdateComponent],
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
      .overrideTemplate(CatalogoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CatalogoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    catalogoFormService = TestBed.inject(CatalogoFormService);
    catalogoService = TestBed.inject(CatalogoService);
    tipoCatalogoService = TestBed.inject(TipoCatalogoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call TipoCatalogo query and add missing value', () => {
      const catalogo: ICatalogo = { id: 31515 };
      const tipoCatalogo: ITipoCatalogo = { id: 26545 };
      catalogo.tipoCatalogo = tipoCatalogo;

      const tipoCatalogoCollection: ITipoCatalogo[] = [{ id: 26545 }];
      jest.spyOn(tipoCatalogoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoCatalogoCollection })));
      const additionalTipoCatalogos = [tipoCatalogo];
      const expectedCollection: ITipoCatalogo[] = [...additionalTipoCatalogos, ...tipoCatalogoCollection];
      jest.spyOn(tipoCatalogoService, 'addTipoCatalogoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ catalogo });
      comp.ngOnInit();

      expect(tipoCatalogoService.query).toHaveBeenCalled();
      expect(tipoCatalogoService.addTipoCatalogoToCollectionIfMissing).toHaveBeenCalledWith(
        tipoCatalogoCollection,
        ...additionalTipoCatalogos.map(expect.objectContaining),
      );
      expect(comp.tipoCatalogosSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const catalogo: ICatalogo = { id: 31515 };
      const tipoCatalogo: ITipoCatalogo = { id: 26545 };
      catalogo.tipoCatalogo = tipoCatalogo;

      activatedRoute.data = of({ catalogo });
      comp.ngOnInit();

      expect(comp.tipoCatalogosSharedCollection).toContainEqual(tipoCatalogo);
      expect(comp.catalogo).toEqual(catalogo);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICatalogo>>();
      const catalogo = { id: 14600 };
      jest.spyOn(catalogoFormService, 'getCatalogo').mockReturnValue(catalogo);
      jest.spyOn(catalogoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalogo }));
      saveSubject.complete();

      // THEN
      expect(catalogoFormService.getCatalogo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(catalogoService.update).toHaveBeenCalledWith(expect.objectContaining(catalogo));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICatalogo>>();
      const catalogo = { id: 14600 };
      jest.spyOn(catalogoFormService, 'getCatalogo').mockReturnValue({ id: null });
      jest.spyOn(catalogoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: catalogo }));
      saveSubject.complete();

      // THEN
      expect(catalogoFormService.getCatalogo).toHaveBeenCalled();
      expect(catalogoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICatalogo>>();
      const catalogo = { id: 14600 };
      jest.spyOn(catalogoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ catalogo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(catalogoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTipoCatalogo', () => {
      it('should forward to tipoCatalogoService', () => {
        const entity = { id: 26545 };
        const entity2 = { id: 27572 };
        jest.spyOn(tipoCatalogoService, 'compareTipoCatalogo');
        comp.compareTipoCatalogo(entity, entity2);
        expect(tipoCatalogoService.compareTipoCatalogo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
