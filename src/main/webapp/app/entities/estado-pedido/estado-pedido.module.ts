import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { EstadoPedidoComponent } from './estado-pedido.component';
import { EstadoPedidoDetailComponent } from './estado-pedido-detail.component';
import { EstadoPedidoUpdateComponent } from './estado-pedido-update.component';
import { EstadoPedidoDeleteDialogComponent } from './estado-pedido-delete-dialog.component';
import { estadoPedidoRoute } from './estado-pedido.route';

@NgModule({
  imports: [PedidosSharedModule, RouterModule.forChild(estadoPedidoRoute)],
  declarations: [EstadoPedidoComponent, EstadoPedidoDetailComponent, EstadoPedidoUpdateComponent, EstadoPedidoDeleteDialogComponent],
  entryComponents: [EstadoPedidoDeleteDialogComponent]
})
export class PedidosEstadoPedidoModule {}
