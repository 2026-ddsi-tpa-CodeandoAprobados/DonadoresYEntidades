package ar.edu.utn.dds.k3003.controllers;
import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/quejas")
public class QuejasController {
    private Fachada fachada;
    private MeterRegistry meterRegistry;

    public QuejasController(Fachada fachada, MeterRegistry meterRegistry) {
        this.fachada = fachada;
        this.meterRegistry = meterRegistry;
    }
    @PostMapping
    public ResponseEntity<QuejaDTO> agregarQueja(@RequestBody QuejaDTO quejaDTO) {
        meterRegistry.counter("api.donadores.quejas.registrada", "origen", "http").increment();
        return ResponseEntity.status(HttpStatus.CREATED).body(this.fachada.agregarQueja(quejaDTO));
    }
    @GetMapping
    public ResponseEntity<List<QuejaDTO>> getAllQuejasDeUnDonador(@PathVariable String donadorID) {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.obtenerQuejasDe(donadorID));
    }
}
