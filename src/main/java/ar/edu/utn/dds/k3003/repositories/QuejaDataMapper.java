package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.model.Queja;

import java.util.List;

public class QuejaDataMapper {

    public QuejaDTO toQuejaDTO(Queja queja){
        return new QuejaDTO(queja.getId(),
                queja.getDonacionID(),
                queja.getDonadorID(),
                queja.getFecha(),
                queja.getDescripcion());
    }

    public Queja toQueja(QuejaDTO quejaDTO){
       return new Queja(quejaDTO.donacionID(),
                         quejaDTO.donadorID(),
                         quejaDTO.fecha(),
                         quejaDTO.descripcion());

    }

    public List<QuejaDTO> toQuejasDTO(List<Queja> quejas){
        return quejas.stream().map(x -> toQuejaDTO(x)).toList();
    }
}
