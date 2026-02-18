import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoEvento } from '../tipo-evento.model';
import { TipoEventoService } from '../service/tipo-evento.service';

const tipoEventoResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoEvento> => {
  const id = route.params.id;
  if (id) {
    return inject(TipoEventoService)
      .find(id)
      .pipe(
        mergeMap((tipoEvento: HttpResponse<ITipoEvento>) => {
          if (tipoEvento.body) {
            return of(tipoEvento.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default tipoEventoResolve;
