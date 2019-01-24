package ec.com.jaapz.controlador;

import ec.com.jaapz.modelo.TipoTrabajo;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

public class AsignacionesInstRepC {
	@FXML private ComboBox<TipoTrabajo> cboTipoTrabajo;
	@FXML private Button btnCargar;
	@FXML private AnchorPane apContenido;
	
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			llenarCombosIns();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void llenarCombosIns() {
		try {
			ObservableList<TipoTrabajo> listaEstado = FXCollections.observableArrayList(TipoTrabajo.values()); 
			cboTipoTrabajo.setItems(listaEstado);
			cboTipoTrabajo.setPromptText("Seleccione Estado");
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
	}
	
	public void cargarContenido() {
		try {
			if(cboTipoTrabajo.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar tipo de solicitud", Context.getInstance().getStage());
				return;
			}
			if(cboTipoTrabajo.getSelectionModel().getSelectedIndex() == 0) { // es una solicitud de inspeccion
				helper.mostrarVentanaContenedor("/asignaciones/AsignacionInstalacion.fxml", apContenido);
			}
			if(cboTipoTrabajo.getSelectionModel().getSelectedIndex() == 1) { // es una solicitud de reparacion
				helper.mostrarVentanaContenedor("/asignaciones/AsignacionReparacion.fxml", apContenido);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}