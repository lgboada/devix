import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUsuarioCentro } from '../usuario-centro.model';
import { UsuarioCentroService } from '../service/usuario-centro.service';

const usuarioCentroResolve = (route: ActivatedRouteSnapshot): Observable<null | IUsuarioCentro> => {
  const id = route.params.id;
  if (id) {
    return inject(UsuarioCentroService)
      .find(id)
      .pipe(
        mergeMap((usuarioCentro: HttpResponse<IUsuarioCentro>) => {
          if (usuarioCentro.body) {
            return of(usuarioCentro.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default usuarioCentroResolve;
