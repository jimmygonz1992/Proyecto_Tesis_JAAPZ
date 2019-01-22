package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Instalacion;
import ec.com.jaapz.modelo.InstalacionDAO;
import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.util.Constantes;
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

public class AsignacionInstalacionesC {
	@FXML private Button btnQuitarAsig;
	@FXML private Tab tpRealizadas;
	@FXML private TabPane tpAsignaciones;
	@FXML private TableView<SegUsuario> tvPersonalAsig;
	@FXML private Tab tpNuevas;
	@FXML TableView<Instalacion> tvAsignados;
	@FXML TableView<Instalacion> tvNuevosAsig;
	@FXML private Button btnImprimirAsig;
	@FXML private Button btnGrabarAsig;
	@FXML private Button btnAsignarAsig;
	
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	ControllerHelper helper = new ControllerHelper();
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	List<Instalacion> listaInstalacionesEliminar = new ArrayList<Instalacion>();
	InstalacionDAO instalacionDao = new InstalacionDAO();
	
	public void initialize() {
		try {
			llenarListaResponsables();
			if(tpRealizadas.isSelected())
				bloquearBotonesAsignacion();

			tpNuevas.setOnSelectionChanged(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					if(tpNuevas.isSelected()) {
						desbloquearBotonesAsignacion();
						listaInstalacionesEliminar.clear();
						recuperarPersonalInstalacion(tvPersonalAsig.getSelectionModel().getSelectedItem());
						
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
					recuperarPersonalInstalacion(tvPersonalAsig.getSelectionModel().getSelectedItem());
				}
			});
			tvAsignados.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if(tvAsignados.getSelectionModel().getSelectedItem().getSolInspeccionIn().getEstadoInspeccion().equals(Constantes.EST_INSPECCION_REALIZADO)) 
						btnQuitarAsig.setDisable(true);
					else
						btnQuitarAsig.setDisable(false);
				}
			});
		}catch(Exception ex) {
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
	
	@SuppressWarnings("unchecked")
	private void recuperarPersonalInstalacion(SegUsuario personalInstalacion) {
		try {
			List<Instalacion> listadoInstalaciones = instalacionDao.getListaInstalacionAsignada(personalInstalacion.getIdUsuario());
			tvAsignados.getItems().clear();
			tvAsignados.getColumns().clear();
			ObservableList<Instalacion> datos = FXCollections.observableArrayList();
			datos.setAll(listadoInstalaciones);
			//llenar los datos en la tabla
			TableColumn<Instalacion, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdInstalacion());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<Instalacion, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String dato = "";
					dato = String.valueOf(formateador.format(param.getValue().getSolInspeccionIn().getFechaIngreso()));
					return new SimpleObjectProperty<String>(dato);
				}
			});
			

			TableColumn<Instalacion, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<Instalacion, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(350);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String dato = "";
					dato = param.getValue().getSolInspeccionIn().getReferencia();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<Instalacion, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String dato = "";
					dato = param.getValue().getSolInspeccionIn().getEstadoInspeccion();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			tvAsignados.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvAsignados.setItems(datos);

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
	private void llenarListaResponsables() {
		try{
			tvPersonalAsig.getColumns().clear();
			tvPersonalAsig.getItems().clear();
			List<SegUsuario> listaClientes;
			ObservableList<SegUsuario> datos = FXCollections.observableArrayList();
			listaClientes = usuarioDAO.getListaUsuariosInstalacion();
			datos.setAll(listaClientes);

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
	
	public void grabarAsig() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if(tpRealizadas.isSelected()) {
					instalacionDao.getEntityManager().getTransaction().begin();
					for(Instalacion instalacionGrabar : listaInstalacionesEliminar) {
						instalacionGrabar.setUsuarioInstalacion(null);
						instalacionDao.getEntityManager().merge(instalacionGrabar);
					}
					instalacionDao.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarPersonalInstalacion(tvPersonalAsig.getSelectionModel().getSelectedItem());
					listaInstalacionesEliminar.clear();
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
				if(tpNuevas.isSelected()) {
					instalacionDao.getEntityManager().getTransaction().begin();
					for(Instalacion instalacionGrabar : tvNuevosAsig.getItems()) {
						instalacionGrabar.setUsuarioInstalacion(tvPersonalAsig.getSelectionModel().getSelectedItem().getIdUsuario());
						instalacionDao.getEntityManager().merge(instalacionGrabar);
					}
					instalacionDao.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarPersonalInstalacion(tvPersonalAsig.getSelectionModel().getSelectedItem());
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			instalacionDao.getEntityManager().getTransaction().rollback();
		}
	}
	
	public void imprimirAsignacion() {
		try {
			System.out.println("listado a eliminar: " + listaInstalacionesEliminar.size());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void agregarOrden(Instalacion ordenAgregar) {
		try {
			ObservableList<Instalacion> datos = FXCollections.observableArrayList();
			datos.setAll(tvNuevosAsig.getItems());
			datos.add(ordenAgregar);

			tvNuevosAsig.getItems().clear();
			tvNuevosAsig.getColumns().clear();

			//llenar los datos en la tabla
			TableColumn<Instalacion, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdInstalacion());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<Instalacion, String> fechaColum = new TableColumn<>("Fecha Solicitud");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String dato = "";
					dato = String.valueOf(formateador.format(param.getValue().getSolInspeccionIn().getFechaIngreso()));
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<Instalacion, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<Instalacion, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(350);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String dato = "";
					dato = param.getValue().getSolInspeccionIn().getReferencia();
					return new SimpleObjectProperty<String>(dato);
				}
			});


			TableColumn<Instalacion, String> estadoColum = new TableColumn<>("Estado Inspección");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String dato = "";
					dato = param.getValue().getSolInspeccionIn().getEstadoInspeccion();
					return new SimpleObjectProperty<String>(dato);
				}
			});


			tvNuevosAsig.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvNuevosAsig.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarOrdenEmitida() {
		try {
			if(tvPersonalAsig.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaError("Debe Seleccionar un Responsable de Instalación", Context.getInstance().getStage());
				return;
			}
			//pasar por parametro la lista de inspecciones a realzar para ir aminorando en el listado
			Context.getInstance().setListaInstalaciones(tvNuevosAsig.getItems());
			helper.abrirPantallaModal("/recaudaciones/ListadoOrdenesInstalaciones.fxml","Listado de Órdenes de Instalación", Context.getInstance().getStage());
			if (Context.getInstance().getInstalacion() != null) {
				Instalacion ordenAgregar = Context.getInstance().getInstalacion();
				agregarOrden(ordenAgregar);
				Context.getInstance().setInspeccion(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void quitarAsignacion() {
		try {
			if(tpRealizadas.isSelected()) {
				if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoInstalacion().equals(Constantes.EST_INSPECCION_REALIZADO)) {
					helper.mostrarAlertaError("No se puede quitar una instalación ya realizada", Context.getInstance().getStage());
					return;
				}else if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoInstalacion().equals(Constantes.EST_INSPECCION_PENDIENTE)) {
					Instalacion insEliminar = tvAsignados.getSelectionModel().getSelectedItem();
					listaInstalacionesEliminar.add(insEliminar);
					tvAsignados.getItems().remove(tvAsignados.getSelectionModel().getSelectedItem());
				}
			}
			if(tpNuevas.isSelected()) {
				Instalacion ordenQuitar = tvNuevosAsig.getSelectionModel().getSelectedItem();
				if(ordenQuitar == null) {
					helper.mostrarAlertaError("Debe Seleccionar una Asignación", Context.getInstance().getStage());
					return;
				}
				tvNuevosAsig.getItems().remove(ordenQuitar);
				if (tvNuevosAsig.getItems().size() == 0) {
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}	
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}