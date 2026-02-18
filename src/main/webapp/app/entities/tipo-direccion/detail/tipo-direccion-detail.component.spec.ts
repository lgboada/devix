import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TipoDireccionDetailComponent } from './tipo-direccion-detail.component';

describe('TipoDireccion Management Detail Component', () => {
  let comp: TipoDireccionDetailComponent;
  let fixture: ComponentFixture<TipoDireccionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipoDireccionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./tipo-direccion-detail.component').then(m => m.TipoDireccionDetailComponent),
              resolve: { tipoDireccion: () => of({ id: 22481 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TipoDireccionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TipoDireccionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load tipoDireccion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TipoDireccionDetailComponent);

      // THEN
      expect(instance.tipoDireccion()).toEqual(expect.objectContaining({ id: 22481 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
