package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class DonadoresYEntidadesClient {
    private RestClient restClient;

    public DonadoresYEntidadesClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://donadoresyentidades-hxss.onrender.com")
                .build();
    }
    public List<InsigniaDTO> getInsigniasDeDonador(String donadorID) {
        return restClient.get()
                .uri("/insignias/donador/{donadorID}", donadorID)
                .retrieve()
                .body(new ParameterizedTypeReference<List<InsigniaDTO>>() {});
    }
    public MisionDTO getMisionEnCursoDeDonador(String donadorID) {
        return restClient.get()
                .uri("/insignias/donador/{donadorID}", donadorID)
                .retrieve()
                .body(MisionDTO.class);
    }
}
