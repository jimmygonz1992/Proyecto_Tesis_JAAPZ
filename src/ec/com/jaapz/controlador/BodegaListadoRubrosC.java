package ec.com.jaapz.controlador;

import java.util.List;

import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.PrintReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BodegaListadoRubrosC {
	private @FXML TableView<Rubro> tvDatos;
	private @FXML Button btnVerReporte;
	RubroDAO rubroDAO = new RubroDAO();
	public void initialize(){
		llenarDatos();
		tvDatos.setRowFactory(tv -> {
            TableRow<Rubro> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
    					Context.getInstance().setRubros(tvDatos.getSelectionModel().getSelectedItem());
    					Context.getInstance().getStageModal().close();
    				}
                }
            });
            return row ;
        });
	}
	
	public void verReporte() {
		PrintReport pr = new PrintReport();
		pr.crearReporte("/recursos/informes/listado_materiales.jasper", rubroDAO, null);
		pr.showReport("Listado");
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(){
		try{
			tvDatos.getColumns().clear();
			RubroDAO rubroDAO = new RubroDAO();
			List<Rubro> listaRubros;
			listaRubros = rubroDAO.getListaRubros();
			ObservableList<Rubro> datos = FXCollections.observableArrayList();
			datos.setAll(listaRubros);

			//llenar los datos en la tabla
			TableColumn<Rubro, String> idColum = new TableColumn<>("Codigo");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(60);
			idColum.setCellValueFactory(new PropertyValueFactory<Rubro, String>("idRubro"));
			TableColumn<Rubro, String> nombresColum = new TableColumn<>("Material");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new PropertyValueFactory<Rubro, String>("descripcion"));
			TableColumn<Rubro, String> marcaColum = new TableColumn<>("Marca");
			marcaColum.setMinWidth(10);
			marcaColum.setPrefWidth(100);
			marcaColum.setCellValueFactory(new PropertyValueFactory<Rubro, String>("marca"));
			TableColumn<Rubro, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellValueFactory(new PropertyValueFactory<Rubro, String>("stock"));
			TableColumn<Rubro, Double> fechColum = new TableColumn<>("Precio");
			fechColum.setMinWidth(10);
			fechColum.setPrefWidth(90);
			fechColum.setCellValueFactory(new PropertyValueFactory<Rubro, Double>("precio"));
			TableColumn<Rubro, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(50);
			estadoColum.setCellValueFactory(new PropertyValueFactory<Rubro, String>("estado"));
			tvDatos.getColumns().addAll(idColum, nombresColum,marcaColum,cantidadColum,fechColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}