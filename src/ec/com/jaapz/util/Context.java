package ec.com.jaapz.util;

import java.sql.Connection;
import java.util.List;

import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.Barrio;
import ec.com.jaapz.modelo.Categoria;
import ec.com.jaapz.modelo.Cliente;
import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.Medidor;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.SegUsuario;
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
	
	private Stage stage;
	private Stage stageModal;
	private SegUsuario usuarios;
	private SegUsuario usuariosC;
	
	private Rubro rubro;
	private Rubro rubros;
	private Planilla planillas;
	private Cliente cliente;
	private Medidor medidor;
	private CuentaCliente cuentaCliente;
	private Categoria categoria;
	private Barrio barrio;
	private List<SolInspeccionIn> listaInspecciones;
	private List<SolInspeccionRep> listaInspeccionesRep;
	private List<Barrio> listaBarrios;
	
	private AperturaLectura apertura;
	private SolInspeccionIn inspeccion;
	private SolInspeccionRep reparacion;
	
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
	
	
	
}