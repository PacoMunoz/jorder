import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFamilia } from 'app/shared/model/familia.model';
import { FamiliaService } from './familia.service';

@Component({
  templateUrl: './familia-delete-dialog.component.html'
})
export class FamiliaDeleteDialogComponent {
  familia?: IFamilia;

  constructor(protected familiaService: FamiliaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.familiaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('familiaListModification');
      this.activeModal.close();
    });
  }
}
