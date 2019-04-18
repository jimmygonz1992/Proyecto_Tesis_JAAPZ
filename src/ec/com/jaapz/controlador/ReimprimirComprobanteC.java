package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.Factura;
import ec.com.jaapz.modelo.FacturaDAO;
import ec.com.jaapz.modelo.FacturaDetalle;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.PrintReport;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
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

public class ReimprimirComprobanteC {
	@FXML private TextField txtNumMedidor;
	@FXML private TextField txtCedula;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtIdCuenta;
	@FXML private TextField txtCliente;
	@FXML private TextField txtNumComprobante;
	@FXML private TextField txtFechaPago;
	@FXML private TextField txtMontoPago;
	
	@FXML private Button btnBuscaPago;
	@FXML private Button btnImprimir;
	@FXML private Button btnNuevo;
	private @FXML TableView<FacturaDetalle> tvDetallePago;
	
	ControllerHelper helper = new ControllerHelper();
	Factura facturaSeleccionada = new Factura();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	FacturaDAO facturaDao = new FacturaDAO();
	
	public void initialize(){
		try {
			Context.getInstance().setFactura(null);
			noEditable();
			
			//solo numeros
			txtIdCuenta.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtIdCuenta.setText(oldValue);
					}
				}
			});
			
			//solo numeros
			txtNumComprobante.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtNumComprobante.setText(oldValue);
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void noEditable() {
		try {
			txtCedula.setEditable(false);
			txtCliente.setEditable(false);
			txtDireccion.setEditable(false);
			txtFechaPago.setEditable(false);
			txtMontoPago.setEditable(false);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo() {
		txtNumMedidor.setText("");
		txtIdCuenta.setText("");
		txtNumComprobante.setText("");
		txtCedula.setText("");
		txtCliente.setText("");
		txtFechaPago.setText("");
		txtDireccion.setText("");
		txtMontoPago.setText("");
		tvDetallePago.getItems().clear();
		tvDetallePago.getColumns().clear();
		facturaSeleccionada = null;
	}
	
	public void imprimir() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("numComprobante", txtNumComprobante.getText());
			param.put("cedula", facturaSeleccionada.getCuentaCliente().getIdCuenta());
			pr.crearReporte("/recursos/informes/comprobante.jasper", facturaDao, param);
			pr.showReport("Comprobante de Pago");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarPago() {
		try{
			helper.abrirPantallaModal("/recaudaciones/RecaudacionesPlanillasCobradas.fxml","Planillas Cobrabas", Context.getInstance().getStage());
			if (Context.getInstance().getFactura() != null) {
				facturaSeleccionada = Context.getInstance().getFactura();
				llenarDatos(facturaSeleccionada);
				Context.getInstance().setFactura(null);				
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	public void llenarDatos(Factura datoSeleccionado){
		try {				
			if(datoSeleccionado.getCuentaCliente().getMedidor() != null) {
				if(datoSeleccionado.getCuentaCliente().getMedidor().getCodigo() == null)
					txtNumMedidor.setText("");
				else
					txtNumMedidor.setText(datoSeleccionado.getCuentaCliente().getMedidor().getCodigo());
			}else {
				txtNumMedidor.setText("No Asignado");
			}

			if(datoSeleccionado.getCuentaCliente().getIdCuenta() == null)
				txtIdCuenta.setText("");
			else
				txtIdCuenta.setText(String.valueOf(datoSeleccionado.getCuentaCliente().getIdCuenta()));
			
			if(datoSeleccionado.getNumFactura() == null)
				txtNumComprobante.setText("");
			else
				txtNumComprobante.setText(datoSeleccionado.getNumFactura());
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getCedula() == null)
				txtCedula.setText("");
			else
				txtCedula.setText(datoSeleccionado.getCuentaCliente().getCliente().getCedula());
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getNombre() + " " + datoSeleccionado.getCuentaCliente().getCliente().getApellido() == null)
				txtCliente.setText("");
			else
				txtCliente.setText(datoSeleccionado.getCuentaCliente().getCliente().getNombre() + " " + datoSeleccionado.getCuentaCliente().getCliente().getApellido());
			
			if(datoSeleccionado.getFecha() == null)
				txtFechaPago.setText("");
			else
				txtFechaPago.setText(formateador.format(datoSeleccionado.getFecha()));

			if(datoSeleccionado.getCuentaCliente().getDireccion() == null)
				txtDireccion.setText("");
			else
				txtDireccion.setText(datoSeleccionado.getCuentaCliente().getDireccion());
			
			txtMontoPago.setText(String.valueOf(datoSeleccionado.getTotalFactura()));
			recuperarDetalleFactura(datoSeleccionado);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetalleFactura(Factura fact) {
		List<FacturaDetalle> detalle = new ArrayList<FacturaDetalle>();
		ObservableList<FacturaDetalle> datos = FXCollections.observableArrayList();
		tvDetallePago.getColumns().clear();
		tvDetallePago.getItems().clear();
		for(FacturaDetalle detallePrevia : fact.getFacturaDetalles()) {
			FacturaDetalle detAdd = new FacturaDetalle();
			detAdd.setFactura(detallePrevia.getFactura());
			detAdd.setIdDetalleFac(detallePrevia.getIdDetalleFac());
			detAdd.setPlanilla(detallePrevia.getPlanilla());
			detAdd.setSubtotal(detallePrevia.getSubtotal());
			detAdd.setEstado(detallePrevia.getEstado());
			detalle.add(detAdd);
		}
		datos.setAll(detalle);
		//llenar los datos en la tabla			
		TableColumn<FacturaDetalle, String> descripcionColum = new TableColumn<>("Descripción");
		descripcionColum.setMinWidth(10);
		descripcionColum.setPrefWidth(80);
		descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
				String descripcion = "";
				if(param.getValue().getPlanilla().getIdentInstalacion() != null) {
					if(param.getValue().getPlanilla().getIdentInstalacion().equals(Constantes.IDENT_INSTALACION))
						descripcion = "Por instalacion de nuevo medidor";
					else if(param.getValue().getPlanilla().getIdentInstalacion().equals(Constantes.IDENT_REPARACION))
						descripcion = "Por reparacion en el servicio";
				}
				else
					descripcion = "Factura mes de: " + String.valueOf(param.getValue().getPlanilla().getAperturaLectura().getMe());
				return new SimpleObjectProperty<String>(descripcion);
			}
		});

		TableColumn<FacturaDetalle, String> idColum = new TableColumn<>("IdPlanilla");
		idColum.setMinWidth(10);
		idColum.setPrefWidth(90);
		idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPlanilla().getIdPlanilla()));
			}
		});

		TableColumn<FacturaDetalle, String> subtotalColum = new TableColumn<>("Subtotal");
		subtotalColum.setMinWidth(10);
		subtotalColum.setPrefWidth(90);
		subtotalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSubtotal()));
			}
		});

		TableColumn<FacturaDetalle, String> totalColum = new TableColumn<>("Estado");
		totalColum.setMinWidth(10);
		totalColum.setPrefWidth(90);
		totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getEstado()));
			}
		});

		tvDetallePago.getColumns().addAll(idColum, descripcionColum, subtotalColum, totalColum);
		tvDetallePago.setItems(datos);
	}
}