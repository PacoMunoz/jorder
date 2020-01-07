import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEstadoPedido, EstadoPedido } from 'app/shared/model/estado-pedido.model';
import { EstadoPedidoService } from './estado-pedido.service';

@Component({
  selector: 'jhi-estado-pedido-update',
  templateUrl: './estado-pedido-update.component.html'
})
export class EstadoPedidoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    codigo: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(8)]],
    descripcion: [null, [Validators.required]]
  });

  constructor(protected estadoPedidoService: EstadoPedidoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ estadoPedido }) => {
      this.updateForm(estadoPedido);
    });
  }

  updateForm(estadoPedido: IEstadoPedido): void {
    this.editForm.patchValue({
      id: estadoPedido.id,
      codigo: estadoPedido.codigo,
      descripcion: estadoPedido.descripcion
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const estadoPedido = this.createFromForm();
    if (estadoPedido.id !== undefined) {
      this.subscribeToSaveResponse(this.estadoPedidoService.update(estadoPedido));
    } else {
      this.subscribeToSaveResponse(this.estadoPedidoService.create(estadoPedido));
    }
  }

  private createFromForm(): IEstadoPedido {
    return {
      ...new EstadoPedido(),
      id: this.editForm.get(['id'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEstadoPedido>>): void {
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
}
