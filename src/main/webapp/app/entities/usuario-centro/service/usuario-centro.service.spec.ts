import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUsuarioCentro } from '../usuario-centro.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../usuario-centro.test-samples';

import { UsuarioCentroService } from './usuario-centro.service';

const requireRestSample: IUsuarioCentro = {
  ...sampleWithRequiredData,
};

describe('UsuarioCentro Service', () => {
  let service: UsuarioCentroService;
  let httpMock: HttpTestingController;
  let expectedResult: IUsuarioCentro | IUsuarioCentro[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UsuarioCentroService);
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

    it('should create a UsuarioCentro', () => {
      const usuarioCentro = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(usuarioCentro).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UsuarioCentro', () => {
      const usuarioCentro = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(usuarioCentro).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UsuarioCentro', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UsuarioCentro', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UsuarioCentro', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUsuarioCentroToCollectionIfMissing', () => {
      it('should add a UsuarioCentro to an empty array', () => {
        const usuarioCentro: IUsuarioCentro = sampleWithRequiredData;
        expectedResult = service.addUsuarioCentroToCollectionIfMissing([], usuarioCentro);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(usuarioCentro);
      });

      it('should not add a UsuarioCentro to an array that contains it', () => {
        const usuarioCentro: IUsuarioCentro = sampleWithRequiredData;
        const usuarioCentroCollection: IUsuarioCentro[] = [
          {
            ...usuarioCentro,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUsuarioCentroToCollectionIfMissing(usuarioCentroCollection, usuarioCentro);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UsuarioCentro to an array that doesn't contain it", () => {
        const usuarioCentro: IUsuarioCentro = sampleWithRequiredData;
        const usuarioCentroCollection: IUsuarioCentro[] = [sampleWithPartialData];
        expectedResult = service.addUsuarioCentroToCollectionIfMissing(usuarioCentroCollection, usuarioCentro);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(usuarioCentro);
      });

      it('should add only unique UsuarioCentro to an array', () => {
        const usuarioCentroArray: IUsuarioCentro[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const usuarioCentroCollection: IUsuarioCentro[] = [sampleWithRequiredData];
        expectedResult = service.addUsuarioCentroToCollectionIfMissing(usuarioCentroCollection, ...usuarioCentroArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const usuarioCentro: IUsuarioCentro = sampleWithRequiredData;
        const usuarioCentro2: IUsuarioCentro = sampleWithPartialData;
        expectedResult = service.addUsuarioCentroToCollectionIfMissing([], usuarioCentro, usuarioCentro2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(usuarioCentro);
        expect(expectedResult).toContain(usuarioCentro2);
      });

      it('should accept null and undefined values', () => {
        const usuarioCentro: IUsuarioCentro = sampleWithRequiredData;
        expectedResult = service.addUsuarioCentroToCollectionIfMissing([], null, usuarioCentro, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(usuarioCentro);
      });

      it('should return initial array if no UsuarioCentro is added', () => {
        const usuarioCentroCollection: IUsuarioCentro[] = [sampleWithRequiredData];
        expectedResult = service.addUsuarioCentroToCollectionIfMissing(usuarioCentroCollection, undefined, null);
        expect(expectedResult).toEqual(usuarioCentroCollection);
      });
    });

    describe('compareUsuarioCentro', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUsuarioCentro(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30943 };
        const entity2 = null;

        const compareResult1 = service.compareUsuarioCentro(entity1, entity2);
        const compareResult2 = service.compareUsuarioCentro(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30943 };
        const entity2 = { id: 16943 };

        const compareResult1 = service.compareUsuarioCentro(entity1, entity2);
        const compareResult2 = service.compareUsuarioCentro(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 30943 };
        const entity2 = { id: 30943 };

        const compareResult1 = service.compareUsuarioCentro(entity1, entity2);
        const compareResult2 = service.compareUsuarioCentro(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
