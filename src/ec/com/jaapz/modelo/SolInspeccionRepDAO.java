package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SolInspeccionRepDAO extends ClaseDAO{

	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getListaInspeccionAsignada(Integer patron){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.buscarInspeccionAsignada");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPerfilUsuario", patron );
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
}