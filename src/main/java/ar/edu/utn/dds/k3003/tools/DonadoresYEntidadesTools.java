package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.service.Fachada;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DonadoresYEntidadesTools {
    private Fachada fachada;

    public DonadoresYEntidadesTools(Fachada fachada) {
        this.fachada = fachada;
    }

    @Tool(description = "Busco todos los donadores que se encuentran en mi sistema")
    public List<DonadorDTO> getAllDonadores(){
        return this.fachada.obtenerTodosLosDonadores();
    }

    @Tool(description = "Busco un donador que se encuentran en mi sistema por ID")
    public DonadorDTO getDonadorPorID(String donadorID){
        return this.fachada.buscarDonadorPorID(donadorID);
    }

    @Tool(description = "Doy de alta un donador en mi sistema")
    public DonadorDTO postDonador(DonadorDTO donadorDTO){
        return this.fachada.agregarDonador(donadorDTO);
    }

    @Tool(description = "Agrego una queja a un donador en mi sistema")
    public QuejaDTO agregarQueja(QuejaDTO quejaDTO){
        return this.fachada.agregarQueja(quejaDTO);
    }

    @Tool(description = "Busco todas las entidades beneficas que se encuentran en mi sistema")
    public List<EntidadBeneficaDTO> getAllEntidadesBeneficas(){
        return this.fachada.obtenerTodasLasEntidades();
    }

    @Tool(description = "Busco una entidad benefica que se encuentran en mi sistema por ID")
    public EntidadBeneficaDTO getEntidadBenefica(String entidadID){
        return this.fachada.buscarEntidadPorID(entidadID);
    }

    @Tool(description = "Doy de alta una entidad benefica en mi sistema")
    public EntidadBeneficaDTO postEntidadBenefica(EntidadBeneficaDTO entidadBeneficaDTO){
        return this.fachada.agregarEntidad(entidadBeneficaDTO);
    }

}
