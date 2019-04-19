package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.List;

import ec.com.jaapz.modelo.Factura;
import ec.com.jaapz.modelo.FacturaDAO;
import ec.com.jaapz.util.Context;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class RecaudacionesFacturasCobradasC {
	@FXML private TextField txtBuscar;
	@FXML private TableView<Factura> tvDatos;
	FacturaDAO facturaDao = new FacturaDAO();
	SimpleDateFormat formateador= new SimpleDateFormat("dd/MM/yyyy");
	public void initialize() {
		try {
			llenarDatos("");
			tvDatos.setRowFactory(tv -> {
				TableRow<Factura> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
						if(tvDatos.getSelectionModel().getSelectedItem() != null){
							Context.getInstance().setFactura(tvDatos.getSelectionModel().getSelectedItem());
							Context.getInstance().getStageModal().close();
						}
					}
				});
				return row ;
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarCliente() {
		llenarDatos(txtBuscar.getText());
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(String patron) {
		try{
			tvDatos.getColumns().clear();
			List<Factura> listaFacturas;
			listaFacturas = facturaDao.getListaFacturasCobradas(patron);
			ObservableList<Factura> datosFactura = FXCollections.observableArrayList();
			datosFactura.setAll(listaFacturas);
			
			//llenar los datos en la tabla
			TableColumn<Factura, String> idColum = new TableColumn<>("ID");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(80);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdFactura()));
				}
			});
			
			TableColumn<Factura, String> numFactColum = new TableColumn<>("# Factura");
			numFactColum.setMinWidth(10);
			numFactColum.setPrefWidth(80);
			numFactColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getNumFactura()));
				}
			});
			
			TableColumn<Factura, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(80);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});
			
			TableColumn<Factura, String> cedulaColum = new TableColumn<>("Cédula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(80);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCuentaCliente().getCliente().getCedula()));
				}
			});
			
			TableColumn<Factura, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(80);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido()));
				}
			});
			
			TableColumn<Factura, String> medidorColum = new TableColumn<>("Medidor");
			medidorColum.setMinWidth(10);
			medidorColum.setPrefWidth(80);
			medidorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					String codigoMedidor =  "";
					if(param.getValue().getCuentaCliente().getMedidor() != null)
						codigoMedidor = String.valueOf(param.getValue().getCuentaCliente().getMedidor().getCodigo());
					return new SimpleObjectProperty<String>(codigoMedidor);
				}
			});
			
			TableColumn<Factura, String> direccionColum = new TableColumn<>("Dirección");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(80);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCuentaCliente().getDireccion()));
				}
			});
			
			TableColumn<Factura, String> totalFactColum = new TableColumn<>("Total Factura");
			totalFactColum.setMinWidth(10);
			totalFactColum.setPrefWidth(80);
			totalFactColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTotalFactura()));
				}
			});
			
			tvDatos.getColumns().addAll(idColum, numFactColum, fechaColum, cedulaColum, clienteColum, medidorColum, direccionColum, totalFactColum);
			tvDatos.setItems(datosFactura);
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}