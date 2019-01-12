package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the reparacion database table.
 * 
 */
@Entity
@Table(name="Reparacion")
@NamedQueries({
	@NamedQuery(name="Reparacion.findAll", query="SELECT r FROM Reparacion r WHERE r.estado = 'A'"),
	@NamedQuery(name="Reparacion.buscarIDRepar", query="SELECT r FROM Reparacion r WHERE r.estado = 'A' order by r.idReparacion desc"),
	@NamedQuery(name="Reparacion.findAllReparaciones", query="SELECT r FROM Reparacion r "
			+ "where (lower(r.cuentaCliente.cliente.apellido) like :patron or lower(r.cuentaCliente.cliente.nombre) like :patron "
			+ "or lower(r.cuentaCliente.cliente.cedula) like :patron) and r.estadoReparacion = 'PENDIENTE' "
			+ "order by r.idReparacion asc"),
	@NamedQuery(name="Reparacion.buscarReparacionPerfilReparaciones", query="SELECT r FROM Reparacion r "
			+ "where (lower(r.cuentaCliente.cliente.apellido) like :patron or lower(r.cuentaCliente.cliente.nombre) like :patron "
			+ "or lower(r.cuentaCliente.cliente.cedula) like :patron) and r.estadoReparacion = 'PENDIENTE'"
			+ "and r.usuarioCrea = :idPerfilUsuario order by r.idReparacion asc"),
	@NamedQuery(name="Reparacion.findAllReparacionesListadoSalida", query="SELECT r FROM Reparacion r "
			+ "where (lower(r.cuentaCliente.cliente.apellido) like :patron or lower(r.cuentaCliente.cliente.nombre) like :patron "
			+ "or lower(r.cuentaCliente.cliente.cedula) like :patron) and r.estadoEntrega = 'PENDIENTE' "
			+ "order by r.idReparacion asc"),
	@NamedQuery(name="Reparacion.buscarReparacionListadoSalidaPerfilReparaciones", query="SELECT r FROM Reparacion r "
			+ "where (lower(r.cuentaCliente.cliente.apellido) like :patron or lower(r.cuentaCliente.cliente.nombre) like :patron "
			+ "or lower(r.cuentaCliente.cliente.cedula) like :patron) and r.estadoEntrega = 'PENDIENTE'"
			+ "and r.usuarioCrea = :idPerfilUsuario order by r.idReparacion asc")
})
public class Reparacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_reparacion")
	private Integer idReparacion;

	private double descuento;

	private String estado;

	@Column(name="estado_entrega")
	private String estadoEntrega;
	
	@Column(name="estado_reparacion")
	private String estadoReparacion;

	@Column(name="estado_valor")
	private String estadoValor;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_reparacion")
	private Date fechaReparacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_salida")
	private Date fechaSalida;

	@Column(name="foto_predio")
	private byte[] fotoPredio;

	@Column(name="hora_reparacion")
	private Time horaReparacion;

	private String observcion;
	
	private String referencia;

	private double subtotal;

	private double total;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;
	
	@Column(name="usuario_reparacion")
	private Integer usuarioReparacion;

	//bi-directional many-to-one association to PlanillaDetalle
	@OneToMany(mappedBy="reparacion", cascade = CascadeType.ALL)
	private List<PlanillaDetalle> planillaDetalles;

	//bi-directional many-to-one association to CuentaCliente
	@ManyToOne
	@JoinColumn(name="id_cuenta")
	private CuentaCliente cuentaCliente;

	//bi-directional many-to-one association to SolInspeccionRep
	@ManyToOne
	@JoinColumn(name="id_solicitud_rep")
	private SolInspeccionRep solInspeccionRep;

	//bi-directional many-to-one association to ReparacionDetalle
	@OneToMany(mappedBy="reparacion", cascade = CascadeType.ALL)
	private List<ReparacionDetalle> reparacionDetalles;

	public Reparacion() {
	}

	public Integer getIdReparacion() {
		return this.idReparacion;
	}

	public void setIdReparacion(Integer idReparacion) {
		this.idReparacion = idReparacion;
	}

	public double getDescuento() {
		return this.descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public String getEstadoReparacion() {
		return estadoReparacion;
	}

	public void setEstadoReparacion(String estadoReparacion) {
		this.estadoReparacion = estadoReparacion;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Integer getUsuarioReparacion() {
		return usuarioReparacion;
	}

	public void setUsuarioReparacion(Integer usuarioReparacion) {
		this.usuarioReparacion = usuarioReparacion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoEntrega() {
		return this.estadoEntrega;
	}

	public void setEstadoEntrega(String estadoEntrega) {
		this.estadoEntrega = estadoEntrega;
	}

	public String getEstadoValor() {
		return this.estadoValor;
	}

	public void setEstadoValor(String estadoValor) {
		this.estadoValor = estadoValor;
	}

	public Date getFechaReparacion() {
		return this.fechaReparacion;
	}

	public void setFechaReparacion(Date fechaReparacion) {
		this.fechaReparacion = fechaReparacion;
	}

	public byte[] getFotoPredio() {
		return this.fotoPredio;
	}

	public void setFotoPredio(byte[] fotoPredio) {
		this.fotoPredio = fotoPredio;
	}

	public Time getHoraReparacion() {
		return this.horaReparacion;
	}

	public void setHoraReparacion(Time horaReparacion) {
		this.horaReparacion = horaReparacion;
	}

	public String getObservcion() {
		return this.observcion;
	}

	public void setObservcion(String observcion) {
		this.observcion = observcion;
	}

	public double getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
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

	public List<PlanillaDetalle> getPlanillaDetalles() {
		return this.planillaDetalles;
	}

	public void setPlanillaDetalles(List<PlanillaDetalle> planillaDetalles) {
		this.planillaDetalles = planillaDetalles;
	}

	public PlanillaDetalle addPlanillaDetalle(PlanillaDetalle planillaDetalle) {
		getPlanillaDetalles().add(planillaDetalle);
		planillaDetalle.setReparacion(this);

		return planillaDetalle;
	}

	public PlanillaDetalle removePlanillaDetalle(PlanillaDetalle planillaDetalle) {
		getPlanillaDetalles().remove(planillaDetalle);
		planillaDetalle.setReparacion(null);

		return planillaDetalle;
	}

	public CuentaCliente getCuentaCliente() {
		return this.cuentaCliente;
	}

	public void setCuentaCliente(CuentaCliente cuentaCliente) {
		this.cuentaCliente = cuentaCliente;
	}

	public SolInspeccionRep getSolInspeccionRep() {
		return this.solInspeccionRep;
	}

	public void setSolInspeccionRep(SolInspeccionRep solInspeccionRep) {
		this.solInspeccionRep = solInspeccionRep;
	}

	public List<ReparacionDetalle> getReparacionDetalles() {
		return this.reparacionDetalles;
	}

	public void setReparacionDetalles(List<ReparacionDetalle> reparacionDetalles) {
		this.reparacionDetalles = reparacionDetalles;
	}

	public ReparacionDetalle addReparacionDetalle(ReparacionDetalle reparacionDetalle) {
		getReparacionDetalles().add(reparacionDetalle);
		reparacionDetalle.setReparacion(this);

		return reparacionDetalle;
	}

	public ReparacionDetalle removeReparacionDetalle(ReparacionDetalle reparacionDetalle) {
		getReparacionDetalles().remove(reparacionDetalle);
		reparacionDetalle.setReparacion(null);

		return reparacionDetalle;
	}

}