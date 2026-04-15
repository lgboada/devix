import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LineaNegocioResolve from './route/linea-negocio-routing-resolve.service';

const lineaNegocioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/linea-negocio.component').then(m => m.LineaNegocioComponent),
    data: { defaultSort: `lineaNo,${ASC}` },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':noCia/:lineaNo/view',
    loadComponent: () => import('./detail/linea-negocio-detail.component').then(m => m.LineaNegocioDetailComponent),
    resolve: { lineaNegocio: LineaNegocioResolve },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/linea-negocio-update.component').then(m => m.LineaNegocioUpdateComponent),
    resolve: { lineaNegocio: LineaNegocioResolve },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':noCia/:lineaNo/edit',
    loadComponent: () => import('./update/linea-negocio-update.component').then(m => m.LineaNegocioUpdateComponent),
    resolve: { lineaNegocio: LineaNegocioResolve },
    canActivate: [UserRouteAccessService],
  },
];

export default lineaNegocioRoute;
