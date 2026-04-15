import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILineaNegocio } from '../linea-negocio.model';
import { LineaNegocioService } from '../service/linea-negocio.service';

const lineaNegocioResolve = (route: ActivatedRouteSnapshot): Observable<null | ILineaNegocio> => {
  const noCia = route.params['noCia'];
  const lineaNo = route.params['lineaNo'];
  if (noCia && lineaNo) {
    return inject(LineaNegocioService)
      .find(+noCia, lineaNo)
      .pipe(
        mergeMap((res: HttpResponse<ILineaNegocio>) => {
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

export default lineaNegocioResolve;
