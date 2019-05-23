package ec.com.jaapz.controlador;

import java.util.HashMap;
import java.util.Map;

import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.util.PrintReport;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ReportesClientesConveniosC {
	@FXML private Button btnImprimir;
	RubroDAO rubroDao = new RubroDAO();
	
	public void initialize() {
		btnImprimir.setStyle("-fx-cursor: hand;");
		
		btnImprimir.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Map<String, Object> param = new HashMap<String, Object>();
				PrintReport reporte = new PrintReport();
				reporte.crearReporte("/recursos/informes/listadoClientesConvenio.jasper", rubroDao, param);
				reporte.showReport("Clientes con convenio de pago");
			}
		});
	}
}