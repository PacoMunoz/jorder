import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { InputTextareaModule } from 'primeng/primeng';
import { GrowlModule } from 'primeng/components/growl/growl';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';

import { InputTextAreaDemoComponent, inputTextAreaDemoRoute } from './';

const primeng_STATES = [inputTextAreaDemoRoute];

@NgModule({
  imports: [
    PedidosSharedModule,
    FormsModule,
    InputTextareaModule,
    GrowlModule,
    WizardModule,
    RouterModule.forRoot(primeng_STATES, { useHash: true })
  ],
  declarations: [InputTextAreaDemoComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosInputTextAreaDemoModule {}
