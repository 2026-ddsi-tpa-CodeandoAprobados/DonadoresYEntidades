package ar.edu.utn.dds.k3003.repositories;


import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.model.Queja;

import java.util.List;
import java.util.Optional;

public interface QuejasRepository {

    Queja save(Queja queja);

    Optional<Queja> findById(String id);

    List<Queja> quejasDeUnDonador(String donadorId);

//    List<QuejaDTO> toQuejasDTO(List<Queja> quejas);
}
