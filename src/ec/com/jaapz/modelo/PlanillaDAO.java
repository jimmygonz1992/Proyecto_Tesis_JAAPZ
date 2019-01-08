package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.com.jaapz.util.Context;

public class PlanillaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Planilla> getListaPlanilla(String patron){
		List<Planilla> resultado = new ArrayList<Planilla>();
		Query query = getEntityManager().createNamedQuery("Planilla.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Planilla>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Planilla> getListaPlanillaPerfil(String patron){
		List<Planilla> resultado = new ArrayList<Planilla>();
		Query query = getEntityManager().createNamedQuery("Planilla.buscarPlanillaPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdPerfil());
		resultado = (List<Planilla>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Planilla> getPlanillaCuenta(Integer patron){
		List<Planilla> resultado = new  ArrayList<Planilla>();
		Query query = getEntityManager().createNamedQuery("Planilla.buscarPorCuenta");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCuenta", patron);
		resultado = (List<Planilla>) query.getResultList();
		return resultado;
	}	
	
	@SuppressWarnings("unchecked")
	public List<Planilla> getPlanillaAtrasada(Integer idCuenta,Integer idPlanillaActual){
		List<Planilla> resultado = new  ArrayList<Planilla>();
		Query query = getEntityManager().createNamedQuery("Planilla.buscarPorCuentaPlanilla");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCuenta", idCuenta);
		query.setParameter("idPlanillaActual", idPlanillaActual);
		resultado = (List<Planilla>) query.getResultList();
		return resultado;
	}
}