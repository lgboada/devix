import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MarcaDetailComponent } from './marca-detail.component';

describe('Marca Management Detail Component', () => {
  let comp: MarcaDetailComponent;
  let fixture: ComponentFixture<MarcaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MarcaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./marca-detail.component').then(m => m.MarcaDetailComponent),
              resolve: { marca: () => of({ id: 9264 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MarcaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MarcaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load marca on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MarcaDetailComponent);

      // THEN
      expect(instance.marca()).toEqual(expect.objectContaining({ id: 9264 }));
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
