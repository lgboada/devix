import { Routes } from '@angular/router';

const historiaClinicaRoutes: Routes = [
  {
    path: 'categoria-paciente',
    data: { pageTitle: 'devixApp.categoriaPaciente.home.title' },
    loadChildren: () => import('./categoria-paciente/categoria-paciente.routes'),
  },
  {
    path: 'paciente',
    data: { pageTitle: 'devixApp.paciente.home.title' },
    loadChildren: () => import('./paciente/paciente.routes'),
  },
];

export default historiaClinicaRoutes;
