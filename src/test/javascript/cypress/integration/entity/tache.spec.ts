import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Tache e2e test', () => {
  const tachePageUrl = '/tache';
  const tachePageUrlPattern = new RegExp('/tache(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tacheSample = {};

  let tache: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/taches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/taches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/taches/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tache) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/taches/${tache.id}`,
      }).then(() => {
        tache = undefined;
      });
    }
  });

  it('Taches menu should load Taches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tache');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Tache').should('exist');
    cy.url().should('match', tachePageUrlPattern);
  });

  describe('Tache page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tachePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Tache page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tache/new$'));
        cy.getEntityCreateUpdateHeading('Tache');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tachePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/taches',
          body: tacheSample,
        }).then(({ body }) => {
          tache = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/taches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/taches?page=0&size=20>; rel="last",<http://localhost/api/taches?page=0&size=20>; rel="first"',
              },
              body: [tache],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(tachePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Tache page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tache');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tachePageUrlPattern);
      });

      it('edit button click should load edit Tache page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Tache');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tachePageUrlPattern);
      });

      it('last delete button click should delete instance of Tache', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tache').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', tachePageUrlPattern);

        tache = undefined;
      });
    });
  });

  describe('new Tache page', () => {
    beforeEach(() => {
      cy.visit(`${tachePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Tache');
    });

    it('should create an instance of Tache', () => {
      cy.get(`[data-cy="role"]`).select('CHEF_DE_PROJET');

      cy.get(`[data-cy="descriptiontache"]`).type('Outdoors unleash monitor').should('have.value', 'Outdoors unleash monitor');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        tache = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', tachePageUrlPattern);
    });
  });
});
