package ec.com.jaapz.controlador;

import java.util.HashMap;
import java.util.Map;

import ec.com.jaapz.modelo.AnioDAO;
import ec.com.jaapz.util.PrintReport;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ReporteNominasC {
	@FXML private Button btnNominaSimple;
	@FXML private Button btnNominaFirma;
	AnioDAO anioDAO = new AnioDAO();
	public void initialize() {
		btnNominaSimple.setStyle("-fx-cursor: hand;");
		btnNominaFirma.setStyle("-fx-cursor: hand;");
		
		btnNominaFirma.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("FECHA", "SDSDFSDF");
				PrintReport reporte = new PrintReport();
				reporte.crearReporte("/recursos/informes/nominaFirma.jasper", anioDAO, param);
				reporte.showReport("Nómina de clientes");
			}
		});
		
		btnNominaSimple.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Map<String, Object> param = new HashMap<String, Object>();
				
				param.put("FECHA", "SDSDFSDF");
				PrintReport reporte = new PrintReport();
				reporte.crearReporte("/recursos/informes/nominaSimple.jasper", anioDAO, param);
				reporte.showReport("Nómina de clientes");
			}
		});
	}
}
