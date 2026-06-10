package ar.edu.utn.dds.k3003.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;


@RestControllerAdvice
public class ManejadorDeExcepciones {
        @ExceptionHandler(DonadorNoEncontradoException.class)
        public ResponseEntity<String> manejarDonadorNoEncontrado(DonadorNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        @ExceptionHandler(DonadorYaExistenteException.class)
        public ResponseEntity<String> manejarDonadorNoEncontrado(DonadorYaExistenteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        @ExceptionHandler(EntidadBeneficaNoEncontradaException.class)
        public ResponseEntity<String> manejarDonadorNoEncontrado(EntidadBeneficaNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        @ExceptionHandler(EntidadBeneficaYaExistenteException.class)
        public ResponseEntity<String> manejarDonadorNoEncontrado(EntidadBeneficaYaExistenteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
         @ExceptionHandler(NoSuchElementException.class)
         public ResponseEntity<String> NoSuchElementException(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> RuntimeException(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }