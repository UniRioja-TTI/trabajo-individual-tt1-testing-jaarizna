package servicios;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;


public class OpenApiUtils {

    private static final Logger log = LoggerFactory.getLogger(OpenApiUtils.class);

    private static final String DEFAULT_SPEC_PATH = "/openapi.json";
    private static final String ALT_SPEC_PATH     = "/v3/api-docs";

    private final RestClient restClient;
    private final String baseUrl;

    public OpenApiUtils(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public OpenApiUtils() {
        this("http://localhost:8080");
    }

    public String fetchOpenApiSpec() {
        for (String path : new String[]{DEFAULT_SPEC_PATH, ALT_SPEC_PATH}) {
            try {
                String spec = restClient.get()
                        .uri(path)
                        .retrieve()
                        .body(String.class);
                if (spec != null && !spec.isBlank()) {
                    log.info("Especificación OpenAPI obtenida desde {}{}", baseUrl, path);
                    return spec;
                }
            } catch (RestClientException e) {
                log.debug("No encontrado en {}: {}", path, e.getMessage());
            }
        }
        log.warn("No se pudo obtener la especificación OpenAPI desde {}", baseUrl);
        return null;
    }

    public void logSwaggerUiUrl() {
        log.info("Swagger UI disponible en: {}/", baseUrl);
        log.info("Especificación OpenAPI en: {}{} o {}{}", baseUrl, DEFAULT_SPEC_PATH, baseUrl, ALT_SPEC_PATH);
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
