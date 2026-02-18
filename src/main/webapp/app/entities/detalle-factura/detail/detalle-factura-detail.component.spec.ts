import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DetalleFacturaDetailComponent } from './detalle-factura-detail.component';

describe('DetalleFactura Management Detail Component', () => {
  let comp: DetalleFacturaDetailComponent;
  let fixture: ComponentFixture<DetalleFacturaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleFacturaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./detalle-factura-detail.component').then(m => m.DetalleFacturaDetailComponent),
              resolve: { detalleFactura: () => of({ id: 26550 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DetalleFacturaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DetalleFacturaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load detalleFactura on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DetalleFacturaDetailComponent);

      // THEN
      expect(instance.detalleFactura()).toEqual(expect.objectContaining({ id: 26550 }));
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
