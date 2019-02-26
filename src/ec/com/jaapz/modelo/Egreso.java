package ec.com.jaapz.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the egreso database table.
 * 
 */
@Entity
@Table(name="egreso")
@NamedQueries({
	@NamedQuery(name="Egreso.findAll", query="SELECT e FROM Egreso e WHERE e.estado = 'A'"),
	@NamedQuery(name="Egreso.buscarEgresos", query="SELECT e FROM Egreso e WHERE e.fecha between :fechaInicio and :fechaFin and "
			+ "e.estado = 'A' order by e.idEgreso desc")
	
})
public class Egreso implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_egreso")
	private Integer idEgreso;

	private String estado;
	
	private String observaciones;
	
	private String descripcion;
	
	private double monto;

	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	public Integer getIdEgreso() {
		return idEgreso;
	}

	public void setIdEgreso(Integer idEgreso) {
		this.idEgreso = idEgreso;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getUsuarioCrea() {
		return usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}