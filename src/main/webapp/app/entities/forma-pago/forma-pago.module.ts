import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { FormaPagoComponent } from './forma-pago.component';
import { FormaPagoDetailComponent } from './forma-pago-detail.component';
import { FormaPagoUpdateComponent } from './forma-pago-update.component';
import { FormaPagoDeleteDialogComponent } from './forma-pago-delete-dialog.component';
import { formaPagoRoute } from './forma-pago.route';

@NgModule({
  imports: [PedidosSharedModule, RouterModule.forChild(formaPagoRoute)],
  declarations: [FormaPagoComponent, FormaPagoDetailComponent, FormaPagoUpdateComponent, FormaPagoDeleteDialogComponent],
  entryComponents: [FormaPagoDeleteDialogComponent]
})
export class PedidosFormaPagoModule {}
