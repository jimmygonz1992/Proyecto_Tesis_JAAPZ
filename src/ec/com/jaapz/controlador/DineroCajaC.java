package ec.com.jaapz.controlador;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import ec.com.jaapz.modelo.FacturaDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.PrintReport;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;

public class DineroCajaC {
	@FXML private DatePicker dtpFechaInicio;
	@FXML private DatePicker dtpFechaFin;
    @FXML private RadioButton rbResumido;
    @FXML private RadioButton rbDetallado;
    @FXML private RadioButton rbPorUsuario;
    @FXML private RadioButton rbGeneral;
    @FXML private Button btnImprimir;
    
    FacturaDAO facturaDao = new FacturaDAO();
    ControllerHelper helper = new ControllerHelper();
    java.util.Date utilDate = new java.util.Date();
    java.util.Date dateInicio = new java.util.Date(utilDate.getTime());
    java.util.Date dateFin = new java.util.Date(utilDate.getTime());
    
    public void initialize() {
    	try {
    		btnImprimir.setStyle("-fx-cursor: hand;");
    		rbResumido.selectedProperty().set(true);
    		rbPorUsuario.selectedProperty().set(true);
    		dtpFechaInicio.setValue(LocalDate.now());
    		dtpFechaFin.setValue(LocalDate.now());		
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    
    public void cambiarGeneral() {
    	if(rbGeneral.isSelected() == true)
    		rbPorUsuario.selectedProperty().set(false);
    	else
    		rbGeneral.selectedProperty().set(true);
    }
    
    public void cambiarPorUsuario() {
    	if(rbPorUsuario.isSelected() == true)
    		rbGeneral.selectedProperty().set(false);
    	else
    		rbPorUsuario.selectedProperty().set(true);
    }
        
    public void cambiarResumido() {
    	if(rbResumido.isSelected() == true)
    		rbDetallado.selectedProperty().set(false);
    	else
    		rbResumido.selectedProperty().set(true);
    }
    
    public void cambiarDetallado() {
    	if(rbDetallado.isSelected() == true)
    		rbResumido.selectedProperty().set(false);
    	else
    		rbDetallado.selectedProperty().set(true);
    }
    
    public void imprimirReporte() {
    	try {
    		dateInicio = Date.from(dtpFechaInicio.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    		dateFin = Date.from(dtpFechaFin.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    		int usuario = Context.getInstance().getUsuariosC().getIdUsuario();
    		String usuarioNombre = Context.getInstance().getUsuariosC().getNombre() + " " + Context.getInstance().getUsuariosC().getApellido();
    		
    		int result = dateFin.compareTo(dateInicio);
			if(result < 0) {
				helper.mostrarAlertaAdvertencia("Fecha final debe ser superior a fecha inicial", Context.getInstance().getStage());
				return;
			}else {
				if(rbPorUsuario.isSelected()) {
					//aumenta usurio como otro parametro
					if(rbResumido.isSelected()) {
						PrintReport pr = new PrintReport();
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("FECHA_INICIO", dateInicio);
						param.put("usuario", usuario);
						param.put("usuarioNombre", usuarioNombre);
						param.put("FECHA_FIN", dateFin);
						pr.crearReporte("/recursos/informes/reporteCajaResumidoPorUsuario.jasper", facturaDao, param);
						pr.showReport("Reporte de caja resumido");
			    	}else {
			    		PrintReport pr = new PrintReport();
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("FECHA_INICIO", dateInicio);
						param.put("usuario", usuario);
						param.put("usuarioNombre", usuarioNombre);
						param.put("FECHA_FIN", dateFin);
						pr.crearReporte("/recursos/informes/reporteCajaDetalladoPorUsuario.jasper", facturaDao, param);
						pr.showReport("Reporte de caja detallado");
			    	}
				}else {
					//estos llama a los generales tendria q hacer otros reportes pero incluyendo y comparando al usuario
					if(rbResumido.isSelected()) {
						PrintReport pr = new PrintReport();
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("FECHA_INICIO", dateInicio);
						param.put("FECHA_FIN", dateFin);
						pr.crearReporte("/recursos/informes/reporteCajaResumido.jasper", facturaDao, param);
						pr.showReport("Reporte de caja resumido");
			    	}else {
			    		PrintReport pr = new PrintReport();
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("FECHA_INICIO", dateInicio);
						param.put("FECHA_FIN", dateFin);
						pr.crearReporte("/recursos/informes/reporteCajaDetallado.jasper", facturaDao, param);
						pr.showReport("Reporte de caja detallado");
			    	}
				}
			}
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
}