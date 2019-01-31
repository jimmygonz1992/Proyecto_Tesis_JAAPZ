package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class AperturaLecturaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<AperturaLectura> getListaAperturas(){
		List<AperturaLectura> resultado = new ArrayList<AperturaLectura>();
		Query query = getEntityManager().createNamedQuery("AperturaLectura.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<AperturaLectura>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<AperturaLectura> getListaAperturasByPatron(String patron){
		List<AperturaLectura> resultado = new ArrayList<AperturaLectura>();
		Query query = getEntityManager().createNamedQuery("AperturaLectura.buscarAnioMes");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron","%" + patron + "%");
		resultado = (List<AperturaLectura>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<AperturaLectura> getListaAperturasRealizadas(){
		List<AperturaLectura> resultado = new ArrayList<AperturaLectura>();
		Query query = getEntityManager().createNamedQuery("AperturaLectura.findAllRealizado");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<AperturaLectura>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<AperturaLectura> getAperturaByAnio(Integer idAnio){
		List<AperturaLectura> resultado = new ArrayList<AperturaLectura>();
		Query query = getEntityManager().createNamedQuery("AperturaLectura.buscarAperturaIdAnio");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idAnio",idAnio);
		resultado = (List<AperturaLectura>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<AperturaLectura> getListaAperturasEnProceso(){
		List<AperturaLectura> resultado = new ArrayList<AperturaLectura>();
		Query query = getEntityManager().createNamedQuery("AperturaLectura.buscarCiclo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<AperturaLectura>) query.getResultList();
		return resultado;
	}
	
	public AperturaLectura getListaAperturaById(Integer idApertura){
		AperturaLectura resultado = new AperturaLectura();
		Query query = getEntityManager().createNamedQuery("AperturaLectura.buscarAperturaId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idApertura",idApertura);
		resultado = (AperturaLectura) query.getSingleResult();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public boolean validarApertura() {
		try {
			boolean bandera = false;
			List<AperturaLectura> resultado = new ArrayList<AperturaLectura>();
			Query query = getEntityManager().createNamedQuery("AperturaLectura.buscarCiclo");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			resultado = (List<AperturaLectura>) query.getResultList();
			if(resultado.size() > 0)
				bandera = true;
			return bandera;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
}
