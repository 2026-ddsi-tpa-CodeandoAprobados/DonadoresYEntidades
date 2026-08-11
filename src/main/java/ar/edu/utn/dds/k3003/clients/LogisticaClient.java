package ar.edu.utn.dds.k3003.clients;

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

    public Boolean stokeDisponible(String productoID, Integer cantidadObjetivo){
        return true;
//        return restClient.get()
//                .uri("/donaciones/{productoID}", productoID)
//                .retrieve()
//                .body(DonacionDTO.class);
    }

    public void asignar(String productoID, Integer cantidadObjetivo){
        restClient.post()
                .uri("/asignaciones/{productoID}", productoID)
                .contentType(MediaType.APPLICATION_JSON)
                .body(cantidadObjetivo)
                .retrieve()
                .toBodilessEntity();
    }

}
