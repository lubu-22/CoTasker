import { TestBed } from '@angular/core/testing';

import { ListeprojetuserResolver } from './listeprojetuser.resolver';

describe('ListeprojetuserResolver', () => {
  let resolver: ListeprojetuserResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(ListeprojetuserResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
