import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { adminGuard } from './core/auth/admin.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./home/home').then(m => m.HomePage),
  },
  {
    path: 'auth',
    loadComponent: () => import('./auth/auth').then(m => m.AuthPage),
  },
  {
    path: 'productos',
    loadComponent: () => import('./productos/productos').then(m => m.ProductosPage),
  },
  {
    path: 'categorias',
    loadComponent: () => import('./categorias/categorias').then(m => m.CategoriasPage),
  },
  {
    path: 'carrito',
    canActivate: [authGuard],
    loadComponent: () => import('./carrito/carrito').then(m => m.CarritoPage),
  },
  {
    path: 'ordenes',
    canActivate: [authGuard],
    loadComponent: () => import('./ordenes/ordenes').then(m => m.OrdenesPage),
  },
  {
    path: 'perfil',
    canActivate: [authGuard],
    loadComponent: () => import('./perfil/perfil').then(m => m.PerfilPage),
  },
  {
    path: 'zona-gestion',
    canActivate: [adminGuard],
    loadComponent: () => import('./admin/admin').then(m => m.AdminPage),
  },
  { path: '**', redirectTo: '' },
];
