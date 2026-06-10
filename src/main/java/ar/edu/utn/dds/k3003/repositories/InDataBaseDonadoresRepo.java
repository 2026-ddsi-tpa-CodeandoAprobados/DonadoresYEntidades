package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Donador;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

public class InDataBaseDonadoresRepo implements DonadoresRepository {

  private EntityManager entityManager;
  private EntityTransaction transaction;

  public InDataBaseDonadoresRepo(EntityManager entityManager, EntityTransaction transaction) {
    this.entityManager = entityManager;
    this.transaction = transaction;
  }

  @Override
  public Optional<Donador> findById(String id) {
    if (id == null) {
      return Optional.empty();
    }
    val donador = entityManager.find(Donador.class, id);
    return Optional.ofNullable(donador);
  }

  @Override
  public Donador save(Donador donador) {
    try {
      transaction.begin();
      Donador donadorGuardado;
      if (donador.getId() == null) {
        entityManager.persist(donador);
        donadorGuardado = donador;
      } else {
        donadorGuardado = entityManager.merge(donador);
      }
      transaction.commit();
      return donadorGuardado;
    } catch (RuntimeException e) {
      if (transaction.isActive()) {
        transaction.rollback();
      }
      throw e;
    }
  }

  @Override
  public Donador deleteById(String id) {
    val donadorOptional = this.findById(id);
    if (donadorOptional.isPresent()) {
      Donador donador = donadorOptional.get();
      try {
        transaction.begin();
        entityManager.remove(donador);
        transaction.commit();
        return donador;
      } catch (RuntimeException e) {
        if (transaction.isActive()) transaction.rollback();
        throw e;
      }
    }
    return null;
  }

  @Override
  public List<Donador> todosLosDonadores() {
    return entityManager.createQuery("SELECT d FROM Donador d", Donador.class).getResultList();
  }
}


