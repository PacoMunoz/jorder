import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { PedidosSharedModule } from 'app/shared/shared.module';
import { GrowlModule } from 'primeng/primeng';
import { AccordionModule } from 'primeng/primeng';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';

import { RTLDemoComponent, rtlDemoRoute } from './';

const primeng_STATES = [rtlDemoRoute];

@NgModule({
  imports: [
    PedidosSharedModule,
    CommonModule,
    BrowserAnimationsModule,
    GrowlModule,
    AccordionModule,
    WizardModule,
    RouterModule.forRoot(primeng_STATES, { useHash: true })
  ],
  declarations: [RTLDemoComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosRTLDemoModule {}
