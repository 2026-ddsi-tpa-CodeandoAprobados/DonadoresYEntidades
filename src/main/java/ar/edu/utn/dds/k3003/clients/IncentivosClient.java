package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class IncentivosClient {
    private RestClient restClient;

    public IncentivosClient(@Value("${INCENTIVOS_API_URL}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<InsigniaDTO> getInsigniasDeDonador(String donadorID) {
        return restClient.get()
                .uri("/incentivos-donador/{donadorID}/insignias", donadorID)
                .retrieve()
                .body(new ParameterizedTypeReference<List<InsigniaDTO>>() {});
    }
    public MisionDTO getMisionEnCursoDeDonador(String donadorID) {
        return restClient.get()
                .uri("/incentivos-donador/{donadorID}/mision", donadorID)
                .retrieve()
                .body(MisionDTO.class);
    }
}
