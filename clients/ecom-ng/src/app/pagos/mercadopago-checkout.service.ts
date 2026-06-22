import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { loadMercadoPago } from '@mercadopago/sdk-js';

@Injectable({ providedIn: 'root' })
export class MercadoPagoCheckoutService {
  private readonly platformId = inject(PLATFORM_ID);
  private walletController: { unmount: () => void } | null = null;

  async renderWallet(
    containerId: string,
    publicKey: string,
    preferenceId: string,
  ): Promise<void> {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    await this.destroy();

    await loadMercadoPago();
    const mp = new window.MercadoPago(publicKey, { locale: 'es-PE' });
    const bricksBuilder = mp.bricks();

    this.walletController = await bricksBuilder.create('wallet', containerId, {
      initialization: {
        preferenceId,
      },
      customization: {
        visual: {
          hideValueProp: false,
        },
        texts: {
          valueProp: 'smart_option',
        },
      },
    });
  }

  async destroy(): Promise<void> {
    if (this.walletController) {
      this.walletController.unmount();
      this.walletController = null;
    }
  }
}
