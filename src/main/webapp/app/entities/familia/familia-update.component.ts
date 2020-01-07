import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFamilia, Familia } from 'app/shared/model/familia.model';
import { FamiliaService } from './familia.service';

@Component({
  selector: 'jhi-familia-update',
  templateUrl: './familia-update.component.html'
})
export class FamiliaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    codigo: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(8)]],
    descripcion: []
  });

  constructor(protected familiaService: FamiliaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ familia }) => {
      this.updateForm(familia);
    });
  }

  updateForm(familia: IFamilia): void {
    this.editForm.patchValue({
      id: familia.id,
      codigo: familia.codigo,
      descripcion: familia.descripcion
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const familia = this.createFromForm();
    if (familia.id !== undefined) {
      this.subscribeToSaveResponse(this.familiaService.update(familia));
    } else {
      this.subscribeToSaveResponse(this.familiaService.create(familia));
    }
  }

  private createFromForm(): IFamilia {
    return {
      ...new Familia(),
      id: this.editForm.get(['id'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamilia>>): void {
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
