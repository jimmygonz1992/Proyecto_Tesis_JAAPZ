package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.com.jaapz.util.Context;

public class SolInspeccionRepDAO extends ClaseDAO{
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getListaInspeccionPerfilPendiente(String patron){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.buscarInspeccionPerfilPendiente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdUsuario());
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getListaInspeccionPendiente(String patron){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.findAllPendiente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getListaInspeccionAsignada(Integer patron){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.buscarInspeccionAsignada");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPerfilUsuario", patron );
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
	
	//para asignar
	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getListaReparaciones(String patron){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.buscarListaRep");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
	//para asignar
	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getListaReparacionesPerfilPendiente(String patron){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.buscarListaRepPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdUsuario());
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getSolicitudesNoAtendidas(){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.buscarSolicitudesNoAtendidas");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getSolicitudesExistente(Integer idCuenta){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.buscarExistente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCuenta", idCuenta);
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
}