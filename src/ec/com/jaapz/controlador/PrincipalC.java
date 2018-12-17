package ec.com.jaapz.controlador;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.SegMenu;
import ec.com.jaapz.modelo.SegMenuDAO;
import ec.com.jaapz.modelo.SegPermiso;
import ec.com.jaapz.modelo.SegPermisoDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PrincipalC {
	private @FXML AnchorPane rootPrincipal;
	private @FXML SplitPane sp_contenedor;
	private @FXML Label lblPerfil;
	private @FXML Label lblUsuario;
	private @FXML Label lblHora;
	private @FXML Label lblFecha;
	private @FXML AnchorPane ap_derecha;
	private @FXML AnchorPane ap_izquierda;
	private @FXML Accordion acd_menu;
	@FXML private ImageView ivLogo;
	private @FXML VBox vBoxMenu;


	Calendar fecha = new GregorianCalendar();
	ControllerHelper helper = new ControllerHelper();
	SegPermisoDAO permisoDAO = new SegPermisoDAO();
	SegMenuDAO menuDAO = new SegMenuDAO();
	Map<VBox,VBox> map = new HashMap<VBox,VBox>();

	int contador = 0,mayor = 0;
	public void initialize(){
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Calendar fecha = new GregorianCalendar();
		int anio,mes,dia;
		rootPrincipal.setPrefWidth(screenSize.width);
		rootPrincipal.setPrefHeight(screenSize.height);
		sp_contenedor.setDividerPositions(0.2);
		lblUsuario.setText(Context.getInstance().getUsuario());
		lblPerfil.setText(Context.getInstance().getPerfil());
		Image image = new Image("logo2.png", 220, 140, true, true);
		ivLogo.setImage(image);
		//ivLogo.setFitHeight(250);
		//ivLogo.setFitHeight(250);
		anio = fecha.get(Calendar.YEAR);
		mes = fecha.get(Calendar.MONTH);
		dia = fecha.get(Calendar.DAY_OF_MONTH);
		lblFecha.setText("" + dia + "/" + (mes + 1) + "/" + anio);
		llenarMenu();
	}
	
	void llenarMenu() {
		try {
			int dimesionIcono = 40;
			Button btnMenuPadre;
			Button btnMenuHijo;
			List<SegMenu> menuList;
			List<SegPermiso> listaAcceso = permisoDAO.getPermisoPerfil(Context.getInstance().getIdPerfil());
			List<SegMenu> menuListTitle = new ArrayList<SegMenu>();
			List<SegMenu> menuListCont = new ArrayList<SegMenu>();
			
			
			for(int i = 0 ; i < listaAcceso.size() ; i ++) {
				if(listaAcceso.get(i).getSegMenu().getIdMenuPadre() != 0) {
					//menuListTitle.add(listaAcceso.get(i).getSegMenu());
					menuListCont.add(listaAcceso.get(i).getSegMenu());
				}
			}
			boolean bandera = false;
			for(int i = 0 ; i < listaAcceso.size() ; i ++) {
				bandera = false;
				if(listaAcceso.get(i).getSegMenu().getIdMenuPadre() != 0) {
					List<SegMenu> menuPadre = menuDAO.getMenuPadreByIdMenu(listaAcceso.get(i).getSegMenu().getIdMenuPadre());
					if(menuPadre.size() > 0) {
						for(SegMenu s : menuListTitle) {
							if(s.getIdMenu() == menuPadre.get(0).getIdMenu())
								bandera = true;
						}
						if (bandera == false)
							menuListTitle.add(menuPadre.get(0));
					}
					//menuListCont.add(listaAcceso.get(i).getSegMenu());
					Collections.sort(menuListTitle);
				}
			}
			
			for(int i = 0 ; i < menuListTitle.size() ; i ++) {
				
				if(menuListTitle.get(i).getIdMenuPadre() == 0 && menuListTitle.get(i).getEstado().equals("A")) {//se pregunta si el menu es padre
					VBox vbMenu = new VBox();
					menuList = new ArrayList<SegMenu>();
					btnMenuPadre = new Button();
					btnMenuPadre.getStyleClass().add("botonMenuPadre");
					btnMenuPadre.setText(menuListTitle.get(i).getDescripcion());
					btnMenuPadre.setPrefHeight(dimesionIcono);
					if(menuListTitle.get(i).getIcono() != null) {
						Image imageBoton = new Image(getClass().getResourceAsStream(menuListTitle.get(i).getIcono()),dimesionIcono - 5,dimesionIcono - 5,false,false);
						btnMenuPadre.setGraphic(new ImageView(imageBoton));	
					}
					
					btnMenuPadre.setMaxWidth(Double.MAX_VALUE);
					vbMenu.getChildren().add(btnMenuPadre);

					//se empiezan a agregar los menu hijos
					for(int j = 0 ; j < menuListCont.size() ; j ++) {
						if(menuListCont.get(j).getIdMenuPadre() == menuListTitle.get(i).getIdMenu()) {
							SegMenu menu = new SegMenu();
							menu = menuListCont.get(j);
							menuList.add(menu);
						}
					}
					VBox vbSubMenu = new VBox();
					if(menuList.size() != 0) {
						for(SegMenu menuHijo : menuList) {
							btnMenuHijo = new Button();
							btnMenuHijo.setMaxWidth(Double.MAX_VALUE);
							btnMenuHijo.setText(menuHijo.getDescripcion());
							btnMenuHijo.getStyleClass().add("botonMenuHijo");
							btnMenuHijo.setOnMouseClicked(new EventHandler<Event>() {
								@Override
								public void handle(Event event) {
									helper.mostrarVentanaContenedor(menuHijo.getNombreFxml(), ap_derecha);
								}
							});
							vbSubMenu.getChildren().add(btnMenuHijo);//agrego cada elemento en el Vbox submenu	
						}
					}
					vbMenu.getChildren().add(vbSubMenu);
					vBoxMenu.getChildren().add(vbMenu);
					map.put(vbMenu,vbSubMenu);

					btnMenuPadre.setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							toolsSlider(vbMenu,vbSubMenu);
							removeOtherMenus(vbMenu);
						}
					});

				}

			}
			for (Map.Entry<VBox,VBox> entry : map.entrySet()) {
				entry.getKey().getChildren().remove(entry.getValue());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void toolsSlider(VBox menu,VBox subMenu){
		toolsSliderImpl(menu,subMenu);
	}

	private void toolsSliderImpl(VBox menu,VBox subMenu) {
		if(menu.getChildren().contains(subMenu)){
			final FadeTransition transition = new FadeTransition(Duration.millis(500), menu);
			transition.setFromValue(0.5);
			transition.setToValue(1);
			transition.setInterpolator(Interpolator.EASE_IN);
			menu.getChildren().remove(subMenu);
			transition.play();
		}else{
			final FadeTransition transition = new FadeTransition(Duration.millis(500), menu);
			transition.setFromValue(0.5);
			transition.setToValue(1);
			transition.setInterpolator(Interpolator.EASE_IN);
			menu.getChildren().add(subMenu);
			transition.play();
		}
	}
	/**
	 * Remove other menus
	 * @param menu
	 */
	public void removeOtherMenus(VBox menu){
		removeOtherMenusImpl(menu);
	}
	private void removeOtherMenusImpl(VBox menu) {
		for (Map.Entry<VBox,VBox> entry : map.entrySet()) {
			if(!entry.getKey().equals(menu))
				entry.getKey().getChildren().remove(entry.getValue());
		}	
	}
	/*
	void llenarMenu(){
		try{
			int contador;
			List<SegMenu> menuList;
			List<SegPermiso> listaAcceso = permisoDAO.getPermisoPerfil(Context.getInstance().getIdPerfil());
			TitledPane tp;
			List<SegMenu> menuListTitle = new ArrayList<SegMenu>();
			List<SegMenu> menuListCont = new ArrayList<SegMenu>();
			for(int i = 0 ; i < listaAcceso.size() ; i ++) {
				menuListTitle.add(listaAcceso.get(i).getSegMenu());
				menuListCont.add(listaAcceso.get(i).getSegMenu());	
			}

			for(int i = 0 ; i < menuListTitle.size() ; i ++) {
				if(menuListTitle.get(i).getIdMenuPadre() == 0 && menuListTitle.get(i).getEstado().equals("A")) {
					contador = 0;
					ListView<SegMenu> lvMenu = new ListView<SegMenu>();
					menuList = new ArrayList<SegMenu>();
					tp = new TitledPane();
					tp.setText(menuListTitle.get(i).getDescripcion());
					for(int j = 0 ; j < menuListCont.size() ; j ++) {
						if(menuListCont.get(j).getIdMenuPadre() == menuListTitle.get(i).getIdMenu()) {
							SegMenu menu = new SegMenu();
							menu = menuListCont.get(j);
							menuList.add(menu);
							contador ++;
						}
					}
					if(contador != 0) {
						ObservableList<SegMenu> listaobservableMenu = FXCollections.observableArrayList(menuList);
						lvMenu.setItems(listaobservableMenu);
						lvMenu.setPrefHeight(contador * 30);
						lvMenu.setOnMouseClicked(new EventHandler<Event>() {
							@Override
							public void handle(Event event) {
								helper.mostrarVentanaContenedor(lvMenu.getSelectionModel().getSelectedItem().getNombreFxml(), ap_derecha);
							}
						});
						lvMenu.setStyle("-fx-font-family: 'Yu Mincho Demibold';");
						tp.setContent(lvMenu);
					}
					acd_menu.getPanes().add(tp);
				}
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	 */
}