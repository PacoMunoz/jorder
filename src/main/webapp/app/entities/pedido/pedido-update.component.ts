import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPedido, Pedido } from 'app/shared/model/pedido.model';
import { PedidoService } from './pedido.service';
import { IDireccion } from 'app/shared/model/direccion.model';
import { DireccionService } from 'app/entities/direccion/direccion.service';
import { ICliente } from 'app/shared/model/cliente.model';
import { ClienteService } from 'app/entities/cliente/cliente.service';
import { IFormaPago } from 'app/shared/model/forma-pago.model';
import { FormaPagoService } from 'app/entities/forma-pago/forma-pago.service';
import { IEstadoPedido } from 'app/shared/model/estado-pedido.model';
import { EstadoPedidoService } from 'app/entities/estado-pedido/estado-pedido.service';

type SelectableEntity = IDireccion | ICliente | IFormaPago | IEstadoPedido;

@Component({
  selector: 'jhi-pedido-update',
  templateUrl: './pedido-update.component.html'
})
export class PedidoUpdateComponent implements OnInit {
  isSaving = false;

  direccions: IDireccion[] = [];

  clientes: ICliente[] = [];

  formapagos: IFormaPago[] = [];

  estadopedidos: IEstadoPedido[] = [];

  editForm = this.fb.group({
    id: [],
    codigo: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(8)]],
    aDomicilio: [],
    fechaPedido: [null, [Validators.required]],
    direccion: [],
    cliente: [],
    formaPago: [],
    estadoPedido: []
  });

  constructor(
    protected pedidoService: PedidoService,
    protected direccionService: DireccionService,
    protected clienteService: ClienteService,
    protected formaPagoService: FormaPagoService,
    protected estadoPedidoService: EstadoPedidoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pedido }) => {
      this.updateForm(pedido);

      this.direccionService
        .query({ 'pedidoId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDireccion[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDireccion[]) => {
          if (!pedido.direccion || !pedido.direccion.id) {
            this.direccions = resBody;
          } else {
            this.direccionService
              .find(pedido.direccion.id)
              .pipe(
                map((subRes: HttpResponse<IDireccion>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IDireccion[]) => {
                this.direccions = concatRes;
              });
          }
        });

      this.clienteService
        .query()
        .pipe(
          map((res: HttpResponse<ICliente[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICliente[]) => (this.clientes = resBody));

      this.formaPagoService
        .query()
        .pipe(
          map((res: HttpResponse<IFormaPago[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IFormaPago[]) => (this.formapagos = resBody));

      this.estadoPedidoService
        .query()
        .pipe(
          map((res: HttpResponse<IEstadoPedido[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IEstadoPedido[]) => (this.estadopedidos = resBody));
    });
  }

  updateForm(pedido: IPedido): void {
    this.editForm.patchValue({
      id: pedido.id,
      codigo: pedido.codigo,
      aDomicilio: pedido.aDomicilio,
      fechaPedido: pedido.fechaPedido != null ? pedido.fechaPedido.format(DATE_TIME_FORMAT) : null,
      direccion: pedido.direccion,
      cliente: pedido.cliente,
      formaPago: pedido.formaPago,
      estadoPedido: pedido.estadoPedido
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pedido = this.createFromForm();
    if (pedido.id !== undefined) {
      this.subscribeToSaveResponse(this.pedidoService.update(pedido));
    } else {
      this.subscribeToSaveResponse(this.pedidoService.create(pedido));
    }
  }

  private createFromForm(): IPedido {
    return {
      ...new Pedido(),
      id: this.editForm.get(['id'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      aDomicilio: this.editForm.get(['aDomicilio'])!.value,
      fechaPedido:
        this.editForm.get(['fechaPedido'])!.value != null ? moment(this.editForm.get(['fechaPedido'])!.value, DATE_TIME_FORMAT) : undefined,
      direccion: this.editForm.get(['direccion'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
      formaPago: this.editForm.get(['formaPago'])!.value,
      estadoPedido: this.editForm.get(['estadoPedido'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPedido>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
