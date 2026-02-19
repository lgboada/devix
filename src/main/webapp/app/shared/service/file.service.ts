import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

export interface FileUploadResponse {
  filename?: string;
  error?: string;
}

export type EntityResponseType = HttpResponse<FileUploadResponse>;

/**
 * Service for managing file uploads.
 */
@Injectable({ providedIn: 'root' })
export class FileService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/files');

  /**
   * Upload a file to the server.
   *
   * @param file the file to upload
   * @returns an Observable of the HttpResponse containing the stored filename
   */
  upload(file: File): Observable<EntityResponseType> {
    const formData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post<FileUploadResponse>(this.resourceUrl, formData, {
      observe: 'response',
      withCredentials: true, // Asegura que las cookies de sesión se envíen
    });
  }

  /**
   * Get the URL for accessing an uploaded file.
   *
   * @param filename the filename of the file
   * @returns the full URL to access the file
   */
  getFileUrl(filename: string): string {
    const baseUrl = this.applicationConfigService.getEndpointFor('');
    return `${baseUrl}api/files/${filename}`;
  }
}
