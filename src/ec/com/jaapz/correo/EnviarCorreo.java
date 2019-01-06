package ec.com.jaapz.correo;

import java.net.Socket;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Encriptado;

public class EnviarCorreo {
	public static boolean ComprobarConexionInternet(){
		String dirWeb = "www.google.com";
		int puerto = 80;
		try{
			@SuppressWarnings("resource")
			Socket s = new Socket(dirWeb, puerto);
			if (s.isConnected()) {
				System.out.println("Conexión establecida con la dirección: " + dirWeb + " a través del puerto: " + puerto);
			}
		}
		catch (Exception e){
			System.err.println("No se pudo establecer conexión con: " + dirWeb + " a través del puerto: " + puerto);
			return false;
		}
		return true;
	}
	public static void enviarCorreo() {
		try {

			// Propiedades de la conexión
			Properties props = new Properties();
			props.setProperty("mail.smtp.host", "smtp.gmail.com");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "587");
			props.setProperty("mail.smtp.user", Encriptado.Desencriptar(Constantes.CORREO_ORIGEN));
			props.setProperty("mail.smtp.auth", "true");

			// Preparamos la sesion
			Session session = Session.getDefaultInstance(props);

			// Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();
            texto.setText("Texto del mensaje");

            // Se compone el adjunto con la imagen
            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource("D:/catalogo.pdf")));
            adjunto.setFileName("catalogo.pdf");
			
            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjunto);
			
			
			// Construimos el mensaje
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Encriptado.Desencriptar(Constantes.CORREO_ORIGEN)));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("byronram_20@hotmail.com"));
			message.setSubject("Mensaje con archivo adjunto");
			message.setContent(multiParte);
			// Lo enviamos.
			Transport t = session.getTransport("smtp");
			t.connect(Encriptado.Desencriptar(Constantes.CORREO_ORIGEN), Encriptado.Desencriptar(Constantes.CONTRASENIA_CORREO));
			t.sendMessage(message, message.getAllRecipients());
			// Cierre.
			t.close();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
