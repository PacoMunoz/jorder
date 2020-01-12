import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PedidosButtonDemoModule } from './buttons/button/buttondemo.module';
import { PedidosSplitbuttonDemoModule } from './buttons/splitbutton/splitbuttondemo.module';

import { PedidosDialogDemoModule } from './overlay/dialog/dialogdemo.module';
import { PedidosConfirmDialogDemoModule } from './overlay/confirmdialog/confirmdialogdemo.module';
import { PedidosLightboxDemoModule } from './overlay/lightbox/lightboxdemo.module';
import { PedidosTooltipDemoModule } from './overlay/tooltip/tooltipdemo.module';
import { PedidosOverlayPanelDemoModule } from './overlay/overlaypanel/overlaypaneldemo.module';
import { PedidosSideBarDemoModule } from './overlay/sidebar/sidebardemo.module';

import { PedidosKeyFilterDemoModule } from './inputs/keyfilter/keyfilterdemo.module';
import { PedidosInputTextDemoModule } from './inputs/inputtext/inputtextdemo.module';
import { PedidosInputTextAreaDemoModule } from './inputs/inputtextarea/inputtextareademo.module';
import { PedidosInputGroupDemoModule } from './inputs/inputgroup/inputgroupdemo.module';
import { PedidosCalendarDemoModule } from './inputs/calendar/calendardemo.module';
import { PedidosCheckboxDemoModule } from './inputs/checkbox/checkboxdemo.module';
import { PedidosChipsDemoModule } from './inputs/chips/chipsdemo.module';
import { PedidosColorPickerDemoModule } from './inputs/colorpicker/colorpickerdemo.module';
import { PedidosInputMaskDemoModule } from './inputs/inputmask/inputmaskdemo.module';
import { PedidosInputSwitchDemoModule } from './inputs/inputswitch/inputswitchdemo.module';
import { PedidosPasswordIndicatorDemoModule } from './inputs/passwordindicator/passwordindicatordemo.module';
import { PedidosAutoCompleteDemoModule } from './inputs/autocomplete/autocompletedemo.module';
import { PedidosSliderDemoModule } from './inputs/slider/sliderdemo.module';
import { PedidosSpinnerDemoModule } from './inputs/spinner/spinnerdemo.module';
import { PedidosRatingDemoModule } from './inputs/rating/ratingdemo.module';
import { PedidosSelectDemoModule } from './inputs/select/selectdemo.module';
import { PedidosSelectButtonDemoModule } from './inputs/selectbutton/selectbuttondemo.module';
import { PedidosListboxDemoModule } from './inputs/listbox/listboxdemo.module';
import { PedidosRadioButtonDemoModule } from './inputs/radiobutton/radiobuttondemo.module';
import { PedidosToggleButtonDemoModule } from './inputs/togglebutton/togglebuttondemo.module';
import { PedidosEditorDemoModule } from './inputs/editor/editordemo.module';

import { PedidosMessagesDemoModule } from './messages/messages/messagesdemo.module';
import { PedidosToastDemoModule } from './messages/toast/toastdemo.module';
import { PedidosGalleriaDemoModule } from './multimedia/galleria/galleriademo.module';

import { PedidosFileUploadDemoModule } from './fileupload/fileupload/fileuploaddemo.module';

import { PedidosAccordionDemoModule } from './panel/accordion/accordiondemo.module';
import { PedidosPanelDemoModule } from './panel/panel/paneldemo.module';
import { PedidosTabViewDemoModule } from './panel/tabview/tabviewdemo.module';
import { PedidosFieldsetDemoModule } from './panel/fieldset/fieldsetdemo.module';
import { PedidosToolbarDemoModule } from './panel/toolbar/toolbardemo.module';
import { PedidosScrollPanelDemoModule } from './panel/scrollpanel/scrollpaneldemo.module';
import { PedidosCardDemoModule } from './panel/card/carddemo.module';
import { PedidosFlexGridDemoModule } from './panel/flexgrid/flexgriddemo.module';

import { PedidosTableDemoModule } from './data/table/tabledemo.module';
import { PedidosVirtualScrollerDemoModule } from './data/virtualscroller/virtualscrollerdemo.module';
import { PedidosPickListDemoModule } from './data/picklist/picklistdemo.module';
import { PedidosOrderListDemoModule } from './data/orderlist/orderlistdemo.module';
import { PedidosFullCalendarDemoModule } from './data/fullcalendar/fullcalendardemo.module';
import { PedidosTreeDemoModule } from './data/tree/treedemo.module';
import { PedidosTreeTableDemoModule } from './data/treetable/treetabledemo.module';
import { PedidosPaginatorDemoModule } from './data/paginator/paginatordemo.module';
import { PedidosGmapDemoModule } from './data/gmap/gmapdemo.module';
import { PedidosOrgChartDemoModule } from './data/orgchart/orgchartdemo.module';
import { PedidosCarouselDemoModule } from './data/carousel/carouseldemo.module';
import { PedidosDataViewDemoModule } from './data/dataview/dataviewdemo.module';

import { PedidosBarchartDemoModule } from './charts/barchart/barchartdemo.module';
import { PedidosDoughnutchartDemoModule } from './charts/doughnutchart/doughnutchartdemo.module';
import { PedidosLinechartDemoModule } from './charts/linechart/linechartdemo.module';
import { PedidosPiechartDemoModule } from './charts/piechart/piechartdemo.module';
import { PedidosPolarareachartDemoModule } from './charts/polarareachart/polarareachartdemo.module';
import { PedidosRadarchartDemoModule } from './charts/radarchart/radarchartdemo.module';

import { PedidosDragDropDemoModule } from './dragdrop/dragdrop/dragdropdemo.module';

import { PedidosMenuDemoModule } from './menu/menu/menudemo.module';
import { PedidosContextMenuDemoModule } from './menu/contextmenu/contextmenudemo.module';
import { PedidosPanelMenuDemoModule } from './menu/panelmenu/panelmenudemo.module';
import { PedidosStepsDemoModule } from './menu/steps/stepsdemo.module';
import { PedidosTieredMenuDemoModule } from './menu/tieredmenu/tieredmenudemo.module';
import { PedidosBreadcrumbDemoModule } from './menu/breadcrumb/breadcrumbdemo.module';
import { PedidosMegaMenuDemoModule } from './menu/megamenu/megamenudemo.module';
import { PedidosMenuBarDemoModule } from './menu/menubar/menubardemo.module';
import { PedidosSlideMenuDemoModule } from './menu/slidemenu/slidemenudemo.module';
import { PedidosTabMenuDemoModule } from './menu/tabmenu/tabmenudemo.module';

import { PedidosBlockUIDemoModule } from './misc/blockui/blockuidemo.module';
import { PedidosCaptchaDemoModule } from './misc/captcha/captchademo.module';
import { PedidosDeferDemoModule } from './misc/defer/deferdemo.module';
import { PedidosInplaceDemoModule } from './misc/inplace/inplacedemo.module';
import { PedidosProgressBarDemoModule } from './misc/progressbar/progressbardemo.module';
import { PedidosRTLDemoModule } from './misc/rtl/rtldemo.module';
import { PedidosTerminalDemoModule } from './misc/terminal/terminaldemo.module';
import { PedidosValidationDemoModule } from './misc/validation/validationdemo.module';
import { PedidosProgressSpinnerDemoModule } from './misc/progressspinner/progressspinnerdemo.module';

@NgModule({
  imports: [
    PedidosMenuDemoModule,
    PedidosContextMenuDemoModule,
    PedidosPanelMenuDemoModule,
    PedidosStepsDemoModule,
    PedidosTieredMenuDemoModule,
    PedidosBreadcrumbDemoModule,
    PedidosMegaMenuDemoModule,
    PedidosMenuBarDemoModule,
    PedidosSlideMenuDemoModule,
    PedidosTabMenuDemoModule,

    PedidosBlockUIDemoModule,
    PedidosCaptchaDemoModule,
    PedidosDeferDemoModule,
    PedidosInplaceDemoModule,
    PedidosProgressBarDemoModule,
    PedidosInputMaskDemoModule,
    PedidosRTLDemoModule,
    PedidosTerminalDemoModule,
    PedidosValidationDemoModule,

    PedidosButtonDemoModule,
    PedidosSplitbuttonDemoModule,

    PedidosInputTextDemoModule,
    PedidosInputTextAreaDemoModule,
    PedidosInputGroupDemoModule,
    PedidosCalendarDemoModule,
    PedidosChipsDemoModule,
    PedidosInputMaskDemoModule,
    PedidosInputSwitchDemoModule,
    PedidosPasswordIndicatorDemoModule,
    PedidosAutoCompleteDemoModule,
    PedidosSliderDemoModule,
    PedidosSpinnerDemoModule,
    PedidosRatingDemoModule,
    PedidosSelectDemoModule,
    PedidosSelectButtonDemoModule,
    PedidosListboxDemoModule,
    PedidosRadioButtonDemoModule,
    PedidosToggleButtonDemoModule,
    PedidosEditorDemoModule,
    PedidosColorPickerDemoModule,
    PedidosCheckboxDemoModule,
    PedidosKeyFilterDemoModule,

    PedidosMessagesDemoModule,
    PedidosToastDemoModule,
    PedidosGalleriaDemoModule,

    PedidosFileUploadDemoModule,

    PedidosAccordionDemoModule,
    PedidosPanelDemoModule,
    PedidosTabViewDemoModule,
    PedidosFieldsetDemoModule,
    PedidosToolbarDemoModule,
    PedidosScrollPanelDemoModule,
    PedidosCardDemoModule,
    PedidosFlexGridDemoModule,

    PedidosBarchartDemoModule,
    PedidosDoughnutchartDemoModule,
    PedidosLinechartDemoModule,
    PedidosPiechartDemoModule,
    PedidosPolarareachartDemoModule,
    PedidosRadarchartDemoModule,

    PedidosDragDropDemoModule,

    PedidosDialogDemoModule,
    PedidosConfirmDialogDemoModule,
    PedidosLightboxDemoModule,
    PedidosTooltipDemoModule,
    PedidosOverlayPanelDemoModule,
    PedidosSideBarDemoModule,

    PedidosTableDemoModule,
    PedidosDataViewDemoModule,
    PedidosVirtualScrollerDemoModule,
    PedidosFullCalendarDemoModule,
    PedidosOrderListDemoModule,
    PedidosPickListDemoModule,
    PedidosTreeDemoModule,
    PedidosTreeTableDemoModule,
    PedidosPaginatorDemoModule,
    PedidosOrgChartDemoModule,
    PedidosGmapDemoModule,
    PedidosCarouselDemoModule,
    PedidosProgressSpinnerDemoModule
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PedidosprimengModule {}
