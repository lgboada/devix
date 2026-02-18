import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CatalogoDetailComponent } from './catalogo-detail.component';

describe('Catalogo Management Detail Component', () => {
  let comp: CatalogoDetailComponent;
  let fixture: ComponentFixture<CatalogoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./catalogo-detail.component').then(m => m.CatalogoDetailComponent),
              resolve: { catalogo: () => of({ id: 14600 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CatalogoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CatalogoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load catalogo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CatalogoDetailComponent);

      // THEN
      expect(instance.catalogo()).toEqual(expect.objectContaining({ id: 14600 }));
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
