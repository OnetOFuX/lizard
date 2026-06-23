import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from '../services/api.service';

export interface UserResponse {
  id: number;
  username: string;
  enabled: boolean;
  createdAt: string;
  roles: string[];
}

@Injectable({ providedIn: 'root' })
export class UsuariosService {
  constructor(
    private http: HttpClient,
    private api: ApiService,
  ) {}

  listar(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>(this.api.buildUrl('/auth/users'));
  }

  obtenerPerfil(id: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(this.api.buildUrl(`/auth/users/${id}`));
  }

  cambiarContrasena(id: number, newPassword: string): Observable<UserResponse> {
    return this.http.post<UserResponse>(
      this.api.buildUrl(`/auth/users/${id}/change-password`),
      { newPassword }
    );
  }

  actualizarEstado(id: number, enabled: boolean): Observable<UserResponse> {
    return this.http.put<UserResponse>(
      this.api.buildUrl(`/auth/users/${id}/status`),
      { enabled }
    );
  }

  actualizarRoles(id: number, roles: string[]): Observable<UserResponse> {
    return this.http.put<UserResponse>(
      this.api.buildUrl(`/auth/users/${id}/roles`),
      { roles }
    );
  }
}
