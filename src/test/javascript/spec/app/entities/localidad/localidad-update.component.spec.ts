import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PedidosTestModule } from '../../../test.module';
import { LocalidadUpdateComponent } from 'app/entities/localidad/localidad-update.component';
import { LocalidadService } from 'app/entities/localidad/localidad.service';
import { Localidad } from 'app/shared/model/localidad.model';

describe('Component Tests', () => {
  describe('Localidad Management Update Component', () => {
    let comp: LocalidadUpdateComponent;
    let fixture: ComponentFixture<LocalidadUpdateComponent>;
    let service: LocalidadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PedidosTestModule],
        declarations: [LocalidadUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LocalidadUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocalidadUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LocalidadService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Localidad(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Localidad();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
