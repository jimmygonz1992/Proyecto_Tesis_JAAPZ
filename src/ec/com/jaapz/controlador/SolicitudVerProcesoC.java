package ec.com.jaapz.controlador;

import ec.com.jaapz.util.ControllerHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;

public class SolicitudVerProcesoC {
	@FXML private AnchorPane apVisualizar;
    @FXML private RadioButton rbInstalaciones;
    @FXML private Button btnCargar;
    @FXML private RadioButton rbReparaciones;

    ControllerHelper helper = new ControllerHelper();
    
    public void initialize() {
    	try {
    		btnCargar.setStyle("-fx-cursor: hand;");
    		rbInstalaciones.selectedProperty().set(true);
    	}catch(Exception ex) {
    		
    	}
    }
    public void cambiarInstalacion() {
    	if(rbInstalaciones.isSelected() == true)
    		rbReparaciones.selectedProperty().set(false);
    	else
    		rbInstalaciones.selectedProperty().set(true);
    }
    public void cambiarReparacion() {
    	if(rbReparaciones.isSelected() == true)
    		rbInstalaciones.selectedProperty().set(false);
    	else
    		rbReparaciones.selectedProperty().set(true);
    }
    public void cargarFormulario() {
    	if(rbInstalaciones.isSelected())
    		helper.mostrarVentanaContenedor("/solicitudes/SolicitudVerProcesoInstalacion.fxml", apVisualizar);
    	else
    		helper.mostrarVentanaContenedor("/solicitudes/SolicitudVerProcesoReparacion.fxml", apVisualizar);
    }

}
