package ec.com.jaapz.controlador;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.Reparacion;
import ec.com.jaapz.modelo.ReparacionDAO;
import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ReparacionesSolicitudC {
	@FXML private TextField txtCodigo;
	@FXML private Button btnBuscar;
	@FXML private TextField txtCuenta;
	@FXML private TextField txtMedidor;
	@FXML private TextField txtCedula;
	@FXML private TextField txtNombres;
	@FXML private TextField txtApellidos;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtNumReparacion;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtUsuario;
	@FXML private TextField txtDireccion;
	@FXML private TextArea txtObservacion;
	@FXML private ImageView ivPredio;
	@FXML private Button btnGuardar;
	@FXML private Button btnNuevo;
	
	ControllerHelper helper = new ControllerHelper();
	SegUsuario usuarioLogueado = new SegUsuario();
	ReparacionDAO reparacionDao = new ReparacionDAO();
	
	public void initialize(){
		try {
			dtpFecha.setValue(LocalDate.now());
			usuarioLogueado = Context.getInstance().getUsuariosC();
			txtUsuario.setText(Encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
			txtNumReparacion.setText(String.valueOf(reparacionDao.getIdReparacion()+1));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarCuenta() {
		try{
			helper.abrirPantallaModal("/reparaciones/ReparacionesListadoCuentas.fxml","Listado de Cuentas", Context.getInstance().getStage());
			if (Context.getInstance().getCuentaCliente() != null) {
				CuentaCliente cuentaSeleccionada = Context.getInstance().getCuentaCliente();
				llenarDatos(cuentaSeleccionada);
				Context.getInstance().setUsuarios(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	void llenarDatos(CuentaCliente cuentaSeleccionada){
		try {
			txtCodigo.setText(String.valueOf(cuentaSeleccionada.getCliente().getIdCliente()));
			if(cuentaSeleccionada.getIdCuenta() == null)
				txtCuenta.setText("");
			else
				txtCuenta.setText(Integer.toString(cuentaSeleccionada.getIdCuenta()));

			if(cuentaSeleccionada.getMedidor() == null)
				txtMedidor.setText("");
			else
				txtMedidor.setText(cuentaSeleccionada.getMedidor().getCodigo());

			if(cuentaSeleccionada.getCliente().getCedula() == null)
				txtCedula.setText("");
			else
				txtCedula.setText(cuentaSeleccionada.getCliente().getCedula());

			if(cuentaSeleccionada.getCliente().getNombre() == null)
				txtNombres.setText("");
			else
				txtNombres.setText(cuentaSeleccionada.getCliente().getNombre());

			if(cuentaSeleccionada.getCliente().getApellido() == null)
				txtApellidos.setText("");
			else
				txtApellidos.setText(cuentaSeleccionada.getCliente().getApellido());

			if(cuentaSeleccionada.getCliente().getTelefono() == null)
				txtTelefono.setText("");
			else
				txtTelefono.setText(cuentaSeleccionada.getCliente().getTelefono());

			if(cuentaSeleccionada.getDireccion() == null)
				txtDireccion.setText("");
			else
				txtDireccion.setText(cuentaSeleccionada.getDireccion());
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void limpiar() {
		txtCodigo.setText("");
		txtCuenta.setText("");
		txtMedidor.setText("");
		txtCedula.setText("");
		txtNombres.setText("");
		txtApellidos.setText("");
		txtTelefono.setText("");
		txtDireccion.setText("");
		txtObservacion.setText("");
	}
	
	public void nuevo() {
		limpiar();
	}
	
	boolean validarDatos() {
		try {
			if(txtCuenta.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe cuenta del cliente", Context.getInstance().getStage());
				txtCuenta.requestFocus();
				return false;
			}
			
			if(dtpFecha.getValue().equals(null)) {
				helper.mostrarAlertaAdvertencia("Ingresar Fecha", Context.getInstance().getStage());
				dtpFecha.requestFocus();
				return false;
			}
			
			if(txtUsuario.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe usuario del sistema", Context.getInstance().getStage());
				txtCuenta.requestFocus();
				return false;
			}
			
			if(txtObservacion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe describir el problema", Context.getInstance().getStage());
				txtObservacion.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void grabar() {
		try {
			if (validarDatos() == false)
				return;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK) {
				//CuentaCliente cuentaSeleccionada = new CuentaCliente();
				Reparacion reparacion = new Reparacion();
				reparacion.setIdReparacion(null);
				Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				Timestamp fecha = new Timestamp(date.getTime());
				//reparacion.setCuentaCliente(cuentaSeleccionada);
				reparacion.setFechaReparacion(fecha);
				reparacion.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
				reparacion.setEstado(Constantes.ESTADO_ACTIVO);
				reparacion.setObservcion(txtObservacion.getText());
				
				reparacionDao.getEntityManager().getTransaction().begin();
				reparacionDao.getEntityManager().persist(reparacion);
				reparacionDao.getEntityManager().getTransaction().commit();
				
				helper.mostrarAlertaInformacion("Solicitud de Raparación Registrada", Context.getInstance().getStage());
				initialize();	
			}
		}catch(Exception ex) {
			reparacionDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
}