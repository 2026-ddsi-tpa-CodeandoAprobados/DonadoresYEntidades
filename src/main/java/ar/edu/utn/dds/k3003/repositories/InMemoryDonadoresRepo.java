package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Donador;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryDonadoresRepo implements DonadoresRepository {

  private List<Donador> donadores;
  private AtomicLong idSecuencial = new AtomicLong(1);

  public InMemoryDonadoresRepo() {
    this.donadores = new ArrayList<>();
  }


  @Override
  public Optional<Donador> findById(String id) {

    return this.donadores.stream()
            .filter(d -> Objects.equals(d.getId(), id))
            .findFirst();
  }

  @Override
  public Donador save(Donador donador) {
    if (donador.getId() == null || donador.getId().isEmpty()) {
      donador.setId(String.valueOf(idSecuencial.getAndIncrement()));
    }
    this.donadores.add(donador);
    return donador;
  }

  @Override
  public Donador deleteById(String id) {
    val donador = this.findById(id);
    this.donadores.remove(donador.get());
    return donador.get();
  }


  @Override
  public List<Donador> todosLosDonadores(){
    return donadores.stream().toList();
  }
}



