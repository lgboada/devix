import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CiudadDetailComponent } from './ciudad-detail.component';

describe('Ciudad Management Detail Component', () => {
  let comp: CiudadDetailComponent;
  let fixture: ComponentFixture<CiudadDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CiudadDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ciudad-detail.component').then(m => m.CiudadDetailComponent),
              resolve: { ciudad: () => of({ id: 13640 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CiudadDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CiudadDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load ciudad on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CiudadDetailComponent);

      // THEN
      expect(instance.ciudad()).toEqual(expect.objectContaining({ id: 13640 }));
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
