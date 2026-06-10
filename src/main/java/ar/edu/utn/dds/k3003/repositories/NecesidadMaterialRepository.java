package ar.edu.utn.dds.k3003.repositories;


import ar.edu.utn.dds.k3003.model.NecesidadMaterial;

import java.util.List;
import java.util.Optional;

public interface NecesidadMaterialRepository {

        Optional<NecesidadMaterial> findById(String id);

        NecesidadMaterial save(NecesidadMaterial necesidadMaterial);

        NecesidadMaterial deleteById(String id);

        List<NecesidadMaterial> todasLasNecesidades(String productoSolicitadoID);

        List<NecesidadMaterial> todasNecesidades();

}
