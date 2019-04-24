package ec.com.jaapz.util;

import java.sql.Connection;
import java.util.List;

import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.Barrio;
import ec.com.jaapz.modelo.Categoria;
import ec.com.jaapz.modelo.Cliente;
import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.Empresa;
import ec.com.jaapz.modelo.Factura;
import ec.com.jaapz.modelo.Ingreso;
import ec.com.jaapz.modelo.IngresoDetalle;
import ec.com.jaapz.modelo.Instalacion;
import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.modelo.Medidor;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.Reparacion;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.SegPerfil;
import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioPerfil;
import ec.com.jaapz.modelo.SolInspeccionIn;
import ec.com.jaapz.modelo.SolInspeccionRep;
import javafx.stage.Stage;

public class Context {
	private final static Context instance = new Context();
	
	Connection conexion = null;
	private String usuario;
	private String perfil;
	private int idUsuario;
	private int idPerfil;
	
	
	private boolean mensajeEnviado;
	
	private Stage stage;
	private Stage stageModal;
	private Stage stageModalSolicitud;
	private SegUsuario usuarios;
	private SegUsuario usuariosC;
	private Empresa empresaC;
	
	private Rubro rubro;
	private Rubro rubros;
	private LiquidacionOrden liquidaciones;
	private Reparacion reparaciones;
	
	private Planilla planillas;
	private Cliente cliente;
	private Medidor medidor;
	private CuentaCliente cuentaCliente;
	private Factura factura;
	private Categoria categoria;
	private Barrio barrio;
	private List<SolInspeccionIn> listaInspecciones;
	private List<Instalacion> listaInstalaciones;
	private List<LiquidacionOrden> listaLiquidaciones;
	private List<Reparacion> listaReparaciones;
	private List<SolInspeccionRep> listaInspeccionesRep;
	private List<Barrio> listaBarrios;
	private List<SegUsuarioPerfil> listaPerfiles;
	private SegPerfil perfilSeleccionado;
	
	private AperturaLectura apertura;
	private SolInspeccionIn inspeccion;
	private Instalacion instalacion;
	private SolInspeccionRep reparacion;
	
	private IngresoDetalle detalleMedidor;
	private Ingreso ingresoSeleccionado;
	
	List<LiquidacionOrden> listadoAsignados;
	
	public static Context getInstance() {
		return instance;
	}
	
	public Connection getConexion() {
		return conexion;
	}
	public void setConexion(Connection conexion) {
		this.conexion = conexion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public Stage getStageModal() {
		return stageModal;
	}
	public void setStageModal(Stage stageModal) {
		this.stageModal = stageModal;
	}
	public SegUsuario getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(SegUsuario usuarios) {
		this.usuarios = usuarios;
	}
	
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdPerfil() {
		return idPerfil;
	}
	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<SolInspeccionIn> getListaInspecciones() {
		return listaInspecciones;
	}

	public void setListaInspecciones(List<SolInspeccionIn> listaInspecciones) {
		this.listaInspecciones = listaInspecciones;
	}
	
	public List<Instalacion> getListaInstalaciones() {
		return listaInstalaciones;
	}

	public void setListaInstalaciones(List<Instalacion> listaInstalaciones) {
		this.listaInstalaciones = listaInstalaciones;
	}
	
	public List<LiquidacionOrden> getListaLiquidaciones() {
		return listaLiquidaciones;
	}

	public void setListaLiquidaciones(List<LiquidacionOrden> listaLiquidaciones) {
		this.listaLiquidaciones = listaLiquidaciones;
	}
	
	//para las asignaciones de reparaciones
	public List<Reparacion> getListaReparaciones() {
		return listaReparaciones;
	}

	//para las asignaciones de reparaciones
	public void setListaReparaciones(List<Reparacion> listaReparaciones) {
		this.listaReparaciones = listaReparaciones;
	}
	
	public List<SolInspeccionRep> getListaInspeccionesRep() {
		return listaInspeccionesRep;
	}

	public void setListaInspeccionesRep(List<SolInspeccionRep> listaInspeccionesRep) {
		this.listaInspeccionesRep = listaInspeccionesRep;
	}

	public SolInspeccionIn getInspeccion() {
		return inspeccion;
	}

	public void setInspeccion(SolInspeccionIn inspeccion) {
		this.inspeccion = inspeccion;
	}
	
	public Instalacion getInstalacion() {
		return instalacion;
	}

	public void setInstalacion(Instalacion instalacion) {
		this.instalacion = instalacion;
	}
	
	public SolInspeccionRep getReparacion() {
		return reparacion;
	}

	public void setReparacion(SolInspeccionRep reparacion) {
		this.reparacion = reparacion;
	}

	public Rubro getRubro() {
		return rubro;
	}

	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}

	public CuentaCliente getCuentaCliente() {
		return cuentaCliente;
	}

	public void setCuentaCliente(CuentaCliente cuentaCliente) {
		this.cuentaCliente = cuentaCliente;
	}
	
	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Barrio getBarrio() {
		return barrio;
	}

	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}

	public SegUsuario getUsuariosC() {
		return usuariosC;
	}

	public void setUsuariosC(SegUsuario usuariosC) {
		this.usuariosC = usuariosC;
	}
	
	public Empresa getEmpresaC() {
		return empresaC;
	}

	public void setEmpresaC(Empresa empresaC) {
		this.empresaC = empresaC;
	}
	
	public Rubro getRubros() {
		return rubros;
	}

	public void setRubros(Rubro rubros) {
		this.rubros = rubros;
	}

	public Planilla getPlanillas() {
		return planillas;
	}

	public void setPlanillas(Planilla planillas) {
		this.planillas = planillas;
	}
	public AperturaLectura getApertura() {
		return apertura;
	}
	public void setApertura(AperturaLectura apertura) {
		this.apertura = apertura;
	}
	public List<Barrio> getListaBarrios() {
		return listaBarrios;
	}
	public void setListaBarrios(List<Barrio> listaBarrios) {
		this.listaBarrios = listaBarrios;
	}

	public Medidor getMedidor() {
		return medidor;
	}

	public void setMedidor(Medidor medidor) {
		this.medidor = medidor;
	}
	
	public LiquidacionOrden getLiquidaciones() {
		return liquidaciones;
	}

	public void setLiquidaciones(LiquidacionOrden liquidaciones) {
		this.liquidaciones = liquidaciones;
	}
	
	public Reparacion getReparaciones() {
		return reparaciones;
	}

	public void setReparaciones(Reparacion reparaciones) {
		this.reparaciones = reparaciones;
	}

	public List<SegUsuarioPerfil> getListaPerfiles() {
		return listaPerfiles;
	}

	public void setListaPerfiles(List<SegUsuarioPerfil> listaPerfiles) {
		this.listaPerfiles = listaPerfiles;
	}

	public SegPerfil getPerfilSeleccionado() {
		return perfilSeleccionado;
	}

	public void setPerfilSeleccionado(SegPerfil perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}

	public boolean isMensajeEnviado() {
		return mensajeEnviado;
	}

	public void setMensajeEnviado(boolean mensajeEnviado) {
		this.mensajeEnviado = mensajeEnviado;
	}

	public IngresoDetalle getDetalleMedidor() {
		return detalleMedidor;
	}

	public void setDetalleMedidor(IngresoDetalle detalleMedidor) {
		this.detalleMedidor = detalleMedidor;
	}

	public Ingreso getIngresoSeleccionado() {
		return ingresoSeleccionado;
	}

	public void setIngresoSeleccionado(Ingreso ingresoSeleccionado) {
		this.ingresoSeleccionado = ingresoSeleccionado;
	}

	public List<LiquidacionOrden> getListadoAsignados() {
		return listadoAsignados;
	}

	public void setListadoAsignados(List<LiquidacionOrden> listadoAsignados) {
		this.listadoAsignados = listadoAsignados;
	}

	public Stage getStageModalSolicitud() {
		return stageModalSolicitud;
	}

	public void setStageModalSolicitud(Stage stageModalSolicitud) {
		this.stageModalSolicitud = stageModalSolicitud;
	}
	
}