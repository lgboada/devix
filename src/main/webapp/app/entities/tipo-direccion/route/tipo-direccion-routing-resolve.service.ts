import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoDireccion } from '../tipo-direccion.model';
import { TipoDireccionService } from '../service/tipo-direccion.service';

const tipoDireccionResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoDireccion> => {
  const id = route.params.id;
  if (id) {
    return inject(TipoDireccionService)
      .find(id)
      .pipe(
        mergeMap((tipoDireccion: HttpResponse<ITipoDireccion>) => {
          if (tipoDireccion.body) {
            return of(tipoDireccion.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default tipoDireccionResolve;
