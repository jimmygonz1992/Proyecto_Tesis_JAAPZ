package ec.com.jaapz.controlador;

import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.SegPerfil;
import ec.com.jaapz.modelo.SegPerfilDAO;
import ec.com.jaapz.modelo.SegUsuarioPerfil;
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

public class SeguridadListaPerfilC {
	@FXML private TableView<SegPerfil> tvDatos;
	SegPerfilDAO perfilDAO = new SegPerfilDAO();
	List<SegUsuarioPerfil> listaPerfiles = new ArrayList<SegUsuarioPerfil>();
	
	public void initialize() {
		try {
			listaPerfiles = Context.getInstance().getListaPerfiles();
			Context.getInstance().setPerfil(null);
			
			llenarDatos();
			tvDatos.setRowFactory(tv -> {
				TableRow<SegPerfil> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
						if(tvDatos.getSelectionModel().getSelectedItem() != null){
							Context.getInstance().setPerfilSeleccionado(tvDatos.getSelectionModel().getSelectedItem());
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
	void llenarDatos(){
		try{
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<SegPerfil> listaPerfil;
			ObservableList<SegPerfil> datos = FXCollections.observableArrayList();
			listaPerfil = perfilDAO.getListaPerfil();
			datos.setAll(listaPerfil);
			for(SegUsuarioPerfil perfil : listaPerfiles) {
				for(int i = 0 ; i < datos.size() ; i ++) {
					if(perfil.getSegPerfil().getIdPerfil() == datos.get(i).getIdPerfil())
						datos.remove(i);
				}
			}
			//llenar los datos en la tabla
			TableColumn<SegPerfil, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegPerfil,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegPerfil, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdPerfil()));
				}
			});
			TableColumn<SegPerfil, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(150);
			descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegPerfil,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegPerfil, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getNombre());
				}
			});

			TableColumn<SegPerfil, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegPerfil,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegPerfil, String> param) {
					String estado;
					if(param.getValue().getEstado().equals("A"))
						estado = "Activo";
					else
						estado = "Inactivo";
					return new SimpleObjectProperty<String>(estado);
				}
			});

			tvDatos.getColumns().addAll(idColum, descripcionColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
}
