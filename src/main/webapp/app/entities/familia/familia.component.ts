import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFamilia } from 'app/shared/model/familia.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FamiliaService } from './familia.service';
import { FamiliaDeleteDialogComponent } from './familia-delete-dialog.component';

@Component({
  selector: 'jhi-familia',
  templateUrl: './familia.component.html'
})
export class FamiliaComponent implements OnInit, OnDestroy {
  familias: IFamilia[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected familiaService: FamiliaService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.familias = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.familiaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IFamilia[]>) => this.paginateFamilias(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.familias = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFamilias();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFamilia): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFamilias(): void {
    this.eventSubscriber = this.eventManager.subscribe('familiaListModification', () => this.reset());
  }

  delete(familia: IFamilia): void {
    const modalRef = this.modalService.open(FamiliaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.familia = familia;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFamilias(data: IFamilia[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.familias.push(data[i]);
      }
    }
  }
}
