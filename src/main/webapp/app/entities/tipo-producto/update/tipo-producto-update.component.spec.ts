import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TipoProductoService } from '../service/tipo-producto.service';
import { ITipoProducto } from '../tipo-producto.model';
import { TipoProductoFormService } from './tipo-producto-form.service';

import { TipoProductoUpdateComponent } from './tipo-producto-update.component';

describe('TipoProducto Management Update Component', () => {
  let comp: TipoProductoUpdateComponent;
  let fixture: ComponentFixture<TipoProductoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoProductoFormService: TipoProductoFormService;
  let tipoProductoService: TipoProductoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TipoProductoUpdateComponent],
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
      .overrideTemplate(TipoProductoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoProductoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoProductoFormService = TestBed.inject(TipoProductoFormService);
    tipoProductoService = TestBed.inject(TipoProductoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const tipoProducto: ITipoProducto = { id: 23322 };

      activatedRoute.data = of({ tipoProducto });
      comp.ngOnInit();

      expect(comp.tipoProducto).toEqual(tipoProducto);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoProducto>>();
      const tipoProducto = { id: 6329 };
      jest.spyOn(tipoProductoFormService, 'getTipoProducto').mockReturnValue(tipoProducto);
      jest.spyOn(tipoProductoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoProducto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoProducto }));
      saveSubject.complete();

      // THEN
      expect(tipoProductoFormService.getTipoProducto).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoProductoService.update).toHaveBeenCalledWith(expect.objectContaining(tipoProducto));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoProducto>>();
      const tipoProducto = { id: 6329 };
      jest.spyOn(tipoProductoFormService, 'getTipoProducto').mockReturnValue({ id: null });
      jest.spyOn(tipoProductoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoProducto: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoProducto }));
      saveSubject.complete();

      // THEN
      expect(tipoProductoFormService.getTipoProducto).toHaveBeenCalled();
      expect(tipoProductoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoProducto>>();
      const tipoProducto = { id: 6329 };
      jest.spyOn(tipoProductoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoProducto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoProductoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
