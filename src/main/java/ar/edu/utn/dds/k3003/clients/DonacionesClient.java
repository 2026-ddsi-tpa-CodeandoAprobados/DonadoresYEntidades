package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.ProductoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class DonacionesClient {

    private RestClient restClient;

    public DonacionesClient(@Value("${DONACION_API_URL}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public DonacionDTO getDonacion(String donacionID) {
        return restClient.get()
                .uri("/donaciones/{donacionID}", donacionID)
                .retrieve()
                .body(DonacionDTO.class);
    }
    public ProductoDTO getProducto(String productoID) {
        return restClient.get()
                .uri("/productos/{productoID}", productoID)
                .retrieve()
                .body(ProductoDTO.class);
    }
    public void postQueja(String donacionID) {
        restClient.post()
                .uri("/donaciones/{donacionID}/queja", donacionID)
                .retrieve()
                .toBodilessEntity();
    }
}
