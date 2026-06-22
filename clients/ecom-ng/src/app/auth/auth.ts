import { Component, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../core/auth/auth.service';

@Component({
  selector: 'app-auth',
  imports: [FormsModule],
  templateUrl: './auth.html',
  styleUrl: './auth.scss',
})
export class AuthPage implements OnInit {
  username = '';
  password = '';
  loading = signal(false);
  error = signal('');
  modoRegistro = signal(false);

  constructor(
    private auth: AuthService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.modoRegistro.set(params['mode'] === 'register');
      this.error.set('');
    });
  }

  submit() {
    const user = this.username.trim();
    const pass = this.password;

    if (!user || !pass) {
      this.error.set('Ingrese usuario y contrasena');
      return;
    }

    this.error.set('');
    this.loading.set(true);

    const action = this.modoRegistro() 
      ? this.auth.register({ username: user, password: pass })
      : this.auth.login({ username: user, password: pass });

    action.subscribe({
      next: () => {
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') ?? '/productos';
        this.router.navigateByUrl(returnUrl);
      },
      error: (err) => {
        if (this.modoRegistro()) {
          this.error.set(err.error?.message || 'Error al registrar el usuario');
        } else {
          this.error.set('Usuario o contrasena invalidos');
        }
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }

  toggleModo() {
    this.modoRegistro.set(!this.modoRegistro());
    this.error.set('');
  }
}
