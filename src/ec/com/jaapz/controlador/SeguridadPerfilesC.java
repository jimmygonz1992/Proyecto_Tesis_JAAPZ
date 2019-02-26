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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SeguridadPerfilesC {
	@FXML TableView<SegPerfil> tvDatos;
	@FXML TextField txtCodigo;
	@FXML TextField txtNombre;
	@FXML TextField txtDescripcion;
	@FXML CheckBox chkEstado;
	@FXML Button btnAceptar;
	@FXML Button btnNuevo;
	
	ControllerHelper helper = new ControllerHelper();
	SegPerfilDAO segPerfilDAO = new SegPerfilDAO();
	SegPermisoDAO permisoDAO = new SegPermisoDAO();
	SegMenuDAO menuDAO = new SegMenuDAO();
	
	public void initialize(){
		btnAceptar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
		
		limpiar();
		llenarDatos();
		//solo letras mayusculas
		txtDescripcion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtDescripcion.getText().toUpperCase();
				txtDescripcion.setText(cadena);
			}
		});
		
		txtNombre.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtNombre.getText().toUpperCase();
				txtNombre.setText(cadena);
			}
		});
		
		tvDatos.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				recuperarDatos(tvDatos.getSelectionModel().getSelectedItem().getIdPerfil());
			}
		});
	}
	
	void limpiar(){
		txtCodigo.setText("0");
		txtNombre.setText("");
		txtDescripcion.setText("");
		chkEstado.setSelected(true);
	}
	
	public void recuperarDatos(int codigo){
		try{
			List<SegPerfil> listaPerfil = new ArrayList<SegPerfil>();
			listaPerfil = segPerfilDAO.getPerfil(codigo);
			for(int i = 0 ; i < listaPerfil.size() ; i ++) {
				
				txtCodigo.setText(String.valueOf(listaPerfil.get(i).getIdPerfil()));
				txtNombre.setText(listaPerfil.get(i).getNombre());
				txtDescripcion.setText(listaPerfil.get(i).getDescripcion());
				if(listaPerfil.get(i).getEstado().equals("A"))
					chkEstado.setSelected(true);
				else
					chkEstado.setSelected(false);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(){
		try {
			tvDatos.getColumns().clear();
			List<SegPerfil> listaPerfiles = new ArrayList<SegPerfil>();
			listaPerfiles = segPerfilDAO.getAllListaPerfil();

			ObservableList<SegPerfil> datos = FXCollections.observableArrayList();
			datos.addAll(listaPerfiles);

			//llenar los datos en la tabla
			TableColumn<SegPerfil, String> idColum = new TableColumn<>("Codigo");
			idColum.setMinWidth(90);
			idColum.setCellValueFactory(new PropertyValueFactory<SegPerfil, String>("idPerfil"));
			TableColumn<SegPerfil, String> descripcionColum = new TableColumn<>("Nombre");
			descripcionColum.setMinWidth(200);
			descripcionColum.setCellValueFactory(new PropertyValueFactory<SegPerfil, String>("nombre"));
			TableColumn<SegPerfil, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(50);
			estadoColum.setCellValueFactory(new PropertyValueFactory<SegPerfil, String>("estado"));
			tvDatos.getColumns().addAll(idColum, descripcionColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo(){
		limpiar();
	}

	boolean validarDatos() {
		try {
			if(txtNombre.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar el nombre del Usuario", Context.getInstance().getStage());
				txtNombre.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void grabar(){
		try{
			if(validarDatos() == false)
				return;
			String estado = "";
			boolean band = false;
			SegPerfil perfil = new SegPerfil();
			if(chkEstado.isSelected() == true)
				estado = "A";
			else
				estado = "I";
			perfil.setEstado(estado);
			perfil.setDescripcion(txtDescripcion.getText());
			perfil.setNombre(txtNombre.getText());
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				segPerfilDAO.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")){//inserta
					band = true;//es para saber q se esta realizando una insercion de un nuevo perfil.. para proceder a asignarle los menus padres
					perfil.setIdPerfil(null);
					segPerfilDAO.getEntityManager().persist(perfil);
				}else {//modifica
					perfil.setIdPerfil(Integer.parseInt(txtCodigo.getText()));
					segPerfilDAO.getEntityManager().merge(perfil);
				}
				segPerfilDAO.getEntityManager().getTransaction().commit();
				
				if (band == true){
					List<SegPerfil> ultimoPerfil = new ArrayList<SegPerfil>();
					List<SegMenu> listaMenuPadre = menuDAO.getMenuPadre();
					ultimoPerfil = segPerfilDAO.getUltimoPerfil();
					permisoDAO.getEntityManager().getTransaction().begin();
					
					for(int i = 0 ; i < listaMenuPadre.size() ; i ++) {
						SegPermiso accesoAnadir = new SegPermiso();
						accesoAnadir.setSegMenu(listaMenuPadre.get(i));
						accesoAnadir.setSegPerfil(ultimoPerfil.get(0));
						accesoAnadir.setEstado("A");
						//accesoAnadir.setEstado("I");
						accesoAnadir.setIdPermiso(null);
						permisoDAO.getEntityManager().persist(accesoAnadir);
					}
					permisoDAO.getEntityManager().getTransaction().commit();
				}
				helper.mostrarAlertaInformacion("Datos Grabados con exito!!!", Context.getInstance().getStage());
				llenarDatos();
				limpiar();
			}
		}catch(Exception ex){
			segPerfilDAO.getEntityManager().getTransaction().rollback();
			permisoDAO.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
}