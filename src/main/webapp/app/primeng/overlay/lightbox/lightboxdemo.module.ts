import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { LightboxModule } from 'primeng/components/lightbox/lightbox';
import { GrowlModule } from 'primeng/primeng';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';

import { LightboxDemoComponent, lightboxDemoRoute } from './';

const primeng_STATES = [lightboxDemoRoute];

@NgModule({
  imports: [
    PedidosSharedModule,
    LightboxModule,
    GrowlModule,
    BrowserAnimationsModule,
    WizardModule,
    RouterModule.forRoot(primeng_STATES, { useHash: true })
  ],
  declarations: [LightboxDemoComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosLightboxDemoModule {}
