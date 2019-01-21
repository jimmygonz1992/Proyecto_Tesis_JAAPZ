package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.com.jaapz.util.Context;

public class InstalacionDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Instalacion> getListaInstalacionAsignada(Integer patron){
		List<Instalacion> resultado = new ArrayList<Instalacion>();
		Query query = getEntityManager().createNamedQuery("Instalacion.buscarInstalacionAsignada");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPerfilUsuario", patron );
		resultado = (List<Instalacion>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Instalacion> getListaInstalacionPendiente(String patron){
		List<Instalacion> resultado = new ArrayList<Instalacion>();
		Query query = getEntityManager().createNamedQuery("Instalacion.findAllPendiente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Instalacion>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Instalacion> getListaInstalacionPerfilPendiente(String patron){
		List<Instalacion> resultado = new ArrayList<Instalacion>();
		Query query = getEntityManager().createNamedQuery("Instalacion.buscarInstalacionPerfilPendiente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdUsuario());
		resultado = (List<Instalacion>) query.getResultList();
		return resultado;
	}
}