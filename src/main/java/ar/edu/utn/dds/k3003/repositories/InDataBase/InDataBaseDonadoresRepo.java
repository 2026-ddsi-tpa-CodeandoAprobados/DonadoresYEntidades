package ar.edu.utn.dds.k3003.repositories.InDataBase;

import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.repositories.DonadoresRepository;
import jakarta.persistence.*;
import lombok.val;
import java.util.Optional;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class InDataBaseDonadoresRepo implements DonadoresRepository {

  private EntityManager entityManager;

  public InDataBaseDonadoresRepo(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  public Optional<Donador> findById(String id) {
    if (id == null) {
      return Optional.empty();
    }
    val donador = entityManager.find(Donador.class, id);
    return Optional.ofNullable(donador);
  }

  @Override
  @Transactional
  public Donador save(Donador donador) {
    if (donador.getId() == null) {
      entityManager.persist(donador);
      return donador;
    } else {
      return entityManager.merge(donador);
    }
  }

  @Override
  @Transactional
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
  @Transactional(readOnly = true)
  public List<Donador> todosLosDonadores() {
    return entityManager.createQuery("SELECT d FROM Donador d", Donador.class).getResultList();
  }

  @Override
  @Transactional
  public void deleteAll() {
    entityManager.createQuery("DELETE FROM Donador").executeUpdate();
  }
}


