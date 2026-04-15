import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TipoDocumentoResolve from './route/tipo-documento-routing-resolve.service';

const tipoDocumentoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tipo-documento.component').then(m => m.TipoDocumentoComponent),
    data: { defaultSort: `indice,${ASC}` },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':noCia/:codigo/view',
    loadComponent: () => import('./detail/tipo-documento-detail.component').then(m => m.TipoDocumentoDetailComponent),
    resolve: { tipoDocumento: TipoDocumentoResolve },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tipo-documento-update.component').then(m => m.TipoDocumentoUpdateComponent),
    resolve: { tipoDocumento: TipoDocumentoResolve },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':noCia/:codigo/edit',
    loadComponent: () => import('./update/tipo-documento-update.component').then(m => m.TipoDocumentoUpdateComponent),
    resolve: { tipoDocumento: TipoDocumentoResolve },
    canActivate: [UserRouteAccessService],
  },
];

export default tipoDocumentoRoute;
