import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFormaPago, FormaPago } from 'app/shared/model/forma-pago.model';
import { FormaPagoService } from './forma-pago.service';

@Component({
  selector: 'jhi-forma-pago-update',
  templateUrl: './forma-pago-update.component.html'
})
export class FormaPagoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    codigo: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(8)]],
    descripcion: [null, [Validators.required]]
  });

  constructor(protected formaPagoService: FormaPagoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formaPago }) => {
      this.updateForm(formaPago);
    });
  }

  updateForm(formaPago: IFormaPago): void {
    this.editForm.patchValue({
      id: formaPago.id,
      codigo: formaPago.codigo,
      descripcion: formaPago.descripcion
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formaPago = this.createFromForm();
    if (formaPago.id !== undefined) {
      this.subscribeToSaveResponse(this.formaPagoService.update(formaPago));
    } else {
      this.subscribeToSaveResponse(this.formaPagoService.create(formaPago));
    }
  }

  private createFromForm(): IFormaPago {
    return {
      ...new FormaPago(),
      id: this.editForm.get(['id'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormaPago>>): void {
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
