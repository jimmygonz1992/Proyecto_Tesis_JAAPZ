package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the cuenta_cliente database table.
 * 
 */
@Entity
@Table(name="cuenta_cliente")
@NamedQueries({
	@NamedQuery(name="CuentaCliente.findAll", query="SELECT c FROM CuentaCliente c where "
		+ "(lower(c.cliente.nombre) like lower(:patron) or lower(c.cliente.apellido) like lower(:patron) or lower(c.cliente.cedula) like lower(:patron)) and c.estado='A' order by c.idCuenta asc"),
	@NamedQuery(name="CuentaCliente.bucarTodos", query="SELECT c FROM CuentaCliente c where c.estado = 'A' order by c.idCuenta"),
	@NamedQuery(name="CuentaCliente.buscarCuentaClientePerfil", query="SELECT c FROM CuentaCliente c "
		+ "where (lower(c.cliente.apellido) like :patron or lower(c.cliente.nombre) like :patron "
		+ "or lower(c.cliente.cedula) like :patron)"
		+ "and c.usuarioCrea = :idPerfilUsuario and c.estado='A' order by c.idCuenta asc"),
	@NamedQuery(name="CuentaCliente.existeCuenta", query="SELECT c FROM CuentaCliente c where (c.idCuenta = (:cuenta) and c.estado = 'A')"),
	@NamedQuery(name="CuentaCliente.existeCuentaMedidor", query="SELECT c FROM CuentaCliente c where (c.medidor.codigo = (:medidor) and c.estado = 'A')"),

	//para corte
	@NamedQuery(name="CuentaCliente.buscarCuentaAsignada", query="SELECT c FROM CuentaCliente c "
		+ "where c.idUsuCorteEncargado = :idPerfilUsuario and c.estado = 'A' order by c.idCuenta desc"),
	
	//para corte
	@NamedQuery(name="CuentaCliente.buscaCuentasParaCorte", query="SELECT c FROM CuentaCliente c "
		+ "where (lower(c.cliente.apellido) like :patron or lower(c.cliente.nombre) like :patron or lower(c.cliente.cedula) like :patron) "
		+ "and c.cortado = 'false' and c.idUsuCorteEncargado = null and c.estado = 'A' order by c.idCuenta desc"),
	
	//para corte
	@NamedQuery(name="CuentaCliente.buscarCuentasCortePerfil", query="SELECT c FROM CuentaCliente c "
		+ "where (lower(c.cliente.apellido) like :patron or lower(c.cliente.nombre) like :patron or lower(c.cliente.cedula) like :patron) "
		+ " and c.idUsuCorteEncargado = :idPerfilUsuario and c.idUsuCorteEncargado = null "
		+ " and c.cortado = 'false' and c.estado = 'A' order by c.idCuenta desc"),
	
	//para ver cortes pendientes ya asignados
	@NamedQuery(name="CuentaCliente.cuentasCortePendientes", query="SELECT c FROM CuentaCliente c "
			+ "where (lower(c.cliente.nombre) like lower(:patron) or lower(c.cliente.apellido) like lower(:patron) or lower(c.cliente.cedula) like lower(:patron)) "
			+ " and c.idUsuCorteEncargado = :idPerfilUsuario and c.cortado = 'false' and c.estado = 'A' order by c.idCuenta desc"),
	
	//para cortes asignados admin
	@NamedQuery(name="CuentaCliente.CuentasPendientesAdm", query="SELECT c FROM CuentaCliente c where "
			+ "(lower(c.cliente.nombre) like lower(:patron) or lower(c.cliente.apellido) like lower(:patron) or lower(c.cliente.cedula) like lower(:patron))"
			+ " and c.estado='A' and c.cortado = 'false' order by c.idCuenta asc")
	
})
public class CuentaCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_cuenta")
	private Integer idCuenta;

	private String direccion;

	private String email;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_ingreso")
	private Date fechaIngreso;

	@Column(name="hora_ingreso")
	private Time horaIngreso;

	private Double latitud;

	private Double longitud;

	private String observacion;

	@Column(nullable = false, columnDefinition = "bit")
	private Boolean cortado;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;
	
	@Column(name="id_usuario_corte")
	private Integer idUsuCorteEncargado;

	//bi-directional many-to-one association to Convenio
	@OneToMany(mappedBy="cuentaCliente", cascade = CascadeType.ALL)
	private List<Convenio> convenios;

	//bi-directional many-to-one association to Reconexion
	@OneToMany(mappedBy="cuentaCliente", cascade = CascadeType.ALL)
	private List<Reconexion> reconexions;
	public List<Reconexion> getReconexions() {
		return reconexions;
	}
	public void setReconexions(List<Reconexion> reconexions) {
		this.reconexions = reconexions;
	}
	public Reconexion addReconexion(Reconexion reconexion) {
		getReconexions().add(reconexion);
		reconexion.setCuentaCliente(this);
		return reconexion;
	}

	public Reconexion removeReconexion(Reconexion reconexion) {
		getReconexions().remove(reconexion);
		reconexion.setCuentaCliente(null);

		return reconexion;
	}


	//bi-directional many-to-one association to Reconexion
	@OneToMany(mappedBy="cuentaCliente", cascade = CascadeType.ALL)
	private List<Corte> cortes;
	public List<Corte> getCortes() {
		return cortes;
	}
	public void setCortes(List<Corte> cortes) {
		this.cortes = cortes;
	}
	public Corte addCorte(Corte corte) {
		getCortes().add(corte);
		corte.setCuentaCliente(this);
		return corte;
	}

	public Corte removeCorte(Corte corte) {
		getCortes().remove(corte);
		corte.setCuentaCliente(null);

		return corte;
	}
	//bi-directional many-to-one association to Barrio
	@ManyToOne
	@JoinColumn(name="id_barrio")
	private Barrio barrio;

	//bi-directional many-to-one association to Categoria
	@ManyToOne
	@JoinColumn(name="id_categoria")
	private Categoria categoria;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;

	//bi-directional many-to-one association to Medidor
	@ManyToOne
	@JoinColumn(name="id_medidor")
	private Medidor medidor;

	//bi-directional many-to-one association to Factura
	@OneToMany(mappedBy="cuentaCliente")
	private List<Factura> facturas;

	//bi-directional many-to-one association to Instalacion
	@OneToMany(mappedBy="cuentaCliente")
	private List<Instalacion> instalacions;

	//bi-directional many-to-one association to LiquidacionOrden
	@OneToMany(mappedBy="cuentaCliente")
	private List<LiquidacionOrden> liquidacionOrdens;

	//bi-directional many-to-one association to Planilla
	@OneToMany(mappedBy="cuentaCliente")
	private List<Planilla> planillas;

	//bi-directional many-to-one association to Reparacion
	@OneToMany(mappedBy="cuentaCliente")
	private List<Reparacion> reparacions;

	//bi-directional many-to-one association to SolInspeccionRep
	@OneToMany(mappedBy="cuentaCliente")
	private List<SolInspeccionRep> solInspeccionReps;

	public CuentaCliente() {
	}

	public Integer getIdCuenta() {
		return this.idCuenta;
	}

	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}

	public Integer getIdUsuCorteEncargado() {
		return idUsuCorteEncargado;
	}
	public void setIdUsuCorteEncargado(Integer idUsuCorteEncargado) {
		this.idUsuCorteEncargado = idUsuCorteEncargado;
	}
	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return this.estado;
	}	

	public Boolean getCortado() {
		return cortado;
	}

	public void setCortado(Boolean cortado) {
		this.cortado = cortado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Time getHoraIngreso() {
		return this.horaIngreso;
	}

	public void setHoraIngreso(Time horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	public Double getLatitud() {
		return this.latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return this.longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Integer getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public List<Convenio> getConvenios() {
		return this.convenios;
	}

	public void setConvenios(List<Convenio> convenios) {
		this.convenios = convenios;
	}

	public Convenio addConvenio(Convenio convenio) {
		getConvenios().add(convenio);
		convenio.setCuentaCliente(this);

		return convenio;
	}

	public Convenio removeConvenio(Convenio convenio) {
		getConvenios().remove(convenio);
		convenio.setCuentaCliente(null);

		return convenio;
	}

	public Barrio getBarrio() {
		return this.barrio;
	}

	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}

	public Categoria getCategoria() {
		return this.categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Medidor getMedidor() {
		return this.medidor;
	}

	public void setMedidor(Medidor medidor) {
		this.medidor = medidor;
	}

	public List<Factura> getFacturas() {
		return this.facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public Factura addFactura(Factura factura) {
		getFacturas().add(factura);
		factura.setCuentaCliente(this);

		return factura;
	}

	public Factura removeFactura(Factura factura) {
		getFacturas().remove(factura);
		factura.setCuentaCliente(null);

		return factura;
	}

	public List<Instalacion> getInstalacions() {
		return this.instalacions;
	}

	public void setInstalacions(List<Instalacion> instalacions) {
		this.instalacions = instalacions;
	}

	public Instalacion addInstalacion(Instalacion instalacion) {
		getInstalacions().add(instalacion);
		instalacion.setCuentaCliente(this);

		return instalacion;
	}

	public Instalacion removeInstalacion(Instalacion instalacion) {
		getInstalacions().remove(instalacion);
		instalacion.setCuentaCliente(null);

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
		liquidacionOrden.setCuentaCliente(this);

		return liquidacionOrden;
	}

	public LiquidacionOrden removeLiquidacionOrden(LiquidacionOrden liquidacionOrden) {
		getLiquidacionOrdens().remove(liquidacionOrden);
		liquidacionOrden.setCuentaCliente(null);

		return liquidacionOrden;
	}

	public List<Planilla> getPlanillas() {
		return this.planillas;
	}

	public void setPlanillas(List<Planilla> planillas) {
		this.planillas = planillas;
	}

	public Planilla addPlanilla(Planilla planilla) {
		getPlanillas().add(planilla);
		planilla.setCuentaCliente(this);

		return planilla;
	}

	public Planilla removePlanilla(Planilla planilla) {
		getPlanillas().remove(planilla);
		planilla.setCuentaCliente(null);

		return planilla;
	}

	public List<Reparacion> getReparacions() {
		return this.reparacions;
	}

	public void setReparacions(List<Reparacion> reparacions) {
		this.reparacions = reparacions;
	}

	public Reparacion addReparacion(Reparacion reparacion) {
		getReparacions().add(reparacion);
		reparacion.setCuentaCliente(this);

		return reparacion;
	}

	public Reparacion removeReparacion(Reparacion reparacion) {
		getReparacions().remove(reparacion);
		reparacion.setCuentaCliente(null);

		return reparacion;
	}

	public List<SolInspeccionRep> getSolInspeccionReps() {
		return this.solInspeccionReps;
	}

	public void setSolInspeccionReps(List<SolInspeccionRep> solInspeccionReps) {
		this.solInspeccionReps = solInspeccionReps;
	}

	public SolInspeccionRep addSolInspeccionRep(SolInspeccionRep solInspeccionRep) {
		getSolInspeccionReps().add(solInspeccionRep);
		solInspeccionRep.setCuentaCliente(this);

		return solInspeccionRep;
	}

	public SolInspeccionRep removeSolInspeccionRep(SolInspeccionRep solInspeccionRep) {
		getSolInspeccionReps().remove(solInspeccionRep);
		solInspeccionRep.setCuentaCliente(null);

		return solInspeccionRep;
	}
	@Override
	public String toString() {
		return this.cliente.getNombre() + " " + this.cliente.getApellido();
	}
}