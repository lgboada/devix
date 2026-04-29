import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import usuarioCentroParentResolve from './route/usuario-centro-parent-resolve.service';
import ucbResolve from './route/usuario-centro-bodega-routing-resolve.service';

const ucbRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/usuario-centro-bodega-list.component').then(m => m.UsuarioCentroBodegaListComponent),
    resolve: { usuarioCentro: usuarioCentroParentResolve },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/usuario-centro-bodega-update.component').then(m => m.UsuarioCentroBodegaUpdateComponent),
    resolve: { usuarioCentro: usuarioCentroParentResolve, ucb: ucbResolve },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':ucbId/edit',
    loadComponent: () => import('./update/usuario-centro-bodega-update.component').then(m => m.UsuarioCentroBodegaUpdateComponent),
    resolve: { usuarioCentro: usuarioCentroParentResolve, ucb: ucbResolve },
    canActivate: [UserRouteAccessService],
  },
];

export default ucbRoute;
