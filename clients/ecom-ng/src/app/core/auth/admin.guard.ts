import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const adminGuard: CanActivateFn = (_route, _state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (auth.isAuthenticated() && auth.hasAnyRole(['ROLE_ADMIN', 'ADMIN'])) {
    return true;
  }

  if (!auth.isAuthenticated()) {
    return router.createUrlTree(['/auth'], {
      queryParams: { returnUrl: '/zona-gestion' },
    });
  }

  return router.createUrlTree(['/productos']);
};
