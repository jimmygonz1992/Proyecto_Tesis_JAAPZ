package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class ClienteDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Cliente> getListaClientes(){
		List<Cliente> resultado = new ArrayList<Cliente>();
		Query query = getEntityManager().createNamedQuery("Cliente.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Cliente>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> getListaClientesPatron(String patron){
		List<Cliente> resultado = new ArrayList<Cliente>();
		Query query = getEntityManager().createNamedQuery("Cliente.bucarPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Cliente>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Cliente> getListaClientesCedula(String patron){
		List<Cliente> resultado = new ArrayList<Cliente>();
		Query query = getEntityManager().createNamedQuery("Cliente.bucarCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Cliente>) query.getResultList();
		return resultado;
	}
}
