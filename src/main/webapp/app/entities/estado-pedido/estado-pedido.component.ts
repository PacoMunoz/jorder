import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEstadoPedido } from 'app/shared/model/estado-pedido.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { EstadoPedidoService } from './estado-pedido.service';
import { EstadoPedidoDeleteDialogComponent } from './estado-pedido-delete-dialog.component';

@Component({
  selector: 'jhi-estado-pedido',
  templateUrl: './estado-pedido.component.html'
})
export class EstadoPedidoComponent implements OnInit, OnDestroy {
  estadoPedidos: IEstadoPedido[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected estadoPedidoService: EstadoPedidoService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.estadoPedidos = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.estadoPedidoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IEstadoPedido[]>) => this.paginateEstadoPedidos(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.estadoPedidos = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEstadoPedidos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEstadoPedido): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEstadoPedidos(): void {
    this.eventSubscriber = this.eventManager.subscribe('estadoPedidoListModification', () => this.reset());
  }

  delete(estadoPedido: IEstadoPedido): void {
    const modalRef = this.modalService.open(EstadoPedidoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.estadoPedido = estadoPedido;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEstadoPedidos(data: IEstadoPedido[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.estadoPedidos.push(data[i]);
      }
    }
  }
}
