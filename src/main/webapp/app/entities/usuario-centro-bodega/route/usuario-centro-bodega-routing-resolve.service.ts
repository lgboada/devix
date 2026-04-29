import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUsuarioCentroBodega } from '../usuario-centro-bodega.model';
import { UsuarioCentroBodegaService } from '../service/usuario-centro-bodega.service';

const ucbResolve = (route: ActivatedRouteSnapshot): Observable<null | IUsuarioCentroBodega> => {
  const id = route.params['ucbId'];
  if (id) {
    return inject(UsuarioCentroBodegaService)
      .find(+id)
      .pipe(
        mergeMap((res: HttpResponse<IUsuarioCentroBodega>) => {
          if (res.body) return of(res.body);
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ucbResolve;
