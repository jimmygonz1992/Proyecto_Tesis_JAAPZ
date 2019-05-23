package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.util.PrintReport;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ReportesMaterialesC {
	@FXML private Button btnDisponibles;
	@FXML private Button btnAgotados;
	RubroDAO rubroDao = new RubroDAO();
	public void initialize() {
		btnDisponibles.setStyle("-fx-cursor: hand;");
		btnAgotados.setStyle("-fx-cursor: hand;");
		
		btnDisponibles.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Map<String, Object> param = new HashMap<String, Object>();
				/*SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("MX"));
				Date fechaDate = new Date();
				String fechaSistema = formateador.format(fechaDate);
				String fecha = dateFormatter("yyyy-MM-dd hh:mm:ss","d 'de' MMMM 'del' yyyy", fechaSistema);
				param.put("FECHA", fecha);*/
				PrintReport reporte = new PrintReport();
				reporte.crearReporte("/recursos/informes/listado_materiales.jasper", rubroDao, param);
				reporte.showReport("Materiales Disponibles");
			}
		});
		
		btnAgotados.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Map<String, Object> param = new HashMap<String, Object>();
				SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("MX"));
				Date fechaDate = new Date();
				String fechaSistema = formateador.format(fechaDate);
				String fecha = dateFormatter("yyyy-MM-dd hh:mm:ss","d 'de' MMMM 'del' yyyy", fechaSistema);
				param.put("FECHA", fecha);
				PrintReport reporte = new PrintReport();
				reporte.crearReporte("/recursos/informes/materiales_agotados.jasper", rubroDao, param);
				reporte.showReport("Materiales Agotados");
			}
		});
	}
	public static final Locale LOCALE_MX = new Locale("es", "MX");
	public static String dateFormatter(String inputFormat, String outputFormat, String inputDate){
	      //Define formato default de entrada.   
	      String input = inputFormat.isEmpty()? "yyyy-MM-dd hh:mm:ss" : inputFormat; 
	      //Define formato default de salida.
	      String output = outputFormat.isEmpty()? "d 'de' MMMM 'del' yyyy" : outputFormat;
	    String outputDate = inputDate;
	    try {
	        outputDate = new SimpleDateFormat(output, LOCALE_MX).format(new SimpleDateFormat(input, LOCALE_MX).parse(inputDate));
	    } catch (Exception e) {
	        System.out.println("dateFormatter(): " + e.getMessage());           
	    }
	    return outputDate;
	}
}