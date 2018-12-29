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
}