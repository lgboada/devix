import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICatalogo } from '../catalogo.model';
import { CatalogoService } from '../service/catalogo.service';

const catalogoResolve = (route: ActivatedRouteSnapshot): Observable<null | ICatalogo> => {
  const id = route.params.id;
  if (id) {
    return inject(CatalogoService)
      .find(id)
      .pipe(
        mergeMap((catalogo: HttpResponse<ICatalogo>) => {
          if (catalogo.body) {
            return of(catalogo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default catalogoResolve;
