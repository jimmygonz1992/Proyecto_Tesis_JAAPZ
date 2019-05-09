package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the seg_usuario database table.
 * 
 */
@Entity
@Table(name="seg_usuario")
@NamedQueries({
	@NamedQuery(name="SegUsuario.findAll", query="SELECT u FROM SegUsuario u Order By u.idUsuario asc"),
	@NamedQuery(name="SegUsuario.buscarPatron", query="SELECT u FROM SegUsuario u "
			+ "WHERE u.usuario = (:usuario) AND u.clave = (:clave) and u.estado = 'A'"),
	@NamedQuery(name="SegUsuario.buscarUsuario", query="SELECT u FROM SegUsuario u "
			+ "WHERE u.usuario = (:usuario) and u.estado = 'A'"),
	@NamedQuery(name="SegUsuario.validarUsuario", query="SELECT u FROM SegUsuario u "
			+ "WHERE u.usuario = (:usuario) AND u.idUsuario <> (:idUsuario) and u.estado = 'A'"),
	@NamedQuery(name="SegUsuario.buscarTodosUsuarios", query="SELECT u FROM SegUsuario u where u.estado = 'A'"),
	@NamedQuery(name="SegUsuario.recuperaUsuario", query="SELECT u FROM SegUsuario u WHERE u.cedula = (:cedula) and u.estado = 'A'"),
	@NamedQuery(name="SegUsuario.recuperaUsuarioValidaContrasenia", query="SELECT u FROM SegUsuario u WHERE (u.clave = (:clave) and u.usuario = (:usuario)) and u.estado = 'A'")
})
public class SegUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private Integer idUsuario;

	private String apellido;

	private String cargo;

	private String cedula;

	private String clave;

	private String direccion;

	private String estado;

	private byte[] foto;

	private String nombre;

	private String nuevo;

	private String telefono;

	private String usuario;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	@Column(name="usuario_desencriptado")
	private String usuarioDesencriptado;

	//bi-directional many-to-one association to ResponsableLectura
	@OneToMany(mappedBy="segUsuario", cascade = CascadeType.ALL)
	private List<ResponsableLectura> responsableLecturas;



	//bi-directional many-to-one association to SegUsuario
	@OneToMany(mappedBy="segUsuario", cascade = CascadeType.ALL)
	private List<SegUsuarioPerfil> segUsuarioPerfils;

	public List<SegUsuarioPerfil> getSegUsuarioPerfils() {
		return this.segUsuarioPerfils;
	}

	public void setSegUsuarioPerfils(List<SegUsuarioPerfil> segUsuarioPerfils) {
		this.segUsuarioPerfils = segUsuarioPerfils;
	}

	public SegUsuarioPerfil addSegUsuarioPerfil(SegUsuarioPerfil segUsuarioPerfils) {
		getSegUsuarioPerfils().add(segUsuarioPerfils);
		segUsuarioPerfils.setSegUsuario(this);

		return segUsuarioPerfils;
	}

	public SegUsuarioPerfil removeSegUsuarioPerfil(SegUsuarioPerfil segUsuarioPerfils) {
		getSegUsuarioPerfils().remove(segUsuarioPerfils);
		segUsuarioPerfils.setSegUsuario(null);

		return segUsuarioPerfils;
	}





	//bi-directional many-to-one association to Reconexion
	@OneToMany(mappedBy="segUsuario", cascade = CascadeType.ALL)
	private List<Reconexion> reconexions;
	public List<Reconexion> getReconexions() {
		return reconexions;
	}
	public void setReconexions(List<Reconexion> reconexions) {
		this.reconexions = reconexions;
	}
	public Reconexion addReconexion(Reconexion reconexion) {
		getReconexions().add(reconexion);
		reconexion.setSegUsuario(this);
		return reconexion;
	}
	public Reconexion removeReconexion(Reconexion reconexion) {
		getReconexions().remove(reconexion);
		reconexion.setSegUsuario(null);

		return reconexion;
	}



	//bi-directional many-to-one association to Reconexion
	@OneToMany(mappedBy="segUsuario", cascade = CascadeType.ALL)
	private List<Corte> cortes;
	public List<Corte> getCortes() {
		return cortes;
	}
	public void setCortes(List<Corte> cortes) {
		this.cortes = cortes;
	}
	public Corte addCorte(Corte corte) {
		getCortes().add(corte);
		corte.setSegUsuario(this);
		return corte;
	}
	public Corte removeCorte(Corte corte) {
		getCortes().remove(corte);
		corte.setSegUsuario(null);

		return corte;
	}

	public SegUsuario() {
	}

	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCargo() {
		return this.cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNuevo() {
		return this.nuevo;
	}

	public void setNuevo(String nuevo) {
		this.nuevo = nuevo;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public List<ResponsableLectura> getResponsableLecturas() {
		return this.responsableLecturas;
	}

	public void setResponsableLecturas(List<ResponsableLectura> responsableLecturas) {
		this.responsableLecturas = responsableLecturas;
	}

	public ResponsableLectura addResponsableLectura(ResponsableLectura responsableLectura) {
		getResponsableLecturas().add(responsableLectura);
		responsableLectura.setSegUsuario(this);

		return responsableLectura;
	}

	public ResponsableLectura removeResponsableLectura(ResponsableLectura responsableLectura) {
		getResponsableLecturas().remove(responsableLectura);
		responsableLectura.setSegUsuario(null);

		return responsableLectura;
	}

	public String getUsuarioDesencriptado() {
		return usuarioDesencriptado;
	}

	public void setUsuarioDesencriptado(String usuarioDesencriptado) {
		this.usuarioDesencriptado = usuarioDesencriptado;
	}

}