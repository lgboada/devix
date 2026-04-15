import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoDocumento } from '../tipo-documento.model';
import { TipoDocumentoService } from '../service/tipo-documento.service';

const tipoDocumentoResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoDocumento> => {
  const noCia = route.params['noCia'];
  const codigo = route.params['codigo'];
  if (noCia && codigo) {
    return inject(TipoDocumentoService)
      .find(+noCia, codigo)
      .pipe(
        mergeMap((res: HttpResponse<ITipoDocumento>) => {
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

export default tipoDocumentoResolve;
