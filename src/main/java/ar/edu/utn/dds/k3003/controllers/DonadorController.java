package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.service.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorStatsDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/donadores")
public class DonadorController {

  private Fachada fachada;
  private MeterRegistry meterRegistry;

  public DonadorController(Fachada fachada, MeterRegistry meterRegistry) {
    this.fachada = fachada;
    this.meterRegistry = meterRegistry;
  }

  @PostMapping
  public ResponseEntity<DonadorDTO> postDonador(@RequestBody DonadorDTO donadorDTO) {
    log.info("Se registro un donador");
    meterRegistry.counter("api.donadores.creados", "origen", "http").increment();
    return ResponseEntity.status(HttpStatus.CREATED).body(this.fachada.agregarDonador(donadorDTO));
  }

  @GetMapping("/{donadorID}")
  public ResponseEntity<DonadorDTO> getDonadorByID(@PathVariable String donadorID) {
    return ResponseEntity.ok(this.fachada.buscarDonadorPorID(donadorID));
  }

  @GetMapping
  public ResponseEntity<List<DonadorDTO>> getAllDonadores() {
    return ResponseEntity.status(HttpStatus.OK).body(this.fachada.obtenerTodosLosDonadores());
  }

  @PatchMapping("/{donadorID}/estado")
  public ResponseEntity<DonadorDTO> patchEstado(@PathVariable String donadorID, @RequestBody Map<String, EstadoDonadorEnum> requestBody) {
    var estado = requestBody.get("estado");
    log.info("Se modificó el estado del donador con ID: {} por el estado: {}", donadorID, estado);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.fachada.modificarEstado(donadorID, estado));
  }

  @PatchMapping("/{donadorID}/categoria")
  public ResponseEntity<DonadorDTO> patchCategoria(@PathVariable String donadorID, @RequestBody Map<String,String> requestBody) {
    var categoria = requestBody.get("categoria");
    log.info("Se modifico la categoria del donador con ID: {} por la categoria: {}", donadorID, categoria);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.fachada.modifcarCategoria(donadorID, categoria));
  }

  @GetMapping("/{donadorID}/puede-donar")
  public ResponseEntity<Map<String, Boolean>> puedeDonar(@PathVariable String donadorID) {
    return ResponseEntity.ok(Map.of("puedeDonar", this.fachada.puedeDonar(donadorID)));
  }

  @DeleteMapping("/{donadorID}")
  public ResponseEntity<Void> deleteDonador(@PathVariable String donadorID) {
    log.info("Se elimino el donador de ID: {}", donadorID);
    this.fachada.quitarDonador(donadorID);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteAllDonadores() {
    log.info("Se eliminaron todos los donadores");
    this.fachada.quitarTodosLosDonadores();
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{donadorID}/estadisticas")
  public ResponseEntity<DonadorStatsDTO> estadisticasDonador(@PathVariable String donadorID) {
    return ResponseEntity.status(HttpStatus.OK).body(this.fachada.estadisticasDonador(donadorID));
  }
}