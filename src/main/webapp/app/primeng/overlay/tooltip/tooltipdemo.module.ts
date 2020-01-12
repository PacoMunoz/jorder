import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { TooltipModule } from 'primeng/components/tooltip/tooltip';
import { InputTextModule } from 'primeng/components/inputtext/inputtext';
import { GrowlModule } from 'primeng/primeng';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';

import { TooltipDemoComponent, tooltipDemoRoute } from './';

const primeng_STATES = [tooltipDemoRoute];

@NgModule({
  imports: [
    PedidosSharedModule,
    TooltipModule,
    InputTextModule,
    GrowlModule,
    BrowserAnimationsModule,
    WizardModule,
    RouterModule.forRoot(primeng_STATES, { useHash: true })
  ],
  declarations: [TooltipDemoComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosTooltipDemoModule {}
