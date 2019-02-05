package ec.com.jaapz.modelo;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ClaseDAO {
	// Crea una sola instancia de EntityManagerFactory para toda la applicacion.
		private static final String PERSISTENCE_UNIT_NAME = "Proyecto_Tesis_JAAPZ";
		private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
<<<<<<< HEAD
=======

>>>>>>> 4a3e64a78af2b02b84540f30112ea8727208a891
		// Objeto Entity Manager para cada instancia de un objeto que 
		// herede de esta clase.
		private EntityManager em;
		
		/**
		 * Retorna el Entity Mananager, si no existe lo crea.
		 * @return
		 */
		public EntityManager getEntityManager() {
<<<<<<< HEAD
			//EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			if (em == null){
		        em = emf.createEntityManager();
		    }
		    return em; 
		}
=======
			if (em == null){
		        em = emf.createEntityManager();
		    }
			//EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			//EntityManager em = emf.createEntityManager();
		    return em; 
		} 
		 
>>>>>>> 4a3e64a78af2b02b84540f30112ea8727208a891
		
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