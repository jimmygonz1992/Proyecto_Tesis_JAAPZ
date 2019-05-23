package ec.com.jaapz.controlador;

import java.util.HashMap;
import java.util.Map;

import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.util.PrintReport;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ReportesInstalaRepaC {
	@FXML private Button btnImprimirInsta;
	@FXML private Button btnImprimirRepa;
	RubroDAO rubroDao = new RubroDAO();
	
	public void initialize() {
		btnImprimirInsta.setStyle("-fx-cursor: hand;");
		btnImprimirRepa.setStyle("-fx-cursor: hand;");
		
		btnImprimirInsta.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Map<String, Object> param = new HashMap<String, Object>();
				PrintReport reporte = new PrintReport();
				reporte.crearReporte("/recursos/informes/listadoInstalacionRealizadas.jasper", rubroDao, param);
				reporte.showReport("Instalaciones Realizadas");
			}
		});
		
		btnImprimirRepa.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Map<String, Object> param = new HashMap<String, Object>();
				PrintReport reporte = new PrintReport();
				reporte.crearReporte("/recursos/informes/listadoReparacionRealizadas.jasper", rubroDao, param);
				reporte.showReport("Reparaciones Realizadas");
			}
		});
	}
}