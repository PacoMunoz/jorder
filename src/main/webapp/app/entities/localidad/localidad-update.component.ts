import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILocalidad, Localidad } from 'app/shared/model/localidad.model';
import { LocalidadService } from './localidad.service';

@Component({
  selector: 'jhi-localidad-update',
  templateUrl: './localidad-update.component.html'
})
export class LocalidadUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    codigo: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(8)]],
    nombre: [null, [Validators.required]],
    codigoPostal: [null, [Validators.required]]
  });

  constructor(protected localidadService: LocalidadService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ localidad }) => {
      this.updateForm(localidad);
    });
  }

  updateForm(localidad: ILocalidad): void {
    this.editForm.patchValue({
      id: localidad.id,
      codigo: localidad.codigo,
      nombre: localidad.nombre,
      codigoPostal: localidad.codigoPostal
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const localidad = this.createFromForm();
    if (localidad.id !== undefined) {
      this.subscribeToSaveResponse(this.localidadService.update(localidad));
    } else {
      this.subscribeToSaveResponse(this.localidadService.create(localidad));
    }
  }

  private createFromForm(): ILocalidad {
    return {
      ...new Localidad(),
      id: this.editForm.get(['id'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      codigoPostal: this.editForm.get(['codigoPostal'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocalidad>>): void {
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
