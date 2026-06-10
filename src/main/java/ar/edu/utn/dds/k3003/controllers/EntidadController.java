package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
    @RequestMapping("/entidades")
    public class EntidadController {

        private Fachada fachada;
        private MeterRegistry meterRegistry;

        public EntidadController(Fachada fachada, MeterRegistry meterRegistry) {
            this.fachada = fachada;
            this.meterRegistry = meterRegistry;
        }

    @PostMapping
    public ResponseEntity<EntidadBeneficaDTO> postEntidad(@RequestBody EntidadBeneficaDTO entidadDTO) {
            meterRegistry.counter("api.entidades.creada", "origen", "http").increment();
            return ResponseEntity.status(HttpStatus.CREATED).body(fachada.agregarEntidad(entidadDTO));
        }

    @GetMapping("/{entidadID}")
    public ResponseEntity<EntidadBeneficaDTO> getEntidad(@PathVariable String entidadID){
            return ResponseEntity.status(HttpStatus.OK).body(fachada.buscarEntidadPorID(entidadID));
    }

    @GetMapping
    public ResponseEntity<List<EntidadBeneficaDTO>> getAllEntidades() {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.obtenerTodasLasEntidades());
    }

    @DeleteMapping("/{entidadID}")
    public ResponseEntity<Void> deleteEntidad(@PathVariable String entidadID) {
        this.fachada.quitarEntidad(entidadID);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{entidadID}/razon-social")
        public ResponseEntity<EntidadBeneficaDTO> patchEntidad(@PathVariable String entidadID, @RequestBody Map<String,String> requestBody){
            return ResponseEntity.status(HttpStatus.CREATED).body(this.fachada.modificarRazonSocial(entidadID, requestBody.get("razonSocial")));
        }
    }
