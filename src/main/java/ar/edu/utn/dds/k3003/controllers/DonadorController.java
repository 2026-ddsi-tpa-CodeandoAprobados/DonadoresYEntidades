package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorStatsDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

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
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.fachada.modificarEstado(donadorID,requestBody.get("estado")));
  }

  @PatchMapping("/{donadorID}/categoria")
  public ResponseEntity<DonadorDTO> patchCategoria(@PathVariable String donadorID, @RequestBody Map<String,String> requestBody) {
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.fachada.modifcarCategoria(donadorID,requestBody.get("categoria")));
  }

  @GetMapping("/{donadorID}/puede-donar")
  public ResponseEntity<Map<String, Boolean>> puedeDonar(@PathVariable String donadorID) {
    return ResponseEntity.ok(Map.of("puedeDonar", this.fachada.puedeDonar(donadorID)));
  }

  @DeleteMapping("/{donadorID}")
  public ResponseEntity<Void> deleteDonador(@PathVariable String donadorID) {
    this.fachada.quitarDonador(donadorID);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteAllDonadores() {
    this.fachada.quitarTodosLosDonadores();
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{donadorID}/estadisticas")
  public ResponseEntity<DonadorStatsDTO> estadisticasDonador(@PathVariable String donadorID) {
    return ResponseEntity.status(HttpStatus.OK).body(this.fachada.estadisticasDonador(donadorID));
  }

  @PostMapping("/{donadorID}/quejas")
  public ResponseEntity<QuejaDTO> agregarQueja(@RequestBody QuejaDTO quejaDTO) {
    meterRegistry.counter("api.donadores.quejas.registrada", "origen", "http").increment();
    return ResponseEntity.status(HttpStatus.CREATED).body(this.fachada.agregarQueja(quejaDTO));
  }

  @GetMapping("/{donadorID}/quejas")
  public ResponseEntity<List<QuejaDTO>> obtenerQuejasDe(@PathVariable String donadorID) {
    return ResponseEntity.status(HttpStatus.OK).body(this.fachada.obtenerQuejasDe(donadorID));
  }
}