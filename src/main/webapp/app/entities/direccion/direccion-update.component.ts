import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IDireccion, Direccion } from 'app/shared/model/direccion.model';
import { DireccionService } from './direccion.service';
import { ICliente } from 'app/shared/model/cliente.model';
import { ClienteService } from 'app/entities/cliente/cliente.service';
import { ILocalidad } from 'app/shared/model/localidad.model';
import { LocalidadService } from 'app/entities/localidad/localidad.service';

type SelectableEntity = ICliente | ILocalidad;

@Component({
  selector: 'jhi-direccion-update',
  templateUrl: './direccion-update.component.html'
})
export class DireccionUpdateComponent implements OnInit {
  isSaving = false;

  clientes: ICliente[] = [];

  localidads: ILocalidad[] = [];

  editForm = this.fb.group({
    id: [],
    nombreVia: [null, [Validators.required]],
    numero: [],
    piso: [],
    bloque: [],
    puerta: [],
    escalera: [],
    usuario: [],
    localidad: []
  });

  constructor(
    protected direccionService: DireccionService,
    protected clienteService: ClienteService,
    protected localidadService: LocalidadService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ direccion }) => {
      this.updateForm(direccion);

      this.clienteService
        .query()
        .pipe(
          map((res: HttpResponse<ICliente[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICliente[]) => (this.clientes = resBody));

      this.localidadService
        .query()
        .pipe(
          map((res: HttpResponse<ILocalidad[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ILocalidad[]) => (this.localidads = resBody));
    });
  }

  updateForm(direccion: IDireccion): void {
    this.editForm.patchValue({
      id: direccion.id,
      nombreVia: direccion.nombreVia,
      numero: direccion.numero,
      piso: direccion.piso,
      bloque: direccion.bloque,
      puerta: direccion.puerta,
      escalera: direccion.escalera,
      usuario: direccion.usuario,
      localidad: direccion.localidad
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const direccion = this.createFromForm();
    if (direccion.id !== undefined) {
      this.subscribeToSaveResponse(this.direccionService.update(direccion));
    } else {
      this.subscribeToSaveResponse(this.direccionService.create(direccion));
    }
  }

  private createFromForm(): IDireccion {
    return {
      ...new Direccion(),
      id: this.editForm.get(['id'])!.value,
      nombreVia: this.editForm.get(['nombreVia'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      piso: this.editForm.get(['piso'])!.value,
      bloque: this.editForm.get(['bloque'])!.value,
      puerta: this.editForm.get(['puerta'])!.value,
      escalera: this.editForm.get(['escalera'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
      localidad: this.editForm.get(['localidad'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDireccion>>): void {
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
