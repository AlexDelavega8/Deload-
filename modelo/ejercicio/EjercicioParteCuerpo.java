package es.pocketrainer.modelo.ejercicio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EjercicioParteCuerpo {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ejercicio_id")
	private Ejercicio ejercicio;

	@ManyToOne
	@JoinColumn(name = "parte_cuerpo_id")
	private ParteCuerpo parteCuerpo;

	private Boolean principal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ejercicio getEjercicio() {
		return ejercicio;
	}

	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}

	public ParteCuerpo getParteCuerpo() {
		return parteCuerpo;
	}

	public void setParteCuerpo(ParteCuerpo parteCuerpo) {
		this.parteCuerpo = parteCuerpo;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

}
