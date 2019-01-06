package ec.com.jaapz.correo;



import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

public class EnviarMail {
	String miCorreo;
	String miPassword;
	String servidorSMTP;
	String puertoEnvio;
	String[] destinatarios;
	String asunto = null;
	String cuerpo = null;
	int servidor;

	public EnviarMail(String[] mailReceptor, String asunto, String cuerpo, int servidor) {
		this.destinatarios = mailReceptor;
		this.asunto = asunto;
		this.cuerpo = cuerpo;
		this.servidor = servidor;
		configurarServidor();
	}

	public EnviarMail(String user, String pass, String[] mailReceptor, String asunto, String cuerpo, int servidor) {
		this(mailReceptor, asunto, cuerpo, servidor);
		this.miCorreo = user;
		this.miPassword = pass;
	}

	public final void configurarServidor() {
		if (this.servidor == 0) {
			this.servidorSMTP = "smtp.gmail.com";
			this.puertoEnvio = "587";
		}
		if (this.servidor == 1){
			this.servidorSMTP = "smtp.live.com";
			this.puertoEnvio = "587";
		}
		if (this.servidor == 2) {
			this.servidorSMTP = "smtp.mail.yahoo.com";
			this.puertoEnvio = "587";
		}
	}

	public void enviar() throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.user", this.miCorreo);
		props.put("mail.smtp.host", this.servidorSMTP);
		props.put("mail.smtp.port", this.puertoEnvio);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.port", this.puertoEnvio);
		props.setProperty("mail.smtp.port", "587");

		//SecurityManager security = System.getSecurityManager();

		Authenticator auth = new autentificadorSMTP();
		Session session = Session.getInstance(props, auth);
		session.setDebug(true);

		MimeMessage msg = new MimeMessage(session);
		msg.setText(this.cuerpo);
		msg.setSubject(this.asunto);
		msg.setFrom(new InternetAddress(this.miCorreo));
		
		Address[] destinos = new Address[this.destinatarios.length];
		for (int i = 0; i < destinos.length; i++) {
			destinos[i] = new InternetAddress(this.destinatarios[i]);
		}
		msg.addRecipients(Message.RecipientType.TO, destinos);
		try {
			Transport t = session.getTransport("smtp");
			t.connect(this.miCorreo, this.miPassword);
			t.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Correo Enviado exitosamente!");
			t.close();
		}
		catch (AuthenticationFailedException ex) {
			JOptionPane.showMessageDialog(null, new Object[] { "Usuario o contraseña incorrecto" }, "Error de autentificacion", 0);
			throw new MessagingException();
		}
	}

	private class autentificadorSMTP extends Authenticator {
		private autentificadorSMTP() {}

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(EnviarMail.this.miCorreo, EnviarMail.this.miPassword);
		}
	}
}