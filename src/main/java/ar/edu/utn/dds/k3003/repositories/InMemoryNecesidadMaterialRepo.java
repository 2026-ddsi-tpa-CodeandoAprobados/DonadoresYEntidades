package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


public class InMemoryNecesidadMaterialRepo implements NecesidadMaterialRepository{

    private List<NecesidadMaterial> necesidadMaterials;
    private AtomicLong idSecuencial = new AtomicLong(1);

    public InMemoryNecesidadMaterialRepo() {
        this.necesidadMaterials = new ArrayList<>();
    }

    @Override
    public NecesidadMaterial save(NecesidadMaterial necesidadMaterial) {
        NecesidadMaterial necesidadMaterialConID = necesidadMaterial;
        necesidadMaterialConID.setId(String.valueOf(idSecuencial.getAndIncrement()));

        this.necesidadMaterials.add(necesidadMaterialConID);
        return this.findById(necesidadMaterialConID.getId()).get();
    }

    @Override
    public NecesidadMaterial deleteById(String id) {
        val necesidad = this.findById(id);
        this.necesidadMaterials.remove(necesidad.get());
        return necesidad.get();
        }


    @Override
    public Optional<NecesidadMaterial> findById(String id){
        return this.necesidadMaterials.stream().filter(d -> d.getId().equals(id)).findFirst();
    }

    @Override
    public List<NecesidadMaterial> todasLasNecesidades(String productoSolicitadoID){
        return necesidadMaterials
                .stream()
                .filter(n -> n.getProductoSolicitadoID()
                        .equals(productoSolicitadoID))
                .toList();
    }

    public List<NecesidadMaterial> todasNecesidades(){
        return this.necesidadMaterials;
    }
}
