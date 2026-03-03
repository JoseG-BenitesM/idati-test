import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { sPTRGuard } from './sptr-guard';

describe('sPTRGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => sPTRGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
