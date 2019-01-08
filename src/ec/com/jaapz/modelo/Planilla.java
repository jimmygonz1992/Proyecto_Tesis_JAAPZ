package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the planilla database table.
 * 
 */
@Entity
@Table(name="planilla")
@NamedQueries({
	@NamedQuery(name="Planilla.findAll", query="SELECT p FROM Planilla p "
			+ "where (lower(p.cuentaCliente.cliente.apellido) like :patron or lower(p.cuentaCliente.cliente.nombre) like :patron or lower(p.cuentaCliente.cliente.cedula) like :patron) and p.cancelado = 'PENDIENTE' "
			+ "order by p.idPlanilla asc"),
	@NamedQuery(name="Planilla.buscarPlanillaPerfil", query="SELECT p FROM Planilla p "
			+ "where (lower(p.cuentaCliente.cliente.apellido) like :patron or lower(p.cuentaCliente.cliente.nombre) like :patron or lower(p.cuentaCliente.cliente.cedula) like :patron) and p.cancelado = 'PENDIENTE' "
			+ "and p.usuarioCrea = :idPerfilUsuario order by p.idPlanilla asc"),
	@NamedQuery(name="Planilla.buscarPorCuenta", query="SELECT p FROM Planilla p "
			+ "where p.cuentaCliente.idCuenta = :idCuenta and p.estado = 'A' "
			+ "order by p.idPlanilla desc"),
	@NamedQuery(name="Planilla.buscarPorCuentaPlanilla", query="SELECT p FROM Planilla p "
			+ "where p.cuentaCliente.idCuenta = :idCuenta and p.idPlanilla < (:idPlanillaActual) and p.estado = 'A' "
			+ "order by p.idPlanilla asc")
})
public class Planilla implements Serializable, Comparable<Planilla> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_planilla")
	private Integer idPlanilla;

	private String cancelado;

	private Integer consumo;

	@Column(name="consumo_minimo")
	private Integer consumoMinimo;

	private String convenio;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Time hora;

	@Column(name="ident_instalacion")
	private String identInstalacion;

	private String latitud;

	@Column(name="lectura_actual")
	private Integer lecturaActual;

	@Column(name="lectura_anterior")
	private Integer lecturaAnterior;

	private String longitud;

	@Column(name="total_letras")
	private String totalLetras;

	@Column(name="total_pagar")
	private double totalPagar;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	@Column(nullable = false, columnDefinition = "bit")
	private Boolean imprime;

	@Column(nullable = false, columnDefinition = "bit")
	private Boolean envia;

	//bi-directional many-to-one association to ConvenioPlanilla
	@OneToMany(mappedBy="planilla", cascade = CascadeType.ALL)
	private List<ConvenioPlanilla> convenioPlanillas;

	//bi-directional many-to-one association to FacturaDetalle
	@OneToMany(mappedBy="planilla", cascade = CascadeType.ALL)
	private List<FacturaDetalle> facturaDetalles;

	//bi-directional many-to-one association to Pago
	@OneToMany(mappedBy="planilla", cascade = CascadeType.ALL)
	private List<Pago> pagos;

	//bi-directional many-to-one association to AperturaLectura
	@ManyToOne
	@JoinColumn(name="id_apertura")
	private AperturaLectura aperturaLectura;

	//bi-directional many-to-one association to CuentaCliente
	@ManyToOne
	@JoinColumn(name="id_cuenta")
	private CuentaCliente cuentaCliente;

	//bi-directional many-to-one association to PlanillaDetalle
	@OneToMany(mappedBy="planilla", cascade = CascadeType.ALL)
	private List<PlanillaDetalle> planillaDetalles;

	public Planilla() {
	}

	public Integer getIdPlanilla() {
		return this.idPlanilla;
	}

	public void setIdPlanilla(Integer idPlanilla) {
		this.idPlanilla = idPlanilla;
	}

	public String getCancelado() {
		return this.cancelado;
	}

	public void setCancelado(String cancelado) {
		this.cancelado = cancelado;
	}

	public Integer getConsumo() {
		return this.consumo;
	}

	public void setConsumo(Integer consumo) {
		this.consumo = consumo;
	}

	public Integer getConsumoMinimo() {
		return this.consumoMinimo;
	}

	public void setConsumoMinimo(Integer consumoMinimo) {
		this.consumoMinimo = consumoMinimo;
	}

	public String getConvenio() {
		return this.convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
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

	public Time getHora() {
		return this.hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public String getIdentInstalacion() {
		return this.identInstalacion;
	}

	public void setIdentInstalacion(String identInstalacion) {
		this.identInstalacion = identInstalacion;
	}

	public String getLatitud() {
		return this.latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public Integer getLecturaActual() {
		return this.lecturaActual;
	}

	public void setLecturaActual(Integer lecturaActual) {
		this.lecturaActual = lecturaActual;
	}

	public Integer getLecturaAnterior() {
		return this.lecturaAnterior;
	}

	public void setLecturaAnterior(Integer lecturaAnterior) {
		this.lecturaAnterior = lecturaAnterior;
	}

	public String getLongitud() {
		return this.longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public String getTotalLetras() {
		return this.totalLetras;
	}

	public void setTotalLetras(String totalLetras) {
		this.totalLetras = totalLetras;
	}

	public double getTotalPagar() {
		return this.totalPagar;
	}

	public void setTotalPagar(double totalPagar) {
		this.totalPagar = totalPagar;
	}

	public Integer getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public List<ConvenioPlanilla> getConvenioPlanillas() {
		return this.convenioPlanillas;
	}

	public void setConvenioPlanillas(List<ConvenioPlanilla> convenioPlanillas) {
		this.convenioPlanillas = convenioPlanillas;
	}

	public ConvenioPlanilla addConvenioPlanilla(ConvenioPlanilla convenioPlanilla) {
		getConvenioPlanillas().add(convenioPlanilla);
		convenioPlanilla.setPlanilla(this);

		return convenioPlanilla;
	}

	public ConvenioPlanilla removeConvenioPlanilla(ConvenioPlanilla convenioPlanilla) {
		getConvenioPlanillas().remove(convenioPlanilla);
		convenioPlanilla.setPlanilla(null);

		return convenioPlanilla;
	}

	public List<FacturaDetalle> getFacturaDetalles() {
		return this.facturaDetalles;
	}

	public void setFacturaDetalles(List<FacturaDetalle> facturaDetalles) {
		this.facturaDetalles = facturaDetalles;
	}

	public FacturaDetalle addFacturaDetalle(FacturaDetalle facturaDetalle) {
		getFacturaDetalles().add(facturaDetalle);
		facturaDetalle.setPlanilla(this);

		return facturaDetalle;
	}

	public FacturaDetalle removeFacturaDetalle(FacturaDetalle facturaDetalle) {
		getFacturaDetalles().remove(facturaDetalle);
		facturaDetalle.setPlanilla(null);

		return facturaDetalle;
	}

	public List<Pago> getPagos() {
		return this.pagos;
	}

	public void setPagos(List<Pago> pagos) {
		this.pagos = pagos;
	}

	public Pago addPago(Pago pago) {
		getPagos().add(pago);
		pago.setPlanilla(this);

		return pago;
	}

	public Pago removePago(Pago pago) {
		getPagos().remove(pago);
		pago.setPlanilla(null);

		return pago;
	}

	public AperturaLectura getAperturaLectura() {
		return this.aperturaLectura;
	}

	public void setAperturaLectura(AperturaLectura aperturaLectura) {
		this.aperturaLectura = aperturaLectura;
	}

	public CuentaCliente getCuentaCliente() {
		return this.cuentaCliente;
	}

	public void setCuentaCliente(CuentaCliente cuentaCliente) {
		this.cuentaCliente = cuentaCliente;
	}

	public List<PlanillaDetalle> getPlanillaDetalles() {
		return this.planillaDetalles;
	}

	public void setPlanillaDetalles(List<PlanillaDetalle> planillaDetalles) {
		this.planillaDetalles = planillaDetalles;
	}

	public PlanillaDetalle addPlanillaDetalle(PlanillaDetalle planillaDetalle) {
		getPlanillaDetalles().add(planillaDetalle);
		planillaDetalle.setPlanilla(this);

		return planillaDetalle;
	}

	public PlanillaDetalle removePlanillaDetalle(PlanillaDetalle planillaDetalle) {
		getPlanillaDetalles().remove(planillaDetalle);
		planillaDetalle.setPlanilla(null);

		return planillaDetalle;
	}
	public Boolean getImprime() {
		return imprime;
	}

	public void setImprime(Boolean imprime) {
		this.imprime = imprime;
	}

	public Boolean getEnvia() {
		return envia;
	}

	public void setEnvia(Boolean envia) {
		this.envia = envia;
	}
	@Override
	public int compareTo(Planilla o) {
		if (this.idPlanilla < o.idPlanilla) {
			return -1;
		}
		if (this.idPlanilla  > o.idPlanilla) {
			return 1;
		}
		return 0;
	}
}