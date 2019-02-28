package ec.com.jaapz.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import ec.com.jaapz.modelo.Egreso;
import ec.com.jaapz.modelo.EgresoDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class EgresosC {
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtDescripcion;
	@FXML private TextField txtValorMonto;
	@FXML private TextField txtObservaciones;
	@FXML private TextField txtCodigo;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;

	EgresoDAO egresoDao = new EgresoDAO();
	ControllerHelper helper = new ControllerHelper();

	public void initialize(){
		btnGrabar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
		dtpFecha.setValue(LocalDate.now());
		txtCodigo.setEditable(false);
		txtCodigo.setVisible(false);
		nuevo();

		//numeros con decimales
		txtValorMonto.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					txtValorMonto.setText(oldValue);
				}
			}
		});

		//solo letras mayusculas
		txtDescripcion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtDescripcion.getText().toUpperCase();
				txtDescripcion.setText(cadena);
			}
		});

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

	public void grabar() {
		try {
			if(validarDatos() == false)
				return;
			Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			Egreso egreso = new Egreso();
			egreso.setDescripcion(txtDescripcion.getText());
			egreso.setFecha(date);
			egreso.setMonto(Double.parseDouble(txtValorMonto.getText()));
			egreso.setObservaciones(txtObservaciones.getText());
			egreso.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
			egreso.setEstado(Constantes.ESTADO_ACTIVO);

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				egresoDao.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")) {//inserta
					egreso.setIdEgreso(null);
					egresoDao.getEntityManager().persist(egreso);
				}else {
					egreso.setIdEgreso(Integer.parseInt(txtCodigo.getText()));
					egresoDao.getEntityManager().merge(egreso);
				}
				egresoDao.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados", Context.getInstance().getStage());
				nuevo();
			}
		}catch(Exception ex) {
			egresoDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}

	boolean validarDatos() {
		try {
			if(txtDescripcion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Describa la causa de salida de dinero", Context.getInstance().getStage());
				txtDescripcion.requestFocus();
				return false;
			}

			if(txtValorMonto.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingrese cantidad a retirar", Context.getInstance().getStage());
				txtValorMonto.requestFocus();
				return false;
			}

			if(txtObservaciones.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingrese alguna observación o novedad de la salida de dinero", Context.getInstance().getStage());
				txtObservaciones.requestFocus();
				return false;
			}

			if(dtpFecha.getValue().equals(null)) {
				helper.mostrarAlertaAdvertencia("Ingresar Fecha", Context.getInstance().getStage());
				dtpFecha.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	public void nuevo() {
		txtCodigo.setText("0");
		dtpFecha.setValue(LocalDate.now());
		txtDescripcion.setText("");
		txtValorMonto.setText("");
		txtObservaciones.setText("");		
	}
}