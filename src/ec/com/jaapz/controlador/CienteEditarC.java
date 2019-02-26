package ec.com.jaapz.controlador;

import java.util.Optional;

import ec.com.jaapz.modelo.Cliente;
import ec.com.jaapz.modelo.ClienteDAO;
import ec.com.jaapz.modelo.Genero;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CienteEditarC {
	@FXML TextField txtCedula; 
	@FXML TextField txtNombres;
	@FXML TextField txtApellidos;
	@FXML TextField txtTelefono;
	@FXML ComboBox<Genero> cboGenero;
	@FXML CheckBox chkEstado;
	@FXML Button btnGrabar;
	@FXML Button btnSalir;
	Genero[] genero = Genero.values();
	Cliente clienteGrabar;
	ClienteDAO clienteDAO = new ClienteDAO();
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		try {
			btnGrabar.setStyle("-fx-cursor: hand;");
			btnSalir.setStyle("-fx-cursor: hand;");
			cboGenero.setStyle("-fx-cursor: hand;");
			
			cargarComboGenero();
			if(Context.getInstance().getCliente() != null) {
				recuperarDatos(Context.getInstance().getCliente());
				clienteGrabar = Context.getInstance().getCliente();
			}else {
				limpiar();
				clienteGrabar = new Cliente();
				clienteGrabar.setIdCliente(null);
			}
			//validar solo numeros
			txtCedula.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtCedula.setText(oldValue);
					}
				}
			});
			txtTelefono.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtTelefono.setText(oldValue);
					}
				}
			});
			//validar solo 10 valores
			txtCedula.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
					if (txtCedula.getText().length() > 10) {
						String s = txtCedula.getText().substring(0, 10);
						txtCedula.setText(s);
					}
				}
			});
			txtTelefono.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
					if (txtCedula.getText().length() > 10) {
						String s = txtCedula.getText().substring(0, 10);
						txtCedula.setText(s);
					}
				}
			});
			//solo letras mayusculas
			txtNombres.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtNombres.getText().toUpperCase();
					txtNombres.setText(cadena);
				}
			});
			txtApellidos.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtApellidos.getText().toUpperCase();
					txtApellidos.setText(cadena);
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void limpiar() {
		try {
			txtCedula.setText("");
			txtNombres.setText("");
			txtApellidos.setText("");
			txtTelefono.setText("");
			chkEstado.setSelected(true);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void recuperarDatos(Cliente clienteSeleccionado) {
		try {
			txtCedula.setText(clienteSeleccionado.getCedula());
			txtNombres.setText(clienteSeleccionado.getNombre());
			txtApellidos.setText(clienteSeleccionado.getApellido());
			txtTelefono.setText(clienteSeleccionado.getTelefono());
			for(Genero g : genero){
				if(g.toString().equals(clienteSeleccionado.getGenero()))
					cboGenero.getSelectionModel().select(g);
			}
			if(clienteSeleccionado.getEstado().equals("A"))
				chkEstado.setSelected(true);
			else
				chkEstado.setSelected(false);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarComboGenero(){
		try{
			ObservableList<Genero> listaGenero = FXCollections.observableArrayList(Genero.values());
			cboGenero.setItems(listaGenero);
			cboGenero.setPromptText("Seleccione Genero");
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void grabar() {
		try {
			if(helper.validarDeCedula(txtCedula.getText()) == false) {
				helper.mostrarAlertaAdvertencia("Cédula Incorrecta!!!", Context.getInstance().getStageModal());
				return;
			}
			if(cboGenero.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Seleccione Género", Context.getInstance().getStageModal());
				return;
			}
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				String estado = "";
				clienteDAO.getEntityManager().getTransaction().begin();
				if(clienteGrabar.getIdCliente() == null) {//inserta
					clienteGrabar.setApellido(txtApellidos.getText());
					clienteGrabar.setCedula(txtCedula.getText());
					if(chkEstado.isSelected())
						estado = "A";
					else
						estado = "I";
					clienteGrabar.setEstado(estado);
					clienteGrabar.setTelefono(txtTelefono.getText());
					clienteGrabar.setGenero(cboGenero.getSelectionModel().getSelectedItem().name());
					clienteGrabar.setNombre(txtNombres.getText());
					clienteGrabar.setUsuarioCrea(Context.getInstance().getIdUsuario());
					clienteDAO.getEntityManager().persist(clienteGrabar);
				}else {//modifica
					clienteGrabar.setApellido(txtApellidos.getText());
					clienteGrabar.setCedula(txtCedula.getText());
					if(chkEstado.isSelected())
						estado = "A";
					else
						estado = "I";
					clienteGrabar.setEstado(estado);
					clienteGrabar.setTelefono(txtTelefono.getText());
					clienteGrabar.setGenero(cboGenero.getSelectionModel().getSelectedItem().name());
					clienteGrabar.setNombre(txtNombres.getText());
					clienteDAO.getEntityManager().merge(clienteGrabar);
				}
				clienteDAO.getEntityManager().getTransaction().commit();
				Context.getInstance().getStageModal().close();
				helper.mostrarAlertaInformacion("Datos Grabados", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			clienteDAO.getEntityManager().getTransaction().rollback();
			ex.printStackTrace();
		}
	}
	public void salir() {
		Context.getInstance().getStageModal().close();
	}
}
