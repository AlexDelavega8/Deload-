package es.pocketrainer.util.bbdd;

import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

@MappedSuperclass
public abstract class TablaI18N<T extends TraduccionI18N> {

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "tablai18n_id")
	private Set<T> traducciones;

	public Set<T> getTraducciones() {
		return traducciones;
	}

	public void setTraducciones(Set<T> traducciones) {
		this.traducciones = traducciones;
	}

}
