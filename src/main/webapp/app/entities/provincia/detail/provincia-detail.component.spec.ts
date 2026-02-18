import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProvinciaDetailComponent } from './provincia-detail.component';

describe('Provincia Management Detail Component', () => {
  let comp: ProvinciaDetailComponent;
  let fixture: ComponentFixture<ProvinciaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProvinciaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./provincia-detail.component').then(m => m.ProvinciaDetailComponent),
              resolve: { provincia: () => of({ id: 18523 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProvinciaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProvinciaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load provincia on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProvinciaDetailComponent);

      // THEN
      expect(instance.provincia()).toEqual(expect.objectContaining({ id: 18523 }));
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
