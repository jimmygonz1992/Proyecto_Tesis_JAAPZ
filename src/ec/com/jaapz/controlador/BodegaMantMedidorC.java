package ec.com.jaapz.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.EstadoMedidor;
import ec.com.jaapz.modelo.EstadoMedidorDAO;
import ec.com.jaapz.modelo.Medidor;
import ec.com.jaapz.modelo.MedidorDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BodegaMantMedidorC {
	@FXML private TextField txtCodigo;
	@FXML private TextField txtMarca;
	@FXML private TextField txtModelo;
	@FXML private TextField txtPrecio;
	@FXML private TextField txtIdMedidor;
	@FXML private ComboBox<EstadoMedidor> cboEstadoMed;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	@FXML private Button btnBuscar;
	
	ControllerHelper helper = new ControllerHelper();
	Medidor medidorSeleccionado = new Medidor();
	MedidorDAO medidorDao = new MedidorDAO();
	EstadoMedidorDAO estadoMedidorDao = new EstadoMedidorDAO();

	public void initialize() {
		btnBuscar.setStyle("-fx-cursor: hand;");
		btnGrabar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
		cboEstadoMed.setStyle("-fx-cursor: hand;");
		nuevo();
		txtIdMedidor.setEditable(false);
		txtIdMedidor.setVisible(false);
		llenarComboPerfil();
		
		//solo letras mayusculas
		txtMarca.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtMarca.getText().toUpperCase();
				txtMarca.setText(cadena);
			}
		});
		
		//solo letras mayusculas
		txtCodigo.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtCodigo.getText().toUpperCase();
				txtCodigo.setText(cadena);
			}
		});
		
		//solo letras mayusculas
		txtModelo.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtModelo.getText().toUpperCase();
				txtModelo.setText(cadena);
			}
		});
		
		//numeros con decimales
		txtPrecio.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					txtPrecio.setText(oldValue);
				}
			}
		});
		
		//para buscar a través de código
		txtCodigo.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke){
				if (ke.getCode().equals(KeyCode.ENTER)){
					if (validarMedidorExiste() == false) {
						medidorSeleccionado = new Medidor();
					}else {
						recuperarDatosMedidor(txtCodigo.getText());
					}
				}
			}
		});
	}
	
	//validarMedidorexiste
	boolean validarMedidorExiste() {
		try {
			List<Medidor> listaMedidores;
			listaMedidores = medidorDao.getRecuperaMedidor((txtCodigo.getText()));
			if(listaMedidores.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	private void llenarComboPerfil(){
		try{
			cboEstadoMed.setPromptText("Seleccione Estado de Medidor");
			List<EstadoMedidor> listaEstadoMedidor;
			listaEstadoMedidor = estadoMedidorDao.getListaEstadoMedidor();
			ObservableList<EstadoMedidor> datos = FXCollections.observableArrayList();
		
			datos.addAll(listaEstadoMedidor);
			cboEstadoMed.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	//recupera datos del proveedor
	public void recuperarDatosMedidor(String codigo){
		try{
			List<Medidor> listaMedidor = new ArrayList<Medidor>();
			listaMedidor = medidorDao.getRecuperaMedidor(codigo);
			for(int i = 0 ; i < listaMedidor.size() ; i ++) {
				txtIdMedidor.setText(String.valueOf(listaMedidor.get(i).getIdMedidor()));
				txtCodigo.setText(listaMedidor.get(i).getCodigo());
				cboEstadoMed.setValue(listaMedidor.get(i).getEstadoMedidor());
				txtMarca.setText(listaMedidor.get(i).getMarca());
				txtModelo.setText(listaMedidor.get(i).getModelo());
				txtPrecio.setText(String.valueOf(listaMedidor.get(i).getPrecio()));
				
				medidorSeleccionado = listaMedidor.get(i);
			}
			if (listaMedidor.size() == 0)
				medidorSeleccionado = new Medidor();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	boolean validarDatos() {
		try {
			if(cboEstadoMed.getValue() == null) {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un Estado de Medidor", Context.getInstance().getStage());
				cboEstadoMed.requestFocus();
				return false;	
			}
			
			if(txtCodigo.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar Código de Medidor", Context.getInstance().getStage());
				txtCodigo.requestFocus();
				return false;
			}
			
			if(txtPrecio.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar Precio del Medidor", Context.getInstance().getStage());
				txtPrecio.requestFocus();
				return false;
			}
			
			/*if(validarCodigoMedidor() == true) {
				helper.mostrarAlertaAdvertencia("El código ya existe para un Medidor!!", Context.getInstance().getStage());
				txtCodigo.requestFocus();
				return false;	
			}*/
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	boolean validarCodigoMedidor() {
		try {
			List<Medidor> listaMedidor;
			listaMedidor = medidorDao.getValidarCodigoMedidor(txtCodigo.getText());
			if(listaMedidor.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void grabar() {
		try {
			if(validarDatos() == false){
				return;
			}
			Medidor medidor = new Medidor();
			medidor.setEstado(Constantes.ESTADO_ACTIVO);
			medidor.setEstadoMedidor(cboEstadoMed.getSelectionModel().getSelectedItem());
			medidor.setCodigo(txtCodigo.getText());
			medidor.setMarca(txtMarca.getText());
			medidor.setModelo(txtModelo.getText());
			medidor.setPrecio(Double.parseDouble(txtPrecio.getText()));
			medidor.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
			
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				medidorDao.getEntityManager().getTransaction().begin();
				if(txtIdMedidor.getText().equals("0")) {//inserta
					medidor.setIdMedidor(null);
					medidorDao.getEntityManager().persist(medidor);
				}else {//modifica
					medidor.setIdMedidor(Integer.parseInt(txtIdMedidor.getText()));
					medidorDao.getEntityManager().merge(medidor);
				}
				medidorDao.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				nuevo();
			}
		}catch(Exception ex) {
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			medidorDao.getEntityManager().getTransaction().rollback();
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscar() {
		try{
			helper.abrirPantallaModal("/bodega/ListadoMedidores.fxml","Listado de Medidores", Context.getInstance().getStage());
			if (Context.getInstance().getMedidor() != null) {
				Medidor datoSeleccionado = Context.getInstance().getMedidor();
				llenarDatos(datoSeleccionado);
				Context.getInstance().setMedidor(null);;
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	void llenarDatos(Medidor datoSeleccionado){
		try {			
			if(datoSeleccionado.getIdMedidor() == null)
				txtIdMedidor.setText("");
			else
				txtIdMedidor.setText(String.valueOf(datoSeleccionado.getIdMedidor()));
						
			if(datoSeleccionado.getCodigo() == null)
				txtCodigo.setText("");
			else
				txtCodigo.setText(datoSeleccionado.getCodigo());
			
			cboEstadoMed.getSelectionModel().select(datoSeleccionado.getEstadoMedidor());

			if(datoSeleccionado.getMarca() == null)
				txtMarca.setText("");
			else
				txtMarca.setText(datoSeleccionado.getMarca());
			
			if(datoSeleccionado.getModelo() == null)
				txtModelo.setText("");
			else
				txtModelo.setText(datoSeleccionado.getModelo());
			
			txtPrecio.setText(String.valueOf(datoSeleccionado.getPrecio()));

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo() {
		medidorSeleccionado = null;
		txtCodigo.setText("");
		txtMarca.setText("");
		txtModelo.setText("");
		txtPrecio.setText("");
		txtIdMedidor.setText("0");		
	}
}