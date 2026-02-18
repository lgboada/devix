import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMarca } from '../marca.model';
import { MarcaService } from '../service/marca.service';

const marcaResolve = (route: ActivatedRouteSnapshot): Observable<null | IMarca> => {
  const id = route.params.id;
  if (id) {
    return inject(MarcaService)
      .find(id)
      .pipe(
        mergeMap((marca: HttpResponse<IMarca>) => {
          if (marca.body) {
            return of(marca.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default marcaResolve;
