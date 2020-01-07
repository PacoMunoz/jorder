import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEstadoPedido } from 'app/shared/model/estado-pedido.model';

@Component({
  selector: 'jhi-estado-pedido-detail',
  templateUrl: './estado-pedido-detail.component.html'
})
export class EstadoPedidoDetailComponent implements OnInit {
  estadoPedido: IEstadoPedido | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ estadoPedido }) => {
      this.estadoPedido = estadoPedido;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
