package ec.com.jaapz.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.Factura;
import ec.com.jaapz.modelo.FacturaDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
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
    @FXML private Button btnImprimir;
    
    FacturaDAO facturaDao = new FacturaDAO();
    ControllerHelper helper = new ControllerHelper();
    Date dateInicio = new Date();
	Date dateFin = new Date();
	Date fechaImpresion = new Date();
	    
    public void initialize() {
    	try {
    		btnImprimir.setStyle("-fx-cursor: hand;");
    		rbResumido.selectedProperty().set(true);
    		dtpFechaInicio.setValue(LocalDate.now());
    		dtpFechaFin.setValue(LocalDate.now());
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
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
    	dateInicio = Date.from(dtpFechaInicio.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		dateFin = Date.from(dtpFechaFin.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		
		List<Factura> listaFactura = new ArrayList<>();
		for(int i=0; i<listaFactura.size(); i++) {
			Double totalIngreso = 0.0;
			//if(listaFactura.get(i).getFecha().equals(dateInicio)&&listaFactura.get(i).getFecha().equals(dateFin)) {
				Double valorTotal = new Double(listaFactura.get(i).getTotalFactura());
				totalIngreso += valorTotal;
			//}
			System.out.println(totalIngreso);
		}
		
    /*	if(rbResumido.isSelected()) {
    		PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("fechaInicio", dateInicio);
			param.put("fechaFin", dateFin);
			param.put("usuarioCrea", Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
			param.put("fechaImpresion", fechaImpresion);
			pr.crearReporte("/recursos/informes/dinero_caja_resumido.jasper", facturaDao, param);
			pr.showReport("Clientes al Día");
    	}
    	else {
    		PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("fechaInicio", dateInicio);
			param.put("fechaFin", dateFin);
			param.put("usuarioCrea", Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
			param.put("fechaImpresion", fechaImpresion);
			pr.crearReporte("/recursos/informes/dinero_caja_detallado.jasper", facturaDao, param);
			pr.showReport("Clientes al Día");
    	}*/
    }
}