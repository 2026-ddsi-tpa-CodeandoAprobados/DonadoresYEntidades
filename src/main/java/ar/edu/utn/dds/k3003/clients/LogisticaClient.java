package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.dtos.AsignacionNecesidadDTO;
import ar.edu.utn.dds.k3003.dtos.StockDisponibleDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class LogisticaClient {
    private RestClient restClient;

    public LogisticaClient(@Value("${LOGISTICA_API_URL}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public StockDisponibleDTO stockDisponible(String productoID){
            return restClient.get()
                    .uri("/stock/{productoID}", productoID)
                    .retrieve()
                    .body(StockDisponibleDTO.class);
    }

    public void asignar(String productoID, String necesidadID,Integer cantidadObjetivo){
        AsignacionNecesidadDTO asignacionNecesidadDTO = new AsignacionNecesidadDTO(necesidadID, cantidadObjetivo, true);
        restClient.post()
                .uri("/stock/{productoID}/asignaciones", productoID)
                .contentType(MediaType.APPLICATION_JSON)
                .body(asignacionNecesidadDTO)
                .retrieve()
                .toBodilessEntity();
    }

}
