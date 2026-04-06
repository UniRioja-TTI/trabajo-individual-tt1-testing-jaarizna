package servicios;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;

@Service
public class Contacto implements InterfazContactoSim {

	private DatosSolicitud ds;
	@Override
	public int solicitarSimulation(DatosSolicitud sol) {
this.ds=sol;
Random r= new Random();
return r.nextInt(99999);

	}

	@Override
	public DatosSimulation descargarDatos(int ticket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entidad> getEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValidEntityId() {
		// TODO Auto-generated method stub
		return false;
	}

}
