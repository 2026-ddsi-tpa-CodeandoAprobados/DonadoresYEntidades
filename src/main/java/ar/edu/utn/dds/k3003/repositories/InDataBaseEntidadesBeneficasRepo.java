package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.EntidadBenefica;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.val;
import java.util.List;
import java.util.Optional;

public class InDataBaseEntidadesBeneficasRepo implements EntidadesBeneficasRepository{

    private EntityManager entityManager;
    private EntityTransaction transaction;

    public InDataBaseEntidadesBeneficasRepo(EntityManager entityManager, EntityTransaction transaction) {
        this.entityManager = entityManager;
        this.transaction = transaction;
    }

    @Override
    public Optional<EntidadBenefica> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        EntidadBenefica entidadBenefica = entityManager.find(EntidadBenefica.class, id);
        return Optional.ofNullable(entidadBenefica);
    }

    @Override
    public EntidadBenefica save(EntidadBenefica entidadBenefica) {
        try {
            transaction.begin();
            EntidadBenefica entidadBeneficaGuardada;
            if (entidadBenefica.getId() == null) {
                entityManager.persist(entidadBenefica);
                entidadBeneficaGuardada = entidadBenefica;
            } else {
                entidadBeneficaGuardada = entityManager.merge(entidadBenefica);
            }
            transaction.commit();
            return entidadBeneficaGuardada;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public EntidadBenefica deleteById(String id) {
        val entidadBeneficaOptional = this.findById(id);
        if (entidadBeneficaOptional.isPresent()) {
            EntidadBenefica entidadBenefica = entidadBeneficaOptional.get();
            try {
                transaction.begin();
                entityManager.remove(entidadBenefica);
                transaction.commit();
                return entidadBenefica;
            } catch (RuntimeException e) {
                if (transaction.isActive()) transaction.rollback();
                throw e;
            }
        }
        return null;
    }

    @Override
    public List<EntidadBenefica> todasLasEntidades(){
        return entityManager.createQuery("SELECT e FROM EntidadBenefica e", EntidadBenefica.class).getResultList();
    }
}
