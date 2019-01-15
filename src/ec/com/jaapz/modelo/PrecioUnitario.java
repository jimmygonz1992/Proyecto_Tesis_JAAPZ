package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the precio_unitario database table.
 * 
 */
@Entity
@Table(name="precio_unitario")
@NamedQuery(name="PrecioUnitario.findAll", query="SELECT p FROM PrecioUnitario p where p.estado = 'A' order by p.rubro.idRubro asc")
public class PrecioUnitario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_precio")
	private Integer idPrecio;

	private Integer cantidad;

	private String estado;

	private double total;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	//bi-directional many-to-one association to Rubro
	@ManyToOne
	@JoinColumn(name="id_rubro")
	private Rubro rubro;

	public PrecioUnitario() {
	}

	public Integer getIdPrecio() {
		return this.idPrecio;
	}

	public void setIdPrecio(Integer idPrecio) {
		this.idPrecio = idPrecio;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getTotal() {
		return this.total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Integer getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public Rubro getRubro() {
		return this.rubro;
	}

	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}

}