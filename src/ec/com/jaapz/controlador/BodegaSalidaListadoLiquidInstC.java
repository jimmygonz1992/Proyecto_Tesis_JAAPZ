package ec.com.jaapz.controlador;

import java.util.List;

import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.modelo.LiquidacionOrdenDAO;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class BodegaSalidaListadoLiquidInstC {
	LiquidacionOrdenDAO liquidacionOrdenDao = new LiquidacionOrdenDAO();
	@FXML private TextField txtBuscar;
	@FXML private TableView<LiquidacionOrden> tvDatos;
	public void initialize() {
		llenarDatos("");
		tvDatos.setRowFactory(tv -> {
            TableRow<LiquidacionOrden> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
                		Context.getInstance().setLiquidaciones(tvDatos.getSelectionModel().getSelectedItem());
    					Context.getInstance().getStageModal().close();
    				}
                }
            });
            return row ;
        });
	}
	
	public void buscarLiquidCuenta() {
		llenarDatos(txtBuscar.getText());
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(String patron) {
		try{
			tvDatos.getColumns().clear();
			List<LiquidacionOrden> listaLiquidaciones;
			if(Context.getInstance().getIdPerfil() == 1) {
				listaLiquidaciones = liquidacionOrdenDao.getListaLiquidacionOrden(patron);
			}else {
				listaLiquidaciones = liquidacionOrdenDao.getListaLiquidacionOrdenPerfil(patron);
			}
			
			ObservableList<LiquidacionOrden> datosReq = FXCollections.observableArrayList();
			datosReq.setAll(listaLiquidaciones);

			//llenar los datos en la tabla
			TableColumn<LiquidacionOrden, String> idColum = new TableColumn<>("Nº");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(40);
			idColum.setCellValueFactory(new PropertyValueFactory<LiquidacionOrden, String>("idLiquidacion"));
			
			TableColumn<LiquidacionOrden, String> ordenColum = new TableColumn<>("Inspección");
			ordenColum.setMinWidth(10);
			ordenColum.setPrefWidth(40);
			ordenColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSolInspeccionIn().getIdSolInspeccion()));
				}
			});

			TableColumn<LiquidacionOrden, String> fechaOrdenColum = new TableColumn<>("Fecha de emisión");
			fechaOrdenColum.setMinWidth(10);
			fechaOrdenColum.setPrefWidth(80);
			fechaOrdenColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getFecha()));
				}
			});
			
			TableColumn<LiquidacionOrden, String> fechaInspColum = new TableColumn<>("Fecha de Inspección");
			fechaInspColum.setMinWidth(10);
			fechaInspColum.setPrefWidth(80);
			fechaInspColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSolInspeccionIn().getFechaIngreso()));
				}
			});
			
			TableColumn<LiquidacionOrden, String> cedulaColum = new TableColumn<>("Cédula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(80);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					//return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getJunOrdenPreviaDespacho().getJunInspeccionNuevoCliente().getFecha()));
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCuentaCliente().getCliente().getCedula()));
				}
			});
			
			TableColumn<LiquidacionOrden, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(240);
			clienteColum.setCellValueFactory(new PropertyValueFactory<LiquidacionOrden, String>("cuentaCliente"));
			
			TableColumn<LiquidacionOrden, String> direccionColum = new TableColumn<>("Dirección");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(80);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCuentaCliente().getDireccion()));
				}
			});
			
			TableColumn<LiquidacionOrden, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(80);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSolInspeccionIn().getReferencia()));
				}
			});
			
			TableColumn<LiquidacionOrden, String> estadoInspColum = new TableColumn<>("Estado Inspección");
			estadoInspColum.setMinWidth(10);
			estadoInspColum.setPrefWidth(80);
			estadoInspColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSolInspeccionIn().getEstadoInspeccion()));
				}
			});

			TableColumn<LiquidacionOrden, String> estadoOrdColum = new TableColumn<>("Estado Liquidacion");
			estadoOrdColum.setMinWidth(10);
			estadoOrdColum.setPrefWidth(85);
			estadoOrdColum.setCellValueFactory(new PropertyValueFactory<LiquidacionOrden, String>("estadoOrden"));
			
			tvDatos.getColumns().addAll(idColum, ordenColum, fechaOrdenColum, fechaInspColum, cedulaColum, clienteColum, direccionColum, referenciaColum, estadoInspColum, estadoOrdColum);
			tvDatos.setItems(datosReq);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}