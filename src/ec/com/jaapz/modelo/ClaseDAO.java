package ec.com.jaapz.modelo;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ClaseDAO {

	private static final String PERSISTENCE_UNIT_NAME = "Proyecto_Tesis_JAAPZ";
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	
	

	private EntityManager em;
	private EntityManager emNuevo;

	
	public EntityManager getEntityManager() {
		if (em == null){
			em = emf.createEntityManager();
		}
		return em; 
	} 

	public EntityManager getEntityManagerNuevo() {
		EntityManagerFactory emfNuevo = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		if (emNuevo == null){
			emNuevo = emfNuevo.createEntityManager();
		}
		return emNuevo; 
	} 
	

	public Connection abreConexion() {
		EntityManager entityManager; 
		entityManager = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
		Connection connection = null;
		entityManager.getTransaction().begin();
		connection = entityManager.unwrap(Connection.class);
		return connection;
	}

	/**
	 * Cierra una conexion JDBC.
	 * @param cn
	 */
	public void cierraConexion(Connection cn) {
		try {
			cn.close();
			cn = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}