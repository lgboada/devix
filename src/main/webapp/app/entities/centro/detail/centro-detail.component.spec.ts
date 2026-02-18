import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CentroDetailComponent } from './centro-detail.component';

describe('Centro Management Detail Component', () => {
  let comp: CentroDetailComponent;
  let fixture: ComponentFixture<CentroDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CentroDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./centro-detail.component').then(m => m.CentroDetailComponent),
              resolve: { centro: () => of({ id: 10065 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CentroDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CentroDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load centro on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CentroDetailComponent);

      // THEN
      expect(instance.centro()).toEqual(expect.objectContaining({ id: 10065 }));
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
