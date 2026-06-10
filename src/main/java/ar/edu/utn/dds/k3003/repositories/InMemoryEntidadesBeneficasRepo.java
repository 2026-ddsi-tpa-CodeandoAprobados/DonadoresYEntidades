package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryEntidadesBeneficasRepo implements EntidadesBeneficasRepository{
    List<EntidadBenefica> entidadesBeneficas;
    private AtomicLong idSecuencial = new AtomicLong(1);

    public InMemoryEntidadesBeneficasRepo() {
        this.entidadesBeneficas = new ArrayList<>();
    }

    @Override
    public Optional<EntidadBenefica> findById(String id) {
        return this.entidadesBeneficas.stream().filter(d -> d.getId().equals(id)).findFirst();
    }

    @Override
    public EntidadBenefica save(EntidadBenefica entidadBenefica) {
        EntidadBenefica entidadBeneficaConID = entidadBenefica;
        entidadBeneficaConID.setId(String.valueOf(idSecuencial.getAndIncrement()));

        this.entidadesBeneficas.add(entidadBeneficaConID);
        return this.findById(entidadBeneficaConID.getId()).get();
    }

    //Eliminar por ID
    @Override
    public EntidadBenefica deleteById(String id) {
        var entidadBenefica = this.findById(id);
        this.entidadesBeneficas.remove(entidadBenefica.get());
        return entidadBenefica.get();
    }

    @Override
    public List<EntidadBenefica> todasLasEntidades(){
        return entidadesBeneficas.stream().toList();
    }
}
