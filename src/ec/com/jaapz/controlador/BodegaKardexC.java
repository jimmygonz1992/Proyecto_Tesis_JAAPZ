package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ec.com.jaapz.modelo.Kardex;
import ec.com.jaapz.modelo.KardexDAO;
import ec.com.jaapz.util.Constantes;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class BodegaKardexC {
	@FXML private TextField txtBuscar;
	private @FXML TableView<KardexValorado> tvDatos;
	KardexDAO kardexDao = new KardexDAO();
	List<KardexValorado> kardexMostrar = new ArrayList<KardexValorado>();
	
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

	public void initialize(){
		llenarKardex();
		llenarTablaKardex("");
	}
	private void llenarKardex() {
		try {
			List<Kardex> listaKardex = kardexDao.getListaKardex("");
			for(int i = 0 ; i < listaKardex.size() ; i ++) {
				if(listaKardex.get(i).getTipoMovimiento().equals(Constantes.BODEGA_INGRESO)) {
					KardexValorado kardexAdd = new KardexValorado();
					kardexAdd.setFecha(listaKardex.get(i).getFecha());
					kardexAdd.setDocumento(listaKardex.get(i).getNumDocumento());
					kardexMostrar.add(kardexAdd);
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarCliente() {
		llenarTablaKardex(txtBuscar.getText());
	}

	@SuppressWarnings("unchecked")
	void llenarTablaKardex(String patron) {
		try{
			tvDatos.getColumns().clear();

			ObservableList<KardexValorado> datos = FXCollections.observableArrayList();
			datos.setAll(kardexMostrar);

			TableColumn<KardexValorado, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(70);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});
			TableColumn<KardexValorado, String> detalleColum = new TableColumn<>("detalle");
			detalleColum.setMinWidth(10);
			detalleColum.setPrefWidth(70);
			detalleColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});
			//tvDatos.getColumns().addAll(fechaColum, documentoColum, operacionColum, cantidadColum, valorUColum, costoTColum, cantidadEgrColum, valorUEgrColum, costoTEgrColum);
			tvDatos.getColumns().addAll(fechaColum, detalleColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public class KardexValorado{
		Date fecha;
		String detalle;
		String documento;
		int cantEntrada;
		double valEntrada;
		double valUniEntrada;
		double totalEntrada;
		int cantSalida;
		double valSalida;
		double valUniSalida;
		double totalSalida;
		int cantSaldo;
		double valSaldo;
		double valUniSaldo;
		double totalSaldo;
		public Date getFecha() {
			return fecha;
		}
		public void setFecha(Date fecha) {
			this.fecha = fecha;
		}
		public String getDetalle() {
			return detalle;
		}
		public void setDetalle(String detalle) {
			this.detalle = detalle;
		}
		public String getDocumento() {
			return documento;
		}
		public void setDocumento(String documento) {
			this.documento = documento;
		}
		public int getCantEntrada() {
			return cantEntrada;
		}
		public void setCantEntrada(int cantEntrada) {
			this.cantEntrada = cantEntrada;
		}
		public double getValEntrada() {
			return valEntrada;
		}
		public void setValEntrada(double valEntrada) {
			this.valEntrada = valEntrada;
		}
		public double getValUniEntrada() {
			return valUniEntrada;
		}
		public void setValUniEntrada(double valUniEntrada) {
			this.valUniEntrada = valUniEntrada;
		}
		public double getTotalEntrada() {
			return totalEntrada;
		}
		public void setTotalEntrada(double totalEntrada) {
			this.totalEntrada = totalEntrada;
		}
		public int getCantSalida() {
			return cantSalida;
		}
		public void setCantSalida(int cantSalida) {
			this.cantSalida = cantSalida;
		}
		public double getValSalida() {
			return valSalida;
		}
		public void setValSalida(double valSalida) {
			this.valSalida = valSalida;
		}
		public double getValUniSalida() {
			return valUniSalida;
		}
		public void setValUniSalida(double valUniSalida) {
			this.valUniSalida = valUniSalida;
		}
		public double getTotalSalida() {
			return totalSalida;
		}
		public void setTotalSalida(double totalSalida) {
			this.totalSalida = totalSalida;
		}
		public int getCantSaldo() {
			return cantSaldo;
		}
		public void setCantSaldo(int cantSaldo) {
			this.cantSaldo = cantSaldo;
		}
		public double getValSaldo() {
			return valSaldo;
		}
		public void setValSaldo(double valSaldo) {
			this.valSaldo = valSaldo;
		}
		public double getValUniSaldo() {
			return valUniSaldo;
		}
		public void setValUniSaldo(double valUniSaldo) {
			this.valUniSaldo = valUniSaldo;
		}
		public double getTotalSaldo() {
			return totalSaldo;
		}
		public void setTotalSaldo(double totalSaldo) {
			this.totalSaldo = totalSaldo;
		}
		
		
	}
}