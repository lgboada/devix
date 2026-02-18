import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ModeloDetailComponent } from './modelo-detail.component';

describe('Modelo Management Detail Component', () => {
  let comp: ModeloDetailComponent;
  let fixture: ComponentFixture<ModeloDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModeloDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./modelo-detail.component').then(m => m.ModeloDetailComponent),
              resolve: { modelo: () => of({ id: 11658 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ModeloDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModeloDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load modelo on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ModeloDetailComponent);

      // THEN
      expect(instance.modelo()).toEqual(expect.objectContaining({ id: 11658 }));
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
