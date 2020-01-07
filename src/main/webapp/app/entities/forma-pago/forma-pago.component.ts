import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormaPago } from 'app/shared/model/forma-pago.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FormaPagoService } from './forma-pago.service';
import { FormaPagoDeleteDialogComponent } from './forma-pago-delete-dialog.component';

@Component({
  selector: 'jhi-forma-pago',
  templateUrl: './forma-pago.component.html'
})
export class FormaPagoComponent implements OnInit, OnDestroy {
  formaPagos: IFormaPago[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected formaPagoService: FormaPagoService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.formaPagos = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.formaPagoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IFormaPago[]>) => this.paginateFormaPagos(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.formaPagos = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFormaPagos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFormaPago): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFormaPagos(): void {
    this.eventSubscriber = this.eventManager.subscribe('formaPagoListModification', () => this.reset());
  }

  delete(formaPago: IFormaPago): void {
    const modalRef = this.modalService.open(FormaPagoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.formaPago = formaPago;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFormaPagos(data: IFormaPago[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.formaPagos.push(data[i]);
      }
    }
  }
}
