package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Reparacion;
import ec.com.jaapz.modelo.ReparacionDAO;
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

public class AsignacionesReparaC {
	@FXML private Button btnQuitarAsig;
	@FXML private Tab tpRealizadas;
	@FXML private TabPane tpAsignaciones;
	@FXML private TableView<SegUsuario> tvPersonalAsig;
	@FXML private Tab tpNuevas;
	@FXML TableView<Reparacion> tvAsignados;
	@FXML TableView<Reparacion> tvNuevosAsig;
	@FXML private Button btnImprimirAsig;
	@FXML private Button btnGrabarAsig;
	@FXML private Button btnAsignarAsig;
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	ReparacionDAO reparacionDao = new ReparacionDAO();
	List<Reparacion> listaReparacionesEliminar = new ArrayList<Reparacion>();
	ControllerHelper helper = new ControllerHelper();
	
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
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
						listaReparacionesEliminar.clear();
						recuperarPersonalInspeccion(tvPersonalAsig.getSelectionModel().getSelectedItem());
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
					recuperarPersonalInspeccion(tvPersonalAsig.getSelectionModel().getSelectedItem());
				}
			});
			
			tvAsignados.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoReparacion().equals(Constantes.EST_INSPECCION_REALIZADO)) 
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
			listaClientes = usuarioDAO.getListaUsuariosReparacion();
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
	
	@SuppressWarnings("unchecked")
	private void recuperarPersonalInspeccion(SegUsuario personalReparacion) {
		try {
			List<Reparacion> listadoReparaciones = reparacionDao.getListaReparacionAsignada(personalReparacion.getIdUsuario());
			tvAsignados.getItems().clear();
			tvAsignados.getColumns().clear();
			ObservableList<Reparacion> datos = FXCollections.observableArrayList();
			datos.setAll(listadoReparaciones);
			//llenar los datos en la tabla
			TableColumn<Reparacion, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdReparacion());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<Reparacion, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String dato = "";
					dato = String.valueOf(formateador.format(param.getValue().getFechaCierreInspeccion()));
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<Reparacion, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<Reparacion, String> direccionColum = new TableColumn<>("Dirección");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(350);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String dato = "";
					dato = param.getValue().getCuentaCliente().getDireccion();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<Reparacion, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String dato = "";
					dato = param.getValue().getSolInspeccionRep().getEstadoInspecRep();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			tvAsignados.getColumns().addAll(idColum, fechaColum,clienteColum,direccionColum,estadoColum);
			tvAsignados.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void quitarAsignacion() {
		try {
			if(tpRealizadas.isSelected()) {
				if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoReparacion().equals(Constantes.EST_INSPECCION_REALIZADO)) {
					helper.mostrarAlertaError("No se puede quitar una reparación ya realizada", Context.getInstance().getStage());
					return;
				}else if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoReparacion().equals(Constantes.EST_INSPECCION_PENDIENTE)) {
					Reparacion repEliminar = tvAsignados.getSelectionModel().getSelectedItem();
					listaReparacionesEliminar.add(repEliminar);
					tvAsignados.getItems().remove(tvAsignados.getSelectionModel().getSelectedItem());
				}
			}
			if(tpNuevas.isSelected()) {
				Reparacion ordenQuitar = tvNuevosAsig.getSelectionModel().getSelectedItem();
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
	
	public void imprimirAsignacion() {
		try {
			System.out.println("listado a eliminar: " + listaReparacionesEliminar.size());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void buscarOrdenEmitida() {
		try {
			if(tvPersonalAsig.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaError("Debe Seleccionar un Responsable de Reparación", Context.getInstance().getStage());
				return;
			}
			//pasar por parametro la lista de inspecciones a realzar para ir aminorando en el listado
			Context.getInstance().setListaReparaciones(tvNuevosAsig.getItems());
			//helper.abrirPantallaModal("/reparaciones/ReparacionesListadoInspRep.fxml","Listado de Órdenes de Reparación", Context.getInstance().getStage());
			helper.abrirPantallaModal("/asignaciones/AsignacionListadoReparaciones.fxml","Listado de Órdenes de Reparación", Context.getInstance().getStage());
			if (Context.getInstance().getReparaciones() != null) {
				Reparacion ordenAgregar = Context.getInstance().getReparaciones();
				agregarOrden(ordenAgregar);
				Context.getInstance().setReparaciones(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void agregarOrden(Reparacion ordenAgregar) {
		try {
			ObservableList<Reparacion> datos = FXCollections.observableArrayList();
			datos.setAll(tvNuevosAsig.getItems());
			datos.add(ordenAgregar);

			tvNuevosAsig.getItems().clear();
			tvNuevosAsig.getColumns().clear();

			//llenar los datos en la tabla
			TableColumn<Reparacion, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdReparacion());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<Reparacion, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String dato = "";
					dato = String.valueOf(formateador.format(param.getValue().getFechaCierreInspeccion()));
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<Reparacion, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<Reparacion, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(350);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String dato = "";
					dato = param.getValue().getReferencia();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<Reparacion, String> estadoColum = new TableColumn<>("Estado Inspección");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String dato = "";
					dato = param.getValue().getSolInspeccionRep().getEstadoInspecRep();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			tvNuevosAsig.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvNuevosAsig.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void grabarAsig() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if(tpRealizadas.isSelected()) {
					reparacionDao.getEntityManager().getTransaction().begin();
					for(Reparacion reparacionGrabar : listaReparacionesEliminar) {
						reparacionGrabar.setUsuarioReparacion(null);
						reparacionDao.getEntityManager().merge(reparacionGrabar);
					}
					reparacionDao.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarPersonalInspeccion(tvPersonalAsig.getSelectionModel().getSelectedItem());
					listaReparacionesEliminar.clear();
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
				if(tpNuevas.isSelected()) {
					reparacionDao.getEntityManager().getTransaction().begin();
					for(Reparacion reparacionGrabar : tvNuevosAsig.getItems()) {
						reparacionGrabar.setUsuarioReparacion(tvPersonalAsig.getSelectionModel().getSelectedItem().getIdUsuario());
						reparacionDao.getEntityManager().merge(reparacionGrabar);
					}
					reparacionDao.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarPersonalInspeccion(tvPersonalAsig.getSelectionModel().getSelectedItem());
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			reparacionDao.getEntityManager().getTransaction().rollback();
		}
	}
}