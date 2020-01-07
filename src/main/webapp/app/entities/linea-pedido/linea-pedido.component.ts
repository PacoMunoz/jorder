import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILineaPedido } from 'app/shared/model/linea-pedido.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { LineaPedidoService } from './linea-pedido.service';
import { LineaPedidoDeleteDialogComponent } from './linea-pedido-delete-dialog.component';

@Component({
  selector: 'jhi-linea-pedido',
  templateUrl: './linea-pedido.component.html'
})
export class LineaPedidoComponent implements OnInit, OnDestroy {
  lineaPedidos: ILineaPedido[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected lineaPedidoService: LineaPedidoService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.lineaPedidos = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.lineaPedidoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ILineaPedido[]>) => this.paginateLineaPedidos(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.lineaPedidos = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLineaPedidos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILineaPedido): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLineaPedidos(): void {
    this.eventSubscriber = this.eventManager.subscribe('lineaPedidoListModification', () => this.reset());
  }

  delete(lineaPedido: ILineaPedido): void {
    const modalRef = this.modalService.open(LineaPedidoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.lineaPedido = lineaPedido;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLineaPedidos(data: ILineaPedido[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.lineaPedidos.push(data[i]);
      }
    }
  }
}
