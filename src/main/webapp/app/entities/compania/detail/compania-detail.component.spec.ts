import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CompaniaDetailComponent } from './compania-detail.component';

describe('Compania Management Detail Component', () => {
  let comp: CompaniaDetailComponent;
  let fixture: ComponentFixture<CompaniaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompaniaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./compania-detail.component').then(m => m.CompaniaDetailComponent),
              resolve: { compania: () => of({ id: 14846 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CompaniaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompaniaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load compania on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CompaniaDetailComponent);

      // THEN
      expect(instance.compania()).toEqual(expect.objectContaining({ id: 14846 }));
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
