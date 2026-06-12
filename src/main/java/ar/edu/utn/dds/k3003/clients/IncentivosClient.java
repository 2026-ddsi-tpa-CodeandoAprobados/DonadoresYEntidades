package ar.edu.utn.dds.k3003.clients;


import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class IncentivosClient {
    private RestClient restClient;

    public IncentivosClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://entrega-2-lseijasferraudo.onrender.com")
                .build();
    }
    public List<InsigniaDTO> getInsigniasDeDonador(String donadorID) {
        return restClient.get()
                .uri("incentivos-donador/{donadorID}/insignias", donadorID)
                .retrieve()
                .body(new ParameterizedTypeReference<List<InsigniaDTO>>() {});
    }
    public MisionDTO getMisionEnCursoDeDonador(String donadorID) {
        return restClient.get()
                .uri("incentivos-donador/{donadorID}/mision", donadorID)
                .retrieve()
                .body(MisionDTO.class);
    }
}
