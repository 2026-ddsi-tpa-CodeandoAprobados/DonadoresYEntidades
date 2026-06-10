package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.model.EntidadBenefica;

import java.util.List;
import java.util.Optional;

public interface EntidadesBeneficasRepository {
    Optional<EntidadBenefica> findById(String id);

    EntidadBenefica save(EntidadBenefica entidadBenefica);

    EntidadBenefica deleteById(String id);

    List<EntidadBenefica> todasLasEntidades();
}
