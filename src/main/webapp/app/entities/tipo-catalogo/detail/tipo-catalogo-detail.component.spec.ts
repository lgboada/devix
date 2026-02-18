import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TipoCatalogoDetailComponent } from './tipo-catalogo-detail.component';

describe('TipoCatalogo Management Detail Component', () => {
  let comp: TipoCatalogoDetailComponent;
  let fixture: ComponentFixture<TipoCatalogoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipoCatalogoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./tipo-catalogo-detail.component').then(m => m.TipoCatalogoDetailComponent),
              resolve: { tipoCatalogo: () => of({ id: 26545 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TipoCatalogoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TipoCatalogoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load tipoCatalogo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TipoCatalogoDetailComponent);

      // THEN
      expect(instance.tipoCatalogo()).toEqual(expect.objectContaining({ id: 26545 }));
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
