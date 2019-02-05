package ec.com.jaapz.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the convenio database table.
 * 
 */
@Entity
@NamedQuery(name="Convenio.findAll", query="SELECT c FROM Convenio c")
public class Convenio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_convenio")
	private Integer idConvenio;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;


	@Column(name="num_letras")
	private Integer numLetras;

	private double total;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	//bi-directional many-to-one association to CuentaCliente
	@ManyToOne
	@JoinColumn(name="id_cuenta")
	private CuentaCliente cuentaCliente;

	//bi-directional many-to-one association to ConvenioDetalle
	@OneToMany(mappedBy="convenio", cascade = CascadeType.ALL)
	private List<ConvenioDetalle> convenioDetalles;

	//bi-directional many-to-one association to ConvenioPlanilla
	@OneToMany(mappedBy="convenio", cascade = CascadeType.ALL)
	private List<ConvenioPlanilla> convenioPlanillas;

	public Convenio() {
	}

	public Integer getIdConvenio() {
		return this.idConvenio;
	}

	public void setIdConvenio(Integer idConvenio) {
		this.idConvenio = idConvenio;
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

	public Integer getNumLetras() {
		return this.numLetras;
	}

	public void setNumLetras(Integer numLetras) {
		this.numLetras = numLetras;
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

	public CuentaCliente getCuentaCliente() {
		return this.cuentaCliente;
	}

	public void setCuentaCliente(CuentaCliente cuentaCliente) {
		this.cuentaCliente = cuentaCliente;
	}

	public List<ConvenioDetalle> getConvenioDetalles() {
		return this.convenioDetalles;
	}

	public void setConvenioDetalles(List<ConvenioDetalle> convenioDetalles) {
		this.convenioDetalles = convenioDetalles;
	}

	public ConvenioDetalle addConvenioDetalle(ConvenioDetalle convenioDetalle) {
		getConvenioDetalles().add(convenioDetalle);
		convenioDetalle.setConvenio(this);

		return convenioDetalle;
	}

	public ConvenioDetalle removeConvenioDetalle(ConvenioDetalle convenioDetalle) {
		getConvenioDetalles().remove(convenioDetalle);
		convenioDetalle.setConvenio(null);

		return convenioDetalle;
	}

	public List<ConvenioPlanilla> getConvenioPlanillas() {
		return this.convenioPlanillas;
	}

	public void setConvenioPlanillas(List<ConvenioPlanilla> convenioPlanillas) {
		this.convenioPlanillas = convenioPlanillas;
	}

	public ConvenioPlanilla addConvenioPlanilla(ConvenioPlanilla convenioPlanilla) {
		getConvenioPlanillas().add(convenioPlanilla);
		convenioPlanilla.setConvenio(this);

		return convenioPlanilla;
	}

	public ConvenioPlanilla removeConvenioPlanilla(ConvenioPlanilla convenioPlanilla) {
		getConvenioPlanillas().remove(convenioPlanilla);
		convenioPlanilla.setConvenio(null);

		return convenioPlanilla;
	}

}