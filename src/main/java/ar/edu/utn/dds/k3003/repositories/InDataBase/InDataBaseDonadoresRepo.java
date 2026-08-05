package ar.edu.utn.dds.k3003.repositories.InDataBase;

import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.repositories.DonadoresRepository;
import jakarta.persistence.*;
import lombok.val;
import java.util.Optional;
import java.util.List;

public class InDataBaseDonadoresRepo implements DonadoresRepository {

  private EntityManager entityManager;

  public InDataBaseDonadoresRepo(EntityManager entityManager) {
    this.entityManager = entityManager;
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
    if (donador.getId() == null) {
      entityManager.persist(donador);
      return donador;
    } else {
      return entityManager.merge(donador);
    }
  }

  @Override
  public Donador deleteById(String id) {
    var donadorOptional = this.findById(id);
    if (donadorOptional.isPresent()) {
      Donador donador = donadorOptional.get();
      entityManager.remove(donador);
      return donador;
    }
    return null;
  }

  @Override
  public List<Donador> todosLosDonadores() {
    return entityManager.createQuery("SELECT d FROM Donador d", Donador.class).getResultList();
  }

  @Override
  public void deleteAll() {
    entityManager.createQuery("DELETE FROM Donador").executeUpdate();
  }
}