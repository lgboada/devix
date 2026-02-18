import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMarca } from '../marca.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../marca.test-samples';

import { MarcaService } from './marca.service';

const requireRestSample: IMarca = {
  ...sampleWithRequiredData,
};

describe('Marca Service', () => {
  let service: MarcaService;
  let httpMock: HttpTestingController;
  let expectedResult: IMarca | IMarca[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MarcaService);
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

    it('should create a Marca', () => {
      const marca = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(marca).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Marca', () => {
      const marca = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(marca).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Marca', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Marca', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Marca', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMarcaToCollectionIfMissing', () => {
      it('should add a Marca to an empty array', () => {
        const marca: IMarca = sampleWithRequiredData;
        expectedResult = service.addMarcaToCollectionIfMissing([], marca);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(marca);
      });

      it('should not add a Marca to an array that contains it', () => {
        const marca: IMarca = sampleWithRequiredData;
        const marcaCollection: IMarca[] = [
          {
            ...marca,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMarcaToCollectionIfMissing(marcaCollection, marca);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Marca to an array that doesn't contain it", () => {
        const marca: IMarca = sampleWithRequiredData;
        const marcaCollection: IMarca[] = [sampleWithPartialData];
        expectedResult = service.addMarcaToCollectionIfMissing(marcaCollection, marca);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(marca);
      });

      it('should add only unique Marca to an array', () => {
        const marcaArray: IMarca[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const marcaCollection: IMarca[] = [sampleWithRequiredData];
        expectedResult = service.addMarcaToCollectionIfMissing(marcaCollection, ...marcaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const marca: IMarca = sampleWithRequiredData;
        const marca2: IMarca = sampleWithPartialData;
        expectedResult = service.addMarcaToCollectionIfMissing([], marca, marca2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(marca);
        expect(expectedResult).toContain(marca2);
      });

      it('should accept null and undefined values', () => {
        const marca: IMarca = sampleWithRequiredData;
        expectedResult = service.addMarcaToCollectionIfMissing([], null, marca, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(marca);
      });

      it('should return initial array if no Marca is added', () => {
        const marcaCollection: IMarca[] = [sampleWithRequiredData];
        expectedResult = service.addMarcaToCollectionIfMissing(marcaCollection, undefined, null);
        expect(expectedResult).toEqual(marcaCollection);
      });
    });

    describe('compareMarca', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMarca(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9264 };
        const entity2 = null;

        const compareResult1 = service.compareMarca(entity1, entity2);
        const compareResult2 = service.compareMarca(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9264 };
        const entity2 = { id: 13647 };

        const compareResult1 = service.compareMarca(entity1, entity2);
        const compareResult2 = service.compareMarca(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 9264 };
        const entity2 = { id: 9264 };

        const compareResult1 = service.compareMarca(entity1, entity2);
        const compareResult2 = service.compareMarca(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
