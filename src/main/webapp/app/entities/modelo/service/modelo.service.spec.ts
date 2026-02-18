import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IModelo } from '../modelo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../modelo.test-samples';

import { ModeloService } from './modelo.service';

const requireRestSample: IModelo = {
  ...sampleWithRequiredData,
};

describe('Modelo Service', () => {
  let service: ModeloService;
  let httpMock: HttpTestingController;
  let expectedResult: IModelo | IModelo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ModeloService);
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

    it('should create a Modelo', () => {
      const modelo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(modelo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Modelo', () => {
      const modelo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(modelo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Modelo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Modelo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Modelo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addModeloToCollectionIfMissing', () => {
      it('should add a Modelo to an empty array', () => {
        const modelo: IModelo = sampleWithRequiredData;
        expectedResult = service.addModeloToCollectionIfMissing([], modelo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(modelo);
      });

      it('should not add a Modelo to an array that contains it', () => {
        const modelo: IModelo = sampleWithRequiredData;
        const modeloCollection: IModelo[] = [
          {
            ...modelo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addModeloToCollectionIfMissing(modeloCollection, modelo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Modelo to an array that doesn't contain it", () => {
        const modelo: IModelo = sampleWithRequiredData;
        const modeloCollection: IModelo[] = [sampleWithPartialData];
        expectedResult = service.addModeloToCollectionIfMissing(modeloCollection, modelo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(modelo);
      });

      it('should add only unique Modelo to an array', () => {
        const modeloArray: IModelo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const modeloCollection: IModelo[] = [sampleWithRequiredData];
        expectedResult = service.addModeloToCollectionIfMissing(modeloCollection, ...modeloArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const modelo: IModelo = sampleWithRequiredData;
        const modelo2: IModelo = sampleWithPartialData;
        expectedResult = service.addModeloToCollectionIfMissing([], modelo, modelo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(modelo);
        expect(expectedResult).toContain(modelo2);
      });

      it('should accept null and undefined values', () => {
        const modelo: IModelo = sampleWithRequiredData;
        expectedResult = service.addModeloToCollectionIfMissing([], null, modelo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(modelo);
      });

      it('should return initial array if no Modelo is added', () => {
        const modeloCollection: IModelo[] = [sampleWithRequiredData];
        expectedResult = service.addModeloToCollectionIfMissing(modeloCollection, undefined, null);
        expect(expectedResult).toEqual(modeloCollection);
      });
    });

    describe('compareModelo', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareModelo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11658 };
        const entity2 = null;

        const compareResult1 = service.compareModelo(entity1, entity2);
        const compareResult2 = service.compareModelo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11658 };
        const entity2 = { id: 14716 };

        const compareResult1 = service.compareModelo(entity1, entity2);
        const compareResult2 = service.compareModelo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11658 };
        const entity2 = { id: 11658 };

        const compareResult1 = service.compareModelo(entity1, entity2);
        const compareResult2 = service.compareModelo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
