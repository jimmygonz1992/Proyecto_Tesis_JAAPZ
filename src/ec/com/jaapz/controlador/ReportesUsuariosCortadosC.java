package ec.com.jaapz.controlador;

import java.util.HashMap;
import java.util.Map;

import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.Encriptado;
import ec.com.jaapz.util.PrintReport;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ReportesUsuariosCortadosC {
	@FXML private Button btnReporte;
	CuentaClienteDAO cuentaClienteDao = new CuentaClienteDAO();
	
	public void initialize(){
		btnReporte.setStyle("-fx-cursor: hand;");
	}
	
	public void verReporte() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("usuario_crea", Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
			pr.crearReporte("/recursos/informes/clientesCortados.jasper", cuentaClienteDao, param);
			pr.showReport("Clientes con corte de servicio de agua");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}