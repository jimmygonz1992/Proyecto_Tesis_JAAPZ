package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDetalle;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class PlanillasImpresionC {
	@FXML private CheckBox chkTodo;
	@FXML private Button btnImprimirPlanilla;
	@FXML private Button btnBuscarApertura;
	@FXML private TableView<PlanillaDetalle> tvDatos;
	@FXML private Button btnEnviarCorreo;
	@FXML private TextField txtMes;
	@FXML private TextField txtAnio;
	@FXML private Button btnImprimirEnviar;
	@FXML private TextField txtFecha;

	AperturaLectura aperturaActual = new AperturaLectura();
	ControllerHelper helper = new ControllerHelper();
	private AperturaLectura aperturaSeleccionada = new AperturaLectura();

	public void initialize() {
		try {
			tvDatos.setEditable(true);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarApertura() {
		try {
			helper.abrirPantallaModal("/planillas/PlanillasListaApertura.fxml","Aperturas Realizadas", Context.getInstance().getStage());
			if(Context.getInstance().getApertura() != null) {
				aperturaSeleccionada = Context.getInstance().getApertura();
				Context.getInstance().setApertura(null);
				recuperarAperturaSeleccionada(aperturaSeleccionada);//recupera los datos de la asignacion seleccionada
			}else {
				txtAnio.setText("");
				txtMes.setText("");
				txtFecha.setText("");
				Context.getInstance().setApertura(null);
				tvDatos.getItems().clear();
				tvDatos.getColumns().clear();
				aperturaSeleccionada = null;
				aperturaActual = null;
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void recuperarAperturaSeleccionada(AperturaLectura aperturaSeleccionada) {
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
			txtAnio.setText(String.valueOf(aperturaSeleccionada.getAnio().getDescripcion()));
			txtMes.setText(String.valueOf(aperturaSeleccionada.getMe().getDescripcion()));
			txtFecha.setText(formateador.format(aperturaSeleccionada.getFecha()));
			if(aperturaSeleccionada.getPlanillas().size() != 0) {
				aperturaActual = aperturaSeleccionada;
				cargarClientes();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private void cargarClientes() {
    	try {
    		tvDatos.getItems().clear();
    		tvDatos.getColumns().clear();
			List<PlanillaDetalle> listaDetalle = new ArrayList<PlanillaDetalle>();
			ObservableList<PlanillaDetalle> datos = FXCollections.observableArrayList();
			for(Planilla planilla : aperturaActual.getPlanillas()) {
				listaDetalle.add(planilla.getPlanillaDetalles().get(0));
			}
			datos.setAll(listaDetalle);
			
			//llenar los datos en la tabla
			TableColumn<PlanillaDetalle, String> medidorColum = new TableColumn<>("Cód. Medidor");
			medidorColum.setMinWidth(10);
			medidorColum.setPrefWidth(90);
			medidorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					String medidor = "";
					if(param.getValue().getPlanilla().getCuentaCliente().getMedidor() != null)
						medidor = param.getValue().getPlanilla().getCuentaCliente().getMedidor().getCodigo();
					else
						medidor = "NO ASIGNADO";
					return new SimpleObjectProperty<String>(medidor);
				}
			});
			
			TableColumn<PlanillaDetalle, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(200);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					String nombre = param.getValue().getPlanilla().getCuentaCliente().getCliente().getNombre();
					String apellido = param.getValue().getPlanilla().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(nombre + " " + apellido);
				}
			});

			TableColumn<PlanillaDetalle, String> antColum = new TableColumn<>("Lec.  Anterior");
			antColum.setMinWidth(10);
			antColum.setPrefWidth(90);
			antColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPlanilla().getLecturaAnterior()));
				}
			});
			TableColumn<PlanillaDetalle, String> actColum = new TableColumn<>("Lec. Actual");
			actColum.setMinWidth(10);
			actColum.setPrefWidth(90);
			actColum.setCellFactory(TextFieldTableCell.<PlanillaDetalle>forTableColumn());
			actColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPlanilla().getLecturaActual()));
				}
			});
			
			TableColumn invitedCol = new TableColumn<PlanillaDetalle, Boolean>();

	        invitedCol.setText("Invited");

	        invitedCol.setMinWidth(50);


	        invitedCol.setCellFactory(new Callback<TableColumn<PlanillaDetalle, Boolean>, TableCell<PlanillaDetalle, Boolean>>() {

	 

	            public TableCell<PlanillaDetalle, Boolean> call(TableColumn<PlanillaDetalle, Boolean> p) {

	                return new CheckBoxTableCell<PlanillaDetalle, Boolean>();

	            }

	        });

	        
			
			tvDatos.getColumns().addAll(medidorColum,clienteColum,antColum,actColum,invitedCol);
			tvDatos.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
    }
	public void imprimirPlanilla() {

	}

	public void enviarCorreo() {

	}

	public void imprimirEnviar() {

	}

}
