package ec.com.jaapz.controlador;

import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.AperturaLecturaDAO;
import ec.com.jaapz.modelo.Barrio;
import ec.com.jaapz.modelo.BarrioDAO;
import ec.com.jaapz.modelo.ResponsableLectura;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class LecturasBarriosC {
	@FXML TableView<Barrio> tvDatos;
	BarrioDAO barrioDAO = new BarrioDAO();
	List<Barrio> listadoBarrios = new ArrayList<Barrio>();
	AperturaLectura aperturaActiva = new AperturaLectura();
	AperturaLecturaDAO aperturaDAO = new AperturaLecturaDAO();
	
	
	public void initialize() {
		aperturaActiva = Context.getInstance().getApertura();
		Context.getInstance().setApertura(null);
		listadoBarrios = Context.getInstance().getListaBarrios();
		//limpiar la lsta de barrios
		Context.getInstance().setListaBarrios(null);
		llenarTablaBarrios();
		tvDatos.setRowFactory(tv -> {
            TableRow<Barrio> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
    					Context.getInstance().setBarrio(tvDatos.getSelectionModel().getSelectedItem());
    					Context.getInstance().getStageModal().close();
    				}
                }
            });
            return row ;
        });
	}
	@SuppressWarnings("unchecked")
	void llenarTablaBarrios() {
		try{
			tvDatos.getColumns().clear();
			boolean bandera;
			List<Barrio> listaBarrios = barrioDAO.getListaBarriosActivos();
			AperturaLectura apertura = aperturaDAO.getListaAperturaById(aperturaActiva.getIdApertura());
			
			for(ResponsableLectura responsable : apertura.getResponsableLecturas()) {
				if(responsable.getEstado().equals(Constantes.ESTADO_ACTIVO)) {
					for(int i = 0 ; i < listaBarrios.size() ; i ++) {
						if(listaBarrios.get(i).getIdBarrio() == responsable.getBarrio().getIdBarrio())
							listaBarrios.remove(i);
					}
				}
			}
			List<Barrio> listaAgregar = new ArrayList<Barrio>();

			for(Barrio barrioAdd : listaBarrios) {
				bandera = false;
				for(Barrio barrioLst : listadoBarrios) {
					if(barrioAdd.getIdBarrio() == barrioLst.getIdBarrio())
						bandera = true;
				}
				if(bandera == false)
					listaAgregar.add(barrioAdd);
			}
			
			ObservableList<Barrio> datos = FXCollections.observableArrayList();
			datos.setAll(listaAgregar);

			//llenar los datos en la tabla
			TableColumn<Barrio, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(80);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Barrio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Barrio, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdBarrio()));
				}
			});

			TableColumn<Barrio, String> nombreColum = new TableColumn<>("Nombre");
			nombreColum.setMinWidth(10);
			nombreColum.setPrefWidth(150);
			nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Barrio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Barrio, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getNombre());
				}
			});

			TableColumn<Barrio, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(250);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Barrio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Barrio, String> param) {
					String estado = "";
					if(param.getValue().getEstado().equals(Constantes.ESTADO_ACTIVO))
						estado = "Activo";
					else
						estado = "Inactivo";
					return new SimpleObjectProperty<String>(estado);
				}
			});

			tvDatos.getColumns().addAll(idColum,nombreColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
