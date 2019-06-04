package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the liquidacion_detalle database table.
 * 
 */
@Entity
@Table(name="liquidacion_detalle")
@NamedQuery(name="LiquidacionDetalle.findAll", query="SELECT l FROM LiquidacionDetalle l")
public class LiquidacionDetalle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_detalle")
	private Integer idDetalle;

	private Integer cantidad;

	private String estado;
	
	private double precio;

	private double total;

	//bi-directional many-to-one association to LiquidacionOrden
	@ManyToOne
	@JoinColumn(name="id_liquidacion")
	private LiquidacionOrden liquidacionOrden;

	//bi-directional many-to-one association to Rubro
	@ManyToOne
	@JoinColumn(name="id_rubro")
	private Rubro rubro;

	public LiquidacionDetalle() {
	}

	public Integer getIdDetalle() {
		return this.idDetalle;
	}

	public void setIdDetalle(Integer idDetalle) {
		this.idDetalle = idDetalle;
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

	public double getPrecio() {
		return this.precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getTotal() {
		return this.total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public LiquidacionOrden getLiquidacionOrden() {
		return this.liquidacionOrden;
	}

	public void setLiquidacionOrden(LiquidacionOrden liquidacionOrden) {
		this.liquidacionOrden = liquidacionOrden;
	}

	public Rubro getRubro() {
		return this.rubro;
	}

	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}

}