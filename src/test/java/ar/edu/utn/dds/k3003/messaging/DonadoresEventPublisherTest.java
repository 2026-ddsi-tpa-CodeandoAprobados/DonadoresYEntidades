package ar.edu.utn.dds.k3003.messaging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import ar.edu.utn.dds.k3003.messaging.events.EventoDonadores;
import ar.edu.utn.dds.k3003.messaging.events.TipoEvento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class DonadoresEventPublisherTest {

  @Mock private RabbitTemplate rabbitTemplate;

  private DonadoresEventPublisher publisher;

  @BeforeEach
  void setUp() {
    publisher = new DonadoresEventPublisher(rabbitTemplate);
    ReflectionTestUtils.setField(publisher, "exchangeName", "donatrack.exchange");
  }

  @Test
  void testPublishDonadorCreado() {
    DonadorDTO dto =
        new DonadorDTO(
            "12",
            "Lucia",
            "Fernandez",
            30,
            "lucia@test.com",
            "12345678",
            "Calle 1",
            EstadoDonadorEnum.VERIFICADO,
            "COLABORADOR");

    publisher.publishDonadorCreado(dto);

    ArgumentCaptor<EventoDonadores> captor = ArgumentCaptor.forClass(EventoDonadores.class);
    verify(rabbitTemplate, times(1))
        .convertAndSend(eq("donatrack.exchange"), eq("donadores.donador.creado"), captor.capture());

    EventoDonadores event = captor.getValue();
    assertEquals(TipoEvento.DONADOR_CREADO, event.getTipo());
    assertEquals("12", event.getIdReferencia());
    assertEquals("Lucia", event.getDonador().nombre());
    assertNotNull(event.getTimestamp());
  }

  @Test
  void testPublishDonadorEstadoModificado() {
    publisher.publishDonadorEstadoModificado("12", EstadoDonadorEnum.SOSPECHOSO);

    ArgumentCaptor<EventoDonadores> captor = ArgumentCaptor.forClass(EventoDonadores.class);
    verify(rabbitTemplate, times(1))
        .convertAndSend(eq("donatrack.exchange"), eq("donadores.donador.estado"), captor.capture());

    EventoDonadores event = captor.getValue();
    assertEquals(TipoEvento.DONADOR_ESTADO_MODIFICADO, event.getTipo());
    assertEquals("12", event.getIdReferencia());
    assertEquals("SOSPECHOSO", event.getExtra().get("estado"));
  }

  @Test
  void testPublishEntidadCreada() {
    EntidadBeneficaDTO entidadDTO =
        new EntidadBeneficaDTO("e1", "Comedor", "Av 123", "11223344", "comedor@test.com");

    publisher.publishEntidadCreada(entidadDTO);

    ArgumentCaptor<EventoDonadores> captor = ArgumentCaptor.forClass(EventoDonadores.class);
    verify(rabbitTemplate, times(1))
        .convertAndSend(eq("donatrack.exchange"), eq("donadores.entidad.creada"), captor.capture());

    EventoDonadores event = captor.getValue();
    assertEquals(TipoEvento.ENTIDAD_CREADA, event.getTipo());
    assertEquals("e1", event.getIdReferencia());
  }

  @Test
  void testPublishNecesidadRegistrada() {
    NecesidadMaterialDTO necesidadDTO =
        new NecesidadMaterialDTO(
            "n1", "e1", 100, "fideos", 50, "p1", TipoNecesidadMaterialEnum.EXTRAORDINARIA);

    publisher.publishNecesidadRegistrada(necesidadDTO);

    ArgumentCaptor<EventoDonadores> captor = ArgumentCaptor.forClass(EventoDonadores.class);
    verify(rabbitTemplate, times(1))
        .convertAndSend(
            eq("donatrack.exchange"), eq("donadores.necesidad.registrada"), captor.capture());

    EventoDonadores event = captor.getValue();
    assertEquals(TipoEvento.NECESIDAD_REGISTRADA, event.getTipo());
    assertEquals("n1", event.getIdReferencia());
  }

  @Test
  void testPublishQuejaRegistrada() {
    QuejaDTO quejaDTO = new QuejaDTO("q1", "d1", "donador1", null, "Producto en mal estado");

    publisher.publishQuejaRegistrada(quejaDTO);

    ArgumentCaptor<EventoDonadores> captor = ArgumentCaptor.forClass(EventoDonadores.class);
    verify(rabbitTemplate, times(1))
        .convertAndSend(
            eq("donatrack.exchange"), eq("donadores.queja.registrada"), captor.capture());

    EventoDonadores event = captor.getValue();
    assertEquals(TipoEvento.QUEJA_REGISTRADA, event.getTipo());
    assertEquals("donador1", event.getIdReferencia());
  }

  @Test
  void testPublishNoFallaCuandoRabbitEstaCaido() {
    doThrow(new RuntimeException("RabbitMQ connection refused"))
        .when(rabbitTemplate)
        .convertAndSend(any(String.class), any(String.class), any(Object.class));

    assertDoesNotThrow(() -> publisher.publishDonadorEliminado("12"));
  }
}
