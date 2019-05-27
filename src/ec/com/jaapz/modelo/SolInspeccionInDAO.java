package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.com.jaapz.util.Context;

public class SolInspeccionInDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<SolInspeccionIn> getListaInspeccion(String patron){
		List<SolInspeccionIn> resultado = new ArrayList<SolInspeccionIn>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionIn.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<SolInspeccionIn>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<SolInspeccionIn> getListaAllInspeccion(String patron){
		List<SolInspeccionIn> resultado = new ArrayList<SolInspeccionIn>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionIn.buscarInspeccionesTodas");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<SolInspeccionIn>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<SolInspeccionIn> getListaInspeccionPendiente(String patron){
		List<SolInspeccionIn> resultado = new ArrayList<SolInspeccionIn>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionIn.findAllPendiente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<SolInspeccionIn>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionIn> getListaInspeccionPerfil(String patron){
		List<SolInspeccionIn> resultado = new ArrayList<SolInspeccionIn>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionIn.buscarInspeccionPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdUsuario());
		resultado = (List<SolInspeccionIn>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionIn> getListaInspeccionPerfilPendiente(String patron){
		List<SolInspeccionIn> resultado = new ArrayList<SolInspeccionIn>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionIn.buscarInspeccionPerfilPendiente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdUsuario());
		resultado = (List<SolInspeccionIn>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<SolInspeccionIn> getListaInspeccionAsignada(Integer patron){
		List<SolInspeccionIn> resultado = new ArrayList<SolInspeccionIn>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionIn.buscarInspeccionAsignada");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPerfilUsuario", patron );
		resultado = (List<SolInspeccionIn>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<SolInspeccionIn> getInspeccionCliente(Integer idCliente){
		List<SolInspeccionIn> resultado = new ArrayList<SolInspeccionIn>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionIn.buscarInspeccionCliente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCliente", idCliente );
		resultado = (List<SolInspeccionIn>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionIn> getSolicitudesNoAtendidas(){
		List<SolInspeccionIn> resultado = new ArrayList<SolInspeccionIn>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionIn.buscarSolicitudesNoAtendidas");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<SolInspeccionIn>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionIn> getSolicitudesPendientes(String patron){
		List<SolInspeccionIn> resultado = new ArrayList<SolInspeccionIn>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionIn.buscarNoAtendidas");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<SolInspeccionIn>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionIn> getSolicitudesNoFactibles(String patron){
		List<SolInspeccionIn> resultado = new ArrayList<SolInspeccionIn>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionIn.buscarNoFactibles");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<SolInspeccionIn>) query.getResultList();
		return resultado;
	}
}
