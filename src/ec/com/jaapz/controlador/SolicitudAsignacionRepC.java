package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.modelo.SolInspeccionRep;
import ec.com.jaapz.modelo.SolInspeccionRepDAO;
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

public class SolicitudAsignacionRepC {
	@FXML private Button btnQuitarAsig;
	@FXML private Tab tpRealizadas;
	@FXML private TabPane tpAsignaciones;
	@FXML private TableView<SegUsuario> tvPersonalAsig;
	@FXML private Tab tpNuevas;
	@FXML TableView<SolInspeccionRep> tvAsignados;
	@FXML TableView<SolInspeccionRep> tvNuevosAsig;
	@FXML private Button btnImprimirAsig;
	@FXML private Button btnGrabarAsig;
	@FXML private Button btnAsignarAsig;
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	SolInspeccionRepDAO reparacionDao = new SolInspeccionRepDAO();
	List<SolInspeccionRep> listaInspeccionesEliminar = new ArrayList<SolInspeccionRep>();
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
						listaInspeccionesEliminar.clear();
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
					if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoInspecRep().equals(Constantes.EST_INSPECCION_REALIZADO)) 
						btnQuitarAsig.setDisable(true);
					else
						btnQuitarAsig.setDisable(false);
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}	
	
	@SuppressWarnings("unchecked")
	private void recuperarPersonalInspeccion(SegUsuario personalInspeccion) {
		try {
			List<SolInspeccionRep> listadoInspecciones = reparacionDao.getListaInspeccionAsignada(personalInspeccion.getIdUsuario());
			tvAsignados.getItems().clear();
			tvAsignados.getColumns().clear();
			ObservableList<SolInspeccionRep> datos = FXCollections.observableArrayList();
			datos.setAll(listadoInspecciones);
			//llenar los datos en la tabla
			TableColumn<SolInspeccionRep, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdSolicitudRep());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionRep, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String dato = "";
					dato = String.valueOf(formateador.format(param.getValue().getFecha()));
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionRep, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<SolInspeccionRep, String> direccionColum = new TableColumn<>("Dirección");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(350);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String dato = "";
					dato = param.getValue().getCuentaCliente().getDireccion();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionRep, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String dato = "";
					dato = param.getValue().getEstadoInspecRep();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			tvAsignados.getColumns().addAll(idColum, fechaColum,clienteColum,direccionColum,estadoColum);
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
	private void llenarListaResponsables() {
		try{
			tvPersonalAsig.getColumns().clear();
			tvPersonalAsig.getItems().clear();
			List<SegUsuario> listaClientes;
			ObservableList<SegUsuario> datos = FXCollections.observableArrayList();
			listaClientes = usuarioDAO.getListaUsuariosInspeccion();
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
					reparacionDao.getEntityManager().getTransaction().begin();
					for(SolInspeccionRep inspeccionGrabar : listaInspeccionesEliminar) {
						inspeccionGrabar.setIdUsuEncargado(null);
						reparacionDao.getEntityManager().merge(inspeccionGrabar);
					}
					reparacionDao.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarPersonalInspeccion(tvPersonalAsig.getSelectionModel().getSelectedItem());
					listaInspeccionesEliminar.clear();
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
				if(tpNuevas.isSelected()) {
					reparacionDao.getEntityManager().getTransaction().begin();
					for(SolInspeccionRep inspeccionGrabar : tvNuevosAsig.getItems()) {
						inspeccionGrabar.setIdUsuEncargado(tvPersonalAsig.getSelectionModel().getSelectedItem().getIdUsuario());
						reparacionDao.getEntityManager().merge(inspeccionGrabar);
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
	
	public void imprimirAsignacion() {
		try {
			System.out.println("listado a eliminar: " + listaInspeccionesEliminar.size());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void buscarOrdenEmitida() {
		try {
			if(tvPersonalAsig.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaError("Debe Seleccionar un Responsable de Inspección", Context.getInstance().getStage());
				return;
			}
			//pasar por parametro la lista de inspecciones a realzar para ir aminorando en el listado
			Context.getInstance().setListaInspeccionesRep(tvNuevosAsig.getItems());
			helper.abrirPantallaModal("/solicitudes/SolicitudListaOrdenesRep.fxml","Listado de Órdenes de Solicitud", Context.getInstance().getStage());
			if (Context.getInstance().getReparacion() != null) {
				SolInspeccionRep ordenAgregar = Context.getInstance().getReparacion();
				agregarOrden(ordenAgregar);
				Context.getInstance().setReparacion(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void agregarOrden(SolInspeccionRep ordenAgregar) {
		try {
			ObservableList<SolInspeccionRep> datos = FXCollections.observableArrayList();
			datos.setAll(tvNuevosAsig.getItems());
			datos.add(ordenAgregar);

			tvNuevosAsig.getItems().clear();
			tvNuevosAsig.getColumns().clear();

			//llenar los datos en la tabla
			TableColumn<SolInspeccionRep, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdSolicitudRep());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionRep, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String dato = "";
					dato = String.valueOf(formateador.format(param.getValue().getFecha()));
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionRep, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<SolInspeccionRep, String> referenciaColum = new TableColumn<>("Direccion");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(350);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String dato = "";
					dato = param.getValue().getCuentaCliente().getDireccion();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionRep, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String dato = "";
					dato = param.getValue().getEstadoInspecRep();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			tvNuevosAsig.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvNuevosAsig.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void quitarAsignacion() {
		try {
			if(tpRealizadas.isSelected()) {
				if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoInspecRep().equals(Constantes.EST_INSPECCION_REALIZADO)) {
					System.out.println("realizado");
					helper.mostrarAlertaError("No se puede quitar una inspección ya realizada", Context.getInstance().getStage());
					return;
				}else if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoInspecRep().equals(Constantes.EST_INSPECCION_PENDIENTE)) {
					System.out.println("pendiente");
					SolInspeccionRep insEliminar = tvAsignados.getSelectionModel().getSelectedItem();
					listaInspeccionesEliminar.add(insEliminar);
					tvAsignados.getItems().remove(tvAsignados.getSelectionModel().getSelectedItem());
				}
			}
			if(tpNuevas.isSelected()) {
				SolInspeccionRep ordenQuitar = tvNuevosAsig.getSelectionModel().getSelectedItem();
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