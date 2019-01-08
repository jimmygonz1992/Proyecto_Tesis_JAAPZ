package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the factura database table.
 * 
 */
@Entity
@Table(name="Factura")
@NamedQueries({
	@NamedQuery(name="Factura.findAll", query="SELECT f FROM Factura f WHERE f.estado = 'A'"),
	@NamedQuery(name="Factura.buscarIDFact", query="SELECT f FROM Factura f WHERE f.estado = 'A' order by f.idFactura desc"),
	@NamedQuery(name="Factura.buscarFactCuenta", query="SELECT f FROM Factura f WHERE f.cuentaCliente.idCuenta = :idCuenta and "
			+ "f.estado = 'A' order by f.idFactura desc")
})
public class Factura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_factura")
	private Integer idFactura;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@Column(name="num_factura")
	private String numFactura;

	@Column(name="total_factura")
	private double totalFactura;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	//bi-directional many-to-one association to CuentaCliente
	@ManyToOne
	@JoinColumn(name="id_cuenta")
	private CuentaCliente cuentaCliente;

	//bi-directional many-to-one association to FacturaDetalle
	@OneToMany(mappedBy="factura", cascade = CascadeType.ALL)
	private List<FacturaDetalle> facturaDetalles;

	public Factura() {
	}

	public Integer getIdFactura() {
		return this.idFactura;
	}

	public void setIdFactura(Integer idFactura) {
		this.idFactura = idFactura;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getNumFactura() {
		return this.numFactura;
	}

	public void setNumFactura(String numFactura) {
		this.numFactura = numFactura;
	}

	public double getTotalFactura() {
		return this.totalFactura;
	}

	public void setTotalFactura(double totalFactura) {
		this.totalFactura = totalFactura;
	}

	public Integer getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public CuentaCliente getCuentaCliente() {
		return this.cuentaCliente;
	}

	public void setCuentaCliente(CuentaCliente cuentaCliente) {
		this.cuentaCliente = cuentaCliente;
	}

	public List<FacturaDetalle> getFacturaDetalles() {
		return this.facturaDetalles;
	}

	public void setFacturaDetalles(List<FacturaDetalle> facturaDetalles) {
		this.facturaDetalles = facturaDetalles;
	}

	public FacturaDetalle addFacturaDetalle(FacturaDetalle facturaDetalle) {
		getFacturaDetalles().add(facturaDetalle);
		facturaDetalle.setFactura(this);

		return facturaDetalle;
	}

	public FacturaDetalle removeFacturaDetalle(FacturaDetalle facturaDetalle) {
		getFacturaDetalles().remove(facturaDetalle);
		facturaDetalle.setFactura(null);

		return facturaDetalle;
	}

}