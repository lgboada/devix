import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IModelo } from '../modelo.model';
import { ModeloService } from '../service/modelo.service';

const modeloResolve = (route: ActivatedRouteSnapshot): Observable<null | IModelo> => {
  const id = route.params.id;
  if (id) {
    return inject(ModeloService)
      .find(id)
      .pipe(
        mergeMap((modelo: HttpResponse<IModelo>) => {
          if (modelo.body) {
            return of(modelo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default modeloResolve;
