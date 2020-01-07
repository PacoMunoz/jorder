import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDireccion } from 'app/shared/model/direccion.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DireccionService } from './direccion.service';
import { DireccionDeleteDialogComponent } from './direccion-delete-dialog.component';

@Component({
  selector: 'jhi-direccion',
  templateUrl: './direccion.component.html'
})
export class DireccionComponent implements OnInit, OnDestroy {
  direccions: IDireccion[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected direccionService: DireccionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.direccions = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.direccionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IDireccion[]>) => this.paginateDireccions(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.direccions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDireccions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDireccion): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDireccions(): void {
    this.eventSubscriber = this.eventManager.subscribe('direccionListModification', () => this.reset());
  }

  delete(direccion: IDireccion): void {
    const modalRef = this.modalService.open(DireccionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.direccion = direccion;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateDireccions(data: IDireccion[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.direccions.push(data[i]);
      }
    }
  }
}
