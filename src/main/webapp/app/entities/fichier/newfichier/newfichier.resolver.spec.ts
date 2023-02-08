import { TestBed } from '@angular/core/testing';

import { NewfichierResolver } from './newfichier.resolver';

describe('NewfichierResolver', () => {
  let resolver: NewfichierResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(NewfichierResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
