import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormaPago } from 'app/shared/model/forma-pago.model';

@Component({
  selector: 'jhi-forma-pago-detail',
  templateUrl: './forma-pago-detail.component.html'
})
export class FormaPagoDetailComponent implements OnInit {
  formaPago: IFormaPago | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formaPago }) => {
      this.formaPago = formaPago;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
