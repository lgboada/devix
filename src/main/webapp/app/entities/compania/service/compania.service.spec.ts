import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICompania } from '../compania.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../compania.test-samples';

import { CompaniaService } from './compania.service';

const requireRestSample: ICompania = {
  ...sampleWithRequiredData,
};

describe('Compania Service', () => {
  let service: CompaniaService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompania | ICompania[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CompaniaService);
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

    it('should create a Compania', () => {
      const compania = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compania).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Compania', () => {
      const compania = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compania).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Compania', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Compania', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Compania', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompaniaToCollectionIfMissing', () => {
      it('should add a Compania to an empty array', () => {
        const compania: ICompania = sampleWithRequiredData;
        expectedResult = service.addCompaniaToCollectionIfMissing([], compania);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compania);
      });

      it('should not add a Compania to an array that contains it', () => {
        const compania: ICompania = sampleWithRequiredData;
        const companiaCollection: ICompania[] = [
          {
            ...compania,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompaniaToCollectionIfMissing(companiaCollection, compania);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Compania to an array that doesn't contain it", () => {
        const compania: ICompania = sampleWithRequiredData;
        const companiaCollection: ICompania[] = [sampleWithPartialData];
        expectedResult = service.addCompaniaToCollectionIfMissing(companiaCollection, compania);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compania);
      });

      it('should add only unique Compania to an array', () => {
        const companiaArray: ICompania[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const companiaCollection: ICompania[] = [sampleWithRequiredData];
        expectedResult = service.addCompaniaToCollectionIfMissing(companiaCollection, ...companiaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compania: ICompania = sampleWithRequiredData;
        const compania2: ICompania = sampleWithPartialData;
        expectedResult = service.addCompaniaToCollectionIfMissing([], compania, compania2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compania);
        expect(expectedResult).toContain(compania2);
      });

      it('should accept null and undefined values', () => {
        const compania: ICompania = sampleWithRequiredData;
        expectedResult = service.addCompaniaToCollectionIfMissing([], null, compania, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compania);
      });

      it('should return initial array if no Compania is added', () => {
        const companiaCollection: ICompania[] = [sampleWithRequiredData];
        expectedResult = service.addCompaniaToCollectionIfMissing(companiaCollection, undefined, null);
        expect(expectedResult).toEqual(companiaCollection);
      });
    });

    describe('compareCompania', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompania(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14846 };
        const entity2 = null;

        const compareResult1 = service.compareCompania(entity1, entity2);
        const compareResult2 = service.compareCompania(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14846 };
        const entity2 = { id: 12296 };

        const compareResult1 = service.compareCompania(entity1, entity2);
        const compareResult2 = service.compareCompania(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14846 };
        const entity2 = { id: 14846 };

        const compareResult1 = service.compareCompania(entity1, entity2);
        const compareResult2 = service.compareCompania(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
