import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RestContra } from './rest-contra';

describe('RestContra', () => {
  let component: RestContra;
  let fixture: ComponentFixture<RestContra>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RestContra]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RestContra);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
