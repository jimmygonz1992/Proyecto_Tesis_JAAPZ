package ec.com.jaapz.correo;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.swing.JOptionPane;

import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class Hilo2 {
	private boolean continuar = true;
	public static  boolean  envioExito= false;
	private String adjunto;
	private String[] adjuntos;
	private String[] destinatarios;
	private int servidor;
	private String destinatario;
	private String asunto;
	private String mensaje;
	private String cliente;
	private String medidor;
	private ImageView ivEnviandoMensaje;
	private Button btnEnviarCorreo;
	ControllerHelper helper = new ControllerHelper();
	
	public Hilo2(String adjunto, String[] adjuntos, String[] destinatarios, int servidor, String destinatario,
			String asunto, String mensaje,ImageView ivEnviandoMensaje, Button btnEnviarCorreo,String cliente, String medidor) {
		this.adjunto = adjunto;
		this.adjuntos = adjuntos;
		this.destinatarios = destinatarios;
		this.servidor = servidor;
		this.destinatario = destinatario;
		this.asunto = asunto;
		this.mensaje = mensaje;
		this.ivEnviandoMensaje = ivEnviandoMensaje;
		this.btnEnviarCorreo = btnEnviarCorreo;
		this.cliente = cliente;
		this.medidor = medidor;
	}

	public void detenElHilo(){
		this.continuar = false;
	}
	int i = 0;
	public void enviarCorreo() {
		while (this.continuar) {
			i = i + 1;
			//adjunto es la direccion del archivo adjunto
			if (this.adjunto.isEmpty()) {
				EnviarMail propio = new EnviarMail(Encriptado.Desencriptar(Constantes.CORREO_ORIGEN), Encriptado.Desencriptar(Constantes.CONTRASENIA_CORREO), 
						this.destinatarios, this.asunto, this.mensaje, this.servidor);
				try {
					propio.enviar();
					ivEnviandoMensaje.setVisible(false);
					btnEnviarCorreo.setDisable(false);
				}
				catch (MessagingException ex){
					JOptionPane.showMessageDialog(null, "Error" + ex.getMessage());
					Logger.getLogger(Hilo2.class.getName()).log(Level.SEVERE, null, ex);
					detenElHilo();
				}
			}
			else {
				EnviarMailComplejo propio = new EnviarMailComplejo(Encriptado.Desencriptar(Constantes.CORREO_ORIGEN), Encriptado.Desencriptar(Constantes.CONTRASENIA_CORREO), this.destinatarios, this.asunto, this.mensaje, this.adjuntos, this.servidor);
				try {
					propio.Enviar();
					ivEnviandoMensaje.setVisible(false);
					btnEnviarCorreo.setDisable(false);
				}
				catch (MessagingException ex){
					JOptionPane.showMessageDialog(null, "Error" + ex.getMessage());
					Logger.getLogger(Hilo2.class.getName()).log(Level.SEVERE, null, ex);
					detenElHilo();
				}
			}
			detenElHilo();
			System.out.println(i);
		}
	}
	public String getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(String adjunto) {
		this.adjunto = adjunto;
	}

	public String[] getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(String[] adjuntos) {
		this.adjuntos = adjuntos;
	}

	public String[] getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(String[] destinatarios) {
		this.destinatarios = destinatarios;
	}

	public int getServidor() {
		return servidor;
	}

	public void setServidor(int servidor) {
		this.servidor = servidor;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public ImageView getIvEnviandoMensaje() {
		return ivEnviandoMensaje;
	}

	public void setIvEnviandoMensaje(ImageView ivEnviandoMensaje) {
		this.ivEnviandoMensaje = ivEnviandoMensaje;
	}

	public Button getBtnEnviarCorreo() {
		return btnEnviarCorreo;
	}

	public void setBtnEnviarCorreo(Button btnEnviarCorreo) {
		this.btnEnviarCorreo = btnEnviarCorreo;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getMedidor() {
		return medidor;
	}

	public void setMedidor(String medidor) {
		this.medidor = medidor;
	}
	
}

