package ec.com.jaapz.controlador;

import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Categoria;
import ec.com.jaapz.modelo.CategoriaDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ParametrosCategoriaC {
	private @FXML TableView<Categoria> tvDatos;
	private @FXML TextField txtCodigo;
	private @FXML TextField txtDescripcion;
	private @FXML TextField txtValorm3;
	private @FXML CheckBox chkEstado;
	private @FXML Button btnAceptar;
	private @FXML Button btnNuevo;
	CategoriaDAO junCategoriaDAO = new CategoriaDAO();
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		btnAceptar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
		
		limpiar();
		llenarDatos();
		tvDatos.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				recuperarDatos(tvDatos.getSelectionModel().getSelectedItem());
			}
		});
		txtDescripcion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtDescripcion.getText().toUpperCase();
				txtDescripcion.setText(cadena);
			}
		});
		/*
		txtValorm3.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (newValue.matches("\\d*")) {
					
				} else {
					txtValorm3.setText(oldValue);
				}
			}
		});
		*/
	}
	void limpiar(){
		txtCodigo.setText("0");
		txtDescripcion.setText("");
		txtValorm3.setText("");
		chkEstado.setSelected(true);
	}
	public void recuperarDatos(Categoria datos){
		try {
			if(datos != null) {
				txtCodigo.setText(String.valueOf(datos.getIdCategoria()));
				txtDescripcion.setText(datos.getDescripcion());
				txtValorm3.setText(String.valueOf(datos.getValorM3()));
				if(datos.getEstado().equals("A"))
					chkEstado.setSelected(true);
				else
					chkEstado.setSelected(false);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	void llenarDatos(){
		try{
			tvDatos.getColumns().clear();
			List<Categoria> categoria;
			categoria = junCategoriaDAO.getListaCategorias();
			ObservableList<Categoria> datos = FXCollections.observableArrayList();
			datos.addAll(categoria);
			//llenar los datos en la tabla
			TableColumn<Categoria, String> idColum = new TableColumn<>("Codigo");
			idColum.setMinWidth(70);
			idColum.setCellValueFactory(new PropertyValueFactory<Categoria, String>("idCategoria"));
			TableColumn<Categoria, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(200);
			descripcionColum.setCellValueFactory(new PropertyValueFactory<Categoria, String>("descripcion"));
			TableColumn<Categoria, String> valorm3Colum = new TableColumn<>("Valor m3");
			valorm3Colum.setMinWidth(70);
			valorm3Colum.setCellValueFactory(new PropertyValueFactory<Categoria, String>("valorM3"));
			TableColumn<Categoria, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(40);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Categoria,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Categoria, String> param) {
					String estado = "";
					if(param.getValue().getEstado().equals("A"))
						estado = "Activo";
					else if(param.getValue().getEstado().equals("I"))
						estado = "Inactivo";
						
					return new SimpleObjectProperty<String>(estado);
				}
			});
			tvDatos.getColumns().addAll(idColum, descripcionColum,valorm3Colum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void grabar() {
		try {
			if(txtDescripcion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe Registrar Descripción de la Categoria", Context.getInstance().getStageModal());
				return;
			}
			if(txtValorm3.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe Registrar valor del metro cúbico", Context.getInstance().getStageModal());
				return;
			}
			Categoria categoria = new Categoria();
			if(chkEstado.isSelected() == true)
				categoria.setEstado("A");
			else
				categoria.setEstado("I");
			categoria.setDescripcion(txtDescripcion.getText());
			categoria.setValorM3(Double.parseDouble(txtValorm3.getText()));
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				junCategoriaDAO.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")) {//inserta
					categoria.setIdCategoria(null);
					junCategoriaDAO.getEntityManager().persist(categoria);
				}else {
					categoria.setIdCategoria(Integer.parseInt(txtCodigo.getText()));
					junCategoriaDAO.getEntityManager().merge(categoria);
				}
				junCategoriaDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados", Context.getInstance().getStage());
				llenarDatos();
				limpiar();
			}
		}catch(Exception ex) {
			junCategoriaDAO.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	public void nuevo() {
		limpiar();
	}
}
