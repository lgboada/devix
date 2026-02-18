import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoCatalogo } from '../tipo-catalogo.model';
import { TipoCatalogoService } from '../service/tipo-catalogo.service';

const tipoCatalogoResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoCatalogo> => {
  const id = route.params.id;
  if (id) {
    return inject(TipoCatalogoService)
      .find(id)
      .pipe(
        mergeMap((tipoCatalogo: HttpResponse<ITipoCatalogo>) => {
          if (tipoCatalogo.body) {
            return of(tipoCatalogo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default tipoCatalogoResolve;
