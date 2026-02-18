import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FacturaDetailComponent } from './factura-detail.component';

describe('Factura Management Detail Component', () => {
  let comp: FacturaDetailComponent;
  let fixture: ComponentFixture<FacturaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FacturaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./factura-detail.component').then(m => m.FacturaDetailComponent),
              resolve: { factura: () => of({ id: 30162 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FacturaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FacturaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load factura on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FacturaDetailComponent);

      // THEN
      expect(instance.factura()).toEqual(expect.objectContaining({ id: 30162 }));
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
