import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUsuarioCentro } from 'app/entities/usuario-centro/usuario-centro.model';
import { UsuarioCentroService } from 'app/entities/usuario-centro/service/usuario-centro.service';

function resolveUsuarioCentroId(route: ActivatedRouteSnapshot): string | undefined {
  let current: ActivatedRouteSnapshot | null = route;
  while (current) {
    const id = current.params['id'];
    if (id) return id;
    current = current.parent;
  }
  return undefined;
}

const usuarioCentroParentResolve = (route: ActivatedRouteSnapshot): Observable<null | IUsuarioCentro> => {
  const id = resolveUsuarioCentroId(route);
  if (id) {
    return inject(UsuarioCentroService)
      .find(+id)
      .pipe(
        mergeMap((res: HttpResponse<IUsuarioCentro>) => {
          if (res.body) return of(res.body);
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default usuarioCentroParentResolve;
