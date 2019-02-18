package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.Kardex;
import ec.com.jaapz.modelo.KardexDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class BodegaKardexC {
	@FXML private TextField txtBuscar;
	private @FXML TableView<Kardex> tvDatos;
	KardexDAO kardexDao = new KardexDAO();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

	public void initialize(){
		llenarTablaKardex("");
	}

	public void buscarCliente() {
		llenarTablaKardex(txtBuscar.getText());
	}

	@SuppressWarnings("unchecked")
	void llenarTablaKardex(String patron) {
		try{
			tvDatos.getColumns().clear();
			List<Kardex> listaKardexVisualizar = new ArrayList<Kardex>();
			listaKardexVisualizar = kardexDao.getListaKardex(patron);

			ObservableList<Kardex> datos = FXCollections.observableArrayList();
			datos.setAll(listaKardexVisualizar);

			//llenar los datos en la tabla
			TableColumn<Kardex, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdKardex()));
				}
			});

			TableColumn<Kardex, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(70);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});

			TableColumn<Kardex, String> documentoColum = new TableColumn<>("Documento");
			documentoColum.setMinWidth(10);
			documentoColum.setPrefWidth(110);
			documentoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getTipoDocumento() + " " + param.getValue().getNumDocumento());
				}
			});

			TableColumn<Kardex, String> operacionColum = new TableColumn<>("Operación");
			operacionColum.setMinWidth(10);
			operacionColum.setPrefWidth(140);
			operacionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getDetalleOperacion());
				}
			});

			TableColumn<Kardex, String> unidadColum = new TableColumn<>("Unidad");
			unidadColum.setMinWidth(10);
			unidadColum.setPrefWidth(55);
			unidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getUnidadMedida());
				}
			});
			TableColumn<Kardex, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(65);
			cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
				}
			});

			TableColumn<Kardex, String> valorUColum = new TableColumn<>("Valor U.");
			valorUColum.setMinWidth(10);
			valorUColum.setPrefWidth(65);
			valorUColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getValorUnitario()));
				}
			});

			TableColumn<Kardex, String> costoTColum = new TableColumn<>("Total");
			costoTColum.setMinWidth(10);
			costoTColum.setPrefWidth(65);
			costoTColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCostoTotal()));
				}
			});
			
			/*TableColumn<Kardex, String> cantidadEgrColum = new TableColumn<>("Cantidad");
			cantidadEgrColum.setMinWidth(10);
			cantidadEgrColum.setPrefWidth(65);
			cantidadEgrColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
				}
			});

			TableColumn<Kardex, String> valorUEgrColum = new TableColumn<>("Valor U.");
			valorUEgrColum.setMinWidth(10);
			valorUEgrColum.setPrefWidth(65);
			valorUEgrColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getValorUnitario()));
				}
			});

			TableColumn<Kardex, String> costoTEgrColum = new TableColumn<>("Total");
			costoTEgrColum.setMinWidth(10);
			costoTEgrColum.setPrefWidth(65);
			costoTEgrColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Kardex,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Kardex, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCostoTotal()));
				}
			});*/
			
			//tvDatos.getColumns().addAll(fechaColum, documentoColum, operacionColum, cantidadColum, valorUColum, costoTColum, cantidadEgrColum, valorUEgrColum, costoTEgrColum);
			tvDatos.getColumns().addAll(fechaColum, documentoColum, operacionColum, cantidadColum, valorUColum, costoTColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}