package servicios;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;



@Service
public class ContactoReal implements InterfazContactoSim {

    private static final Logger log = LoggerFactory.getLogger(ContactoReal.class);

    private final SimulacionApiClient apiClient;

    public ContactoReal(
            @Value("${simulacion.api.url:http://localhost:8080}") String apiUrl) {
        this.apiClient = new SimulacionApiClient(apiUrl);
        log.info("ContactoReal inicializado. URL servidor: {}", apiUrl);
    }

    public ContactoReal(SimulacionApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public int solicitarSimulation(DatosSolicitud sol) {
        return apiClient.solicitarSimulacion(sol);
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        return apiClient.descargarResultados(ticket);
    }

    @Override
    public List<Entidad> getEntities() {
        return apiClient.getEntidades();
    }

    @Override
    public boolean isValidEntityId() {
       
        return apiClient.isServerAvailable();
    }

   
    public boolean isValidEntityId(int id) {
        return apiClient.isValidEntityId(id);
    }
}
