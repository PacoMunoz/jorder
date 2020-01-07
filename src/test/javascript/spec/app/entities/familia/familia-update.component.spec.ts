import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PedidosTestModule } from '../../../test.module';
import { FamiliaUpdateComponent } from 'app/entities/familia/familia-update.component';
import { FamiliaService } from 'app/entities/familia/familia.service';
import { Familia } from 'app/shared/model/familia.model';

describe('Component Tests', () => {
  describe('Familia Management Update Component', () => {
    let comp: FamiliaUpdateComponent;
    let fixture: ComponentFixture<FamiliaUpdateComponent>;
    let service: FamiliaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PedidosTestModule],
        declarations: [FamiliaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FamiliaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FamiliaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FamiliaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Familia(123);
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
        const entity = new Familia();
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
