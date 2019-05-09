package ec.com.jaapz.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="corte")
@NamedQueries({
	@NamedQuery(name="Corte.findAll", query="SELECT c FROM Corte c")

})
public class Corte implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_corte")
	private Integer idCorte;

	private String observaciones;

	private String estado;
	
	//bi-directional many-to-one association to TipoRubro
	@ManyToOne
	@JoinColumn(name="id_cuenta")
	private CuentaCliente cuentaCliente;
	
	//bi-directional many-to-one association to TipoRubro
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private SegUsuario segUsuario;

	public Integer getIdCorte() {
		return idCorte;
	}

	public void setIdCorte(Integer idCorte) {
		this.idCorte = idCorte;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public CuentaCliente getCuentaCliente() {
		return cuentaCliente;
	}

	public void setCuentaCliente(CuentaCliente cuentaCliente) {
		this.cuentaCliente = cuentaCliente;
	}

	public SegUsuario getSegUsuario() {
		return segUsuario;
	}

	public void setSegUsuario(SegUsuario segUsuario) {
		this.segUsuario = segUsuario;
	}
	
	
}
