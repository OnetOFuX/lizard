import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Orden, OrdenesService } from './ordenes.service';
import { PagosService, PreferenciaPago } from '../pagos/pagos.service';
import { MercadoPagoCheckoutService } from '../pagos/mercadopago-checkout.service';
import { AuthService } from '../core/auth/auth.service';

@Component({
  selector: 'app-ordenes',
  imports: [],
  templateUrl: './ordenes.html',
  styleUrl: './ordenes.scss',
})
export class OrdenesPage implements OnInit {
  readonly walletContainerId = 'mp-wallet-container';

  ordenes = signal<Orden[]>([]);
  loading = signal(false);
  error = signal('');
  info = signal('');
  pagandoOrdenId = signal<number | null>(null);
  modalPagoAbierto = signal(false);
  ordenPagoActiva = signal<Orden | null>(null);
  preferenciaActiva = signal<PreferenciaPago | null>(null);
  cargandoWallet = signal(false);
  esSimulacion = signal(false);

  constructor(
    private ordenesService: OrdenesService,
    private pagosService: PagosService,
    private mercadoPagoCheckout: MercadoPagoCheckoutService,
    private route: ActivatedRoute,
    private router: Router,
    private auth: AuthService,
  ) {
    this.cargar();
  }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      const paymentStatus = params.get('payment_status');
      const ordenIdParam = params.get('ordenId');
      if (!paymentStatus || !ordenIdParam) {
        return;
      }

      const ordenId = Number(ordenIdParam);
      if (Number.isNaN(ordenId)) {
        return;
      }

      if (paymentStatus === 'mock-success') {
        this.info.set('Confirmando pago simulado...');
        this.pagosService.simularAprobacion(ordenId).subscribe({
          next: () => {
            this.info.set('Pago simulado confirmado.');
            this.cargar();
            this.limpiarQueryParams();
          },
          error: () => {
            this.error.set('No se pudo confirmar el pago simulado');
            this.limpiarQueryParams();
          },
        });
        return;
      }

      if (paymentStatus === 'success') {
        this.info.set('Pago recibido. La orden se confirmara en unos segundos.');
      } else if (paymentStatus === 'failure') {
        this.error.set('El pago fue rechazado. Puedes intentarlo nuevamente.');
      } else if (paymentStatus === 'pending') {
        this.info.set('El pago quedo pendiente de confirmacion.');
      }

      this.cargar();
      this.limpiarQueryParams();
    });
  }

  cargar() {
    const userId = this.auth.userId();
    if (userId == null) {
      this.error.set('Debes iniciar sesion para ver tus ordenes');
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.ordenesService.listarPorUsuario(userId).subscribe({
      next: o => this.ordenes.set(o),
      error: () => {
        this.error.set('No se pudieron cargar las ordenes');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }

  pagar(orden: Orden) {
    this.error.set('');
    this.info.set('');
    this.pagandoOrdenId.set(orden.id);

    this.pagosService.obtenerPreferencia(orden.id, orden.total).subscribe({
      next: preferencia => {
        this.pagandoOrdenId.set(null);

        if (preferencia.simulacion) {
          this.esSimulacion.set(true);
          this.ordenPagoActiva.set(orden);
          this.preferenciaActiva.set(preferencia);
          this.modalPagoAbierto.set(true);
          this.cargandoWallet.set(false);
          return;
        }

        if (!preferencia.publicKey) {
          window.location.href = preferencia.initPoint;
          return;
        }

        this.esSimulacion.set(false);
        this.ordenPagoActiva.set(orden);
        this.preferenciaActiva.set(preferencia);
        this.modalPagoAbierto.set(true);
        this.cargandoWallet.set(true);

        setTimeout(() => {
          const publicKey = preferencia.publicKey as string;
          this.mercadoPagoCheckout
            .renderWallet(this.walletContainerId, publicKey, preferencia.preferenciaId)
            .then(() => this.cargandoWallet.set(false))
            .catch(() => {
              this.cargandoWallet.set(false);
              this.cerrarModalPago();
              window.location.href = preferencia.initPoint;
            });
        }, 100);
      },
      error: () => {
        this.pagandoOrdenId.set(null);
        this.error.set('No se pudo iniciar el pago. Intenta de nuevo en unos segundos.');
      },
    });
  }

  pagarSimulado() {
    const orden = this.ordenPagoActiva();
    if (!orden) return;

    this.cargandoWallet.set(true);
    this.pagosService.simularAprobacion(orden.id).subscribe({
      next: () => {
        this.cargandoWallet.set(false);
        this.cerrarModalPago();
        this.info.set('Pago simulado confirmado exitosamente.');
        this.cargar();
      },
      error: () => {
        this.cargandoWallet.set(false);
        this.error.set('No se pudo procesar el pago simulado');
      },
    });
  }

  async cerrarModalPago() {
    await this.mercadoPagoCheckout.destroy();
    this.modalPagoAbierto.set(false);
    this.ordenPagoActiva.set(null);
    this.preferenciaActiva.set(null);
    this.cargandoWallet.set(false);
    this.esSimulacion.set(false);
  }

  pendientePago(estado: string): boolean {
    return estado?.toUpperCase() === 'PENDIENTE_PAGO';
  }

  claseBadge(estado: string): string {
    const e = estado?.toLowerCase() ?? '';
    if (e.includes('confirm') || e.includes('complet')) return 'badge-success';
    if (e.includes('pendiente') || e.includes('proceso')) return 'badge-warning';
    if (e.includes('cancel')) return 'badge-danger';
    return 'badge-info';
  }

  formatearPrecio(precio: number): string {
    return `S/ ${precio.toFixed(2)}`;
  }

  formatearEstado(estado: string): string {
    return estado?.replace(/_/g, ' ') ?? '';
  }

  private limpiarQueryParams() {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {},
      replaceUrl: true,
    });
  }
}
