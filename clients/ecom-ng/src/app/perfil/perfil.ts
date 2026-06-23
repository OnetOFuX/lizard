import { Component, signal, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../core/auth/auth.service';
import { UsuariosService, UserResponse } from '../core/usuarios/usuarios.service';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './perfil.html',
  styleUrl: './perfil.scss',
})
export class PerfilPage implements OnInit {
  usuario = signal<UserResponse | null>(null);
  loading = signal(false);
  error = signal('');
  exito = signal('');

  // Password fields
  nuevaContrasena = '';
  confirmarContrasena = '';

  constructor(
    public auth: AuthService,
    private usuariosService: UsuariosService,
  ) {}

  ngOnInit() {
    this.cargarPerfil();
  }

  cargarPerfil() {
    const id = this.auth.userId();
    if (!id) {
      this.error.set('No se pudo identificar al usuario');
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.usuariosService.obtenerPerfil(id).subscribe({
      next: (user) => {
        this.usuario.set(user);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Error al cargar datos del perfil');
        this.loading.set(false);
      }
    });
  }

  actualizarContrasena() {
    this.exito.set('');
    this.error.set('');

    if (!this.nuevaContrasena || !this.confirmarContrasena) {
      this.error.set('Por favor completa todos los campos');
      return;
    }

    if (this.nuevaContrasena !== this.confirmarContrasena) {
      this.error.set('Las nuevas contraseñas no coinciden');
      return;
    }

    if (this.nuevaContrasena.length < 4) {
      this.error.set('La nueva contraseña debe tener al menos 4 caracteres');
      return;
    }

    const id = this.auth.userId();
    if (!id) {
      this.error.set('No se pudo identificar al usuario');
      return;
    }

    this.loading.set(true);
    this.usuariosService.cambiarContrasena(id, this.nuevaContrasena).subscribe({
      next: () => {
        this.exito.set('Contraseña actualizada con éxito');
        this.limpiarCampos();
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Fallo al actualizar la contraseña');
        this.loading.set(false);
      }
    });
  }

  limpiarCampos() {
    this.nuevaContrasena = '';
    this.confirmarContrasena = '';
  }

  formatearFecha(fechaStr?: string): string {
    if (!fechaStr) return '';
    try {
      const date = new Date(fechaStr);
      return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch {
      return fechaStr;
    }
  }
}
