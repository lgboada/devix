import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProveedorDetailComponent } from './proveedor-detail.component';

describe('Proveedor Management Detail Component', () => {
  let comp: ProveedorDetailComponent;
  let fixture: ComponentFixture<ProveedorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProveedorDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./proveedor-detail.component').then(m => m.ProveedorDetailComponent),
              resolve: { proveedor: () => of({ id: 9668 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProveedorDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProveedorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load proveedor on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProveedorDetailComponent);

      // THEN
      expect(instance.proveedor()).toEqual(expect.objectContaining({ id: 9668 }));
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
