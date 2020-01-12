import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { InputMaskModule } from 'primeng/primeng';
import { RadioButtonModule } from 'primeng/components/radiobutton/radiobutton';
import { GrowlModule } from 'primeng/components/growl/growl';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';

import { InputMaskDemoComponent, inputmaskDemoRoute } from './';

const primeng_STATES = [inputmaskDemoRoute];

@NgModule({
  imports: [
    PedidosSharedModule,
    FormsModule,
    InputMaskModule,
    GrowlModule,
    RadioButtonModule,
    WizardModule,
    RouterModule.forRoot(primeng_STATES, { useHash: true })
  ],
  declarations: [InputMaskDemoComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosInputMaskDemoModule {}
