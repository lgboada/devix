import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoCliente } from '../tipo-cliente.model';
import { TipoClienteService } from '../service/tipo-cliente.service';

const tipoClienteResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoCliente> => {
  const id = route.params.id;
  if (id) {
    return inject(TipoClienteService)
      .find(id)
      .pipe(
        mergeMap((tipoCliente: HttpResponse<ITipoCliente>) => {
          if (tipoCliente.body) {
            return of(tipoCliente.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default tipoClienteResolve;
