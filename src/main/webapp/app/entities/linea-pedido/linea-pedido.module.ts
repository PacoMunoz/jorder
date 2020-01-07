import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { LineaPedidoComponent } from './linea-pedido.component';
import { LineaPedidoDetailComponent } from './linea-pedido-detail.component';
import { LineaPedidoUpdateComponent } from './linea-pedido-update.component';
import { LineaPedidoDeleteDialogComponent } from './linea-pedido-delete-dialog.component';
import { lineaPedidoRoute } from './linea-pedido.route';

@NgModule({
  imports: [PedidosSharedModule, RouterModule.forChild(lineaPedidoRoute)],
  declarations: [LineaPedidoComponent, LineaPedidoDetailComponent, LineaPedidoUpdateComponent, LineaPedidoDeleteDialogComponent],
  entryComponents: [LineaPedidoDeleteDialogComponent]
})
export class PedidosLineaPedidoModule {}
