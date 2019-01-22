package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class EstadoMedidorDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<EstadoMedidor> getListaEstadoMedidor(){
		List<EstadoMedidor> resultado = new ArrayList<EstadoMedidor>();
		Query query = getEntityManager().createNamedQuery("EstadoMedidor.findAll");
		resultado = (List<EstadoMedidor>) query.getResultList();
		return resultado;
	}
	public EstadoMedidor getEstadoBueno() {
		try {
			EstadoMedidor resultado = new EstadoMedidor();
			Query query = getEntityManager().createNamedQuery("EstadoMedidor.buscarBueno");
			resultado = (EstadoMedidor) query.getSingleResult();
			return resultado;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
}