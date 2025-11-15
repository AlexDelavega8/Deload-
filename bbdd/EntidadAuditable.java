package es.pocketrainer.util.bbdd;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Si uso envers, diria que la fecha de creacion y modificacion sobran, ya que
 * se verán reflejadas siempre en revinfo. Hombre, quizá puede ayudar la
 * redundancia para visualizar a saco en bbdd.
 * 
 * @author Antonio FZ
 *
 */
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public class EntidadAuditable {

	@CreatedDate
	private Long fechaCreacion;

	@LastModifiedDate
	private Long fechaModificacion;

	@CreatedBy
	private String usuarioCreacion;

	@LastModifiedBy
	private String usuarioModificacion;

}
