import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PedidosSharedModule } from 'app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { RadioButtonModule } from 'primeng/components/radiobutton/radiobutton';
import { GrowlModule } from 'primeng/components/growl/growl';
import { WizardModule } from 'primeng-extensions/components/wizard/wizard.js';

import { RadioButtonDemoComponent, radiobuttonDemoRoute } from './';

const primeng_STATES = [radiobuttonDemoRoute];

@NgModule({
  imports: [
    PedidosSharedModule,
    FormsModule,
    RadioButtonModule,
    GrowlModule,
    WizardModule,
    RouterModule.forRoot(primeng_STATES, { useHash: true })
  ],
  declarations: [RadioButtonDemoComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosRadioButtonDemoModule {}
