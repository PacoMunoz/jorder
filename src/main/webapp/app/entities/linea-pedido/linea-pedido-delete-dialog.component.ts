import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILineaPedido } from 'app/shared/model/linea-pedido.model';
import { LineaPedidoService } from './linea-pedido.service';

@Component({
  templateUrl: './linea-pedido-delete-dialog.component.html'
})
export class LineaPedidoDeleteDialogComponent {
  lineaPedido?: ILineaPedido;

  constructor(
    protected lineaPedidoService: LineaPedidoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lineaPedidoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('lineaPedidoListModification');
      this.activeModal.close();
    });
  }
}
