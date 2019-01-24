package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.modelo.LiquidacionOrdenDAO;
import ec.com.jaapz.util.Constantes;
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

public class AsignacionListadoLiquidC {
	LiquidacionOrdenDAO liquidacionOrdenDao = new LiquidacionOrdenDAO();
	@FXML private TextField txtBuscar;
	@FXML private TableView<LiquidacionOrden> tvDatos;
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	List<LiquidacionOrden> listadoLiquidaciones = new ArrayList<LiquidacionOrden>();
	
	public void initialize() {
		listadoLiquidaciones = Context.getInstance().getListaLiquidaciones();
		//poner nuevamente a null
		Context.getInstance().getListaLiquidaciones().clear();
		llenarTablaLiquidaciones("");
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
		llenarTablaLiquidaciones(txtBuscar.getText());
	}
	
	@SuppressWarnings("unchecked")
	void llenarTablaLiquidaciones(String patron) {
		try{
			tvDatos.getColumns().clear();
			boolean bandera;
			List<LiquidacionOrden> listaLiquidaciones;
			List<LiquidacionOrden> listaAgregar = new ArrayList<LiquidacionOrden>();
			if(Context.getInstance().getIdPerfil() == Constantes.ID_USU_ADMINISTRADOR) {
				listaLiquidaciones = liquidacionOrdenDao.getListaAsignacionLiquidacionOrden(patron);
			}else {
				listaLiquidaciones = liquidacionOrdenDao.getListaAsignacionLiquidacionOrdenPerfil(patron);
			}
			for(LiquidacionOrden liquidacionAdd : listaLiquidaciones) {
				bandera = false;
				for(LiquidacionOrden liquidacionLst : listaLiquidaciones) {
					if(liquidacionAdd.getIdLiquidacion() == liquidacionLst.getIdLiquidacion())
						bandera = true;
				}
				if(bandera == false)
					listaAgregar.add(liquidacionAdd);
			}
			
			ObservableList<LiquidacionOrden> datos = FXCollections.observableArrayList();
			datos.setAll(listaAgregar);

			//llenar los datos en la tabla
			TableColumn<LiquidacionOrden, String> idColum = new TableColumn<>("Id");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(100);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdLiquidacion()));
				}
			});

			TableColumn<LiquidacionOrden, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});

			TableColumn<LiquidacionOrden, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});
			
			TableColumn<LiquidacionOrden, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(100);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getSolInspeccionIn().getReferencia());
				}
			});
			
			TableColumn<LiquidacionOrden, String> estadoColum = new TableColumn<>("Estado Inspección");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getSolInspeccionIn().getEstadoInspeccion());
				}
			});

			tvDatos.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}