package ec.com.jaapz.controlador;

import java.util.Optional;

import ec.com.jaapz.modelo.Corte;
import ec.com.jaapz.modelo.CorteDAO;
import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RealizarCorteServicioC {
	@FXML private TextField txtCedula;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtCliente;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtCodigoMedidor;
	@FXML private TextField txtIdCuenta;
	@FXML private TextArea txtObservaciones;
	@FXML private Button btnGrabar;

	CuentaCliente cuentaSeleccionado = new CuentaCliente();
	ControllerHelper helper = new ControllerHelper();
	CorteDAO corteDao = new CorteDAO();

	public void initialize() {
		bloquear();
		btnGrabar.setStyle("-fx-graphic: url('/save.png');-fx-cursor: hand;");

		if (Context.getInstance().getCuentaCliente() != null) {
			recuperarDatos(Context.getInstance().getCuentaCliente());
			cuentaSeleccionado = Context.getInstance().getCuentaCliente();
			Context.getInstance().setCuentaCliente(null);
		}

		//solo letras mayusculas
		txtObservaciones.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtObservaciones.getText().toUpperCase();
				txtObservaciones.setText(cadena);
			}
		});
	}

	void recuperarDatos(CuentaCliente cuenta) {
		try {
			txtCedula.setText(cuenta.getCliente().getCedula());
			txtTelefono.setText(cuenta.getCliente().getTelefono());
			txtCliente.setText(cuenta.getCliente().getNombre() + " " + cuenta.getCliente().getApellido());
			txtDireccion.setText(cuenta.getDireccion());
			txtCodigoMedidor.setText(cuenta.getMedidor().getCodigo());
			txtIdCuenta.setText(String.valueOf(cuenta.getIdCuenta()));
			txtObservaciones.setText("CORTE REALIZADO..!!!");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void bloquear() {
		txtCedula.setEditable(false);
		txtTelefono.setEditable(false);
		txtCliente.setEditable(false);
		txtDireccion.setEditable(false);
		txtCodigoMedidor.setEditable(false);
		txtIdCuenta.setEditable(false);
	}


	public void grabar() {
		try {
			if(validarDatos() == false)
				return;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){				
				Corte corte = new Corte();
				corte.setCuentaCliente(cuentaSeleccionado);
				corte.setEstado(Constantes.ESTADO_ACTIVO);
				corte.setSegUsuario(Context.getInstance().getUsuariosC());
				corte.setObservaciones(txtObservaciones.getText());
				cuentaSeleccionado.setCortado(true);

				corteDao.getEntityManager().getTransaction().begin();
				corteDao.getEntityManager().persist(corte);
				corteDao.getEntityManager().merge(cuentaSeleccionado);
				corteDao.getEntityManager().getTransaction().commit();

				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				nuevo();
				Context.getInstance().getStageModalSolicitud().close();
			}
		}catch(Exception ex) {
			corteDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}

	public void nuevo() {		
		txtCedula.setText("");
		txtTelefono.setText("");
		txtCliente.setText("");
		txtDireccion.setText("");
		txtCodigoMedidor.setText("");
		txtIdCuenta.setText("");
		txtObservaciones.setText("");
		cuentaSeleccionado = null;
	}

	boolean validarDatos() {
		try {	
			if(txtCedula.getText().toString().equals("")) {
				helper.mostrarAlertaAdvertencia("No existen datos del cliente", Context.getInstance().getStage());
				txtCedula.requestFocus();
				return false;
			}

			if(txtIdCuenta.getText().toString().equals("")) {
				helper.mostrarAlertaAdvertencia("No existen datos del cliente", Context.getInstance().getStage());
				txtCedula.requestFocus();
				return false;
			}

			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
}