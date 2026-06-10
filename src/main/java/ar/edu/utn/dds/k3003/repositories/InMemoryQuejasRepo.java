package ar.edu.utn.dds.k3003.repositories;


import ar.edu.utn.dds.k3003.model.Queja;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryQuejasRepo implements QuejasRepository{

    private List<Queja> quejas;

    private AtomicLong idSecuencial = new AtomicLong(1);

    public InMemoryQuejasRepo() {
        this.quejas = new ArrayList<>();
    }

    @Override
    public Queja save(Queja queja) {
        queja.setId(String.valueOf(idSecuencial.getAndIncrement()));

        this.quejas.add(queja);
        return this.findById(queja.getId()).get();
}

    @Override
    public Optional<Queja> findById(String id) {
        return this.quejas.stream().filter(d -> Objects.equals(d.getId(), id)).findFirst();
    }

    @Override
    public List<Queja> quejasDeUnDonador(String donadorId) {
        return this.quejas.stream().filter(d -> Objects.equals(d.getDonadorID(), donadorId)).toList();
    }

}
