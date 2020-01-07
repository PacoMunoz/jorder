import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { FamiliaComponent } from './familia.component';
import { FamiliaDetailComponent } from './familia-detail.component';
import { FamiliaUpdateComponent } from './familia-update.component';
import { FamiliaDeleteDialogComponent } from './familia-delete-dialog.component';
import { familiaRoute } from './familia.route';

@NgModule({
  imports: [PedidosSharedModule, RouterModule.forChild(familiaRoute)],
  declarations: [FamiliaComponent, FamiliaDetailComponent, FamiliaUpdateComponent, FamiliaDeleteDialogComponent],
  entryComponents: [FamiliaDeleteDialogComponent]
})
export class PedidosFamiliaModule {}
