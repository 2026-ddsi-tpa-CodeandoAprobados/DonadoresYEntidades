package ar.edu.utn.dds.k3003.repositories.InDataBase;

import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.repositories.DonadoresRepository;
import jakarta.persistence.*;
import lombok.val;

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

  @Override
  public void deleteAll() {
    try {
      transaction.begin();
      this.todosLosDonadores().forEach(x -> entityManager.remove(x));
      transaction.commit();
    } catch (RuntimeException e) {
      if (transaction.isActive()) transaction.rollback();
      throw e;
    }
  }
}


