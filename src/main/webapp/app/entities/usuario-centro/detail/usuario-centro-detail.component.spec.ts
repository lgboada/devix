import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UsuarioCentroDetailComponent } from './usuario-centro-detail.component';

describe('UsuarioCentro Management Detail Component', () => {
  let comp: UsuarioCentroDetailComponent;
  let fixture: ComponentFixture<UsuarioCentroDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UsuarioCentroDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./usuario-centro-detail.component').then(m => m.UsuarioCentroDetailComponent),
              resolve: { usuarioCentro: () => of({ id: 30943 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UsuarioCentroDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UsuarioCentroDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load usuarioCentro on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UsuarioCentroDetailComponent);

      // THEN
      expect(instance.usuarioCentro()).toEqual(expect.objectContaining({ id: 30943 }));
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
