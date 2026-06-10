package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.val;
import java.util.List;
import java.util.Optional;

public class InDataBaseNecesidadMaterialRepo implements NecesidadMaterialRepository{

    private EntityManager entityManager;
    private EntityTransaction transaction;

    public InDataBaseNecesidadMaterialRepo(EntityManager entityManager, EntityTransaction transaction) {
        this.entityManager = entityManager;
        this.transaction = transaction;
    }
    @Override
    public NecesidadMaterial save(NecesidadMaterial necesidadMaterial) {
        try {
            transaction.begin();
            NecesidadMaterial necesidadMaterialGuardada;
            if (necesidadMaterial.getId() == null) {
                entityManager.persist(necesidadMaterial);
                necesidadMaterialGuardada = necesidadMaterial;
            } else {
                necesidadMaterialGuardada = entityManager.merge(necesidadMaterial);
            }
            transaction.commit();
            return necesidadMaterialGuardada;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public NecesidadMaterial deleteById(String id) {
        val necesidadOptional = this.findById(id);
        if (necesidadOptional.isPresent()) {
            NecesidadMaterial necesidadMaterial = necesidadOptional.get();
            try {
                transaction.begin();
                entityManager.remove(necesidadMaterial);
                transaction.commit();
                return necesidadMaterial;
            } catch (RuntimeException e) {
                if (transaction.isActive()) transaction.rollback();
                throw e;
            }
        }
        return null;
    }

    @Override
    public Optional<NecesidadMaterial> findById(String id){
        if (id == null) {
            return Optional.empty();
        }
        NecesidadMaterial necesidadMaterial = entityManager.find(NecesidadMaterial.class, id);
        return Optional.ofNullable(necesidadMaterial);
    }

    @Override
    public List<NecesidadMaterial> todasLasNecesidades(String productoSolicitadoID) {

        String jpql = "SELECT n FROM NecesidadMaterial n WHERE n.productoSolicitadoID = :productoId";

        return entityManager.createQuery(jpql, NecesidadMaterial.class)
                .setParameter("productoId", productoSolicitadoID)
                .getResultList();
    }
    public List<NecesidadMaterial> todasNecesidades(){
        return entityManager.createQuery("SELECT n FROM NecesidadMaterial n", NecesidadMaterial.class).getResultList();
    }
}
