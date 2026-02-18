import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CatalogoResolve from './route/catalogo-routing-resolve.service';

const catalogoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/catalogo.component').then(m => m.CatalogoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/catalogo-detail.component').then(m => m.CatalogoDetailComponent),
    resolve: {
      catalogo: CatalogoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/catalogo-update.component').then(m => m.CatalogoUpdateComponent),
    resolve: {
      catalogo: CatalogoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/catalogo-update.component').then(m => m.CatalogoUpdateComponent),
    resolve: {
      catalogo: CatalogoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default catalogoRoute;
