package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the estado_medidor database table.
 * 
 */
@Entity
@Table(name="estado_medidor")
@NamedQueries({
	@NamedQuery(name="EstadoMedidor.findAll", query="SELECT e FROM EstadoMedidor e where e.estado = 'A'"),
	@NamedQuery(name="EstadoMedidor.buscarBueno", query="SELECT e FROM EstadoMedidor e where e.estado = 'A' and e.idEstado = 1")
})
public class EstadoMedidor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_estado")
	private Integer idEstado;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Medidor
	@OneToMany(mappedBy="estadoMedidor", cascade = CascadeType.ALL)
	private List<Medidor> medidors;

	public EstadoMedidor() {
	}

	public Integer getIdEstado() {
		return this.idEstado;
	}

	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<Medidor> getMedidors() {
		return this.medidors;
	}

	public void setMedidors(List<Medidor> medidors) {
		this.medidors = medidors;
	}

	public Medidor addMedidor(Medidor medidor) {
		getMedidors().add(medidor);
		medidor.setEstadoMedidor(this);

		return medidor;
	}

	public Medidor removeMedidor(Medidor medidor) {
		getMedidors().remove(medidor);
		medidor.setEstadoMedidor(null);

		return medidor;
	}

	@Override
	public String toString() {
		return this.descripcion;
	}
}