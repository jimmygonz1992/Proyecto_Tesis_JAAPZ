package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class MedidorDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Medidor> getListaMedidores(){
		List<Medidor> resultado = new ArrayList<Medidor>();
		Query query = getEntityManager().createNamedQuery("Medidor.medidoresDisponibles");
		resultado = (List<Medidor>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Medidor> getRecuperaMedidor(String codigo){
		List<Medidor> resultado = new ArrayList<Medidor>();
		Query query = getEntityManager().createNamedQuery("Medidor.recuperaMedidor");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("codigo", codigo);
		resultado = (List<Medidor>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Medidor> getValidarCodigoMedidor(String codigo) {
		List<Medidor> resultado; 
		Query query = getEntityManager().createNamedQuery("Medidor.validarCodigo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("codigo", codigo);
		resultado = (List<Medidor>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Medidor> getRecuperarMedidorFactura(Integer idFactura) {
		List<Medidor> resultado; 
		Query query = getEntityManager().createNamedQuery("Medidor.recuMedidorFactura");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idFactura", idFactura);
		resultado = (List<Medidor>) query.getResultList();
		return resultado;
	}
	
}