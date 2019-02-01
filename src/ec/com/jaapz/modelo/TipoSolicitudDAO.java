package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class TipoSolicitudDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<TipoSolicitud> getListaTipoSolicitud(){
		List<TipoSolicitud> resultado = new ArrayList<TipoSolicitud>();
		Query query = getEntityManager().createNamedQuery("TipoSolicitud.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<TipoSolicitud>) query.getResultList();
		return resultado;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TipoSolicitud> getSolById(Integer idTipo){
		List<TipoSolicitud> resultado = new ArrayList<TipoSolicitud>();
		Query query = getEntityManager().createNamedQuery("TipoSolicitud.findId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idTipo", idTipo);
		resultado = (List<TipoSolicitud>) query.getResultList();
		return resultado;
	}
	
}