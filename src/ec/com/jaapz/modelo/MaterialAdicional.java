package ec.com.jaapz.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="material_adicional")
@NamedQueries({
	@NamedQuery(name="MaterialAdicional.findAll", query="SELECT m FROM MaterialAdicional m")
})
public class MaterialAdicional implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_mat_adicional")
	private Integer idMatAdicional;

	@ManyToOne
	@JoinColumn(name="id_instalacion")
	private Instalacion instalacion;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Double total;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	private String estado;


	//bi-directional many-to-one association to ReparacionDetalle
	@OneToMany(mappedBy="materialAdicional", cascade = CascadeType.ALL)
	private List<MaterialAdicionalDetalle> materialAdicionalDetalles;

	public List<MaterialAdicionalDetalle> getMaterialAdicionalDetalles() {
		return this.materialAdicionalDetalles;
	}

	public void setMaterialAdicionalDetalles(List<MaterialAdicionalDetalle> materialAdicionalDetalles) {
		this.materialAdicionalDetalles = materialAdicionalDetalles;
	}

	public MaterialAdicionalDetalle addMaterialAdicionalDetalles(MaterialAdicionalDetalle materialAdicionalDetalles) {
		getMaterialAdicionalDetalles().add(materialAdicionalDetalles);
		materialAdicionalDetalles.setMaterialAdicional(this);

		return materialAdicionalDetalles;
	}

	public MaterialAdicionalDetalle removeMaterialAdicionalDetalles(MaterialAdicionalDetalle materialAdicionalDetalles) {
		getMaterialAdicionalDetalles().remove(materialAdicionalDetalles);
		materialAdicionalDetalles.setRubro(null);

		return materialAdicionalDetalles;
	}

	public Integer getIdPlanilla() {
		return idMatAdicional;
	}

	public void setIdPlanilla(Integer idPlanilla) {
		this.idMatAdicional = idPlanilla;
	}

	public Instalacion getInstalacion() {
		return instalacion;
	}

	public void setInstalacion(Instalacion instalacion) {
		this.instalacion = instalacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getUsuarioCrea() {
		return usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
