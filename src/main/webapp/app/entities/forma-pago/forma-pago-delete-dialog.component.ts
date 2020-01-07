import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFormaPago } from 'app/shared/model/forma-pago.model';
import { FormaPagoService } from './forma-pago.service';

@Component({
  templateUrl: './forma-pago-delete-dialog.component.html'
})
export class FormaPagoDeleteDialogComponent {
  formaPago?: IFormaPago;

  constructor(protected formaPagoService: FormaPagoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formaPagoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('formaPagoListModification');
      this.activeModal.close();
    });
  }
}
