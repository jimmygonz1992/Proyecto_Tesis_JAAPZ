package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class ClientesListaClientesJuntaC {
	@FXML private TableView<CuentaCliente> tvDatos;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;
	@FXML private Button btnImprimir;
	@FXML private Button btnEliminar;
	CuentaClienteDAO cuentaDAO = new CuentaClienteDAO();
	ControllerHelper helper = new ControllerHelper();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

	public void initialize() {	
		try {
			btnEditar.setStyle("-fx-cursor: hand;");
			btnEliminar.setStyle("-fx-cursor: hand;");
			btnImprimir.setStyle("-fx-cursor: hand;");
			
			btnEditar.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
			btnImprimir.setStyle("-fx-graphic: url('/imprimir.png');-fx-cursor: hand;");
			btnEliminar.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");
			llenarDatos("");
			txtBuscar.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtBuscar.getText().toUpperCase();
					txtBuscar.setText(cadena);
				}
			});
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						buscarCliente();
					} 
				} 
			}); 
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	void llenarDatos(String busqueda){
		try{
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<CuentaCliente> listaClientes;
			ObservableList<CuentaCliente> datos = FXCollections.observableArrayList();
			listaClientes = cuentaDAO.getListaCuentaClientes(busqueda);
			datos.setAll(listaClientes);

			//llenar los datos en la tabla
			TableColumn<CuentaCliente, String> idColum = new TableColumn<>("Cédula");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCliente().getCedula().toString());
				}
			});
			TableColumn<CuentaCliente, String> nombresColum = new TableColumn<>("Nombres y Apellidos");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(260);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido());
				}
			});

			TableColumn<CuentaCliente, String> fechaColum = new TableColumn<>("Fecha de Ingreso");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(80);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String fecha = "";
					if(param.getValue().getFechaIngreso() != null)
						fecha = formateador.format(param.getValue().getFechaIngreso());
					return new SimpleObjectProperty<String>(fecha);
				}
			});

			TableColumn<CuentaCliente, String> medidorColum = new TableColumn<>("Medidor");
			medidorColum.setMinWidth(10);
			medidorColum.setPrefWidth(120);
			medidorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String medidor = "";
					if(param.getValue().getMedidor() == null)
						medidor = "NO ASIGNADO";
					else
						if(param.getValue().getMedidor().getCodigo() != null)
							medidor = param.getValue().getMedidor().getCodigo();
					return new SimpleObjectProperty<String>(medidor);
				}
			});
			TableColumn<CuentaCliente, String> categoriaColum = new TableColumn<>("Categoría");
			categoriaColum.setMinWidth(10);
			categoriaColum.setPrefWidth(120);
			categoriaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String categoria = "";
					if(param.getValue().getCategoria() == null)
						categoria = "NO ASIGNADO";
					else
						categoria = param.getValue().getCategoria().getDescripcion();
					return new SimpleObjectProperty<String>(categoria);
				}
			});
			TableColumn<CuentaCliente, String> barrioColum = new TableColumn<>("Barrio");
			barrioColum.setMinWidth(10);
			barrioColum.setPrefWidth(120);
			barrioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String barrio = "";
					if(param.getValue().getBarrio() == null)
						barrio = "NO ASIGNADO";
					else
						barrio = param.getValue().getBarrio().getNombre();
					return new SimpleObjectProperty<String>(barrio);
				}
			});
			TableColumn<CuentaCliente, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String estado = "";
					if(param.getValue().getEstado().toString().equals("A"))
						estado = "Activo";
					else
						estado = "Inactivo";
					return new SimpleObjectProperty<String>(estado);
				}
			});

			tvDatos.getColumns().addAll(idColum, nombresColum,fechaColum,medidorColum,categoriaColum,barrioColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void buscarCliente() {
		try {
			List<CuentaCliente> listaClientes;
			ObservableList<CuentaCliente> datos = FXCollections.observableArrayList();
			listaClientes = cuentaDAO.getListaCuentaClientes(txtBuscar.getText().toString());
			datos.setAll(listaClientes);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@FXML
	void editarCliente(ActionEvent event) {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setCuentaCliente(tvDatos.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/clientes/ClientesJunta.fxml","Datos del Cliente", Context.getInstance().getStage());
				llenarDatos("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Cliente a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@FXML
	void eliminarCliente(ActionEvent event) {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja al cliente.. Desea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					CuentaCliente cuentaSeleccionada = new CuentaCliente();
					cuentaSeleccionada = tvDatos.getSelectionModel().getSelectedItem();
					cuentaSeleccionada.setEstado("I");
					cuentaDAO.getEntityManager().getTransaction().begin();
					cuentaDAO.getEntityManager().merge(cuentaSeleccionada);
					cuentaDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Cliente Dado de Baja", Context.getInstance().getStage());
					llenarDatos("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Cliente a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@FXML
	void imprimirListado(ActionEvent event) {
		try {

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
