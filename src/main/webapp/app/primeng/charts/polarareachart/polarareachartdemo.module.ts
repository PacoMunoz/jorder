import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { ChartModule } from 'primeng/primeng';

import { PolarareachartDemoComponent, polarareachartDemoRoute } from '../../charts/polarareachart';

const primeng_STATES = [polarareachartDemoRoute];

@NgModule({
  imports: [PedidosSharedModule, ChartModule, RouterModule.forRoot(primeng_STATES, { useHash: true })],
  declarations: [PolarareachartDemoComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosPolarareachartDemoModule {}
