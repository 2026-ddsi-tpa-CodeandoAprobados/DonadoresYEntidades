package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Queja;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class InDataBaseQuejasRepo implements QuejasRepository{

    private EntityManager entityManager;
    private EntityTransaction transaction;

    public InDataBaseQuejasRepo(EntityManager entityManager, EntityTransaction transaction) {
        this.entityManager = entityManager;
        this.transaction = transaction;
    }

    @Override
    public Queja save(Queja queja) {
        try {
            transaction.begin();
            Queja quejaGuardada;
            if (queja.getId() == null) {
                entityManager.persist(queja);
                quejaGuardada = queja;
            } else {
                quejaGuardada = entityManager.merge(queja);
            }
            transaction.commit();
            return quejaGuardada;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Queja> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        Queja queja = entityManager.find(Queja.class, id);
        return Optional.ofNullable(queja);
    }

    @Override
    public List<Queja> quejasDeUnDonador(String donadorId) {
        String jpql = "SELECT n FROM Queja n WHERE n.donadorID = :donadorID";

        return entityManager.createQuery(jpql, Queja.class)
                .setParameter("donadorID", donadorId)
                .getResultList();
    }

}
