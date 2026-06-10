package ar.edu.utn.dds.k3003.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "queja")
public class Queja{
    @Id
    private String id;
    @Column(name = "donacion_id")
    private String donacionID;
    @Column(name = "donador_id")
    private String donadorID;
    @Column(name = "fecha")
    private LocalDate fecha;
    @Column(name = "descripcion")
    private String descripcion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "donador_id", insertable = false, updatable = false)
    private Donador donador;

    @PrePersist
    public void generarIdAutomatico() {
        if (this.id == null) {
            this.id = com.aventrix.jnanoid.jnanoid.NanoIdUtils.randomNanoId();
        }
    }

    public Queja(String donacionID, String donadorID, LocalDate fecha, String descripcion) {
        this.donacionID = donacionID;
        this.donadorID = donadorID;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }
}
