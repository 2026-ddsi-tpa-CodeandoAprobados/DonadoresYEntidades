package ar.edu.utn.dds.k3003.clients;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.ProductoDTO;
import org.springframework.web.client.RestClient;

public class DonacionClient {

    private RestClient restClient;

    public DonacionClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://donaciones-1-7xzd.onrender.com")
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
}
