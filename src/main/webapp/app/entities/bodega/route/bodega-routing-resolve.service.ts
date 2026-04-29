import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBodega } from '../bodega.model';
import { BodegaService } from '../service/bodega.service';

const bodegaResolve = (route: ActivatedRouteSnapshot): Observable<null | IBodega> => {
  const id = route.params['bodegaId'];
  if (id) {
    return inject(BodegaService)
      .find(+id)
      .pipe(
        mergeMap((res: HttpResponse<IBodega>) => {
          if (res.body) {
            return of(res.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bodegaResolve;
