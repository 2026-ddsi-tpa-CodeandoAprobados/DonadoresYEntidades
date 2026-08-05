package ar.edu.utn.dds.k3003.repositories.InDataBase;

import ar.edu.utn.dds.k3003.model.EntidadBenefica;
import ar.edu.utn.dds.k3003.repositories.EntidadesBeneficasRepository;
import jakarta.persistence.EntityManager;
import lombok.val;
import java.util.List;
import java.util.Optional;

public class InDataBaseEntidadesBeneficasRepo implements EntidadesBeneficasRepository {

    private EntityManager entityManager;

    public InDataBaseEntidadesBeneficasRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<EntidadBenefica> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        val entidadBenefica = entityManager.find(EntidadBenefica.class, id);
        return Optional.ofNullable(entidadBenefica);
    }

    @Override
    public EntidadBenefica save(EntidadBenefica entidadBenefica) {
        if (entidadBenefica
                .getId() == null) {
            entityManager.persist(entidadBenefica);
            return entidadBenefica;
        } else {
            return entityManager.merge(entidadBenefica);
        }
    }

    @Override
    public EntidadBenefica deleteById(String id) {
        var entidadBeneficaOptional = this.findById(id);
        if (entidadBeneficaOptional.isPresent()) {
            EntidadBenefica entidadBenefica = entidadBeneficaOptional.get();
            entityManager.remove(entidadBenefica);
            return entidadBenefica;
        }
        return null;
    }

    @Override
    public List<EntidadBenefica> todasLasEntidades(){
        return entityManager.createQuery("SELECT e FROM EntidadBenefica e", EntidadBenefica.class).getResultList();
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM EntidadBenefica").executeUpdate();
    }
}