import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICatalogo } from '../catalogo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../catalogo.test-samples';

import { CatalogoService } from './catalogo.service';

const requireRestSample: ICatalogo = {
  ...sampleWithRequiredData,
};

describe('Catalogo Service', () => {
  let service: CatalogoService;
  let httpMock: HttpTestingController;
  let expectedResult: ICatalogo | ICatalogo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CatalogoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Catalogo', () => {
      const catalogo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(catalogo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Catalogo', () => {
      const catalogo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(catalogo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Catalogo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Catalogo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Catalogo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCatalogoToCollectionIfMissing', () => {
      it('should add a Catalogo to an empty array', () => {
        const catalogo: ICatalogo = sampleWithRequiredData;
        expectedResult = service.addCatalogoToCollectionIfMissing([], catalogo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogo);
      });

      it('should not add a Catalogo to an array that contains it', () => {
        const catalogo: ICatalogo = sampleWithRequiredData;
        const catalogoCollection: ICatalogo[] = [
          {
            ...catalogo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCatalogoToCollectionIfMissing(catalogoCollection, catalogo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Catalogo to an array that doesn't contain it", () => {
        const catalogo: ICatalogo = sampleWithRequiredData;
        const catalogoCollection: ICatalogo[] = [sampleWithPartialData];
        expectedResult = service.addCatalogoToCollectionIfMissing(catalogoCollection, catalogo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogo);
      });

      it('should add only unique Catalogo to an array', () => {
        const catalogoArray: ICatalogo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const catalogoCollection: ICatalogo[] = [sampleWithRequiredData];
        expectedResult = service.addCatalogoToCollectionIfMissing(catalogoCollection, ...catalogoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const catalogo: ICatalogo = sampleWithRequiredData;
        const catalogo2: ICatalogo = sampleWithPartialData;
        expectedResult = service.addCatalogoToCollectionIfMissing([], catalogo, catalogo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(catalogo);
        expect(expectedResult).toContain(catalogo2);
      });

      it('should accept null and undefined values', () => {
        const catalogo: ICatalogo = sampleWithRequiredData;
        expectedResult = service.addCatalogoToCollectionIfMissing([], null, catalogo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(catalogo);
      });

      it('should return initial array if no Catalogo is added', () => {
        const catalogoCollection: ICatalogo[] = [sampleWithRequiredData];
        expectedResult = service.addCatalogoToCollectionIfMissing(catalogoCollection, undefined, null);
        expect(expectedResult).toEqual(catalogoCollection);
      });
    });

    describe('compareCatalogo', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCatalogo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14600 };
        const entity2 = null;

        const compareResult1 = service.compareCatalogo(entity1, entity2);
        const compareResult2 = service.compareCatalogo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14600 };
        const entity2 = { id: 31515 };

        const compareResult1 = service.compareCatalogo(entity1, entity2);
        const compareResult2 = service.compareCatalogo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14600 };
        const entity2 = { id: 14600 };

        const compareResult1 = service.compareCatalogo(entity1, entity2);
        const compareResult2 = service.compareCatalogo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
