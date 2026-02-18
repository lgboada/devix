import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'pais',
    data: { pageTitle: 'devixApp.pais.home.title' },
    loadChildren: () => import('./pais/pais.routes'),
  },
  {
    path: 'provincia',
    data: { pageTitle: 'devixApp.provincia.home.title' },
    loadChildren: () => import('./provincia/provincia.routes'),
  },
  {
    path: 'ciudad',
    data: { pageTitle: 'devixApp.ciudad.home.title' },
    loadChildren: () => import('./ciudad/ciudad.routes'),
  },
  {
    path: 'compania',
    data: { pageTitle: 'devixApp.compania.home.title' },
    loadChildren: () => import('./compania/compania.routes'),
  },
  {
    path: 'centro',
    data: { pageTitle: 'devixApp.centro.home.title' },
    loadChildren: () => import('./centro/centro.routes'),
  },
  {
    path: 'tipo-cliente',
    data: { pageTitle: 'devixApp.tipoCliente.home.title' },
    loadChildren: () => import('./tipo-cliente/tipo-cliente.routes'),
  },
  {
    path: 'factura',
    data: { pageTitle: 'devixApp.factura.home.title' },
    loadChildren: () => import('./factura/factura.routes'),
  },
  {
    path: 'detalle-factura',
    data: { pageTitle: 'devixApp.detalleFactura.home.title' },
    loadChildren: () => import('./detalle-factura/detalle-factura.routes'),
  },
  {
    path: 'cliente',
    data: { pageTitle: 'devixApp.cliente.home.title' },
    loadChildren: () => import('./cliente/cliente.routes'),
  },
  {
    path: 'tipo-direccion',
    data: { pageTitle: 'devixApp.tipoDireccion.home.title' },
    loadChildren: () => import('./tipo-direccion/tipo-direccion.routes'),
  },
  {
    path: 'direccion',
    data: { pageTitle: 'devixApp.direccion.home.title' },
    loadChildren: () => import('./direccion/direccion.routes'),
  },
  {
    path: 'tipo-catalogo',
    data: { pageTitle: 'devixApp.tipoCatalogo.home.title' },
    loadChildren: () => import('./tipo-catalogo/tipo-catalogo.routes'),
  },
  {
    path: 'catalogo',
    data: { pageTitle: 'devixApp.catalogo.home.title' },
    loadChildren: () => import('./catalogo/catalogo.routes'),
  },
  {
    path: 'tipo-producto',
    data: { pageTitle: 'devixApp.tipoProducto.home.title' },
    loadChildren: () => import('./tipo-producto/tipo-producto.routes'),
  },
  {
    path: 'marca',
    data: { pageTitle: 'devixApp.marca.home.title' },
    loadChildren: () => import('./marca/marca.routes'),
  },
  {
    path: 'modelo',
    data: { pageTitle: 'devixApp.modelo.home.title' },
    loadChildren: () => import('./modelo/modelo.routes'),
  },
  {
    path: 'producto',
    data: { pageTitle: 'devixApp.producto.home.title' },
    loadChildren: () => import('./producto/producto.routes'),
  },
  {
    path: 'empleado',
    data: { pageTitle: 'devixApp.empleado.home.title' },
    loadChildren: () => import('./empleado/empleado.routes'),
  },
  {
    path: 'proveedor',
    data: { pageTitle: 'devixApp.proveedor.home.title' },
    loadChildren: () => import('./proveedor/proveedor.routes'),
  },
  {
    path: 'tipo-evento',
    data: { pageTitle: 'devixApp.tipoEvento.home.title' },
    loadChildren: () => import('./tipo-evento/tipo-evento.routes'),
  },
  {
    path: 'evento',
    data: { pageTitle: 'devixApp.evento.home.title' },
    loadChildren: () => import('./evento/evento.routes'),
  },
  {
    path: 'documento',
    data: { pageTitle: 'devixApp.documento.home.title' },
    loadChildren: () => import('./documento/documento.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
