import { TestBed } from '@angular/core/testing';

import { TachedeprojetResolver } from './tachedeprojet.resolver';

describe('TachedeprojetResolver', () => {
  let resolver: TachedeprojetResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(TachedeprojetResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
