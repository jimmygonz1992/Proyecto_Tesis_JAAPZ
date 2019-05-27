package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the sol_inspeccion_ins database table.
 * 
 */
@Entity
@Table(name="sol_inspeccion_ins")
@NamedQueries({
	@NamedQuery(name="SolInspeccionIn.findAll", query="SELECT i FROM SolInspeccionIn i "
			+ "where (lower(i.cliente.apellido) like :patron or lower(i.cliente.nombre) like :patron or lower(i.cliente.cedula) like :patron) and i.estadoInspeccion = 'PENDIENTE' "
			+ "and i.estado = 'A' order by i.estadoInspeccion asc"),
	
	@NamedQuery(name="SolInspeccionIn.buscarInspeccionesTodas", query="SELECT i FROM SolInspeccionIn i "
			+ "where (lower(i.cliente.apellido) like lower(:patron)  or lower(i.cliente.nombre) like :patron) "
			+ "and i.estado = 'A' order by i.estadoInspeccion asc"),
	
	@NamedQuery(name="SolInspeccionIn.findAllPendiente", query="SELECT i FROM SolInspeccionIn i "
			+ "where (lower(i.cliente.apellido) like :patron or lower(i.cliente.nombre) like :patron) "
			+ "and i.estadoInspeccion = 'PENDIENTE' and i.idUsuEncargado = null and i.estado = 'A' order by i.idSolInspeccion desc"),
	
	@NamedQuery(name="SolInspeccionIn.buscarInspeccionPerfil", query="SELECT i FROM SolInspeccionIn i "
			+ "where (lower(i.cliente.apellido) like :patron or lower(i.cliente.nombre) like :patron or lower(i.cliente.cedula) like :patron) "
			+ " and i.idUsuEncargado = :idPerfilUsuario and i.estado = 'A' and i.estadoInspeccion = 'PENDIENTE' order by i.idSolInspeccion desc"),
	
	@NamedQuery(name="SolInspeccionIn.buscarInspeccionPerfilPendiente", query="SELECT i FROM SolInspeccionIn i "
			+ "where lower(i.cliente.apellido) like :patron or lower(i.cliente.nombre) like :patron "
			+ " and i.idUsuEncargado = :idPerfilUsuario and i.idUsuEncargado = null "
			+ " and i.estadoInspeccion = 'PENDIENTE' and i.estado = 'A' order by i.idSolInspeccion desc"),
	
	@NamedQuery(name="SolInspeccionIn.buscarInspeccionAsignada", query="SELECT i FROM SolInspeccionIn i "
			+ "where i.idUsuEncargado = :idPerfilUsuario and i.estado = 'A' order by i.estadoInspeccion,i.idSolInspeccion"),
	
	@NamedQuery(name="SolInspeccionIn.buscarInspeccionCliente", query="SELECT i FROM SolInspeccionIn i "
			+ "where i.cliente.idCliente = :idCliente and i.estado = 'A' order by i.idSolInspeccion desc"),
	
	@NamedQuery(name="SolInspeccionIn.buscarSolicitudesNoAtendidas", query="SELECT i FROM SolInspeccionIn i "
			+ "where i.estado = 'A' and i.estadoSolicitud = 'PENDIENTE' order by i.idSolInspeccion desc"),
	
	@NamedQuery(name="SolInspeccionIn.buscarNoAtendidas", query="SELECT i FROM SolInspeccionIn i "
			+ "where i.estado = 'A' and i.estadoInspeccion = 'PENDIENTE' and (lower(i.cliente.apellido) like lower(:patron)  or lower(i.cliente.nombre) like lower(:patron)) order by i.idSolInspeccion desc"),
	
	@NamedQuery(name="SolInspeccionIn.buscarNoFactibles", query="SELECT i FROM SolInspeccionIn i "
			+ "where i.estado = 'A' and i.estadoInspeccion = 'REALIZADO' and i.factibilidad = 'NO FACTIBLE' and (lower(i.cliente.apellido) like lower(:patron)  or lower(i.cliente.nombre) like lower(:patron)) order by i.idSolInspeccion")
})
public class SolInspeccionIn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_sol_inspeccion")
	private Integer idSolInspeccion;

	private String estado;

	@Column(name="estado_inspeccion")
	private String estadoInspeccion;

	private String factibilidad;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_aprobacion")
	private Date fechaAprobacion;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_ingreso")
	private Date fechaIngreso;

	@Column(name="hora_aprobacion")
	private Time horaAprobacion;

	@Column(name="hora_ingreso")
	private Time horaIngreso;

	@Column(name="id_usu_encargado")
	private Integer idUsuEncargado;

	@Column(name="estado_solicitud")
	private String estadoSolicitud;
	
	private String observacion;

	private String referencia;
	
	private String direccion;
	
	@Column(name="telefono_contacto")
	private String telefonoContacto;

	@Column(name="uso_medidor")
	private String usoMedidor;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	//bi-directional many-to-one association to Instalacion
	@OneToMany(mappedBy="solInspeccionIn", cascade = CascadeType.ALL)
	private List<Instalacion> instalacions;

	//bi-directional many-to-one association to LiquidacionOrden
	@OneToMany(mappedBy="solInspeccionIn", cascade = CascadeType.ALL)
	private List<LiquidacionOrden> liquidacionOrdens;

	//bi-directional many-to-one association to Barrio
	@ManyToOne
	@JoinColumn(name="id_barrio")
	private Barrio barrio;

	//bi-directional many-to-one association to Barrio
	@ManyToOne
	@JoinColumn(name="id_tipo_solicitud")
	private TipoSolicitud tipoSolicitud;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	public SolInspeccionIn() {
	}

	public Integer getIdSolInspeccion() {
		return this.idSolInspeccion;
	}

	public void setIdSolInspeccion(Integer idSolInspeccion) {
		this.idSolInspeccion = idSolInspeccion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoInspeccion() {
		return this.estadoInspeccion;
	}

	public void setEstadoInspeccion(String estadoInspeccion) {
		this.estadoInspeccion = estadoInspeccion;
	}

	public String getFactibilidad() {
		return this.factibilidad;
	}

	public void setFactibilidad(String factibilidad) {
		this.factibilidad = factibilidad;
	}

	public Date getFechaAprobacion() {
		return this.fechaAprobacion;
	}

	public void setFechaAprobacion(Date fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public Date getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Time getHoraAprobacion() {
		return this.horaAprobacion;
	}

	public void setHoraAprobacion(Time horaAprobacion) {
		this.horaAprobacion = horaAprobacion;
	}

	public Time getHoraIngreso() {
		return this.horaIngreso;
	}

	public void setHoraIngreso(Time horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	public Integer getIdUsuEncargado() {
		return this.idUsuEncargado;
	}

	public void setIdUsuEncargado(Integer idUsuEncargado) {
		this.idUsuEncargado = idUsuEncargado;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getUsoMedidor() {
		return this.usoMedidor;
	}

	public void setUsoMedidor(String usoMedidor) {
		this.usoMedidor = usoMedidor;
	}

	public Integer getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public List<Instalacion> getInstalacions() {
		return this.instalacions;
	}

	public void setInstalacions(List<Instalacion> instalacions) {
		this.instalacions = instalacions;
	}

	public Instalacion addInstalacion(Instalacion instalacion) {
		getInstalacions().add(instalacion);
		instalacion.setSolInspeccionIn(this);

		return instalacion;
	}

	public Instalacion removeInstalacion(Instalacion instalacion) {
		getInstalacions().remove(instalacion);
		instalacion.setSolInspeccionIn(null);

		return instalacion;
	}

	public List<LiquidacionOrden> getLiquidacionOrdens() {
		return this.liquidacionOrdens;
	}

	public void setLiquidacionOrdens(List<LiquidacionOrden> liquidacionOrdens) {
		this.liquidacionOrdens = liquidacionOrdens;
	}

	public LiquidacionOrden addLiquidacionOrden(LiquidacionOrden liquidacionOrden) {
		getLiquidacionOrdens().add(liquidacionOrden);
		liquidacionOrden.setSolInspeccionIn(this);

		return liquidacionOrden;
	}

	public LiquidacionOrden removeLiquidacionOrden(LiquidacionOrden liquidacionOrden) {
		getLiquidacionOrdens().remove(liquidacionOrden);
		liquidacionOrden.setSolInspeccionIn(null);

		return liquidacionOrden;
	}

	public Barrio getBarrio() {
		return this.barrio;
	}

	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TipoSolicitud getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefonoContacto() {
		return telefonoContacto;
	}

	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}

	public String getEstadoSolicitud() {
		return estadoSolicitud;
	}

	public void setEstadoSolicitud(String estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}

	@Override
	public String toString() {
		return "SolInspeccionIn [idSolInspeccion=" + idSolInspeccion + ", estado=" + estado + ", estadoInspeccion="
				+ estadoInspeccion + ", factibilidad=" + factibilidad + ", fechaAprobacion=" + fechaAprobacion
				+ ", fechaIngreso=" + fechaIngreso + ", horaAprobacion=" + horaAprobacion + ", horaIngreso="
				+ horaIngreso + ", idUsuEncargado=" + idUsuEncargado + ", estadoSolicitud=" + estadoSolicitud
				+ ", observacion=" + observacion + ", referencia=" + referencia + ", direccion=" + direccion
				+ ", telefonoContacto=" + telefonoContacto + ", usoMedidor=" + usoMedidor + ", usuarioCrea="
				+ usuarioCrea + ", instalacions=" + instalacions + ", liquidacionOrdens=" + liquidacionOrdens
				+ ", barrio=" + barrio + ", tipoSolicitud=" + tipoSolicitud + ", cliente=" + cliente + "]";
	}
	
}