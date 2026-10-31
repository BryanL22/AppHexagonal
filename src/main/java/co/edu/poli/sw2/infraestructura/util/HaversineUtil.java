package co.edu.poli.sw2.infraestructura.util;

import co.edu.poli.sw2.dominio.strategy.PuntoGeografico;

/**
 * Utilidad de infraestructura para el calculo de distancias geograficas
 * sobre la superficie terrestre mediante la formula de Haversine.
 */
public final class HaversineUtil {

    /** Radio medio de la Tierra en kilometros. */
    public static final double RADIO_TIERRA_KM = 6371.0;

    private HaversineUtil() {
    }

    /**
     * Calcula la distancia en kilometros entre dos puntos geograficos.
     *
     * @param origen punto geografico inicial.
     * @param destino punto geografico de llegada.
     * @return distancia en kilometros entre ambos puntos.
     * @throws IllegalArgumentException si alguno de los puntos es nulo.
     */
    public static double calcularDistanciaKm(PuntoGeografico origen, PuntoGeografico destino) {
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser nulos.");
        }
        return calcularDistanciaKm(
                origen.getLatitud(), origen.getLongitud(),
                destino.getLatitud(), destino.getLongitud()
        );
    }

    /**
     * Calcula la distancia en kilometros dadas las coordenadas de latitud y longitud.
     *
     * @param lat1 latitud del primer punto en grados decimales.
     * @param lon1 longitud del primer punto en grados decimales.
     * @param lat2 latitud del segundo punto en grados decimales.
     * @param lon2 longitud del segundo punto en grados decimales.
     * @return distancia aproximada en kilometros.
     */
    public static double calcularDistanciaKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);

        // Limita 'a' a [0, 1] para evitar errores numericos por punto flotante
        a = Math.min(1.0, Math.max(0.0, a));

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        return RADIO_TIERRA_KM * c;
    }
}

