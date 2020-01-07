import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILocalidad } from 'app/shared/model/localidad.model';
import { LocalidadService } from './localidad.service';

@Component({
  templateUrl: './localidad-delete-dialog.component.html'
})
export class LocalidadDeleteDialogComponent {
  localidad?: ILocalidad;

  constructor(protected localidadService: LocalidadService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.localidadService.delete(id).subscribe(() => {
      this.eventManager.broadcast('localidadListModification');
      this.activeModal.close();
    });
  }
}
