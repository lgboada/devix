import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TipoClienteDetailComponent } from './tipo-cliente-detail.component';

describe('TipoCliente Management Detail Component', () => {
  let comp: TipoClienteDetailComponent;
  let fixture: ComponentFixture<TipoClienteDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipoClienteDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./tipo-cliente-detail.component').then(m => m.TipoClienteDetailComponent),
              resolve: { tipoCliente: () => of({ id: 28838 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TipoClienteDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TipoClienteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load tipoCliente on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TipoClienteDetailComponent);

      // THEN
      expect(instance.tipoCliente()).toEqual(expect.objectContaining({ id: 28838 }));
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
