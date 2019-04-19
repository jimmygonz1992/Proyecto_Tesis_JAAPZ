package ec.com.jaapz.controlador;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ec.com.jaapz.modelo.Kardex;
import ec.com.jaapz.modelo.KardexDAO;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.util.Constantes;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class BodegaKardexC {
	@FXML private Button btnCargar;
	@FXML private ComboBox<Rubro> cboRubro;
	private @FXML TableView<KardexValorado> tvDatos;
	KardexDAO kardexDao = new KardexDAO();
	RubroDAO rubroDAO = new RubroDAO();
	DecimalFormat df = new DecimalFormat("#0.00");
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

	public void initialize(){
		llenarCombo();
		cboRubro.setStyle("-fx-cursor: hand;");
		btnCargar.setStyle("-fx-cursor: hand;");
		List<KardexValorado> inicio = new ArrayList<>();
		llenarTablaKardex(inicio);
	}
	private void llenarCombo() {
		try {
			cboRubro.setPromptText("Seleccione Rubro");
			List<Rubro> listaRubros = rubroDAO.getListaRubrosKardex();
			List<Rubro> listaAgregar = new ArrayList<Rubro>();
			Rubro rubroAdd = new Rubro();
			rubroAdd.setIdRubro(null);
			rubroAdd.setDescripcion("Todos");;
			listaAgregar.add(rubroAdd);
			for(Rubro rubro : listaRubros) {
				if(!rubro.getIdRubro().equals(Constantes.ID_MEDIDOR) && !rubro.getIdRubro().equals(Constantes.ID_TASA_CONEXION))
					listaAgregar.add(rubro);
			}
			ObservableList<Rubro> datos = FXCollections.observableArrayList();
			datos.addAll(listaAgregar);
			cboRubro.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void cargarRubro() {
		try {
			if(cboRubro.getSelectionModel().getSelectedItem().getIdRubro() == null)
				cargarDatosKardexSeleccionado(llenarKardexTodo());
			else 
				cargarDatosKardexSeleccionado(llenarKardexSeleccionado());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private List<KardexValorado> llenarKardexSeleccionado() {
		try {
			List<KardexValorado> kardexMostrar = new ArrayList<KardexValorado>();
			
			List<Kardex> listaKardex = kardexDao.getListaKardex(cboRubro.getSelectionModel().getSelectedItem().getIdRubro());
			Integer cantSaldoAnterior = 0;
			double totalSaldoAnterior = 0.0;
			
			for(int i = 0 ; i < listaKardex.size() ; i ++) {
				KardexValorado kardexAdd = new KardexValorado();
				if(listaKardex.get(i).getTipoMovimiento().equals(Constantes.BODEGA_INGRESO)) {
					kardexAdd.setRubro(cboRubro.getSelectionModel().getSelectedItem().getDescripcion());
					kardexAdd.setFecha(listaKardex.get(i).getFecha());
					kardexAdd.setDocumento(listaKardex.get(i).getTipoDocumento() + " " + listaKardex.get(i).getNumDocumento());
					kardexAdd.setDetalle(listaKardex.get(i).getDetalleOperacion());
					kardexAdd.setCantEntrada(listaKardex.get(i).getCantidad());
					kardexAdd.setValEntrada(listaKardex.get(i).getValorUnitario());
					kardexAdd.setTotalEntrada(listaKardex.get(i).getCostoTotal());
				}else {
					kardexAdd.setRubro(cboRubro.getSelectionModel().getSelectedItem().getDescripcion());
					kardexAdd.setFecha(listaKardex.get(i).getFecha());
					kardexAdd.setDocumento(listaKardex.get(i).getTipoDocumento() + " " + listaKardex.get(i).getNumDocumento());
					kardexAdd.setDetalle(listaKardex.get(i).getDetalleOperacion());
					kardexAdd.setCantSalida(listaKardex.get(i).getCantidad());
					kardexAdd.setValSalida(listaKardex.get(i).getValorUnitario());
					kardexAdd.setTotalSalida(listaKardex.get(i).getCostoTotal());
				}
				if(i == 0) {//el saldo cuando es el primero es igual, xq esta validado q no entregue nada si no hay en stock
					kardexAdd.setCantSaldo(listaKardex.get(i).getCantidad());
					kardexAdd.setValSaldo(listaKardex.get(i).getValorUnitario());
					kardexAdd.setTotalSaldo(listaKardex.get(i).getCostoTotal());
					//para mantener los saldos anteriores para cada iteracion - cada rubro
					cantSaldoAnterior = listaKardex.get(i).getCantidad();
					totalSaldoAnterior = listaKardex.get(i).getCostoTotal();
				}else {
					if(listaKardex.get(i).getTipoMovimiento().equals(Constantes.BODEGA_INGRESO)) {//cuando es ingreso hay q tener en cuenta el promedio ponderado
						cantSaldoAnterior = cantSaldoAnterior + listaKardex.get(i).getCantidad();
						totalSaldoAnterior = totalSaldoAnterior + listaKardex.get(i).getCostoTotal();
						kardexAdd.setCantSaldo(cantSaldoAnterior);//en un ingreso se suma la cantidad
						kardexAdd.setTotalSaldo(totalSaldoAnterior);//asi mismo con el salgo se suma
						kardexAdd.setValSaldo(totalSaldoAnterior / cantSaldoAnterior);
					}else {
						cantSaldoAnterior = cantSaldoAnterior - listaKardex.get(i).getCantidad();
						totalSaldoAnterior = totalSaldoAnterior - listaKardex.get(i).getCostoTotal();
						kardexAdd.setCantSaldo(cantSaldoAnterior);//en un ingreso se suma la cantidad
						kardexAdd.setTotalSaldo(totalSaldoAnterior);//asi mismo con el salgo se suma
						kardexAdd.setValSaldo(totalSaldoAnterior / cantSaldoAnterior);
					}
				}
				kardexMostrar.add(kardexAdd);
			}			
			return kardexMostrar;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	private List<KardexValorado> llenarKardexTodo() {
		try {
			List<KardexValorado> kardexMostrar = new ArrayList<KardexValorado>();
			List<Rubro> listaRubro = rubroDAO.getListaRubrosKardex();
			for(Rubro rubro : listaRubro) {//hago un ciclo para cada rubro y mantener agrupado los rubros dentro del kardex
				List<Kardex> listaKardex = kardexDao.getListaKardex(rubro.getIdRubro());
				Integer cantSaldoAnterior = 0;
				double totalSaldoAnterior = 0.0;
				
				for(int i = 0 ; i < listaKardex.size() ; i ++) {
					KardexValorado kardexAdd = new KardexValorado();
					if(listaKardex.get(i).getTipoMovimiento().equals(Constantes.BODEGA_INGRESO)) {
						kardexAdd.setRubro(rubro.getDescripcion());
						kardexAdd.setFecha(listaKardex.get(i).getFecha());
						kardexAdd.setDocumento(listaKardex.get(i).getTipoDocumento() + " " + listaKardex.get(i).getNumDocumento());
						kardexAdd.setDetalle(listaKardex.get(i).getDetalleOperacion());
						kardexAdd.setCantEntrada(listaKardex.get(i).getCantidad());
						kardexAdd.setValEntrada(listaKardex.get(i).getValorUnitario());
						kardexAdd.setTotalEntrada(listaKardex.get(i).getCostoTotal());
					}else {
						kardexAdd.setRubro(rubro.getDescripcion());
						kardexAdd.setFecha(listaKardex.get(i).getFecha());
						kardexAdd.setDocumento(listaKardex.get(i).getTipoDocumento() + " " + listaKardex.get(i).getNumDocumento());
						kardexAdd.setDetalle(listaKardex.get(i).getDetalleOperacion());
						kardexAdd.setCantSalida(listaKardex.get(i).getCantidad());
						kardexAdd.setValSalida(listaKardex.get(i).getValorUnitario());
						kardexAdd.setTotalSalida(listaKardex.get(i).getCostoTotal());
					}
					if(i == 0) {//el saldo cuando es el primero es igual, xq esta validado q no entregue nada si no hay en stock
						kardexAdd.setCantSaldo(listaKardex.get(i).getCantidad());
						kardexAdd.setValSaldo(listaKardex.get(i).getValorUnitario());
						kardexAdd.setTotalSaldo(listaKardex.get(i).getCostoTotal());
						//para mantener los saldos anteriores para cada iteracion - cada rubro
						cantSaldoAnterior = listaKardex.get(i).getCantidad();
						totalSaldoAnterior = listaKardex.get(i).getCostoTotal();
					}else {
						if(listaKardex.get(i).getTipoMovimiento().equals(Constantes.BODEGA_INGRESO)) {//cuando es ingreso hay q tener en cuenta el promedio ponderado
							cantSaldoAnterior = cantSaldoAnterior + listaKardex.get(i).getCantidad();
							totalSaldoAnterior = totalSaldoAnterior + listaKardex.get(i).getCostoTotal();
							kardexAdd.setCantSaldo(cantSaldoAnterior);//en un ingreso se suma la cantidad
							kardexAdd.setTotalSaldo(totalSaldoAnterior);//asi mismo con el salgo se suma
							kardexAdd.setValSaldo(totalSaldoAnterior / cantSaldoAnterior);
						}else {
							cantSaldoAnterior = cantSaldoAnterior - listaKardex.get(i).getCantidad();
							totalSaldoAnterior = totalSaldoAnterior - listaKardex.get(i).getCostoTotal();
							kardexAdd.setCantSaldo(cantSaldoAnterior);//en un ingreso se suma la cantidad
							kardexAdd.setTotalSaldo(totalSaldoAnterior);//asi mismo con el salgo se suma
							kardexAdd.setValSaldo(totalSaldoAnterior / cantSaldoAnterior);
						}
					}
					kardexMostrar.add(kardexAdd);
				}
			}
			return kardexMostrar;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	private void cargarDatosKardexSeleccionado(List<KardexValorado> kardexMostrar) {
		try {
			ObservableList<KardexValorado> datos = FXCollections.observableArrayList();
			datos.setAll(kardexMostrar);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	void llenarTablaKardex(List<KardexValorado> kardexMostrar) {
		try{
			ObservableList<KardexValorado> datos = FXCollections.observableArrayList();
			datos.setAll(kardexMostrar);
			
			TableColumn<KardexValorado, String> rubroColum = new TableColumn<>("Rubro");
			rubroColum.setMinWidth(10);
			rubroColum.setPrefWidth(180);
			rubroColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getRubro());
				}
			});
			
			TableColumn<KardexValorado, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});
			TableColumn<KardexValorado, String> documentoColum = new TableColumn<>("Documento");
			documentoColum.setMinWidth(10);
			documentoColum.setPrefWidth(225);
			documentoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getDocumento());
				}
			});
			TableColumn<KardexValorado, String> detalleColum = new TableColumn<>("Detalle");
			detalleColum.setMinWidth(10);
			detalleColum.setPrefWidth(275);
			detalleColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getDetalle());
				}
			});
			//entradas ------------------------------------------------------------
			TableColumn<KardexValorado, String> cantEColum = new TableColumn<>("Cant");
			cantEColum.setMinWidth(10);
			cantEColum.setPrefWidth(50);
			cantEColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantEntrada()));
				}
			});
			
			TableColumn<KardexValorado, String> valEColum = new TableColumn<>("V. unit");
			valEColum.setMinWidth(10);
			valEColum.setPrefWidth(60);
			valEColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(df.format(param.getValue().getValEntrada())));
				}
			});
			
			TableColumn<KardexValorado, String> totEColum = new TableColumn<>("C. total");
			totEColum.setMinWidth(10);
			totEColum.setPrefWidth(60);
			totEColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(df.format(param.getValue().getTotalEntrada())));
				}
			});
			TableColumn<KardexValorado, String> entradaColum = new TableColumn<>("Entradas");
			entradaColum.setMinWidth(10);
			entradaColum.setPrefWidth(200);
			entradaColum.getColumns().addAll(cantEColum,valEColum,totEColum);
			
			//salidas ------------------------------------------------------------
			TableColumn<KardexValorado, String> cantSColum = new TableColumn<>("Cant");
			cantSColum.setMinWidth(10);
			cantSColum.setPrefWidth(50);
			cantSColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantSalida()));
				}
			});
			
			TableColumn<KardexValorado, String> valSColum = new TableColumn<>("V. unit");
			valSColum.setMinWidth(10);
			valSColum.setPrefWidth(60);
			valSColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(df.format(param.getValue().getValSalida())));
				}
			});
			
			TableColumn<KardexValorado, String> totSColum = new TableColumn<>("C. total");
			totSColum.setMinWidth(10);
			totSColum.setPrefWidth(60);
			totSColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(df.format(param.getValue().getTotalSalida())));
				}
			});
			TableColumn<KardexValorado, String> salidaColum = new TableColumn<>("Salidas");
			salidaColum.setMinWidth(10);
			salidaColum.setPrefWidth(200);
			salidaColum.getColumns().addAll(cantSColum,valSColum,totSColum);
			
			//saldo ------------------------------------------------------------
			TableColumn<KardexValorado, String> cantSalColum = new TableColumn<>("Cant");
			cantSalColum.setMinWidth(10);
			cantSalColum.setPrefWidth(50);
			cantSalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantSaldo()));
				}
			});
			
			TableColumn<KardexValorado, String> valSalColum = new TableColumn<>("V. unit");
			valSalColum.setMinWidth(10);
			valSalColum.setPrefWidth(60);
			valSalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(df.format(param.getValue().getValSaldo())));
				}
			});
			
			TableColumn<KardexValorado, String> totSalColum = new TableColumn<>("C. total");
			totSalColum.setMinWidth(10);
			totSalColum.setPrefWidth(60);
			totSalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<KardexValorado,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<KardexValorado, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(df.format(param.getValue().getTotalSaldo())));
				}
			});
			TableColumn<KardexValorado, String> saldoColum = new TableColumn<>("Saldos");
			saldoColum.setMinWidth(10);
			saldoColum.setPrefWidth(200);
			saldoColum.getColumns().addAll(cantSalColum,valSalColum,totSalColum);
			
			tvDatos.getColumns().addAll(rubroColum,fechaColum,documentoColum,detalleColum,entradaColum,salidaColum,saldoColum);
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
		String rubro;
		
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
		public String getRubro() {
			return rubro;
		}
		public void setRubro(String rubro) {
			this.rubro = rubro;
		}
	}
}