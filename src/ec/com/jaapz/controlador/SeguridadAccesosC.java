package ec.com.jaapz.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.SegMenu;
import ec.com.jaapz.modelo.SegMenuDAO;
import ec.com.jaapz.modelo.SegPerfil;
import ec.com.jaapz.modelo.SegPerfilDAO;
import ec.com.jaapz.modelo.SegPermiso;
import ec.com.jaapz.modelo.SegPermisoDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class SeguridadAccesosC {
	@FXML private ComboBox<SegPerfil> cboPerfil;
	@FXML private Button btnGuardar;
	@FXML private TableView<SegMenu> tvMenu;
	@FXML private TableView<SegPermiso> tvPermiso;
	@FXML private Button btnAnadir;
	@FXML private Button btnQuitar;

	ControllerHelper helper = new ControllerHelper();
	SegPerfilDAO perfilDAO = new SegPerfilDAO();
	SegMenuDAO menuDAO = new SegMenuDAO();
	SegPermisoDAO permisoDAO = new SegPermisoDAO();
	SegPermiso permisoSeleccionado = new SegPermiso(); 
	List<SegPermiso> permisosSeleccionados = new ArrayList<SegPermiso>();

	public void initialize(){
		btnAnadir.setStyle("-fx-cursor: hand;");
		btnGuardar.setStyle("-fx-cursor: hand;");
		btnQuitar.setStyle("-fx-cursor: hand;");
		cboPerfil.setStyle("-fx-cursor: hand;");
		
		llenarComboPerfil();
		llenar_Datos();
	}

	public void grabar(){
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				permisoDAO.getEntityManager().getTransaction().begin();
				ObservableList<SegPermiso> datos = tvPermiso.getItems();
				//System.out.println(datos.size());
				for(int i = 0 ; i < datos.size() ; i++) {
					if(datos.get(i).getIdPermiso() == null)
						permisoDAO.getEntityManager().persist(datos.get(i));
					else
						permisoDAO.getEntityManager().merge(datos.get(i));	
				}
				permisoDAO.getEntityManager().getTransaction().commit();	
				helper.mostrarAlertaInformacion("Datos grabados Corectamente", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void llenarComboPerfil(){
		try{
			cboPerfil.setPromptText("Seleccionar perfil");
			List<SegPerfil> listaPerfiles;
			listaPerfiles = perfilDAO.getListaPerfil();
			ObservableList<SegPerfil> datos = FXCollections.observableArrayList();
			datos.addAll(listaPerfiles);
			cboPerfil.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void cargarAccesos() {
		try {
			cargarPermisosPerfil(cboPerfil.getSelectionModel().getSelectedItem().getIdPerfil());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarPermisosPerfil(int idPerfil) {
		try {
			List<SegPermiso> resultado = permisoDAO.getPermiso(idPerfil);
			if(resultado.size() > 0) {
				boolean bandera = false;
				List<SegMenu> listaMenus = menuDAO.getListaMenuAccesos();
				
				ObservableList<SegMenu> datos = FXCollections.observableArrayList();
				datos.setAll(listaMenus);
				ObservableList<SegMenu> datosMenu = FXCollections.observableArrayList();

				tvMenu.getColumns().clear();
				tvMenu.getItems().clear();

				//verificar si el menu esta asignado a un perfil
				for(int i = 0 ; i < datos.size() ; i ++) {
					bandera = false;
					for(int j = 0 ; j < resultado.size() ; j ++) {
						if(datos.get(i).getIdMenu().equals(resultado.get(j).getSegMenu().getIdMenu()))
							bandera = true;
					}
					if(bandera == false)
						datosMenu.add(datos.get(i));
				}

				//llenar los datos en la tabla
				TableColumn<SegMenu, String> idColum = new TableColumn<>("Código");
				idColum.setMinWidth(10);
				idColum.setPrefWidth(72);
				idColum.setCellValueFactory(new PropertyValueFactory<SegMenu, String>("idMenu"));

				TableColumn<SegMenu, String> nombreColum = new TableColumn<>("Nombre del Menu");
				nombreColum.setMinWidth(10);
				nombreColum.setPrefWidth(280);
				nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegMenu,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<SegMenu, String> param) {
						String dato = param.getValue().getDescripcion();
						List<SegMenu> listaMenuAll = new ArrayList<SegMenu>();
						listaMenuAll = menuDAO.getListaMenu();
						for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
							if(param.getValue().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
								dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
						}
						return new SimpleObjectProperty<String>(dato);
					}
				});

				tvMenu.getColumns().addAll(idColum,nombreColum);
				tvMenu.setItems(datosMenu);

			}else {
				tvMenu.getColumns().clear();
				llenar_Datos();
			}

			//recupera los asignados a esa persona
			if(resultado.size() > 0) {
				//llenar los datos en la tabla
				tvPermiso.getColumns().clear();
				tvPermiso.getItems().clear();
				List<SegPermiso> listaPermisos = resultado;
				ObservableList<SegPermiso> datos = FXCollections.observableArrayList();
				datos.setAll(listaPermisos);
				TableColumn<SegPermiso, String> idColum = new TableColumn<>("Código");
				idColum.setMinWidth(10);
				idColum.setPrefWidth(72);
				idColum.setCellValueFactory(new PropertyValueFactory<SegPermiso, String>("idPermiso"));

				TableColumn<SegPermiso, String> nombreColum = new TableColumn<>("Nombre del Menu");
				nombreColum.setMinWidth(10);
				nombreColum.setPrefWidth(280);
				nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegPermiso,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<SegPermiso, String> param) {
						String dato = param.getValue().getSegMenu().getDescripcion();
						List<SegMenu> listaMenuAll = new ArrayList<SegMenu>();
						listaMenuAll = menuDAO.getListaMenu();
						for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
							if(param.getValue().getSegMenu().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
								dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
						}
						return new SimpleObjectProperty<String>(dato);
					}
				});
				tvPermiso.getColumns().addAll(idColum,nombreColum);
				tvPermiso.setItems(datos);				
			}else {
				tvPermiso.getColumns().clear();
				tvPermiso.getItems().clear();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void anadir() {
		try {
			SegMenu menuSeleccionado = new SegMenu(); 
			menuSeleccionado = tvMenu.getSelectionModel().getSelectedItem();
			if(tvMenu.getSelectionModel().getSelectedItem() != null) {
				if (cboPerfil.getSelectionModel().getSelectedItem() != null){
					tvMenu.getItems().remove(menuSeleccionado);
					ObservableList<SegPermiso> datos = FXCollections.observableArrayList();
					datos.setAll(tvPermiso.getItems());

					tvPermiso.getColumns().clear();
					SegPermiso nuevo = new SegPermiso();
					nuevo.setSegMenu(menuSeleccionado);
					nuevo.setSegPerfil(cboPerfil.getSelectionModel().getSelectedItem());
					nuevo.setEstado("A");
					datos.add(nuevo);

					TableColumn<SegPermiso, String> idColum = new TableColumn<>("Código");
					idColum.setMinWidth(10);
					idColum.setPrefWidth(72);
					idColum.setCellValueFactory(new PropertyValueFactory<SegPermiso, String>("idPermiso"));

					TableColumn<SegPermiso, String> nombreColum = new TableColumn<>("Nombre del Menu");
					nombreColum.setMinWidth(10);
					nombreColum.setPrefWidth(280);
					nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegPermiso,String>, ObservableValue<String>>() {
						@Override
						public ObservableValue<String> call(CellDataFeatures<SegPermiso, String> param) {
							String dato = param.getValue().getSegMenu().getDescripcion();
							List<SegMenu> listaMenuAll = new ArrayList<SegMenu>();
							listaMenuAll = menuDAO.getListaMenu();
							for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
								if(param.getValue().getSegMenu().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
									dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
							}
							return new SimpleObjectProperty<String>(dato);
						}
					});
					tvPermiso.getColumns().addAll(idColum,nombreColum);
					tvPermiso.setItems(datos);
				}else {
					helper.mostrarAlertaAdvertencia("Debe Seleccionar un Perfil", Context.getInstance().getStage());
				}
			}else {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un item de Menú", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void agregarTodo() {
		try {
			List<SegMenu> listaMenus = new ArrayList<SegMenu>();
			listaMenus = menuDAO.getListaMenuAccesos();
			
			if (cboPerfil.getSelectionModel().getSelectedItem() != null) {
				tvMenu.getItems().removeAll(listaMenus);
				ObservableList<SegPermiso> datos = FXCollections.observableArrayList();
				datos.setAll(tvPermiso.getItems());
				
				tvPermiso.getColumns().clear();
				
				for (SegMenu menu : listaMenus) {
					SegPermiso nuevo = new SegPermiso();
					nuevo.setSegMenu(menu);
					nuevo.setSegPerfil(cboPerfil.getSelectionModel().getSelectedItem());
					nuevo.setEstado("A");
					datos.add(nuevo);
				}
				
				TableColumn<SegPermiso, String> idColum = new TableColumn<>("Código");
				idColum.setMinWidth(10);
				idColum.setPrefWidth(72);
				idColum.setCellValueFactory(new PropertyValueFactory<SegPermiso, String>("idPermiso"));
				
				TableColumn<SegPermiso, String> idMenuColum = new TableColumn<>("IdMenu");
				idMenuColum.setMinWidth(10);
				idMenuColum.setPrefWidth(90);
				idMenuColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegPermiso, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<SegPermiso, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSegMenu().getIdMenu()));
					}
				});

				TableColumn<SegPermiso, String> nombreColum = new TableColumn<>("Nombre del Menu");
				nombreColum.setMinWidth(10);
				nombreColum.setPrefWidth(280);
				nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegPermiso,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<SegPermiso, String> param) {
						String dato = param.getValue().getSegMenu().getDescripcion();
						List<SegMenu> listaMenuAll = new ArrayList<SegMenu>();
						listaMenuAll = menuDAO.getListaMenu();
						for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
							if(param.getValue().getSegMenu().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
								dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
						}
						return new SimpleObjectProperty<String>(dato);
					}
				});
				tvPermiso.getColumns().addAll(idColum, idMenuColum, nombreColum);
				tvPermiso.setItems(datos);
			}else {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un Perfil", Context.getInstance().getStage());
			}			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void quitar(){
		try {
			permisoSeleccionado = tvPermiso.getSelectionModel().getSelectedItem();
			if(tvPermiso.getSelectionModel().getSelectedItem() != null) {
				if (cboPerfil.getSelectionModel().getSelectedItem() != null){
					tvPermiso.getItems().remove(permisoSeleccionado);
					
					permisoSeleccionado.setEstado("I");
					permisosSeleccionados.add(permisoSeleccionado);
					
					ObservableList<SegMenu> datos = FXCollections.observableArrayList();
					datos.setAll(tvMenu.getItems());
					
					datos.add(permisoSeleccionado.getSegMenu());
					tvMenu.getColumns().clear();

					TableColumn<SegMenu, String> idColum = new TableColumn<>("Código");
					idColum.setMinWidth(10);
					idColum.setPrefWidth(72);
					idColum.setCellValueFactory(new PropertyValueFactory<SegMenu, String>("idMenu"));

					TableColumn<SegMenu, String> nombreColum = new TableColumn<>("Nombre del Formulario");
					nombreColum.setMinWidth(10);
					nombreColum.setPrefWidth(280);
					nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegMenu,String>, ObservableValue<String>>() {
						@Override
						public ObservableValue<String> call(CellDataFeatures<SegMenu, String> param) {
							String dato = param.getValue().getDescripcion();
							List<SegMenu> listaMenuAll = new ArrayList<SegMenu>();
							listaMenuAll = menuDAO.getListaMenu();
							for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
								if(param.getValue().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
									dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
							}
							return new SimpleObjectProperty<String>(dato);
						}
					});			
					tvMenu.getColumns().addAll(idColum,nombreColum);
					tvMenu.setItems(datos);
				}else {
					helper.mostrarAlertaAdvertencia("Debe Seleccionar un Perfil", Context.getInstance().getStage());
				}
			}else {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un item", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public void eliminarTodo() {
		try {
			permisosSeleccionados = tvPermiso.getSelectionModel().getSelectedItems();
			if(cboPerfil.getSelectionModel().getSelectedItem() != null) {
				tvPermiso.getItems().removeAll(permisosSeleccionados);
				ObservableList<SegMenu> datos = FXCollections.observableArrayList();
				datos.setAll(tvMenu.getItems());
				permisoSeleccionado.setEstado("I");
				permisosSeleccionados.add(permisoSeleccionado);
				tvMenu.getColumns().clear();
				for(SegPermiso permiso : permisosSeleccionados) {
					datos.add(permisoSeleccionado.getSegMenu());
				}
				
				TableColumn<SegMenu, String> idColum = new TableColumn<>("Código");
				idColum.setMinWidth(10);
				idColum.setPrefWidth(72);
				idColum.setCellValueFactory(new PropertyValueFactory<SegMenu, String>("idMenu"));

				TableColumn<SegMenu, String> nombreColum = new TableColumn<>("Nombre del Formulario");
				nombreColum.setMinWidth(10);
				nombreColum.setPrefWidth(280);
				nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegMenu,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegMenu, String> param) {
					String dato = param.getValue().getDescripcion();
					List<SegMenu> listaMenuAll = new ArrayList<SegMenu>();
					listaMenuAll = menuDAO.getListaMenu();
					for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
						if(param.getValue().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
							dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
					}
					return new SimpleObjectProperty<String>(dato);
				}
				});			
				tvMenu.getColumns().addAll(idColum,nombreColum);
				tvMenu.setItems(datos);
			}else {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un Perfil", Context.getInstance().getStage());
			}	
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	void llenar_Datos(){
		try{
			tvMenu.getItems().clear();
			tvMenu.getColumns().clear();
			List<SegMenu> ListaMenu = new ArrayList<SegMenu>();
			ListaMenu = menuDAO.getListaMenuAccesos();

			ObservableList<SegMenu> datos = FXCollections.observableArrayList();
			datos.setAll(ListaMenu);

			//llenar los datos en la tabla
			TableColumn<SegMenu, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(72);
			idColum.setCellValueFactory(new PropertyValueFactory<SegMenu, String>("idMenu"));

			TableColumn<SegMenu, String> nombreColum = new TableColumn<>("Nombre del Formulario");
			nombreColum.setMinWidth(10);
			nombreColum.setPrefWidth(280);
			nombreColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegMenu,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegMenu, String> param) {
					String dato = param.getValue().getDescripcion();
					List<SegMenu> listaMenuAll = new ArrayList<SegMenu>();
					listaMenuAll = menuDAO.getListaMenu();
					for(int j = 0 ; j < listaMenuAll.size() ; j ++) {
						if(param.getValue().getIdMenuPadre().equals(listaMenuAll.get(j).getIdMenu()))
							dato = listaMenuAll.get(j).getDescripcion() +  "/" + dato;
					}
					return new SimpleObjectProperty<String>(dato);
				}
			});			
			tvMenu.getColumns().addAll(idColum,nombreColum);
			tvMenu.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}