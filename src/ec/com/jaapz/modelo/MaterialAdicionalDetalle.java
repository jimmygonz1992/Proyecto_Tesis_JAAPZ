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
@Table(name="mat_adicional_detalle")
@NamedQueries({
	@NamedQuery(name="MaterialAdicionalDetalle.findAll", query="SELECT m FROM MaterialAdicionalDetalle m")
})
public class MaterialAdicionalDetalle implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_mat_adic_detalle")
	private Integer idMatAdicionalDet;
	
	@ManyToOne
	@JoinColumn(name="id_mat_adicional")
	private MaterialAdicional materialAdicional;
	
	@ManyToOne
	@JoinColumn(name="id_rubro")
	private Rubro rubro;
	
	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	private Double precio;
	
	private Integer cantidad;
	
	private Double subtotal;
	
	private String estado;

	public Integer getIdMatAdicionalDet() {
		return idMatAdicionalDet;
	}

	public void setIdMatAdicionalDet(Integer idMatAdicionalDet) {
		this.idMatAdicionalDet = idMatAdicionalDet;
	}

	public MaterialAdicional getMaterialAdicional() {
		return materialAdicional;
	}

	public void setMaterialAdicional(MaterialAdicional materialAdicional) {
		this.materialAdicional = materialAdicional;
	}

	public Rubro getRubro() {
		return rubro;
	}

	public void setRubro(Rubro rubro) {
		this.rubro = rubro;
	}

	public Integer getUsuarioCrea() {
		return usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
