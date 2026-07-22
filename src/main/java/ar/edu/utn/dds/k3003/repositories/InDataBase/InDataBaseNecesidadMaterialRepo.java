package ar.edu.utn.dds.k3003.repositories.InDataBase;

import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import ar.edu.utn.dds.k3003.repositories.NecesidadMaterialRepository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import lombok.val;
import java.util.List;
import java.util.Optional;

public class InDataBaseNecesidadMaterialRepo implements NecesidadMaterialRepository {

    private EntityManager entityManager;

    public InDataBaseNecesidadMaterialRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<NecesidadMaterial> findById(String id){
        if (id == null) {
            return Optional.empty();
        }
        val necesidadMaterial = entityManager.find(NecesidadMaterial.class, id);
        return Optional.ofNullable(necesidadMaterial);
    }

    @Override
    public NecesidadMaterial save(NecesidadMaterial necesidadMaterial) {
        if (necesidadMaterial.getId() == null) {
            entityManager.persist(necesidadMaterial);
            return necesidadMaterial;
        } else {
            return entityManager.merge(necesidadMaterial);
        }
    }

    @Override
    public NecesidadMaterial deleteById(String id) {
        var necesidadMaterialOptional = this.findById(id);
        if (necesidadMaterialOptional.isPresent()) {
            NecesidadMaterial necesidadMaterial = necesidadMaterialOptional.get();
            entityManager.remove(necesidadMaterial);
            return necesidadMaterial;
        }
        return null;
    }

    @Override
    public List<NecesidadMaterial> todasLasNecesidades(String productoSolicitadoID) {

        String jpql = "SELECT n FROM NecesidadMaterial n WHERE n.productoSolicitadoID = :productoId";

        return entityManager.createQuery(jpql, NecesidadMaterial.class)
                .setParameter("productoId", productoSolicitadoID)
                .getResultList();
    }

    @Override
    public List<NecesidadMaterial> todasNecesidades(){
        return entityManager.createQuery("SELECT n FROM NecesidadMaterial n", NecesidadMaterial.class).getResultList();
    }
}
