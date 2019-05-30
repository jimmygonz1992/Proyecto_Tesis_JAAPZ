package ec.com.jaapz.controlador;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.PrecioUnitario;
import ec.com.jaapz.modelo.PrecioUnitarioDAO;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class ParametrosPreciosUnitariosC {
	@FXML private TextField txtCantidad;
	@FXML private TableView<PrecioUnitario> tvDatos;
	@FXML private Button btnAgregar;
	@FXML private Button btnGrabar;
	@FXML private TextField txtDescripcion;
	@FXML private TextField txtPrecio;
	@FXML private Button btnQuitar;
	@FXML private TextField txtTipoRubro;
	@FXML private Button btnBuscarRubro;

	Rubro rubroSeleccionado;
	PrecioUnitarioDAO preciosDAO = new PrecioUnitarioDAO();
	DecimalFormat decimales = new DecimalFormat("#0.00");
	ControllerHelper helper = new ControllerHelper();
	List<PrecioUnitario> listaEliminado;
	public void initialize() {
		try {
			btnAgregar.setStyle("-fx-cursor: hand;");
			btnBuscarRubro.setStyle("-fx-cursor: hand;");
			btnGrabar.setStyle("-fx-cursor: hand;");
			btnQuitar.setStyle("-fx-cursor: hand;");
			txtDescripcion.setEditable(false);
			txtPrecio.setEditable(false);
			txtTipoRubro.setEditable(false);
			
			//validar solo numeros
			txtCantidad.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtCantidad.setText(oldValue);
					}
				}
			});
			
			//para anadir a la grilla con enter
			txtCantidad.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						agregarPrecioUnitario();
						btnBuscarRubro.requestFocus();
					}
				}
			});
			
			recuperarDatos();
			tvDatos.setEditable(true);
			listaEliminado = new ArrayList<PrecioUnitario>();
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	void recuperarDatos() {
		try {
			tvDatos.getItems().clear();
			tvDatos.getColumns().clear();
			List<PrecioUnitario> listaPrecios;
			ObservableList<PrecioUnitario> datos = FXCollections.observableArrayList();
			listaPrecios = preciosDAO.getListaPrecios();
			datos.setAll(listaPrecios);

			//llenar los datos en la tabla
			TableColumn<PrecioUnitario, String> idColum = new TableColumn<>("Cod. Material");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioUnitario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PrecioUnitario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getRubro().getIdRubro()));
				}
			});


			TableColumn<PrecioUnitario, String> descipcionColum = new TableColumn<>("Descripcion");
			descipcionColum.setMinWidth(10);
			descipcionColum.setPrefWidth(250);
			descipcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioUnitario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PrecioUnitario, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getRubro().getDescripcion());
				}
			});

			TableColumn<PrecioUnitario, String> precioColum = new TableColumn<>("Costo U.");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(90);
			precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioUnitario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PrecioUnitario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(decimales.format(param.getValue().getRubro().getPrecio())));
				}
			});
			TableColumn<PrecioUnitario, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellFactory(TextFieldTableCell.<PrecioUnitario>forTableColumn());
			cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioUnitario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PrecioUnitario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
				}
			});
			cantidadColum.setOnEditCommit(
					new EventHandler<CellEditEvent<PrecioUnitario, String>>() {
						@Override
						public void handle(CellEditEvent<PrecioUnitario, String> t) {
							((PrecioUnitario) t.getTableView().getItems().get(
									t.getTablePosition().getRow())
									).setCantidad(Integer.parseInt(t.getNewValue()));
						}
					}
					);
			TableColumn<PrecioUnitario, String> costoColum = new TableColumn<>("Costo Total");
			costoColum.setMinWidth(10);
			costoColum.setPrefWidth(90);
			costoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioUnitario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PrecioUnitario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(decimales.format(param.getValue().getTotal())));
				}
			});

			tvDatos.getColumns().addAll(idColum,descipcionColum,precioColum,cantidadColum,costoColum);
			tvDatos.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void grabar() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				//declaro los objetos a grabar

				List<PrecioUnitario> listadoGrabar = tvDatos.getItems();
				System.out.println(listadoGrabar.size());
				preciosDAO.getEntityManager().getTransaction().begin();
				for(PrecioUnitario datos : listadoGrabar) {
					if(datos.getIdPrecio() == null) { //inserta
						preciosDAO.getEntityManager().persist(datos);
					}else { //modifica
						preciosDAO.getEntityManager().merge(datos);
					}
				}
				
				//quitar los eliminados
				for(PrecioUnitario eliminado : listaEliminado) {
					if(eliminado.getIdPrecio() != null) {
						eliminado.setEstado(Constantes.ESTADO_INACTIVO);
						preciosDAO.getEntityManager().merge(eliminado);
					}
				}
				preciosDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados con exito", Context.getInstance().getStage());
				recuperarDatos();
				listaEliminado = null;
				listaEliminado = new ArrayList<PrecioUnitario>();
			}
		}catch(Exception ex) {
			preciosDAO.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void agregarPrecioUnitario() {
		try {
			if(txtCantidad.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe Ingresar cantidad!!!", Context.getInstance().getStage());
				return;
			}
			ObservableList<PrecioUnitario> datos = tvDatos.getItems();
			tvDatos.getColumns().clear();

			PrecioUnitario datoAnadir = new PrecioUnitario();
			datoAnadir.setEstado("A");
			datoAnadir.setRubro(rubroSeleccionado);
			datoAnadir.setCantidad(Integer.valueOf(txtCantidad.getText()));
			datoAnadir.setTotal(Double.valueOf(txtCantidad.getText()) * rubroSeleccionado.getPrecio());
			rubroSeleccionado.getPrecioUnitarios().add(datoAnadir);
			datos.add(datoAnadir);


			//llenar los datos en la tabla
			TableColumn<PrecioUnitario, String> idColum = new TableColumn<>("Id Material");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioUnitario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PrecioUnitario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getRubro().getIdRubro()));
				}
			});


			TableColumn<PrecioUnitario, String> descipcionColum = new TableColumn<>("Descripcion");
			descipcionColum.setMinWidth(10);
			descipcionColum.setPrefWidth(250);
			descipcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioUnitario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PrecioUnitario, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getRubro().getDescripcion());
				}
			});

			TableColumn<PrecioUnitario, String> precioColum = new TableColumn<>("Costo U.");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(90);
			precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioUnitario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PrecioUnitario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(decimales.format(param.getValue().getRubro().getPrecio())));
				}
			});

			TableColumn<PrecioUnitario, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellFactory(TextFieldTableCell.<PrecioUnitario>forTableColumn());
			cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioUnitario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PrecioUnitario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
				}
			});
			cantidadColum.setOnEditCommit(
					new EventHandler<CellEditEvent<PrecioUnitario, String>>() {
						@Override
						public void handle(CellEditEvent<PrecioUnitario, String> t) {
							((PrecioUnitario) t.getTableView().getItems().get(
									t.getTablePosition().getRow())
									).setCantidad(Integer.parseInt(t.getNewValue()));
						}
					}
					);

			TableColumn<PrecioUnitario, String> costoColum = new TableColumn<>("Costo Total");
			costoColum.setMinWidth(10);
			costoColum.setPrefWidth(90);
			costoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PrecioUnitario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PrecioUnitario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(decimales.format(param.getValue().getTotal())));
				}
			});
			
			tvDatos.getColumns().addAll(idColum,descipcionColum,precioColum,cantidadColum,costoColum);
			tvDatos.setItems(datos);

			rubroSeleccionado = null;
			limpiarRubro();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			rubroSeleccionado = null;
			Context.getInstance().setRubro(null);
		}
	}

	public void quitarPrecioUnitario() {
		try {
			PrecioUnitario precioUnitarioSeleccionado = tvDatos.getSelectionModel().getSelectedItem();
			listaEliminado.add(precioUnitarioSeleccionado);
			tvDatos.getItems().remove(precioUnitarioSeleccionado);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void buscarRubro() {
		try{
			helper.abrirPantallaModal("/clientes/ClientesListadoPrecios.fxml","Listado de Precios Unitarios", Context.getInstance().getStage());
			if (Context.getInstance().getRubro() != null) {
				rubroSeleccionado = Context.getInstance().getRubro();
				recuperarDatosRubros();
			}else
				limpiarRubro();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	private void recuperarDatosRubros() {
		try {
			txtDescripcion.setText(rubroSeleccionado.getDescripcion());
			txtPrecio.setText(String.valueOf(decimales.format(rubroSeleccionado.getPrecio())));
			txtTipoRubro.setText(rubroSeleccionado.getTipoRubro().getDescripcion());
			txtCantidad.requestFocus();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void limpiarRubro() {
		txtDescripcion.setText("");
		txtPrecio.setText("");
		txtTipoRubro.setText("");
		txtCantidad.setText("");
	}
}
