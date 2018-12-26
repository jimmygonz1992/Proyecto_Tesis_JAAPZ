package ec.com.jaapz.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="seg_usuario_perfil")
@NamedQueries({
	@NamedQuery(name="SegUsuarioPerfil.findAll", query="SELECT u FROM SegUsuarioPerfil u"),
})
public class SegUsuarioPerfil implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario_perfil")
	private Integer idUsuarioPerfil;
	
	private String estado;
	
	@ManyToOne
	@JoinColumn(name="id_perfil")
	private SegPerfil segPerfil;
	
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private SegUsuario segUsuario;

	public Integer getIdUsuarioPerfil() {
		return idUsuarioPerfil;
	}

	public void setIdUsuarioPerfil(Integer idUsuarioPerfil) {
		this.idUsuarioPerfil = idUsuarioPerfil;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public SegPerfil getSegPerfil() {
		return segPerfil;
	}

	public void setSegPerfil(SegPerfil segPerfil) {
		this.segPerfil = segPerfil;
	}

	public SegUsuario getSegUsuario() {
		return segUsuario;
	}

	public void setSegUsuario(SegUsuario segUsuario) {
		this.segUsuario = segUsuario;
	}
	
}
