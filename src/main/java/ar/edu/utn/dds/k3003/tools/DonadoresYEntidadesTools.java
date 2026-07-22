package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.service.Fachada;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DonadoresYEntidadesTools {
    private Fachada fachada;

    public DonadoresYEntidadesTools(Fachada fachada) {
        this.fachada = fachada;
    }

    @McpTool(description = "Busco todos los donadores que se encuentran en mi sistema")
    public List<DonadorDTO> getAllDonadores(){
        return this.fachada.obtenerTodosLosDonadores();
    }

    @McpTool(description = "Busco un donador que se encuentran en mi sistema por ID")
    public DonadorDTO getDonadorPorID(String donadorID){
        return this.fachada.buscarDonadorPorID(donadorID);
    }

    @McpTool(description = "Doy de alta un donador en mi sistema")
    public DonadorDTO postDonador(DonadorDTO donadorDTO){
        return this.fachada.agregarDonador(donadorDTO);
    }

    @McpTool(description = "Agrego una queja a un donador en mi sistema")
    public QuejaDTO agregarQueja(QuejaDTO quejaDTO){
        return this.fachada.agregarQueja(quejaDTO);
    }

    @McpTool(description = "Busco todas las entidades beneficas que se encuentran en mi sistema")
    public List<EntidadBeneficaDTO> getAllEntidadesBeneficas(){
        return this.fachada.obtenerTodasLasEntidades();
    }

    @McpTool(description = "Busco una entidad benefica que se encuentran en mi sistema por ID")
    public EntidadBeneficaDTO getEntidadBenefica(String entidadID){
        return this.fachada.buscarEntidadPorID(entidadID);
    }

    @McpTool(description = "Doy de alta una entidad benefica en mi sistema")
    public EntidadBeneficaDTO postEntidadBenefica(EntidadBeneficaDTO entidadBeneficaDTO){
        return this.fachada.agregarEntidad(entidadBeneficaDTO);
    }

}
