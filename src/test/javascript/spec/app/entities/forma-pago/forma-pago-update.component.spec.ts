import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PedidosTestModule } from '../../../test.module';
import { FormaPagoUpdateComponent } from 'app/entities/forma-pago/forma-pago-update.component';
import { FormaPagoService } from 'app/entities/forma-pago/forma-pago.service';
import { FormaPago } from 'app/shared/model/forma-pago.model';

describe('Component Tests', () => {
  describe('FormaPago Management Update Component', () => {
    let comp: FormaPagoUpdateComponent;
    let fixture: ComponentFixture<FormaPagoUpdateComponent>;
    let service: FormaPagoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PedidosTestModule],
        declarations: [FormaPagoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FormaPagoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormaPagoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FormaPagoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FormaPago(123);
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
        const entity = new FormaPago();
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
