import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEstadoPedido } from 'app/shared/model/estado-pedido.model';
import { EstadoPedidoService } from './estado-pedido.service';

@Component({
  templateUrl: './estado-pedido-delete-dialog.component.html'
})
export class EstadoPedidoDeleteDialogComponent {
  estadoPedido?: IEstadoPedido;

  constructor(
    protected estadoPedidoService: EstadoPedidoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.estadoPedidoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('estadoPedidoListModification');
      this.activeModal.close();
    });
  }
}
