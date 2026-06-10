package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;

public class NecesidadMaterialDataMapper {

    public NecesidadMaterial toNecesidadMaterial(NecesidadMaterialDTO necesidadMaterialDTO){

        return new NecesidadMaterial(
                necesidadMaterialDTO.entidadID(),
                necesidadMaterialDTO.nivelDeUrgencia(),
                necesidadMaterialDTO.descripcion(),
                necesidadMaterialDTO.cantidadObjetivo(),
                necesidadMaterialDTO.productoSolicitadoID(),
                necesidadMaterialDTO.tipo());
    }

        public NecesidadMaterialDTO toNecesidadMaterialDTO(NecesidadMaterial necesidadMaterial){

            return new NecesidadMaterialDTO(
                    necesidadMaterial.getId(),
                    necesidadMaterial.getEntidadID(),
                    necesidadMaterial.getNivelDeUrgencia(),
                    necesidadMaterial.getDescripcion(),
                    necesidadMaterial.getCantidadObjetivo(),
                    necesidadMaterial.getProductoSolicitadoID(),
                    necesidadMaterial.getTipo());
        }
}

