import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { ApiService } from '../services/api.service';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  username: string;
  userId: number;
  roles: string[];
}

export interface AuthSession {
  accessToken: string;
  username: string;
  userId: number;
  roles: string[];
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly storageKey = 'lizard-store.auth';

  token = signal<string | null>(null);
  username = signal<string | null>(null);
  userId = signal<number | null>(null);
  roles = signal<string[]>([]);

  constructor(
    private http: HttpClient,
    private api: ApiService,
  ) {
    this.cargarSesion();
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.api.buildUrl('/auth/login'), credentials)
      .pipe(
        tap(response => this.guardarSesion({
          accessToken: response.accessToken,
          username: response.username,
          userId: response.userId,
          roles: response.roles ?? [],
        })),
      );
  }

  register(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.api.buildUrl('/auth/register'), credentials)
      .pipe(
        tap(response => this.guardarSesion({
          accessToken: response.accessToken,
          username: response.username,
          userId: response.userId,
          roles: response.roles ?? [],
        })),
      );
  }

  logout() {
    localStorage.removeItem(this.storageKey);
    this.token.set(null);
    this.username.set(null);
    this.userId.set(null);
    this.roles.set([]);
  }

  isAuthenticated(): boolean {
    return !!this.token();
  }

  hasRole(role: string): boolean {
    return this.roles().includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  private guardarSesion(session: AuthSession) {
    localStorage.setItem(this.storageKey, JSON.stringify(session));
    this.token.set(session.accessToken);
    this.username.set(session.username);
    this.userId.set(session.userId);
    this.roles.set(session.roles);
  }

  private cargarSesion() {
    const rawSession = localStorage.getItem(this.storageKey);
    if (!rawSession) return;

    const session = JSON.parse(rawSession) as AuthSession;
    this.token.set(session.accessToken);
    this.username.set(session.username);
    this.userId.set(session.userId ?? null);
    this.roles.set(session.roles ?? []);
  }
}
