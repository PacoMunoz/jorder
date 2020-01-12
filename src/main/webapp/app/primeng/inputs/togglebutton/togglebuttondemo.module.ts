import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { ToggleButtonModule } from 'primeng/components/togglebutton/togglebutton';
import { GrowlModule } from 'primeng/components/growl/growl';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';

import { ToggleButtonDemoComponent, spinnerDemoRoute } from './';

const primeng_STATES = [spinnerDemoRoute];

@NgModule({
  imports: [
    PedidosSharedModule,
    FormsModule,
    ToggleButtonModule,
    GrowlModule,
    WizardModule,
    RouterModule.forRoot(primeng_STATES, { useHash: true })
  ],
  declarations: [ToggleButtonDemoComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosToggleButtonDemoModule {}
