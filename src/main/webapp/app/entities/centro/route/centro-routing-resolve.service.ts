import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICentro } from '../centro.model';
import { CentroService } from '../service/centro.service';

const centroResolve = (route: ActivatedRouteSnapshot): Observable<null | ICentro> => {
  const id = route.params.id;
  if (id) {
    return inject(CentroService)
      .find(id)
      .pipe(
        mergeMap((centro: HttpResponse<ICentro>) => {
          if (centro.body) {
            return of(centro.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default centroResolve;
