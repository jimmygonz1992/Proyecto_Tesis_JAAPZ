package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class IngresoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Ingreso> getListaIngresos(){
		List<Ingreso> resultado = new ArrayList<Ingreso>();
		Query query = getEntityManager().createNamedQuery("Ingreso.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Ingreso>) query.getResultList();
		return resultado;
	}
	
	//para recuperar factura
	@SuppressWarnings("unchecked")
	public List<Ingreso> getRecuperaIngreso(String numIngreso){
		List<Ingreso> resultado = new ArrayList<Ingreso>();
		Query query = getEntityManager().createNamedQuery("Ingreso.recuperaIngreso");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("numIngreso", numIngreso);
		resultado = (List<Ingreso>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Ingreso> getBuscaFactura(String numIngreso, int idProveedor){
		List<Ingreso> resultado = new ArrayList<Ingreso>();
		Query query = getEntityManager().createNamedQuery("Ingreso.BuscaFactura");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("numIngreso", numIngreso);
		query.setParameter("idProveedor", idProveedor);
		resultado = (List<Ingreso>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public Ingreso getUltimoRegistro() {
		try {
			Ingreso resultado = new Ingreso();
			List<Ingreso> r = new ArrayList<Ingreso>();
			Query query = getEntityManager().createNamedQuery("Ingreso.BuscaUltimo");
			query.setHint("javax.persistence.cache.storeMode", "REFRESH");
			r = (List<Ingreso>) query.getResultList();
			if(r.size() > 0)
				resultado = r.get(0);
			else
				resultado = null;
			return resultado;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Ingreso> getAllFacturaTodo(String numIngreso){
		List<Ingreso> resultado = new ArrayList<Ingreso>();
		Query query = getEntityManager().createNamedQuery("Ingreso.BuscarTodoIngreso");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("numIngreso","%" + numIngreso + "%");
		resultado = (List<Ingreso>) query.getResultList();
		return resultado;
	}
}