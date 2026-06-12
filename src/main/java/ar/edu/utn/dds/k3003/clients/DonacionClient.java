package ar.edu.utn.dds.k3003.clients;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import org.springframework.web.client.RestClient;

public class DonacionClient {

    private RestClient restClient;

    public DonacionClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://donaciones-1-7xzd.onrender.com")
                .build();
    }
    public DonacionDTO getDonacion(String id) {
        return restClient.get()
                .uri("/donaciones/{id}", id)
                .retrieve()
                .body(DonacionDTO.class);
    }
}
