package ec.com.jaapz.controlador;

import ec.com.jaapz.modelo.Estado;
import ec.com.jaapz.modelo.InstalacionDetalle;
import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class InstalacionesAtencionSolicC {
	@FXML private TextField txtIdSolicitud;
	@FXML private Button btnBuscar;
	@FXML private TextField txtEstadoValor;
	@FXML private TextField txtFechaSolic;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtReferencia;
	
	@FXML private TextField txtCedula;
	@FXML private TextField txtCliente;
	@FXML private TextField txtUsuarioSolic;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtUsuarioCreaInst;
	@FXML private ComboBox<Estado> cboEstadoInstalacion;
	
	@FXML private TextField txtCodigoMedidor;
	@FXML private TextField txtMarca;
	@FXML private TextField txtModelo;
	@FXML private TextField txtPrecioMed;
	
	private @FXML TableView<InstalacionDetalle> tvDatos;
	
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	@FXML private Button btnImprimir;
	
	ControllerHelper helper = new ControllerHelper();
	LiquidacionOrden liquidacionSeleccionada = new LiquidacionOrden();	
	
	public void initialize() {
		try {
			llenarCombo();
			Encriptado encriptado = new Encriptado();
			btnImprimir.setStyle("-fx-graphic: url('/imprimir.png');-fx-cursor: hand;");
			txtUsuarioCreaInst.setText(encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void llenarCombo() {
    	try {			
    		ObservableList<Estado> listaEstado = FXCollections.observableArrayList(Estado.values()); 
    		cboEstadoInstalacion.setItems(listaEstado);
    		cboEstadoInstalacion.setPromptText("Seleccione Estado");
    	}catch(Exception ex) {
    		
    		System.out.println(ex.getMessage());
    	}
    }
	
	public void nuevo() {
		
	}
	
	public void grabar() {
		try {
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarSolicitudInst() {
		try{
			helper.abrirPantallaModal("/instalaciones/ListadoOrdenLiquidaciones.fxml","Listado de Órdenes de Liquidaciones", Context.getInstance().getStage());
			if (Context.getInstance().getLiquidaciones() != null) {
				liquidacionSeleccionada = Context.getInstance().getLiquidaciones();
				llenarDatosLiquidacion(liquidacionSeleccionada);
				Context.getInstance().setLiquidaciones(null);;
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	void llenarDatosLiquidacion(LiquidacionOrden datoSeleccionado){
		try {
			if(datoSeleccionado.getSolInspeccionIn().getIdSolInspeccion() == null)
				txtIdSolicitud.setText("");
			else
				txtIdSolicitud.setText(String.valueOf(datoSeleccionado.getSolInspeccionIn().getIdSolInspeccion()));
			
			/*if(datoSeleccionado.getCuentaCliente().getCliente().getCedula() == null)
				txtCedula.setText("");
			else
				txtCedula.setText(datoSeleccionado.getCuentaCliente().getCliente().getCedula());

			if(datoSeleccionado.getCuentaCliente().getIdCuenta() == null)
				txtIdCuenta.setText("");
			else
				txtIdCuenta.setText(String.valueOf(datoSeleccionado.getCuentaCliente().getIdCuenta()));
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getNombre() == null)
				txtNombres.setText("");
			else
				txtNombres.setText(datoSeleccionado.getCuentaCliente().getCliente().getNombre());
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getApellido() == null)
				txtApellidos.setText("");
			else
				txtApellidos.setText(datoSeleccionado.getCuentaCliente().getCliente().getApellido());
			
			if(datoSeleccionado.getCuentaCliente().getDireccion() == null)
				txtDireccion.setText("");
			else
				txtDireccion.setText(datoSeleccionado.getCuentaCliente().getDireccion());
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getTelefono() == null)
				txtTelefono.setText("");
			else
				txtTelefono.setText(datoSeleccionado.getCuentaCliente().getCliente().getTelefono());
			
			if(datoSeleccionado.getIdLiquidacion() == null)
				txtIdLiquid.setText("");
			else
				txtIdLiquid.setText(String.valueOf(datoSeleccionado.getIdLiquidacion()));
			
			if(datoSeleccionado.getMedidor().getCodigo() == null)
				txtCodigoMedidor.setText("");
			else
				txtCodigoMedidor.setText(String.valueOf(datoSeleccionado.getMedidor().getCodigo()));
			
			if(datoSeleccionado.getMedidor().getMarca() == null)
				txtMarca.setText("");
			else
				txtMarca.setText(String.valueOf(datoSeleccionado.getMedidor().getMarca()));
			
			if(datoSeleccionado.getMedidor().getModelo() == null)
				txtModelo.setText("");
			else
				txtModelo.setText(String.valueOf(datoSeleccionado.getMedidor().getModelo()));
			
			if(datoSeleccionado.getMedidor().getPrecio() == 0.0)
				txtPrecioMed.setText("");
			else
				txtPrecioMed.setText(String.valueOf(datoSeleccionado.getMedidor().getPrecio()));
			recuperarDetalleLiquidacion(datoSeleccionado);*/
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void imprimir() {
		
	}
}