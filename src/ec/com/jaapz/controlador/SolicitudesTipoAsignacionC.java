package ec.com.jaapz.controlador;

import java.util.List;

import ec.com.jaapz.modelo.TipoSolicitud;
import ec.com.jaapz.modelo.TipoSolicitudDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

public class SolicitudesTipoAsignacionC {
	@FXML private ComboBox<TipoSolicitud> cboTipoSolicitud;
	@FXML private Button btnCargar;
	@FXML private AnchorPane apContenido;
	
	TipoSolicitudDAO tipoSolicitudDAO = new TipoSolicitudDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			llenarCombosIns();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void llenarCombosIns() {
		try{
			cboTipoSolicitud.setPromptText("Seleccione tipo de solicitud");
			List<TipoSolicitud> listaCategoria = tipoSolicitudDAO.getListaTipoSolicitud();
			ObservableList<TipoSolicitud> datos = FXCollections.observableArrayList();
			datos.addAll(listaCategoria);
			cboTipoSolicitud.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void cargarContenido() {
		try {
			if(cboTipoSolicitud.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar tipo de solicitud", Context.getInstance().getStage());
				return;
			}
			if(cboTipoSolicitud.getSelectionModel().getSelectedItem().getIdTipoSolicitud() == 1) { // es una solicitud de inspeccion
				helper.mostrarVentanaContenedor("/solicitudes/SolicitudesAsignacionIns.fxml", apContenido);
			}
			if(cboTipoSolicitud.getSelectionModel().getSelectedItem().getIdTipoSolicitud() == 2) { // es una solicitud de reparacion
				helper.mostrarVentanaContenedor("/solicitudes/SolicitudesAsignacionRep.fxml", apContenido);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
