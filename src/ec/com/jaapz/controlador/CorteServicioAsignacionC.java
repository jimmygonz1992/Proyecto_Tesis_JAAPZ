package ec.com.jaapz.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class CorteServicioAsignacionC {
	@FXML private Button btnQuitarAsig;
	@FXML private Tab tpRealizadas;
	@FXML private TabPane tpAsignaciones;
	@FXML private TableView<SegUsuario> tvPersonalAsig;
	@FXML private Tab tpNuevas;
	@FXML TableView<CuentaCliente> tvAsignados;
	@FXML TableView<CuentaCliente> tvNuevosAsig;
	@FXML private Button btnImprimirAsig;
	@FXML private Button btnGrabarAsig;
	@FXML private Button btnAsignarAsig;
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	List<CuentaCliente> listaCuentasEliminar = new ArrayList<CuentaCliente>();
	CuentaClienteDAO cuentaDao = new CuentaClienteDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			btnAsignarAsig.setStyle("-fx-cursor: hand;");
			btnGrabarAsig.setStyle("-fx-cursor: hand;");
			btnImprimirAsig.setStyle("-fx-cursor: hand;");
			btnQuitarAsig.setStyle("-fx-cursor: hand;");
			
			llenarListaUsuariosCorte();
			if(tpRealizadas.isSelected())
				bloquearBotonesAsignacion();
			
			tpNuevas.setOnSelectionChanged(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					if(tpNuevas.isSelected()) {
						desbloquearBotonesAsignacion();
						listaCuentasEliminar.clear();
						recuperarPersonalCorte(tvPersonalAsig.getSelectionModel().getSelectedItem());
					}
				}
			});
			
			tpRealizadas.setOnSelectionChanged(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					if(tpRealizadas.isSelected()) {
						bloquearBotonesAsignacion();
					}
				}
			});
			
			tvPersonalAsig.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					recuperarPersonalCorte(tvPersonalAsig.getSelectionModel().getSelectedItem());
				}
			});
			
			tvAsignados.setOnMouseClicked(new EventHandler<Event>() {
				@SuppressWarnings("unlikely-arg-type")
				@Override
				public void handle(Event event) {
					if(tvAsignados.getSelectionModel().getSelectedItem().getCortado()!= null) {
						if(tvAsignados.getSelectionModel().getSelectedItem().getCortado().equals("true"))
							btnQuitarAsig.setDisable(true);
						else
							btnQuitarAsig.setDisable(false);
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void desbloquearBotonesAsignacion() {
		try {
			btnAsignarAsig.setDisable(false);
			btnQuitarAsig.setDisable(false);
			btnImprimirAsig.setDisable(true);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarPersonalCorte(SegUsuario personalCorte) {
		try {
			List<CuentaCliente> listadoCuentas = cuentaDao.getListaCuentasAsignadas(personalCorte.getIdUsuario());
			tvAsignados.getItems().clear();
			tvAsignados.getColumns().clear();
			ObservableList<CuentaCliente> datos = FXCollections.observableArrayList();
			datos.setAll(listadoCuentas);
			//llenar los datos en la tabla
			TableColumn<CuentaCliente, String> idColum = new TableColumn<>("idCuenta");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdCuenta());
					return new SimpleObjectProperty<String>(dato);
				}
			});
			
			TableColumn<CuentaCliente, String> idMedColum = new TableColumn<>("Cod. Medidor");
			idMedColum.setMinWidth(10);
			idMedColum.setPrefWidth(50);
			idMedColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getMedidor().getCodigo());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<CuentaCliente, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String cliente = "";
					cliente = param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<CuentaCliente, String> direccionColum = new TableColumn<>("Dirección");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(350);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String dato = "";
					dato = param.getValue().getDireccion();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			tvAsignados.getColumns().addAll(idColum, idMedColum, clienteColum,direccionColum);
			tvAsignados.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void llenarListaUsuariosCorte() {
		try{
			tvPersonalAsig.getColumns().clear();
			tvPersonalAsig.getItems().clear();
			List<SegUsuario> listaUsuarios;
			ObservableList<SegUsuario> datos = FXCollections.observableArrayList();
			listaUsuarios = usuarioDAO.getListaUsuariosCorte();
			datos.setAll(listaUsuarios);

			//llenar los datos en la tabla
			TableColumn<SegUsuario, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegUsuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegUsuario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdUsuario()));
				}
			});
			TableColumn<SegUsuario, String> nombresColum = new TableColumn<>("Nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegUsuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegUsuario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getNombre()));
				}
			});
			TableColumn<SegUsuario, String> apellidosColum = new TableColumn<>("Apellidos");
			apellidosColum.setMinWidth(10);
			apellidosColum.setPrefWidth(200);
			apellidosColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegUsuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegUsuario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getApellido()));
				}
			});

			TableColumn<SegUsuario, String> fechColum = new TableColumn<>("Telefono");
			fechColum.setMinWidth(10);
			fechColum.setPrefWidth(100);
			fechColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegUsuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegUsuario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTelefono()));
				}
			});
			tvPersonalAsig.getColumns().addAll(idColum, nombresColum,apellidosColum,fechColum);
			tvPersonalAsig.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	private void bloquearBotonesAsignacion() {
		try {
			btnAsignarAsig.setDisable(true);
			btnQuitarAsig.setDisable(true);
			btnImprimirAsig.setDisable(false);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void imprimirAsignacion() {
		try {
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarOrdenEmitida() {
		try {
			if(tvPersonalAsig.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaError("Debe Seleccionar un Responsable de Corte", Context.getInstance().getStage());
				return;
			}
			//pasar por parametro la lista de inspecciones a realzar para ir aminorando en el listado
			Context.getInstance().setListaCortes(tvNuevosAsig.getItems());
			helper.abrirPantallaModal("/cortes/CortesListadoCuentas.fxml","Listado de Cliente para COrte del Servicio", Context.getInstance().getStage());
			if (Context.getInstance().getCuentaCliente() != null) {
				CuentaCliente cuentaAgregar = Context.getInstance().getCuentaCliente();
				agregarCuentaOrden(cuentaAgregar);
				Context.getInstance().setCuentaCliente(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void agregarCuentaOrden(CuentaCliente cuentaAgregar) {
		try {
			ObservableList<CuentaCliente> datos = FXCollections.observableArrayList();
			datos.setAll(tvNuevosAsig.getItems());
			datos.add(cuentaAgregar);

			tvNuevosAsig.getItems().clear();
			tvNuevosAsig.getColumns().clear();

			//llenar los datos en la tabla
			TableColumn<CuentaCliente, String> idColum = new TableColumn<>("idCuenta");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdCuenta());
					return new SimpleObjectProperty<String>(dato);
				}
			});
			
			TableColumn<CuentaCliente, String> idMedColum = new TableColumn<>("Cod. Medidor");
			idMedColum.setMinWidth(10);
			idMedColum.setPrefWidth(50);
			idMedColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getMedidor().getCodigo());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<CuentaCliente, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String cliente = "";
					cliente = param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<CuentaCliente, String> direccionColum = new TableColumn<>("Dirección");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(350);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String dato = "";
					dato = param.getValue().getDireccion();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			tvNuevosAsig.getColumns().addAll(idColum, idMedColum, clienteColum,direccionColum);
			tvNuevosAsig.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void quitarAsignacion() {
		try {
			if(tpRealizadas.isSelected()) {
				if(tvAsignados.getSelectionModel().getSelectedItem().getCortado() == true) {
					helper.mostrarAlertaError("No se puede quitar... Corte Realizado!!!", Context.getInstance().getStage());
					return;
				}else if(tvAsignados.getSelectionModel().getSelectedItem().getCortado() == false) {
					CuentaCliente cuentaEliminar = tvAsignados.getSelectionModel().getSelectedItem();
					listaCuentasEliminar.add(cuentaEliminar);
					tvAsignados.getItems().remove(tvAsignados.getSelectionModel().getSelectedItem());
				}
			}
			if(tpNuevas.isSelected()) {
				CuentaCliente cuentaQuitar = tvNuevosAsig.getSelectionModel().getSelectedItem();
				if(cuentaQuitar == null) {
					helper.mostrarAlertaError("Debe Seleccionar una Asignación", Context.getInstance().getStage());
					return;
				}
				tvNuevosAsig.getItems().remove(cuentaQuitar);
				if (tvNuevosAsig.getItems().size() == 0) {
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}	
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void grabarAsig() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if(tpRealizadas.isSelected()) {
					cuentaDao.getEntityManager().getTransaction().begin();
					for(CuentaCliente cuentaGrabar : listaCuentasEliminar) {
						cuentaGrabar.setIdUsuCorteEncargado(null);
						cuentaDao.getEntityManager().merge(cuentaGrabar);
					}
					cuentaDao.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarPersonalCorte(tvPersonalAsig.getSelectionModel().getSelectedItem());
					listaCuentasEliminar.clear();
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
				if(tpNuevas.isSelected()) {
					cuentaDao.getEntityManager().getTransaction().begin();
					for(CuentaCliente cuentaGrabar : tvNuevosAsig.getItems()) {
						cuentaGrabar.setIdUsuCorteEncargado(tvPersonalAsig.getSelectionModel().getSelectedItem().getIdUsuario());
						cuentaDao.getEntityManager().merge(cuentaGrabar);
					}
					cuentaDao.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarPersonalCorte(tvPersonalAsig.getSelectionModel().getSelectedItem());
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			cuentaDao.getEntityManager().getTransaction().rollback();
		}
	}
}