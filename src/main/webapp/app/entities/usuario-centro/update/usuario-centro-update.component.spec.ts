import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICentro } from 'app/entities/centro/centro.model';
import { CentroService } from 'app/entities/centro/service/centro.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUsuarioCentro } from '../usuario-centro.model';
import { UsuarioCentroService } from '../service/usuario-centro.service';
import { UsuarioCentroFormService } from './usuario-centro-form.service';

import { UsuarioCentroUpdateComponent } from './usuario-centro-update.component';

describe('UsuarioCentro Management Update Component', () => {
  let comp: UsuarioCentroUpdateComponent;
  let fixture: ComponentFixture<UsuarioCentroUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let usuarioCentroFormService: UsuarioCentroFormService;
  let usuarioCentroService: UsuarioCentroService;
  let centroService: CentroService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UsuarioCentroUpdateComponent],
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
      .overrideTemplate(UsuarioCentroUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UsuarioCentroUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    usuarioCentroFormService = TestBed.inject(UsuarioCentroFormService);
    usuarioCentroService = TestBed.inject(UsuarioCentroService);
    centroService = TestBed.inject(CentroService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Centro query and add missing value', () => {
      const usuarioCentro: IUsuarioCentro = { id: 16943 };
      const centro: ICentro = { id: 10065 };
      usuarioCentro.centro = centro;

      const centroCollection: ICentro[] = [{ id: 10065 }];
      jest.spyOn(centroService, 'query').mockReturnValue(of(new HttpResponse({ body: centroCollection })));
      const additionalCentros = [centro];
      const expectedCollection: ICentro[] = [...additionalCentros, ...centroCollection];
      jest.spyOn(centroService, 'addCentroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuarioCentro });
      comp.ngOnInit();

      expect(centroService.query).toHaveBeenCalled();
      expect(centroService.addCentroToCollectionIfMissing).toHaveBeenCalledWith(
        centroCollection,
        ...additionalCentros.map(expect.objectContaining),
      );
      expect(comp.centrosSharedCollection).toEqual(expectedCollection);
    });

    it('should call User query and add missing value', () => {
      const usuarioCentro: IUsuarioCentro = { id: 16943 };
      const user: IUser = { id: '1344246c-16a7-46d1-bb61-2043f965c8d5' };
      usuarioCentro.user = user;

      const userCollection: IUser[] = [{ id: '1344246c-16a7-46d1-bb61-2043f965c8d5' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ usuarioCentro });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const usuarioCentro: IUsuarioCentro = { id: 16943 };
      const centro: ICentro = { id: 10065 };
      usuarioCentro.centro = centro;
      const user: IUser = { id: '1344246c-16a7-46d1-bb61-2043f965c8d5' };
      usuarioCentro.user = user;

      activatedRoute.data = of({ usuarioCentro });
      comp.ngOnInit();

      expect(comp.centrosSharedCollection).toContainEqual(centro);
      expect(comp.usersSharedCollection).toContainEqual(user);
      expect(comp.usuarioCentro).toEqual(usuarioCentro);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuarioCentro>>();
      const usuarioCentro = { id: 30943 };
      jest.spyOn(usuarioCentroFormService, 'getUsuarioCentro').mockReturnValue(usuarioCentro);
      jest.spyOn(usuarioCentroService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioCentro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuarioCentro }));
      saveSubject.complete();

      // THEN
      expect(usuarioCentroFormService.getUsuarioCentro).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(usuarioCentroService.update).toHaveBeenCalledWith(expect.objectContaining(usuarioCentro));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuarioCentro>>();
      const usuarioCentro = { id: 30943 };
      jest.spyOn(usuarioCentroFormService, 'getUsuarioCentro').mockReturnValue({ id: null });
      jest.spyOn(usuarioCentroService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioCentro: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: usuarioCentro }));
      saveSubject.complete();

      // THEN
      expect(usuarioCentroFormService.getUsuarioCentro).toHaveBeenCalled();
      expect(usuarioCentroService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUsuarioCentro>>();
      const usuarioCentro = { id: 30943 };
      jest.spyOn(usuarioCentroService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ usuarioCentro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(usuarioCentroService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCentro', () => {
      it('should forward to centroService', () => {
        const entity = { id: 10065 };
        const entity2 = { id: 27658 };
        jest.spyOn(centroService, 'compareCentro');
        comp.compareCentro(entity, entity2);
        expect(centroService.compareCentro).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: '1344246c-16a7-46d1-bb61-2043f965c8d5' };
        const entity2 = { id: '1e61df13-b2d3-459d-875e-5607a4ccdbdb' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
