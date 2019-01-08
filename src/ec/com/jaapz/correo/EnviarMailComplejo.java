package ec.com.jaapz.correo;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EnviarMailComplejo
{
	public String miCorreo;
	public String miPassword;
	String servidorSMTP;
	String puertoEnvio;
	String[] destinatarios;
	String asunto;
	String cuerpo = null;
	public String[] archivoAdjunto;
	int servidor;

	public EnviarMailComplejo(String[] dest, String asun, String mens, String[] archivo, int server)
	{
		this.destinatarios = dest;
		this.asunto = asun;
		this.cuerpo = mens;
		this.archivoAdjunto = archivo;
		this.servidor = server;
		configurarServidor();
	}

	public EnviarMailComplejo(String usuario, String pass, String[] dest, String asun, String mens, String[] archivo, int server){
		this(dest, asun, mens, archivo, server);
		this.miCorreo = usuario;
		this.miPassword = pass;
	}

	public final void configurarServidor(){
		if (this.servidor == 0)
			this.servidorSMTP = "smtp.gmail.com";
		if (this.servidor == 1)
			this.servidorSMTP = "smtp.live.com";
		if (this.servidor == 2)
			this.servidorSMTP = "smtp.mail.yahoo.com";
	}

	public void Enviar() throws MessagingException {
		Properties props = null;
		props = new Properties();
		props.put("mail.smtp.host", servidorSMTP);
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.port", "587");
		props.setProperty("mail.smtp.user", miCorreo);
		props.setProperty("mail.smtp.auth", "true");
		//SecurityManager security = System.getSecurityManager();
		Session session = Session.getInstance(props, new GMailAuthenticator(miCorreo, miPassword));
		
		BodyPart texto = new MimeBodyPart();
		texto.setText(this.cuerpo);

		BodyPart[] adjunto = new BodyPart[this.archivoAdjunto.length];
		for (int j = 0; j < this.archivoAdjunto.length; j++)
		{
			adjunto[j] = new MimeBodyPart();
			adjunto[j].setDataHandler(new DataHandler(new FileDataSource(this.archivoAdjunto[j])));

			String[] rutaArchivo = this.archivoAdjunto[j].split("/");
			int nombre = rutaArchivo.length - 1;
			adjunto[j].setFileName(rutaArchivo[nombre]);
		}

		MimeMultipart multiParte = new MimeMultipart();
		multiParte.addBodyPart(texto);
		for (BodyPart aux : adjunto) {
			multiParte.addBodyPart(aux);
		}
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(this.miCorreo));
		Address[] destinos = new Address[this.destinatarios.length];
		for (int i = 0; i < destinos.length; i++) {
			destinos[i] = new InternetAddress(this.destinatarios[i]);
		}
		message.addRecipients(Message.RecipientType.TO, destinos);
		message.setSubject(this.asunto);
		message.setContent(multiParte);


		Transport t = session.getTransport("smtp");
		t.connect(miCorreo, miPassword);
		t.sendMessage(message, message.getAllRecipients());

		t.close();

	}

	private class GMailAuthenticator extends Authenticator {
		String user;
		String pw;
		public GMailAuthenticator (String username, String password) {
			super();
			this.user = username;
			this.pw = password;
		}
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, pw);
		}
	}
}