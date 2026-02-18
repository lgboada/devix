import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { EmpleadoService } from '../service/empleado.service';
import { IEmpleado } from '../empleado.model';
import { EmpleadoFormService } from './empleado-form.service';

import { EmpleadoUpdateComponent } from './empleado-update.component';

describe('Empleado Management Update Component', () => {
  let comp: EmpleadoUpdateComponent;
  let fixture: ComponentFixture<EmpleadoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empleadoFormService: EmpleadoFormService;
  let empleadoService: EmpleadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmpleadoUpdateComponent],
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
      .overrideTemplate(EmpleadoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpleadoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empleadoFormService = TestBed.inject(EmpleadoFormService);
    empleadoService = TestBed.inject(EmpleadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const empleado: IEmpleado = { id: 25035 };

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(comp.empleado).toEqual(empleado);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleado>>();
      const empleado = { id: 11214 };
      jest.spyOn(empleadoFormService, 'getEmpleado').mockReturnValue(empleado);
      jest.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleado }));
      saveSubject.complete();

      // THEN
      expect(empleadoFormService.getEmpleado).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(empleadoService.update).toHaveBeenCalledWith(expect.objectContaining(empleado));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleado>>();
      const empleado = { id: 11214 };
      jest.spyOn(empleadoFormService, 'getEmpleado').mockReturnValue({ id: null });
      jest.spyOn(empleadoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleado }));
      saveSubject.complete();

      // THEN
      expect(empleadoFormService.getEmpleado).toHaveBeenCalled();
      expect(empleadoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleado>>();
      const empleado = { id: 11214 };
      jest.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empleadoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
