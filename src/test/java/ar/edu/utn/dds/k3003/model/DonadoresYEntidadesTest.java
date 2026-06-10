package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.ClassFinder;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.*;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.*;
import ar.edu.utn.dds.k3003.repositories.*;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
  @EnabledIf("ar.edu.utn.dds.k3003.catedra.donadoresyentidades.DonadoresYEntidadesTest#condicion")

  public class DonadoresYEntidadesTest {
    @Mock private DonadoresRepository donadoresRepository;
    @Mock private EntidadesBeneficasRepository entidadesBeneficasRepository;
    @Mock private NecesidadMaterialRepository necesidadMaterialRepository;
    @Mock private QuejasRepository quejasRepository;
    @Mock private FachadaIncentivos fachadaIncentivos;

    @InjectMocks
    private Fachada fachada;

    FachadaDonadoresYEntidades instancia;
// EJEMPLOS
private DonadorDTO donadorEjemplo1;
private DonadorDTO donadorEjemplo2;
private EntidadBeneficaDTO entidadEjemplo;
private NecesidadMaterialDTO necesidadRecurrente;
private NecesidadMaterialDTO necesidadExtraordinaria;
private QuejaDTO quejaEjemplo;


      @SneakyThrows
      @BeforeEach
      void setUp() {

        var clazz = ClassFinder.findClass();
        fachada = (Fachada) clazz.getDeclaredConstructor().newInstance();

        fachada.setFachadaIncentivos(fachadaIncentivos);
      donadorEjemplo1 =
              new DonadorDTO(
                      null,
                      "String",
                      "String",
                      1,
                      "String",
                      "String",
                      "String",
                      EstadoDonadorEnum.VERIFICADO,
                      "String");
      donadorEjemplo2 =
              new DonadorDTO(
                      null,
                      "String",
                      "String",
                      1,
                      "String",
                      "String",
                      "String",
                      EstadoDonadorEnum.VERIFICADO,
                      "String");

      entidadEjemplo = new EntidadBeneficaDTO(null,
              "entidad1",
              "entidad1",
              "entidad1",
              "entidad1");


      necesidadRecurrente = new NecesidadMaterialDTO(
              null,
              "1",
              5,
              "necesidad1",
              5,
              "producto1",
              TipoNecesidadMaterialEnum.RECURRENTE);

      necesidadExtraordinaria = new NecesidadMaterialDTO(
                  null,
                  "1",
                  5,
                  "necesidad1",
                  5,
                  "producto1",
                  TipoNecesidadMaterialEnum.EXTRAORDINARIA);

      quejaEjemplo = new QuejaDTO(
              null,
              "donacion1",
              "1",
              null,
              "queja1");

    }

  @Test
  void testSiempreTrue() {
    assertTrue(true);
  }

  @Test
  void testSiempreEquals() {
    assertEquals(1, 1);
  }

  @Test
  void testAgregarDonador() {
    val donador1 = fachada.agregarDonador(donadorEjemplo1);

    assertEquals("1", donador1.id());

    assertEquals("String", donador1.nombre());

    val donador2 = fachada.agregarDonador(donadorEjemplo2);

    assertEquals("2", donador2.id());

    assertEquals("String", donador2.nombre());
  }

  @Test
  void testQuitarDonador(){
      val donador1 = fachada.agregarDonador(donadorEjemplo1);

      assertNotNull(donador1.id());

      assertEquals(fachada.buscarDonadorPorID(donador1.id()).nombre(),"String");

      assertEquals(fachada.buscarDonadorPorID(donador1.id()).id(), "1");

      val resultado = fachada.quitarDonador(donador1.id());

      Assertions.assertThrows(
            RuntimeException.class,
            () -> {
              fachada.buscarDonadorPorID(resultado.id());
            });

    assertEquals(donadoresRepository.findById(resultado.id()), Optional.empty());
  }

  @Test
  void testBuscarDonadorPorID() {
      val donador = fachada.agregarDonador(donadorEjemplo1);

      assertNotNull(donador.id());

      assertEquals(fachada.buscarDonadorPorID(donador.id()).nombre(),"String");

      assertEquals(fachada.buscarDonadorPorID(donador.id()).id(), "1");

      val resultado = fachada.quitarDonador(donador.id());

       Assertions.assertThrows(
            RuntimeException.class,
            () -> {
              fachada.buscarDonadorPorID(resultado.id());
            });
  }

  @Test
  void testAgregarEntidad() {
    assertNull(entidadEjemplo.id());

    val entidad = fachada.agregarEntidad(entidadEjemplo);

    assertNotNull(entidad.id());
  }

  @Test
  void testQuitarEntidad(){
    assertNull(entidadEjemplo.id());

    val entidad = fachada.agregarEntidad(entidadEjemplo);

    assertNotNull(entidad.id());

    val resultado = fachada.quitarEntidad(entidad.id());

    Assertions.assertThrows(
            RuntimeException.class,
            () -> {
              fachada.buscarEntidadPorID(resultado.id());
            });

    assertEquals(entidadesBeneficasRepository.findById(resultado.id()), Optional.empty());
  }

  @Test
  void testBuscarEntidadPorID() {
    Assertions.assertThrows(
            RuntimeException.class,
            () -> {
              fachada.buscarEntidadPorID(entidadEjemplo.id());
            });

    assertNull(entidadEjemplo.id());

    val entidad = fachada.agregarEntidad(entidadEjemplo);

    assertEquals(fachada.buscarEntidadPorID(entidad.id()).id(),"1");
  }

  @Test
  void testRegistrarNecesidad() {

      assertNull(necesidadExtraordinaria.id());
      val entidad = fachada.agregarEntidad(entidadEjemplo);
      val necesidad = fachada.registrarNecesidad(necesidadExtraordinaria);

      assertNotNull(necesidad.id());

  }

  @Test
  void testQuitarUnaNecesidad(){
      assertNull(necesidadExtraordinaria.id());
    val entidad = fachada.agregarEntidad(entidadEjemplo);
    val necesidad = fachada.registrarNecesidad(necesidadExtraordinaria);

    assertNotNull(necesidad.id());

    val resultado = fachada.quitarNecesidad(necesidad.id());

    assertEquals(necesidadMaterialRepository.findById(resultado.id()), Optional.empty());
  }

  @Test
  void testPuedeDonar() {

    val donador = fachada.agregarDonador(donadorEjemplo1);

    assertTrue(fachada.puedeDonar(donador.id()));

    val queja1 = fachada.agregarQueja(quejaEjemplo);
    val queja2 = fachada.agregarQueja(quejaEjemplo);
    val queja3 = fachada.agregarQueja(quejaEjemplo);
    val queja4 = fachada.agregarQueja(quejaEjemplo);
    val queja5 = fachada.agregarQueja(quejaEjemplo);

    assertEquals(fachada.obtenerQuejasDe(donador.id()).size(),5);

    val donadorSospechoso = fachada.buscarDonadorPorID(donador.id());

    assertEquals(EstadoDonadorEnum.SOSPECHOSO,donadorSospechoso.estado());

    //assertTrue(fachada.puedeDonar(donadorSospechoso.id()));

    val queja6 = fachada.agregarQueja(quejaEjemplo);
    val queja7 = fachada.agregarQueja(quejaEjemplo);
    val queja8 = fachada.agregarQueja(quejaEjemplo);
    val queja9 = fachada.agregarQueja(quejaEjemplo);
    val queja10 = fachada.agregarQueja(quejaEjemplo);

    assertEquals(fachada.obtenerQuejasDe(donador.id()).size(),10);

    val donadorBaneado = fachada.buscarDonadorPorID(donador.id());

    assertEquals(EstadoDonadorEnum.BANEADO,donadorBaneado.estado());

    assertFalse(fachada.puedeDonar(donadorBaneado.id()));

  }

  @Test
  void testAgregarQueja() {
    val donador = fachada.agregarDonador(donadorEjemplo1);
    val queja = fachada.agregarQueja(quejaEjemplo);

    assertNotNull(queja.id());
    assertNotNull(donador.id());

    val resultado = fachada.buscarDonadorPorID(queja.donadorID());
    assertEquals(resultado.id(),donador.id());
  }

  @Test
  void testObtenerQuejasDe() {
    val donador = fachada.agregarDonador(donadorEjemplo1);

    assertTrue(fachada.puedeDonar(donador.id()));

    val queja1 = fachada.agregarQueja(quejaEjemplo);
    val queja2 = fachada.agregarQueja(quejaEjemplo);
    val queja3 = fachada.agregarQueja(quejaEjemplo);
    val queja4 = fachada.agregarQueja(quejaEjemplo);
    val queja5 = fachada.agregarQueja(quejaEjemplo);

    assertEquals(fachada.obtenerQuejasDe(donador.id()).size(),5);

  }

  @Test
  void testModificarEstado() {
    val donador = fachada.agregarDonador(donadorEjemplo1);

    assertTrue(fachada.puedeDonar(donador.id()));

    val queja1 = fachada.agregarQueja(quejaEjemplo);
    val queja2 = fachada.agregarQueja(quejaEjemplo);
    val queja3 = fachada.agregarQueja(quejaEjemplo);
    val queja4 = fachada.agregarQueja(quejaEjemplo);
    val queja5 = fachada.agregarQueja(quejaEjemplo);

    assertEquals(fachada.obtenerQuejasDe(donador.id()).size(),5);

    val donadorSospechoso = fachada.buscarDonadorPorID(donador.id());

    assertEquals(EstadoDonadorEnum.SOSPECHOSO,donadorSospechoso.estado());


    val queja6 = fachada.agregarQueja(quejaEjemplo);
    val queja7 = fachada.agregarQueja(quejaEjemplo);
    val queja8 = fachada.agregarQueja(quejaEjemplo);
    val queja9 = fachada.agregarQueja(quejaEjemplo);
    val queja10 = fachada.agregarQueja(quejaEjemplo);

    assertEquals(fachada.obtenerQuejasDe(donador.id()).size(),10);

    val donadorBaneado = fachada.buscarDonadorPorID(donador.id());

    assertEquals(EstadoDonadorEnum.BANEADO,donadorBaneado.estado());

  }
  @Test
  void testModicarRazonSocial(){
          val entidad = fachada.agregarEntidad(entidadEjemplo);
          assertNotNull(entidad.id());

          val resultado = fachada.modificarRazonSocial("1","entidad2");

          assertEquals("entidad2",resultado.razonSocial());
  }

  @Test
  void testModificarCategoria() {
      val donador = fachada.agregarDonador(donadorEjemplo1);

      assertEquals(donador.categoria(),"Ocasional");

    fachada.modifcarCategoria(donador.id(),"Vip");

      val resultado = fachada.buscarDonadorPorID(donador.id());

      assertEquals(resultado.categoria(),"Vip");
  }

  @Test
  void testObtenerNecesidadesInsatisfechasDe() {
      val entidad = fachada.agregarEntidad(entidadEjemplo);
      val necesidad1 = fachada.registrarNecesidad(necesidadExtraordinaria);
      val necesidad2 = fachada.registrarNecesidad(necesidadExtraordinaria);
      val necesidad3 = fachada.registrarNecesidad(necesidadExtraordinaria);
      val necesidad4 = fachada.registrarNecesidad(necesidadExtraordinaria);

      assertTrue(fachada.obtenerNecesidadesInsatisfechasDe("producto1").size() == 4);
  }

  @Test
  void testSatisfacerNecesidadRecurrente() {
          val entidad = fachada.agregarEntidad(entidadEjemplo);
          val necesidad = fachada.registrarNecesidad(necesidadRecurrente);

      Assertions.assertThrows(
              RuntimeException.class,
              () -> {
                  fachada.satisfacerNecesidad(necesidad.id(),2);
              });

    val resultado = fachada.satisfacerNecesidad(necesidad.id(),5);

    assertEquals(0,resultado.cantidadObjetivo());

  }
    @Test
    void testSatisfacerNecesidadExtraordinaria() {
        val entidad = fachada.agregarEntidad(entidadEjemplo);
        val necesidad = fachada.registrarNecesidad(necesidadExtraordinaria);
        val resultado = fachada.satisfacerNecesidad(necesidad.id(),2);
        assertEquals(3,resultado.cantidadObjetivo());
    }

  @Test
  void testEstadisticasDonador() {
      val donador = fachada.agregarDonador(donadorEjemplo1);

      val insigniasFalsas = List.of(new InsigniaDTO("ins1", "Super Donador", "descripcion"));
      val misionFalsa = new MisionDTO("mis1", "Mision Imposible", "desc", null, null, TipoMisionEnum.COMPLETITUD);

      when(fachadaIncentivos.getInsigniasDeDonador(donador.id())).thenReturn(insigniasFalsas);
      when(fachadaIncentivos.getMisionEnCursoDeDonador(donador.id())).thenReturn(misionFalsa);

      val stats = fachada.estadisticasDonador(donador.id());

      assertNotNull(stats);
      assertEquals("String", stats.nombre());
      assertEquals(1, stats.insigniasID().size());
      assertEquals("ins1", stats.insigniasID().getFirst());
      assertEquals("mis1", stats.misionActualID());

      verify(fachadaIncentivos, times(1)).getInsigniasDeDonador(donador.id());
      verify(fachadaIncentivos, times(1)).getMisionEnCursoDeDonador(donador.id());
  }
}

