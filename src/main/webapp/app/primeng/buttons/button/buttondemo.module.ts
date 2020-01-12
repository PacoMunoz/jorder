import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { ButtonModule } from 'primeng/primeng';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';
import { GrowlModule } from 'primeng/primeng';

import { ButtonDemoComponent, buttonDemoRoute } from './';

const primeng_STATES = [buttonDemoRoute];

@NgModule({
  imports: [PedidosSharedModule, ButtonModule, WizardModule, GrowlModule, RouterModule.forRoot(primeng_STATES, { useHash: true })],
  declarations: [ButtonDemoComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosButtonDemoModule {}
