package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.Ingreso;
import ec.com.jaapz.modelo.IngresoDAO;
import ec.com.jaapz.modelo.IngresoDetalle;
import ec.com.jaapz.modelo.Medidor;
import ec.com.jaapz.modelo.MedidorDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class BodegaListaIngresosC {
	@FXML private TableView<Ingreso> tvDatos;
	@FXML private TextField txtBuscar;
	MedidorDAO medidorDAO = new MedidorDAO();
	
	public void initialize() {
		try {
			llenarListaIngresos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						llenarListaIngresos(txtBuscar.getText());
					}
				}
			});
			tvDatos.setRowFactory(tv -> {
	            TableRow<Ingreso> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	                		Context.getInstance().setIngresoSeleccionado(tvDatos.getSelectionModel().getSelectedItem());
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
	@SuppressWarnings("unchecked")
	private void llenarListaIngresos(String codigo) {
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
			tvDatos.getColumns().clear();
			IngresoDAO ingresoDAO = new IngresoDAO();
			List<Ingreso> listaIngresos;
			listaIngresos = ingresoDAO.getAllFacturaTodo(codigo);
			ObservableList<Ingreso> datos = FXCollections.observableArrayList();

			//buscar solo las facturas con medidores
			List<Ingreso> listaIngresoMedidores = new ArrayList<Ingreso>();
			for(Ingreso ing : listaIngresos) {
				int cantidad = 0;
				
				for(IngresoDetalle detalle : ing.getIngresoDetalles()) {
					if(detalle.getRubro().getIdRubro() == Constantes.ID_MEDIDOR)
						cantidad = detalle.getCantidad();
				}
				System.out.println("cantidad de medidores: " + cantidad);
				if(cantidad > 0) {//tiene medidores.. hay q revisar si ya estan codificados
					int cont = 0;
					List<Medidor> listaMedidor = medidorDAO.getRecuperarMedidorFactura(ing.getIdIngreso());
					for(Medidor med : listaMedidor) {
						if(med.getCodigo() != null)
							cont ++;
					}
					
					System.out.println("Cantidad con codigo: " + cont);
					System.out.println("Cantidad medidores recuperados: " + listaMedidor.size());
					if(listaMedidor.size() > cont)
						listaIngresoMedidores.add(ing);
				}
			}
			datos.setAll(listaIngresoMedidores);
			
			//llenar los datos en la tabla
			TableColumn<Ingreso, String> idColum = new TableColumn<>("Id");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ingreso,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Ingreso, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdIngreso()));
				}
			});
			
			TableColumn<Ingreso, String> ingresoColum = new TableColumn<>("Num. Ingreso");
			ingresoColum.setMinWidth(10);
			ingresoColum.setPrefWidth(120);
			ingresoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ingreso,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Ingreso, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getNumeroIngreso()));
				}
			});
			
			TableColumn<Ingreso, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ingreso,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Ingreso, String> param) {
					return new SimpleObjectProperty<String>(formateador.format(param.getValue().getFecha()));
				}
			});
			
			TableColumn<Ingreso, String> proveedorColum = new TableColumn<>("Proveedor");
			proveedorColum.setMinWidth(10);
			proveedorColum.setPrefWidth(170);
			proveedorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ingreso,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Ingreso, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getProveedor().getNombreComercial());
				}
			});
			
			TableColumn<Ingreso, String> cantidadColum = new TableColumn<>("Cant. Medidores");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(170);
			cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ingreso,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Ingreso, String> param) {
					int cantidad = 0;
					for(IngresoDetalle detalle : param.getValue().getIngresoDetalles()) {
						if(detalle.getRubro().getIdRubro() == Constantes.ID_MEDIDOR)
							cantidad = detalle.getCantidad();
					}
					return new SimpleObjectProperty<String>(String.valueOf(cantidad));
				}
			});
			TableColumn<Ingreso, String> totalColum = new TableColumn<>("Total Factura");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(80);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ingreso,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Ingreso, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTotal()));
				}
			});
			
			tvDatos.getColumns().addAll(ingresoColum, fechaColum, proveedorColum,cantidadColum,totalColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
