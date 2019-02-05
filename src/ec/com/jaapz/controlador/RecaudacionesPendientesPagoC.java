package ec.com.jaapz.controlador;

import ec.com.jaapz.modelo.TipoReporte;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

public class RecaudacionesPendientesPagoC {
	@FXML private ComboBox<TipoReporte> cboTipoReporte;
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
			ObservableList<TipoReporte> listaEstado = FXCollections.observableArrayList(TipoReporte.values()); 
			cboTipoReporte.setItems(listaEstado);
			cboTipoReporte.setPromptText("Seleccione Tipo Reporte");
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
	}
	
	public void cargarContenido() {
		try {
			if(cboTipoReporte.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar tipo de reporte", Context.getInstance().getStage());
				return;
			}
			if(cboTipoReporte.getSelectionModel().getSelectedIndex() == 0) { // es una solicitud de inspeccion
				helper.mostrarVentanaContenedor("/recaudaciones/RecaudacionesDeudaPorCliente.fxml", apContenido);
			}
			if(cboTipoReporte.getSelectionModel().getSelectedIndex() == 1) { // es una solicitud de reparacion
				helper.mostrarVentanaContenedor("/recaudaciones/RecaudacionesDeudaGeneral.fxml", apContenido);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}