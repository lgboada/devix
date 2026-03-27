import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CompanyThemeResponse {
  themeName: string;
  logoUrl: string | null;
  backgroundUrl: string | null;
}

@Injectable({ providedIn: 'root' })
export class CompanyThemeAdminService {
  private readonly http = inject(HttpClient);

  getCurrent(): Observable<CompanyThemeResponse> {
    return this.http.get<CompanyThemeResponse>('/api/company-theme/current');
  }

  getThemes(): Observable<{ default: string; themes: string[] }> {
    return this.http.get<{ default: string; themes: string[] }>('/api/company-theme/themes');
  }

  updateTheme(themeName: string): Observable<CompanyThemeResponse> {
    return this.http.put<CompanyThemeResponse>('/api/company-theme/current', { themeName });
  }

  uploadAssets(logo?: File, background?: File): Observable<CompanyThemeResponse> {
    const formData = new FormData();
    if (logo) {
      formData.append('logo', logo);
    }
    if (background) {
      formData.append('background', background);
    }
    return this.http.post<CompanyThemeResponse>('/api/company-theme/current/assets', formData);
  }
}
