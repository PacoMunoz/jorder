import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocalidad } from 'app/shared/model/localidad.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { LocalidadService } from './localidad.service';
import { LocalidadDeleteDialogComponent } from './localidad-delete-dialog.component';

@Component({
  selector: 'jhi-localidad',
  templateUrl: './localidad.component.html'
})
export class LocalidadComponent implements OnInit, OnDestroy {
  localidads: ILocalidad[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected localidadService: LocalidadService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.localidads = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.localidadService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ILocalidad[]>) => this.paginateLocalidads(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.localidads = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLocalidads();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILocalidad): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLocalidads(): void {
    this.eventSubscriber = this.eventManager.subscribe('localidadListModification', () => this.reset());
  }

  delete(localidad: ILocalidad): void {
    const modalRef = this.modalService.open(LocalidadDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.localidad = localidad;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLocalidads(data: ILocalidad[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.localidads.push(data[i]);
      }
    }
  }
}
