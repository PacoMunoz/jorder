import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ILineaPedido, LineaPedido } from 'app/shared/model/linea-pedido.model';
import { LineaPedidoService } from './linea-pedido.service';
import { IPedido } from 'app/shared/model/pedido.model';
import { PedidoService } from 'app/entities/pedido/pedido.service';
import { IProducto } from 'app/shared/model/producto.model';
import { ProductoService } from 'app/entities/producto/producto.service';

type SelectableEntity = IPedido | IProducto;

@Component({
  selector: 'jhi-linea-pedido-update',
  templateUrl: './linea-pedido-update.component.html'
})
export class LineaPedidoUpdateComponent implements OnInit {
  isSaving = false;

  pedidos: IPedido[] = [];

  productos: IProducto[] = [];

  editForm = this.fb.group({
    id: [],
    cantidad: [null, [Validators.required]],
    pedido: [],
    producto: []
  });

  constructor(
    protected lineaPedidoService: LineaPedidoService,
    protected pedidoService: PedidoService,
    protected productoService: ProductoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lineaPedido }) => {
      this.updateForm(lineaPedido);

      this.pedidoService
        .query()
        .pipe(
          map((res: HttpResponse<IPedido[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IPedido[]) => (this.pedidos = resBody));

      this.productoService
        .query()
        .pipe(
          map((res: HttpResponse<IProducto[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IProducto[]) => (this.productos = resBody));
    });
  }

  updateForm(lineaPedido: ILineaPedido): void {
    this.editForm.patchValue({
      id: lineaPedido.id,
      cantidad: lineaPedido.cantidad,
      pedido: lineaPedido.pedido,
      producto: lineaPedido.producto
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lineaPedido = this.createFromForm();
    if (lineaPedido.id !== undefined) {
      this.subscribeToSaveResponse(this.lineaPedidoService.update(lineaPedido));
    } else {
      this.subscribeToSaveResponse(this.lineaPedidoService.create(lineaPedido));
    }
  }

  private createFromForm(): ILineaPedido {
    return {
      ...new LineaPedido(),
      id: this.editForm.get(['id'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      pedido: this.editForm.get(['pedido'])!.value,
      producto: this.editForm.get(['producto'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILineaPedido>>): void {
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
