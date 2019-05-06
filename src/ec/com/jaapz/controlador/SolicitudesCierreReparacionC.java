package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.SolInspeccionRep;
import ec.com.jaapz.modelo.SolInspeccionRepDAO;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class SolicitudesCierreReparacionC {

	@FXML private TableView<SolInspeccionRep> tvDatos;
	@FXML private TextField txtBuscar;
	@FXML private Button btnImprimirFicha;
	@FXML private Button btnRealizarCierre;
	
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	SolInspeccionRepDAO reparacionDAO = new SolInspeccionRepDAO();
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
	public void realizarCierre() {
		try {
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	public void imprimirFicha() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar registro", Context.getInstance().getStage());
				return;
			}
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ID_CUENTA", tvDatos.getSelectionModel().getSelectedItem().getCuentaCliente().getIdCuenta());
			param.put("ID_INSPECCION", tvDatos.getSelectionModel().getSelectedItem().getIdSolicitudRep());
			param.put("referencia", tvDatos.getSelectionModel().getSelectedItem().getReferencia());
			param.put("USUARIO_RESPONSABLE", Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
			if(tvDatos.getSelectionModel().getSelectedItem().getCuentaCliente().getCategoria().equals(Constantes.CAT_VIVIENDA))
				param.put("vivienda", "X");
			else
				param.put("vivienda", "");
			if(tvDatos.getSelectionModel().getSelectedItem().getCuentaCliente().getCategoria().equals(Constantes.CAT_COMERCIAL))
				param.put("comercial", "X");
			else
				param.put("comercial", "");
			param.put("LATITUD", tvDatos.getSelectionModel().getSelectedItem().getCuentaCliente().getLatitud());
			param.put("LONGITUD", tvDatos.getSelectionModel().getSelectedItem().getCuentaCliente().getLongitud());
			if(tvDatos.getSelectionModel().getSelectedItem().getCuentaCliente().getCategoria().equals(Constantes.CAT_ESTABLECIMIENTO))
				param.put("publico", "X");
			else
				param.put("publico", "");

			param.put("fecha_inspeccion", formateador.format(tvDatos.getSelectionModel().getSelectedItem().getFecha()));

			PrintReport printReport = new PrintReport();
			printReport.crearReporte("/recursos/informes/ficha_inspeccion_reparacion.jasper", reparacionDAO, param);
			printReport.showReport("Ficha de Inspección de Reparación");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	void llenarTablaInspecciones(String patron) {
		try{
			tvDatos.getColumns().clear();
			List<SolInspeccionRep> listaInspecciones;
			if(Context.getInstance().getIdPerfil() == 1) {
				listaInspecciones = reparacionDAO.getListaInspeccionPendiente(patron);
			}else {
				listaInspecciones = reparacionDAO.getListaInspeccionPerfilPendiente(patron);
			}
			/*
			for(SolInspeccionRep inspeccionAdd : listaInspecciones) {
				bandera = false;
				for(SolInspeccionRep inspeccionLst : listadoInspecciones) {
					if(inspeccionAdd.getIdSolicitudRep() == inspeccionLst.getIdSolicitudRep())
						bandera = true;
				}
				if(bandera == false)
					listaAgregar.add(inspeccionAdd);
			}
			*/
			ObservableList<SolInspeccionRep> datos = FXCollections.observableArrayList();
			datos.setAll(listaInspecciones);

			//llenar los datos en la tabla
			TableColumn<SolInspeccionRep, String> idColum = new TableColumn<>("Id");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(70);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdSolicitudRep()));
				}
			});
			
			TableColumn<SolInspeccionRep, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(90);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});

			TableColumn<SolInspeccionRep, String> cedulaColum = new TableColumn<>("Cédula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(80);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getCedula();
					return new SimpleObjectProperty<String>(cliente);
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
			
			
			TableColumn<SolInspeccionRep, String> referenciaColum = new TableColumn<>("Referencia domiciliaria");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(270);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String cliente = "";
					cliente = param.getValue().getReferencia();
					return new SimpleObjectProperty<String>(cliente);
				}
			});
			
			TableColumn<SolInspeccionRep, String> telfColum = new TableColumn<>("Telf. Contacto");
			telfColum.setMinWidth(10);
			telfColum.setPrefWidth(80);
			telfColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String cliente = "";
					cliente = param.getValue().getTelfContacto();
					return new SimpleObjectProperty<String>(cliente);
				}
			});
			//falta recuperar referencia no lo he hecho xq no está en la BD ese campo

			TableColumn<SolInspeccionRep, String> estadoColum = new TableColumn<>("Estado Inspección");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(150);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstadoInspecRep());
				}
			});

			tvDatos.getColumns().addAll(idColum, fechaColum,cedulaColum, clienteColum,referenciaColum,telfColum, estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}