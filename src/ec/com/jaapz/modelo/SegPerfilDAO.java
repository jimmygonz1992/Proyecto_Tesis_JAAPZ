package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SegPerfilDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<SegPerfil> getListaPerfil(){
		List<SegPerfil> resultado = new ArrayList<SegPerfil>();
		Query query = getEntityManager().createNamedQuery("SegPerfil.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<SegPerfil>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<SegPerfil> getAllListaPerfil(){
		List<SegPerfil> resultado = new ArrayList<SegPerfil>();
		Query query = getEntityManager().createNamedQuery("SegPerfil.findAllPerfiles");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<SegPerfil>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<SegPerfil> getPerfil(int codigo){
		List<SegPerfil> resultado = new ArrayList<SegPerfil>();
		Query query = getEntityManager().createNamedQuery("SegPerfil.findPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPerfil", codigo);
		resultado = (List<SegPerfil>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<SegPerfil> getUltimoPerfil(){
		List<SegPerfil> resultado = new ArrayList<SegPerfil>();
		Query query = getEntityManager().createNamedQuery("SegPerfil.BuscarUltimoPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<SegPerfil>) query.getResultList();
		return resultado;
	}
}
