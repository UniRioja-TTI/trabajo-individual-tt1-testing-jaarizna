package servicios;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;

@Service

public class EnvioMail implements InterfazEnviarEmails {

	private final Logger logger;
	
	public EnvioMail(Logger logger) {
		this.logger=logger;
	}
	
	@Override
	public boolean enviarEmail(Destinatario dest, String email) {
	this.logger.info(dest+":"+email);
		return false;
	}

}
