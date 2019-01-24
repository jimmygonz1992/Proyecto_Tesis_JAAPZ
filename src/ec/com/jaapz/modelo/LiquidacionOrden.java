package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the liquidacion_orden database table.
 * 
 */
@Entity
@Table(name="liquidacion_orden")
@NamedQueries({
	@NamedQuery(name="LiquidacionOrden.findAll", query="SELECT l FROM LiquidacionOrden l "
			+ "where (lower(l.cuentaCliente.cliente.apellido) like :patron or lower(l.cuentaCliente.cliente.nombre) like :patron "
			+ "or lower(l.cuentaCliente.cliente.cedula) like :patron) and l.solInspeccionIn.estadoInspeccion = 'REALIZADO' and l.estadoOrden = 'PENDIENTE'"
			+ "order by l.idLiquidacion asc"),
	
	@NamedQuery(name="LiquidacionOrden.buscarLiquidacionOrdenPerfil", query="SELECT l FROM LiquidacionOrden l "
			+ "where (lower(l.cuentaCliente.cliente.apellido) like :patron or lower(l.cuentaCliente.cliente.nombre) like :patron "
			+ "or lower(l.cuentaCliente.cliente.cedula) like :patron) and l.solInspeccionIn.estadoInspeccion = 'REALIZADO' and l.estadoOrden = 'PENDIENTE'"
			+ "and l.usuarioCrea = :idPerfilUsuario order by l.idLiquidacion asc"),
	
	@NamedQuery(name="LiquidacionOrden.findAllInstalaciones", query="SELECT l FROM LiquidacionOrden l "
			+ "where (lower(l.cuentaCliente.cliente.apellido) like :patron or lower(l.cuentaCliente.cliente.nombre) like :patron "
			+ "or lower(l.cuentaCliente.cliente.cedula) like :patron) and l.estadoInstalacion = 'PENDIENTE' and l.estado = 'A' "
			+ "order by l.estadoInstalacion asc"),
	
	@NamedQuery(name="LiquidacionOrden.buscarLiquidacionOrdenPerfilInstalaciones", query="SELECT l FROM LiquidacionOrden l "
			+ "where (lower(l.cuentaCliente.cliente.apellido) like :patron or lower(l.cuentaCliente.cliente.nombre) like :patron "
			+ "or lower(l.cuentaCliente.cliente.cedula) like :patron) and l.estadoInstalacion = 'PENDIENTE' "
			+ "and l.usuarioInstalacion = :idPerfilUsuario and l.estado = 'A' order by l.idLiquidacion desc"),
	
	//para agregar mas materiales solo cambia el estado
	@NamedQuery(name="LiquidacionOrden.findAllEmitida", query="SELECT l FROM LiquidacionOrden l "
			+ "where (lower(l.cuentaCliente.cliente.apellido) like :patron or lower(l.cuentaCliente.cliente.nombre) like :patron "
			+ "or lower(l.cuentaCliente.cliente.cedula) like :patron) and l.solInspeccionIn.estadoInspeccion = 'REALIZADO' and l.estadoOrden = 'REALIZADO'"
			+ "order by l.idLiquidacion asc"),
	
	@NamedQuery(name="LiquidacionOrden.buscarLiqOrdenPerfilEmitida", query="SELECT l FROM LiquidacionOrden l "
			+ "where (lower(l.cuentaCliente.cliente.apellido) like :patron or lower(l.cuentaCliente.cliente.nombre) like :patron "
			+ "or lower(l.cuentaCliente.cliente.cedula) like :patron) and l.solInspeccionIn.estadoInspeccion = 'REALIZADO' and l.estadoOrden = 'REALIZADO'"
			+ "and l.usuarioCrea = :idPerfilUsuario order by l.idLiquidacion asc"),
	
	//esta consulta es provisional para ver si sale editar una orden de liquidacion
	@NamedQuery(name="LiquidacionOrden.recuperaLiquidacionEmitida", query="SELECT l FROM LiquidacionOrden l WHERE (l.idLiquidacion = (:idLiquidacion) and l.estado = 'A')"),
	
	@NamedQuery(name="LiquidacionOrden.findAllPendiente", query="SELECT l FROM LiquidacionOrden l "
			+ "where (lower(l.cuentaCliente.cliente.apellido) like :patron  or lower(l.cuentaCliente.cliente.nombre) like :patron) "
			+ "and (l.estadoInstalacion = 'PENDIENTE' and l.usuarioInstalacion = null and l.estado = 'A') order by l.idLiquidacion desc"),
	
	@NamedQuery(name="LiquidacionOrden.buscarAsignacionLiquidacionPerfilPendiente", query="SELECT l FROM LiquidacionOrden l "
			+ "where lower(l.cuentaCliente.cliente.apellido) like :patron  or lower(l.cuentaCliente.cliente.nombre) like :patron "
			+ " and l.usuarioInstalacion = :idPerfilUsuario and l.usuarioInstalacion = null "
			+ " and l.estadoInstalacion = 'PENDIENTE' and l.estado = 'A' order by l.idLiquidacion desc"),
	
	//para las asignaciones
	/*@NamedQuery(name="LiquidacionOrden.findAllPendiente", query="SELECT l FROM LiquidacionOrden l "
			+ "where (lower(l.cuentaCliente.cliente.apellido) like :patron  or lower(l.cuentaCliente.cliente.nombre) like :patron) "
			+ "and l.estadoInstalacion = 'PENDIENTE' and l.usuarioInstalacion = null and l.estado = 'A' order by l.idLiquidacion desc"),*/
	
	/*@NamedQuery(name="LiquidacionOrden.buscarAsignacionPerfilPendiente", query="SELECT l FROM LiquidacionOrden l "
			+ "where (lower(l.cuentaCliente.cliente.apellido) like :patron  or lower(l.cuentaCliente.cliente.nombre) like :patron) "
			+ " and l.usuarioInstalacion = :idPerfilUsuario and l.usuarioInstalacion = null"
			+ " and l.estadoInstalacion = 'PENDIENTE' and l.estado = 'A' order by l.idLiquidacion desc"),*/
	
	@NamedQuery(name="LiquidacionOrden.buscarLiquidacionAsignada", query="SELECT l FROM LiquidacionOrden l "
			+ "where l.usuarioInstalacion = :idPerfilUsuario and l.estado = 'A' order by l.idLiquidacion desc"),
	
	//para asignar trabajos de instalacion
	@NamedQuery(name="LiquidacionOrden.buscarListaLiquidacion", query="SELECT l FROM LiquidacionOrden l "
			+ "where (lower(l.solInspeccionIn.cliente.apellido) like :patron  or lower(l.solInspeccionIn.cliente.nombre) like :patron) "
			+ "and l.estadoInstalacion = 'PENDIENTE' and l.usuarioInstalacion = null and l.estado = 'A' order by l.idLiquidacion desc"),

	//para asignar trabajos de instalacion
	@NamedQuery(name="LiquidacionOrden.buscarListaLiquidacionPerfil", query="SELECT l FROM LiquidacionOrden l "
			+ "where lower(l.solInspeccionIn.cliente.apellido) like :patron  or lower(l.solInspeccionIn.cliente.nombre) like :patron "
			+ " and l.usuarioInstalacion = :idPerfilUsuario and l.usuarioInstalacion = null "
			+ " and l.estadoInstalacion = 'PENDIENTE' and l.estado = 'A' order by l.idLiquidacion desc"),
})
public class LiquidacionOrden implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_liquidacion")
	private Integer idLiquidacion;

	private String estado;

	@Column(name="estado_orden")
	private String estadoOrden;

	@Column(name="estado_valor")
	private String estadoValor;
	
	@Column(name="estado_instalacion")
	private String estadoInstalacion;

	@Temporal(TemporalType.DATE)
	private Date fecha;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_instalacion")
	private Date fechaInstalacion;
	
	@Column(name="hora_instalacion")
	private Time horaInstalacion;

	@Column(name="foto_predio")
	private byte[] fotoPredio;

	private Time hora;

	private double total;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;
	
	@Column(name="usuario_instalacion")
	private Integer usuarioInstalacion;

	@Column(name="valor_pendiente")
	private double valorPendiente;

	//bi-directional many-to-one association to LiquidacionDetalle
	@OneToMany(mappedBy="liquidacionOrden", cascade = CascadeType.ALL)
	private List<LiquidacionDetalle> liquidacionDetalles;

	//bi-directional many-to-one association to CuentaCliente
	@ManyToOne
	@JoinColumn(name="id_cuenta")
	private CuentaCliente cuentaCliente;

	//bi-directional many-to-one association to Medidor
	@ManyToOne
	@JoinColumn(name="id_medidor")
	private Medidor medidor;

	//bi-directional many-to-one association to SolInspeccionIn
	@ManyToOne
	@JoinColumn(name="id_sol_inspeccion")
	private SolInspeccionIn solInspeccionIn;

	public LiquidacionOrden() {
	}

	public Integer getIdLiquidacion() {
		return this.idLiquidacion;
	}

	public void setIdLiquidacion(Integer idLiquidacion) {
		this.idLiquidacion = idLiquidacion;
	}

	public String getEstado() {
		return this.estado;
	}

	public Date getFechaInstalacion() {
		return fechaInstalacion;
	}

	public void setFechaInstalacion(Date fechaInstalacion) {
		this.fechaInstalacion = fechaInstalacion;
	}

	public Time getHoraInstalacion() {
		return horaInstalacion;
	}

	public void setHoraInstalacion(Time horaInstalacion) {
		this.horaInstalacion = horaInstalacion;
	}

	public Integer getUsuarioInstalacion() {
		return usuarioInstalacion;
	}

	public void setUsuarioInstalacion(Integer usuarioInstalacion) {
		this.usuarioInstalacion = usuarioInstalacion;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoOrden() {
		return this.estadoOrden;
	}

	public void setEstadoOrden(String estadoOrden) {
		this.estadoOrden = estadoOrden;
	}
	
	public String getEstadoInstalacion() {
		return this.estadoInstalacion;
	}

	public void setEstadoInstalacion(String estadoInstalacion) {
		this.estadoInstalacion = estadoInstalacion;
	}

	public String getEstadoValor() {
		return this.estadoValor;
	}

	public void setEstadoValor(String estadoValor) {
		this.estadoValor = estadoValor;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public byte[] getFotoPredio() {
		return this.fotoPredio;
	}

	public void setFotoPredio(byte[] fotoPredio) {
		this.fotoPredio = fotoPredio;
	}

	public Time getHora() {
		return this.hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
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

	public double getValorPendiente() {
		return this.valorPendiente;
	}

	public void setValorPendiente(double valorPendiente) {
		this.valorPendiente = valorPendiente;
	}

	public List<LiquidacionDetalle> getLiquidacionDetalles() {
		return this.liquidacionDetalles;
	}

	public void setLiquidacionDetalles(List<LiquidacionDetalle> liquidacionDetalles) {
		this.liquidacionDetalles = liquidacionDetalles;
	}

	public LiquidacionDetalle addLiquidacionDetalle(LiquidacionDetalle liquidacionDetalle) {
		getLiquidacionDetalles().add(liquidacionDetalle);
		liquidacionDetalle.setLiquidacionOrden(this);

		return liquidacionDetalle;
	}

	public LiquidacionDetalle removeLiquidacionDetalle(LiquidacionDetalle liquidacionDetalle) {
		getLiquidacionDetalles().remove(liquidacionDetalle);
		liquidacionDetalle.setLiquidacionOrden(null);

		return liquidacionDetalle;
	}

	public CuentaCliente getCuentaCliente() {
		return this.cuentaCliente;
	}

	public void setCuentaCliente(CuentaCliente cuentaCliente) {
		this.cuentaCliente = cuentaCliente;
	}

	public Medidor getMedidor() {
		return this.medidor;
	}

	public void setMedidor(Medidor medidor) {
		this.medidor = medidor;
	}

	public SolInspeccionIn getSolInspeccionIn() {
		return this.solInspeccionIn;
	}

	public void setSolInspeccionIn(SolInspeccionIn solInspeccionIn) {
		this.solInspeccionIn = solInspeccionIn;
	}

}