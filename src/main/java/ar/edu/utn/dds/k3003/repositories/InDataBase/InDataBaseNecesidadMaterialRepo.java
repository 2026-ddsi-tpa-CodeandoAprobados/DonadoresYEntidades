package ar.edu.utn.dds.k3003.repositories.InDataBase;

import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import ar.edu.utn.dds.k3003.repositories.NecesidadMaterialRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
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
    @Transactional
    public Optional<NecesidadMaterial> findById(String id){
        if (id == null) {
            return Optional.empty();
        }
        val necesidadMaterial = entityManager.find(NecesidadMaterial.class, id);
        return Optional.ofNullable(necesidadMaterial);
    }

    @Override
    @Transactional
    public NecesidadMaterial save(NecesidadMaterial necesidadMaterial) {
        if (necesidadMaterial.getId() == null) {
            entityManager.persist(necesidadMaterial);
            return necesidadMaterial;
        } else {
            return entityManager.merge(necesidadMaterial);
        }
    }

    @Override
    @Transactional
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
    @Transactional
    public List<NecesidadMaterial> todasLasNecesidades(String productoSolicitadoID) {

        String jpql = "SELECT n FROM NecesidadMaterial n WHERE n.productoSolicitadoID = :productoId";

        return entityManager.createQuery(jpql, NecesidadMaterial.class)
                .setParameter("productoId", productoSolicitadoID)
                .getResultList();
    }

    @Override
    @Transactional
    public List<NecesidadMaterial> todasNecesidades(){
        return entityManager.createQuery("SELECT n FROM NecesidadMaterial n", NecesidadMaterial.class).getResultList();
    }
}
