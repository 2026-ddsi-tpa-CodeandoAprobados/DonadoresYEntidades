package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "necesidad_material")
public class NecesidadMaterial {
    @Id
    private String id;
    @Column(name = "entidad_id")
    private String entidadID;
    @Column(name = "nivel_urgencia")
    private Integer nivelDeUrgencia;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "cantidad_objetivo")
    private Integer cantidadObjetivo;
    @Column(name = "producto_solicitado_id")
    private String productoSolicitadoID;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoNecesidadMaterialEnum tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "entidad_id", insertable = false, updatable = false)
    private EntidadBenefica entidadBenefica;

    @PrePersist
    public void generarIdAutomatico() {
        if (this.id == null) {
            this.id = com.aventrix.jnanoid.jnanoid.NanoIdUtils.randomNanoId();
        }
    }

    public NecesidadMaterial(String entidadID, Integer nivelDeUrgencia, String descripcion, Integer cantidadObjetivo, String productoSolicitadoID, TipoNecesidadMaterialEnum tipo) {
        this.entidadID = entidadID;
        this.nivelDeUrgencia = nivelDeUrgencia;
        this.descripcion = descripcion;
        this.cantidadObjetivo = cantidadObjetivo;
        this.productoSolicitadoID = productoSolicitadoID;
        this.tipo = tipo;
    }
}
