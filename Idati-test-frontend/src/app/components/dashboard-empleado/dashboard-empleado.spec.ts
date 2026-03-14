import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardEmpleado } from './dashboard-empleado';

describe('DashboardEmpleado', () => {
  let component: DashboardEmpleado;
  let fixture: ComponentFixture<DashboardEmpleado>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardEmpleado]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardEmpleado);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
