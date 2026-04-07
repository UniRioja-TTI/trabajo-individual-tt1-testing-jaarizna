package utils;



import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;


public class SimulacionApiClient {

    private static final Logger log = LoggerFactory.getLogger(SimulacionApiClient.class);

    private final String baseUrl;

    private final RestClient restClient;

 
    public SimulacionApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
        log.info("SimulacionApiClient inicializado apuntando a {}", baseUrl);
    }

    public SimulacionApiClient() {
        this("http://localhost:8080");
    }


    public List<Entidad> getEntidades() {
        try {
            Entidad[] entidades = restClient.get()
                    .uri("/entidades")
                    .retrieve()
                    .body(Entidad[].class);
            if (entidades == null) return List.of();
            log.debug("getEntidades() -> {} entidades recibidas", entidades.length);
            return List.of(entidades);
        } catch (RestClientException e) {
            log.error("Error al obtener entidades del servidor de simulación: {}", e.getMessage());
            return null;
        }
    }

  
    public boolean isValidEntityId(int id) {
        try {
            restClient.get()
                    .uri("/entidades/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (RestClientException e) {
            log.warn("ID de entidad {} no válido: {}", id, e.getMessage());
            return false;
        }
    }

   
    public int solicitarSimulacion(DatosSolicitud solicitud) {
        try {
            Map<?, ?> respuesta = restClient.post()
                    .uri("/simulaciones")
                    .body(solicitud.getNums())
                    .retrieve()
                    .body(Map.class);

            if (respuesta != null && respuesta.containsKey("token")) {
                int token = ((Number) respuesta.get("token")).intValue();
                log.info("Simulación solicitada correctamente. Token: {}", token);
                return token;
            }
            log.error("Respuesta inesperada del servidor al solicitar simulación");
            return -1;
        } catch (RestClientException e) {
            log.error("Error al solicitar simulación: {}", e.getMessage());
            return -1;
        }
    }

    
    public DatosSimulation descargarResultados(int token) {
        try {
            DatosSimulation datos = restClient.get()
                    .uri("/simulaciones/{token}", token)
                    .retrieve()
                    .body(DatosSimulation.class);
            log.info("Resultados descargados para token {}", token);
            return datos;
        } catch (RestClientException e) {
            log.error("Error al descargar resultados (token {}): {}", token, e.getMessage());
            return null;
        }
    }

    
    public boolean isServerAvailable() {
        try {
            restClient.get()
                    .uri("/actuator/health")
                    .retrieve()
                    .toBodilessEntity();
            log.debug("Servidor de simulación disponible en {}", baseUrl);
            return true;
        } catch (RestClientException e) {
            log.warn("Servidor de simulación NO disponible en {}: {}", baseUrl, e.getMessage());
            return false;
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
