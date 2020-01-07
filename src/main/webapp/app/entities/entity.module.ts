import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cliente',
        loadChildren: () => import('./cliente/cliente.module').then(m => m.PedidosClienteModule)
      },
      {
        path: 'direccion',
        loadChildren: () => import('./direccion/direccion.module').then(m => m.PedidosDireccionModule)
      },
      {
        path: 'localidad',
        loadChildren: () => import('./localidad/localidad.module').then(m => m.PedidosLocalidadModule)
      },
      {
        path: 'forma-pago',
        loadChildren: () => import('./forma-pago/forma-pago.module').then(m => m.PedidosFormaPagoModule)
      },
      {
        path: 'pedido',
        loadChildren: () => import('./pedido/pedido.module').then(m => m.PedidosPedidoModule)
      },
      {
        path: 'estado-pedido',
        loadChildren: () => import('./estado-pedido/estado-pedido.module').then(m => m.PedidosEstadoPedidoModule)
      },
      {
        path: 'linea-pedido',
        loadChildren: () => import('./linea-pedido/linea-pedido.module').then(m => m.PedidosLineaPedidoModule)
      },
      {
        path: 'producto',
        loadChildren: () => import('./producto/producto.module').then(m => m.PedidosProductoModule)
      },
      {
        path: 'familia',
        loadChildren: () => import('./familia/familia.module').then(m => m.PedidosFamiliaModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PedidosEntityModule {}
