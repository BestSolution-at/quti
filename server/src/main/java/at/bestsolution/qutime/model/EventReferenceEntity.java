package at.bestsolution.qutime.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity(name = "EventReference")
public class EventReferenceEntity {
	@Id
	@SequenceGenerator(name = "eventref_seq", sequenceName = "eventref_seq_id", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "eventref_seq")
	@Column(name = "er_id")
	public Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "er_fk_calendar", foreignKey = @ForeignKey(name = "eventref_fkey_calendar"))
	public CalendarEntity calendar;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "er_fk_event", foreignKey = @ForeignKey(name = "eventref_fkey_event"))
	public EventEntity event;
}
