package ec.com.jaapz.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="kardex")
@NamedQuery(name="Kardex.findKardex", query="SELECT k FROM Kardex k where  k.rubro.idRubro = :idRubro "
		+ "and k.numDocumento = :documento")

public class Kardex implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_kardex")
	private Integer idKardex;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name="tipo_documento")
	private String tipoDocumento;

	@Column(name="num_documento")
	private String numDocumento;

	@Column(name="detalle_operacion")
	private String detalleOperacion;

	private double cantidad;

	@Column(name="unidad_medida")
	private String unidadMedida;

	@Column(name="valor_unitario")
	private double valorUnitario;

	@Column(name="costo_total")
	private double costoTotal;

	@Column(name="tipo_movimiento")
	private String tipoMovimiento;

	private String estado;

	//bi-directional many-to-one association to TipoRubro
	@ManyToOne
	@JoinColumn(name="id_rubro")
	private Rubro rubro;

	public Integer getIdKardex() {
		return idKardex;
	}

	public void setIdKardex(Integer idKardex) {
		this.idKardex = idKardex;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public String getDetalleOperacion() {
		return detalleOperacion;
	}

	public void setDetalleOperacion(String detalleOperacion) {
		this.detalleOperacion = detalleOperacion;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public double getCostoTotal() {
		return costoTotal;
	}

	public void setCostoTotal(double costoTotal) {
		this.costoTotal = costoTotal;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Rubro getRubro() {
		return rubro;
	}

	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}
}
