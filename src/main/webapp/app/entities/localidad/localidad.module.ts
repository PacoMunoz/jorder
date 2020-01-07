import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { LocalidadComponent } from './localidad.component';
import { LocalidadDetailComponent } from './localidad-detail.component';
import { LocalidadUpdateComponent } from './localidad-update.component';
import { LocalidadDeleteDialogComponent } from './localidad-delete-dialog.component';
import { localidadRoute } from './localidad.route';

@NgModule({
  imports: [PedidosSharedModule, RouterModule.forChild(localidadRoute)],
  declarations: [LocalidadComponent, LocalidadDetailComponent, LocalidadUpdateComponent, LocalidadDeleteDialogComponent],
  entryComponents: [LocalidadDeleteDialogComponent]
})
export class PedidosLocalidadModule {}
