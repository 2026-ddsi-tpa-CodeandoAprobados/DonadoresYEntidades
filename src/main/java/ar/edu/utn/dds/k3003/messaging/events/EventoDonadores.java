package ar.edu.utn.dds.k3003.messaging.events;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoDonadores implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;
  private TipoEvento tipo;
  private Instant timestamp;
  private String idReferencia;
  private DonadorDTO donador;
  private EntidadBeneficaDTO entidad;
  private NecesidadMaterialDTO necesidad;
  private QuejaDTO queja;

  @Builder.Default private Map<String, Object> extra = new HashMap<>();
}
