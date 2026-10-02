package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.service.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/necesidades")
public class NecesidadController {

    private Fachada fachada;
    private MeterRegistry meterRegistry;

    public NecesidadController(Fachada fachada, MeterRegistry meterRegistry) {
        this.fachada = fachada;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    public ResponseEntity<NecesidadMaterialDTO> postMaterial(@RequestBody NecesidadMaterialDTO necesidadMaterialDTO) {
        log.info("Se registro una necesidad de material sobre la entidad benefica de ID: {} de un producto de ID: {} y con una cantidad objetivo de: {}",
                necesidadMaterialDTO.entidadID(),
                necesidadMaterialDTO.productoSolicitadoID(),
                necesidadMaterialDTO.cantidadObjetivo());
        meterRegistry.counter("api.necesidad.registrada", "origen", "http").increment();
        return ResponseEntity.status(HttpStatus.CREATED).body(this.fachada.registrarNecesidad(necesidadMaterialDTO));
    }

    @GetMapping()
    public ResponseEntity<List<NecesidadMaterialDTO>> getAllNecesidades() {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.obtenerTodasLasNecesidades());
    }

    @GetMapping("/{productoID}")
        public ResponseEntity<List<NecesidadMaterialDTO>> getAllNecesidadesDeUnProducto(@PathVariable String productoID) {
            return ResponseEntity.status(HttpStatus.OK).body(this.fachada.obtenerNecesidadesInsatisfechasDe(productoID));
        }

    @DeleteMapping("/{necesidadID}")
    public ResponseEntity<Void> deleteEntidad(@PathVariable String necesidadID) {
        log.info("Se elimino la necesidad de material de ID: {}", necesidadID);
        this.fachada.quitarNecesidad(necesidadID);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{necesidadID}/satisfaccion")
    public ResponseEntity<NecesidadMaterialDTO> postSatisfacerNecesidad(@PathVariable String necesidadID, @RequestBody Map<String, Integer> requestBody) {
        var cantidad = requestBody.get("cantidad");
        log.info("Se satisface la necesidad de material de ID: {} con una cantidad de: {}", necesidadID, cantidad);
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.satisfacerNecesidad(necesidadID, cantidad));
    }

    @PatchMapping("/{necesidadID}/cantidad-objetivo")
    public ResponseEntity<NecesidadMaterialDTO> patchNecesidadMaterial(@PathVariable String necesidadID, @RequestBody Map<String,Integer> requestBody){
        var cantidadObjetivo = requestBody.get("cantidadObjetivo");
        log.info("Se modifoco la cantidad objetivo de la necesidad de material de ID: {} por la nueva cantidad objetivo de {}", necesidadID, cantidadObjetivo);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.fachada.modificarNecesidad(necesidadID, cantidadObjetivo));
    }
}
