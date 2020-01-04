import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PedidosSharedModule } from 'app/shared/shared.module';
import { PedidosCoreModule } from 'app/core/core.module';
import { PedidosAppRoutingModule } from './app-routing.module';
import { PedidosHomeModule } from './home/home.module';
import { PedidosEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    PedidosSharedModule,
    PedidosCoreModule,
    PedidosHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PedidosEntityModule,
    PedidosAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class PedidosAppModule {}
