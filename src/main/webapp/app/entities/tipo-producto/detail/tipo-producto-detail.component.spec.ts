import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TipoProductoDetailComponent } from './tipo-producto-detail.component';

describe('TipoProducto Management Detail Component', () => {
  let comp: TipoProductoDetailComponent;
  let fixture: ComponentFixture<TipoProductoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipoProductoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./tipo-producto-detail.component').then(m => m.TipoProductoDetailComponent),
              resolve: { tipoProducto: () => of({ id: 6329 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TipoProductoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TipoProductoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load tipoProducto on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TipoProductoDetailComponent);

      // THEN
      expect(instance.tipoProducto()).toEqual(expect.objectContaining({ id: 6329 }));
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
