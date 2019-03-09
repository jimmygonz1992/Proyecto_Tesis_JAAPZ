package ec.com.jaapz.controlador;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Empresa;
import ec.com.jaapz.modelo.SeguridadEmpresaDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class SeguridadEmpresaC {
	@FXML TextField txtCodigo;
	
	@FXML TextField txtNoPlanillas;
	@FXML TextField txtRuc;
	@FXML TextField txtRazonSocial;
	@FXML TextField txtRepresentante;
	@FXML TextField txtTelefono;
	@FXML TextField txtEmail;
	@FXML TextField txtDireccion;
	@FXML ImageView ivLogo;	
	@FXML CheckBox chkEstado;

	@FXML Button btnExaminar;
	@FXML Button btnQuitar;
	@FXML Button btnGrabar;

	ControllerHelper helper = new ControllerHelper();
	SeguridadEmpresaDAO empresaDao = new SeguridadEmpresaDAO();
	public void initialize() {
		btnExaminar.setStyle("-fx-cursor: hand;");
		btnGrabar.setStyle("-fx-cursor: hand;");
		btnQuitar.setStyle("-fx-cursor: hand;");
		
		txtCodigo.setText("0");
		txtCodigo.setEditable(false);
		int maxLength = 13;
		int maxLengthTelf = 10;
		recuperarDatos();
		
		//validar solo numeros
		txtRuc.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtRuc.setText(oldValue);
				}
			}
		});
		txtNoPlanillas.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtRuc.setText(oldValue);
				}
			}
		});
		//validar solo numeros
		txtTelefono.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtTelefono.setText(oldValue);
				}
			}
		});

		//solo letras mayusculas
		txtRazonSocial.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtRazonSocial.getText().toUpperCase();
				txtRazonSocial.setText(cadena);
			}
		});

		//validar solo 13 valores
		txtRuc.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtRuc.getText().length() > maxLength) {
					String s = txtRuc.getText().substring(0, maxLength);
					txtRuc.setText(s);
				}
			}
		});
		
		//validar solo 10 valores
		txtTelefono.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (txtTelefono.getText().length() > maxLengthTelf) {
					String s = txtTelefono.getText().substring(0, maxLengthTelf);
					txtTelefono.setText(s);
				}
			}
		});

		//solo letras mayusculas
		txtRepresentante.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtRepresentante.getText().toUpperCase();
				txtRepresentante.setText(cadena);
			}
		});

		//solo letras mayusculas
		txtDireccion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtDireccion.getText().toUpperCase();
				txtDireccion.setText(cadena);
			}
		});
	}
	
	public void recuperarDatos(){
		try{
			List<Empresa> listaEmpresa = new ArrayList<Empresa>();
			listaEmpresa = empresaDao.getRecuperaDatosEmpresa();
			for(int i = 0 ; i < listaEmpresa.size() ; i ++) {
				txtCodigo.setText(Integer.toString(listaEmpresa.get(i).getIdEmpresa()));
				txtRuc.setText(listaEmpresa.get(i).getRuc());
				txtRazonSocial.setText(listaEmpresa.get(i).getRazonSocial());
				txtRepresentante.setText(listaEmpresa.get(i).getRepresentante());
				txtTelefono.setText(listaEmpresa.get(i).getTelefono());
				txtEmail.setText(listaEmpresa.get(i).getEmail());
				txtDireccion.setText(listaEmpresa.get(i).getDireccion());
				txtNoPlanillas.setText(String.valueOf(listaEmpresa.get(i).getCorte()));
				if (listaEmpresa.get(i).getEstado().equals(Constantes.ESTADO_ACTIVO)) {
					chkEstado.setSelected(true);
				}else {
					chkEstado.setSelected(false);
				}
				
				if(listaEmpresa.get(i).getLogo() != null) {
					String imgString = new String(listaEmpresa.get(i).getLogo(), "UTF-8");
					ivLogo.setImage(helper.getImageFromBase64String(imgString).getImage());
				}
				else {
					Image img = new Image("/usuario.jpg");
					ivLogo.setImage(img);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void examinar() {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Buscar Imagen");
			// Agregar filtros para facilitar la busqueda
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Imagen jpg", "*.jpg"),
					new FileChooser.ExtensionFilter("Imagen png", "*.png")
					);
			// Obtener la imagen seleccionada
			File imgFile = fileChooser.showOpenDialog(Context.getInstance().getStage());
			// Mostar la imagen
			if (imgFile != null) {
				Image image = new Image("file:" + imgFile.getAbsolutePath());
				ivLogo.setImage(image);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void quitar() {
		Image img = new Image("/usuario.jpg");
		ivLogo.setImage(img);
	}

	public void grabar() {
		try {
			String estado;
			if(validarDatos() == false)
				return;
			if(chkEstado.isSelected() == true)
				estado = Constantes.ESTADO_ACTIVO;
			else
				estado = Constantes.ESTADO_INACTIVO;
			
			Empresa empresa = new Empresa();
			empresa.setRuc(txtRuc.getText());
			empresa.setRazonSocial(txtRazonSocial.getText());
			empresa.setRepresentante(txtRepresentante.getText());
			empresa.setTelefono(txtTelefono.getText());
			empresa.setEmail(txtEmail.getText());
			empresa.setDireccion(txtDireccion.getText());
			empresa.setCorte(Integer.parseInt(txtNoPlanillas.getText()));
			empresa.setEstado(estado);
			empresa.setLogo(helper.encodeFileToBase64Binary(ivLogo.getImage()).getBytes());

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				empresaDao.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")) {//inserta
					empresa.setIdEmpresa(null);
					empresaDao.getEntityManager().persist(empresa);
				}else {//modifica
					empresa.setIdEmpresa(Integer.parseInt(txtCodigo.getText()));
					empresaDao.getEntityManager().merge(empresa);
				}
				empresaDao.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				recuperarDatos();
			}
		}catch(Exception ex) {
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			empresaDao.getEntityManager().getTransaction().rollback();
			System.out.println(ex.getMessage());
		}
	}

	void limpiar() {
		txtCodigo.setText("0");
		txtCodigo.setEditable(false);
		txtRuc.setText("");
		txtRazonSocial.setText("");
		txtRepresentante.setText("");
		txtTelefono.setText("");
		txtEmail.setText("");
		txtDireccion.setText("");
		chkEstado.setSelected(true);
		Image img = new Image("/usuario.jpg");
		ivLogo.setImage(img);
	}

	boolean validarDatos() {
		try {
			if(txtRuc.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar RUC de la empresa", Context.getInstance().getStage());
				txtRuc.requestFocus();
				return false;
			}

			if(txtRazonSocial.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Razón Social", Context.getInstance().getStage());
				txtRazonSocial.requestFocus();
				return false;
			}

			if(txtRepresentante.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Representante Legal de la Empresa", Context.getInstance().getStage());
				txtRepresentante.requestFocus();
				return false;	
			}

			if(txtTelefono.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar teléfono de contacto", Context.getInstance().getStage());
				txtTelefono.requestFocus();
				return false;	
			}

			if(txtEmail.getText() != null) {
				if(!txtEmail.getText().toString().equals("")) {
					if(ControllerHelper.validarEmail(txtEmail.getText()) == false) {
						helper.mostrarAlertaAdvertencia("Correo electrónico no valido", Context.getInstance().getStage());
						return false;
					}
				}
			}

			if(txtDireccion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar dirección de la empresa", Context.getInstance().getStage());
				txtDireccion.requestFocus();
				return false;	
			}
			if(txtNoPlanillas.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar número de corte", Context.getInstance().getStage());
				txtDireccion.requestFocus();
				return false;	
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
}