import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICalendarioCita, IGoogleCalendarStatus } from '../calendario.model';

@Injectable({ providedIn: 'root' })
export class CalendarioService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/calendario-citas');

  getByRange(from: string, to: string): Observable<ICalendarioCita[]> {
    const params = createRequestOption({ from, to });
    return this.http.get<ICalendarioCita[]>(this.resourceUrl, { params });
  }

  create(cita: Omit<ICalendarioCita, 'id'>): Observable<ICalendarioCita> {
    return this.http.post<ICalendarioCita>(this.resourceUrl, cita);
  }

  update(cita: ICalendarioCita): Observable<ICalendarioCita> {
    return this.http.put<ICalendarioCita>(`${this.resourceUrl}/${cita.id}`, cita);
  }

  cancel(id: number): Observable<ICalendarioCita> {
    return this.http.post<ICalendarioCita>(`${this.resourceUrl}/${id}/cancel`, {});
  }

  delete(id: number): Observable<void> {
    return this.http.post<void>(`${this.resourceUrl}/${id}/delete`, {});
  }

  syncGoogle(from: string, to: string): Observable<void> {
    const params = createRequestOption({ from, to });
    return this.http.post<void>(`${this.resourceUrl}/google/sync`, {}, { params });
  }

  getGoogleAuthUrl(): Observable<{ url: string }> {
    return this.http.get<{ url: string }>(`${this.resourceUrl}/google/auth-url`);
  }

  getGoogleStatus(): Observable<IGoogleCalendarStatus> {
    return this.http.get<IGoogleCalendarStatus>(`${this.resourceUrl}/google/status`);
  }
}
