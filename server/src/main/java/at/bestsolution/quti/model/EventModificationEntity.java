package at.bestsolution.quti.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity(name = "EventModification")
@DiscriminatorColumn(name = "em_type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class EventModificationEntity {
	public static final String DISCRIMINATOR_CANCELED = "1";
	public static final String DISCRIMINATOR_GENERIC = "2";
	public static final String DISCRIMINATOR_MOVED = "3";

	@Id
	@SequenceGenerator(name = "eventmod_seq", sequenceName = "eventmod_seq_id", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "eventmod_seq")
	@Column(name = "em_id")
	public Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "em_fk_event", foreignKey = @ForeignKey(name = "eventmod_fkey_event"))
	public EventEntity event;

	@Column(name = "em_date", nullable = false)
	public LocalDate date;

	public LocalDate date() {
		return this.date;
	}
}
