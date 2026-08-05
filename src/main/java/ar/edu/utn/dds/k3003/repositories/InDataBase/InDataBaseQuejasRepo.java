package ar.edu.utn.dds.k3003.repositories.InDataBase;

import ar.edu.utn.dds.k3003.model.Queja;
import ar.edu.utn.dds.k3003.repositories.QuejasRepository;
import jakarta.persistence.EntityManager;
import lombok.val;
import java.util.List;
import java.util.Optional;

public class InDataBaseQuejasRepo implements QuejasRepository {

    private EntityManager entityManager;

    public InDataBaseQuejasRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Queja> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        val queja = entityManager.find(Queja.class, id);
        return Optional.ofNullable(queja);
    }

    @Override
    public Queja save(Queja queja) {
        if (queja.getId() == null) {
            entityManager.persist(queja);
            return queja;
        } else {
            return entityManager.merge(queja);
        }
    }

    @Override
    public List<Queja> quejasDeUnDonador(String donadorId) {
        String jpql = "SELECT n FROM Queja n WHERE n.donadorID = :donadorID";
        return entityManager.createQuery(jpql, Queja.class)
                .setParameter("donadorID", donadorId)
                .getResultList();
    }
}