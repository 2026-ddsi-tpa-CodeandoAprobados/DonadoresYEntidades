package ar.edu.utn.dds.k3003.messaging;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.messaging.events.EventoDonadores;
import ar.edu.utn.dds.k3003.messaging.events.TipoEvento;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DonadoresEventPublisher {

  private final RabbitTemplate rabbitTemplate;

  @Value("${app.rabbitmq.exchange:donatrack.exchange}")
  private String exchangeName;

  public DonadoresEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishDonadorCreado(DonadorDTO donadorDTO) {
    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.DONADOR_CREADO)
            .timestamp(Instant.now())
            .idReferencia(donadorDTO.id())
            .donador(donadorDTO)
            .build();
    enviarEvento("donadores.donador.creado", event);
  }

  public void publishDonadorEstadoModificado(String donadorId, EstadoDonadorEnum estado) {
    Map<String, Object> extra = new HashMap<>();
    extra.put("estado", estado != null ? estado.name() : null);

    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.DONADOR_ESTADO_MODIFICADO)
            .timestamp(Instant.now())
            .idReferencia(donadorId)
            .extra(extra)
            .build();
    enviarEvento("donadores.donador.estado", event);
  }

  public void publishDonadorCategoriaModificada(String donadorId, String categoria) {
    Map<String, Object> extra = new HashMap<>();
    extra.put("categoria", categoria);

    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.DONADOR_CATEGORIA_MODIFICADA)
            .timestamp(Instant.now())
            .idReferencia(donadorId)
            .extra(extra)
            .build();
    enviarEvento("donadores.donador.categoria", event);
  }

  public void publishDonadorEliminado(String donadorId) {
    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.DONADOR_ELIMINADO)
            .timestamp(Instant.now())
            .idReferencia(donadorId)
            .build();
    enviarEvento("donadores.donador.eliminado", event);
  }

  public void publishDonadoresTodosEliminados() {
    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.DONADORES_TODOS_ELIMINADOS)
            .timestamp(Instant.now())
            .build();
    enviarEvento("donadores.donador.todos_eliminados", event);
  }

  public void publishEntidadCreada(EntidadBeneficaDTO entidadDTO) {
    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.ENTIDAD_CREADA)
            .timestamp(Instant.now())
            .idReferencia(entidadDTO.id())
            .entidad(entidadDTO)
            .build();
    enviarEvento("donadores.entidad.creada", event);
  }

  public void publishEntidadRazonModificada(String entidadId, String razonSocial) {
    Map<String, Object> extra = new HashMap<>();
    extra.put("razonSocial", razonSocial);

    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.ENTIDAD_RAZON_MODIFICADA)
            .timestamp(Instant.now())
            .idReferencia(entidadId)
            .extra(extra)
            .build();
    enviarEvento("donadores.entidad.razonsocial", event);
  }

  public void publishEntidadEliminada(String entidadId) {
    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.ENTIDAD_ELIMINADA)
            .timestamp(Instant.now())
            .idReferencia(entidadId)
            .build();
    enviarEvento("donadores.entidad.eliminada", event);
  }

  public void publishEntidadesTodasEliminadas() {
    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.ENTIDADES_TODAS_ELIMINADAS)
            .timestamp(Instant.now())
            .build();
    enviarEvento("donadores.entidad.todas_eliminadas", event);
  }

  public void publishNecesidadRegistrada(NecesidadMaterialDTO necesidadDTO) {
    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.NECESIDAD_REGISTRADA)
            .timestamp(Instant.now())
            .idReferencia(necesidadDTO.id())
            .necesidad(necesidadDTO)
            .build();
    enviarEvento("donadores.necesidad.registrada", event);
  }

  public void publishNecesidadModificada(String necesidadId, Integer cantidadObjetivo) {
    Map<String, Object> extra = new HashMap<>();
    extra.put("cantidadObjetivo", cantidadObjetivo);

    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.NECESIDAD_MODIFICADA)
            .timestamp(Instant.now())
            .idReferencia(necesidadId)
            .extra(extra)
            .build();
    enviarEvento("donadores.necesidad.modificada", event);
  }

  public void publishNecesidadSatisfecha(
      String necesidadId, Integer cantidadSatisfecha, Integer cantidadObjetivoRestante) {
    Map<String, Object> extra = new HashMap<>();
    extra.put("cantidadSatisfecha", cantidadSatisfecha);
    extra.put("cantidadObjetivoRestante", cantidadObjetivoRestante);

    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.NECESIDAD_SATISFECHA)
            .timestamp(Instant.now())
            .idReferencia(necesidadId)
            .extra(extra)
            .build();
    enviarEvento("donadores.necesidad.satisfecha", event);
  }

  public void publishNecesidadEliminada(String necesidadId) {
    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.NECESIDAD_ELIMINADA)
            .timestamp(Instant.now())
            .idReferencia(necesidadId)
            .build();
    enviarEvento("donadores.necesidad.eliminada", event);
  }

  public void publishQuejaRegistrada(QuejaDTO quejaDTO) {
    EventoDonadores event =
        EventoDonadores.builder()
            .id(UUID.randomUUID().toString())
            .tipo(TipoEvento.QUEJA_REGISTRADA)
            .timestamp(Instant.now())
            .idReferencia(quejaDTO.donadorID())
            .queja(quejaDTO)
            .build();
    enviarEvento("donadores.queja.registrada", event);
  }

  private void enviarEvento(String routingKey, EventoDonadores event) {
    try {
      log.info(
          "Publicando evento {} a exchange '{}' con routingKey '{}' [ID: {}]",
          event.getTipo(),
          exchangeName,
          routingKey,
          event.getId());
      rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
    } catch (Exception e) {
      log.warn(
          "No se pudo publicar el evento {} a RabbitMQ (RabbitMQ posiblemente no disponible): {}",
          event.getTipo(),
          e.getMessage());
    }
  }
}
