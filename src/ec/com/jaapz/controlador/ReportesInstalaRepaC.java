package ec.com.jaapz.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.PrintReport;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

public class ReportesInstalaRepaC {
	@FXML private Button btnImprimirInsta;
	@FXML private Button btnImprimirRepa;
	@FXML private DatePicker dtpFechaInicio;
	@FXML private DatePicker dtpFechaFin;
	RubroDAO rubroDao = new RubroDAO();
	Date dateInicio = new Date();
	Date dateFin = new Date();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		btnImprimirInsta.setStyle("-fx-cursor: hand;");
		btnImprimirRepa.setStyle("-fx-cursor: hand;");
		
		dtpFechaInicio.setValue(LocalDate.now());
		dtpFechaFin.setValue(LocalDate.now());
	}
	
	public void imprimirInstalaciones() {
		try {
			dateInicio = Date.from(dtpFechaInicio.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			dateFin = Date.from(dtpFechaFin.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			int result = dateFin.compareTo(dateInicio);
			
			if(result < 0) {
				helper.mostrarAlertaAdvertencia("Fecha final debe ser superior a fecha inicial", Context.getInstance().getStage());
			}else {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("fechaInicio", dateInicio);
				param.put("fechaFin", dateFin);
				PrintReport reporte = new PrintReport();
				reporte.crearReporte("/recursos/informes/listadoInstalacionRealizadas.jasper", rubroDao, param);
				reporte.showReport("Instalaciones Realizadas");
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void imprimirReparaciones() {
		try {
			dateInicio = Date.from(dtpFechaInicio.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			dateFin = Date.from(dtpFechaFin.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			int result = dateFin.compareTo(dateInicio);
			
			if(result < 0) {
				helper.mostrarAlertaAdvertencia("Fecha final debe ser superior a fecha inicial", Context.getInstance().getStage());
			}else {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("fechaInicio", dateInicio);
				param.put("fechaFin", dateFin);
				PrintReport reporte = new PrintReport();
				reporte.crearReporte("/recursos/informes/listadoReparacionRealizadas.jasper", rubroDao, param);
				reporte.showReport("Reparaciones Realizadas");
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}