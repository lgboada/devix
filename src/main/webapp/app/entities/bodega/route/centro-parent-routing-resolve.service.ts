import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICentro } from 'app/entities/centro/centro.model';
import { CentroService } from 'app/entities/centro/service/centro.service';

function resolveCentroId(route: ActivatedRouteSnapshot): string | undefined {
  let current: ActivatedRouteSnapshot | null = route;
  while (current) {
    const id = current.params['id'];
    if (id) {
      return id;
    }
    current = current.parent;
  }
  return undefined;
}

const centroFromParentResolve = (route: ActivatedRouteSnapshot): Observable<null | ICentro> => {
  const id = resolveCentroId(route);
  if (id) {
    return inject(CentroService)
      .find(+id)
      .pipe(
        mergeMap((res: HttpResponse<ICentro>) => {
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

export default centroFromParentResolve;
