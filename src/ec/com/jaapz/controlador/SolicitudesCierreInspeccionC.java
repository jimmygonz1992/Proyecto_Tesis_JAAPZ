package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.SolInspeccionIn;
import ec.com.jaapz.modelo.SolInspeccionInDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import ec.com.jaapz.util.PrintReport;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class SolicitudesCierreInspeccionC {
	@FXML private TableView<SolInspeccionIn> tvDatos;
	@FXML private TextField txtBuscar;
	@FXML private Button btnImprimirFicha;
	@FXML private Button btnRealizarCierre;

	SolInspeccionInDAO inspeccionDAO = new SolInspeccionInDAO();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		try {
			btnRealizarCierre.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
			btnImprimirFicha.setStyle("-fx-graphic: url('/imprimir.png');-fx-cursor: hand;");
			llenarTablaInspecciones("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void llenarTablaInspecciones(String patron) {
		try{
			tvDatos.getColumns().clear();
			List<SolInspeccionIn> listaInspecciones;
			
			if(Context.getInstance().getIdPerfil() == Constantes.ID_USU_ADMINISTRADOR) {
				listaInspecciones = inspeccionDAO.getListaInspeccion(patron);
			}else {
				listaInspecciones = inspeccionDAO.getListaInspeccionPerfil(patron);
			}
			
			
			ObservableList<SolInspeccionIn> datos = FXCollections.observableArrayList();
			datos.setAll(listaInspecciones);

			//llenar los datos en la tabla
			TableColumn<SolInspeccionIn, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdSolInspeccion());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionIn, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = String.valueOf(formateador.format(param.getValue().getFechaIngreso()));
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionIn, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String cliente = "";
					cliente = param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});
			
			TableColumn<SolInspeccionIn, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(350);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getReferencia());
				}
			});
			
			TableColumn<SolInspeccionIn, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstadoInspeccion());
				}
			});

			tvDatos.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void realizarCierre() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar la solicitud", Context.getInstance().getStage());
				return;
			}
			Context.getInstance().setInspeccion(tvDatos.getSelectionModel().getSelectedItem());
			helper.abrirPantallaModalSolicitud("/solicitudes/SolicitudesRealizarCierreIns.fxml","Listado de Órdenes", Context.getInstance().getStage());
			llenarTablaInspecciones("");
			Context.getInstance().setInspeccion(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void imprimirFicha() {
		try {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("ID_CLIENTE", tvDatos.getSelectionModel().getSelectedItem().getCliente().getIdCliente());
				param.put("ID_INSPECCION", tvDatos.getSelectionModel().getSelectedItem().getIdSolInspeccion());
				param.put("referencia", tvDatos.getSelectionModel().getSelectedItem().getReferencia());
				param.put("USUARIO_RESPONSABLE", Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
				if(tvDatos.getSelectionModel().getSelectedItem().getUsoMedidor().equals(Constantes.CAT_VIVIENDA))
					param.put("vivienda", "X");
				else
					param.put("vivienda", "");
				if(tvDatos.getSelectionModel().getSelectedItem().getUsoMedidor().equals(Constantes.CAT_COMERCIAL))
					param.put("comercial", "X");
				else
					param.put("comercial", "");
				param.put("LATITUD", "");
				param.put("LONGITUD", "");
				if(tvDatos.getSelectionModel().getSelectedItem().getUsoMedidor().equals(Constantes.CAT_ESTABLECIMIENTO))
					param.put("publico", "X");
				else
					param.put("publico", "");

				param.put("fecha_inspeccion", formateador.format(tvDatos.getSelectionModel().getSelectedItem().getFechaIngreso()));

				if(tvDatos.getSelectionModel().getSelectedItem().getFechaAprobacion() != null)
					param.put("fecha_aprobacion", formateador.format(tvDatos.getSelectionModel().getSelectedItem().getFechaAprobacion()));
				else
					param.put("fecha_aprobacion", "");

				if(tvDatos.getSelectionModel().getSelectedItem().getFactibilidad() == null) {
					param.put("reprobado", "");
					param.put("aprobado", "");
				}else if(tvDatos.getSelectionModel().getSelectedItem().getFactibilidad().equals(Constantes.EST_NO_FACTIBLE)) {
					param.put("reprobado", "X");
					param.put("aprobado", "");
				}
				else if(tvDatos.getSelectionModel().getSelectedItem().getFactibilidad().equals(Constantes.EST_FACTIBLE)) {
					param.put("aprobado", "X");
					param.put("reprobado", "");
				}
				PrintReport printReport = new PrintReport();
				printReport.crearReporte("/recursos/informes/ficha_inspeccion.jasper", inspeccionDAO, param);
				printReport.showReport("Ficha de Inspección");
			}catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
