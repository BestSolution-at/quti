package at.bestsolution.qutime.model;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity(name = "Event")
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "event_uq_key", columnNames = { "ev_key" }),
		@UniqueConstraint(name = "event_uq_fk_repeat_pattern", columnNames = { "ev_fk_repeat_pattern" })
})

public class EventEntity {
	@Id
	@SequenceGenerator(name = "event_seq", sequenceName = "event_seq_id", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "event_seq")
	@Column(name = "ev_id")
	public Long id;

	@Version
	@Column(name = "ev_version")
	public long version;

	@Column(name = "ev_key", nullable = false)
	public UUID key;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ev_fk_calendar", foreignKey = @ForeignKey(name = "event_fkey_calendar"))
	public CalendarEntity calendar;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ev_fk_repeat_pattern", foreignKey = @ForeignKey(name = "event_fkey_repeatpattern"))
	public EventRepeatEntity repeatPattern;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event", orphanRemoval = true)
	public List<EventReferenceEntity> references;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event", orphanRemoval = true)
	public List<EventModificationEntity> modifications;

	@Column(name = "ev_title", nullable = false)
	public String title;
	@Column(name = "ev_description", columnDefinition = "text")
	public String description;
	@Column(name = "ev_start", nullable = false)
	public ZonedDateTime start;
	@Column(name = "ev_end", nullable = false)
	public ZonedDateTime end;

	private transient Map<LocalDate, List<EventModificationEntity>> modificationByDate;

	public List<EventModificationEntity> modificationsAt(LocalDate date) {
		if (modificationByDate == null) {
			modificationByDate = modifications.stream().collect(Collectors.groupingBy(EventModificationEntity::date));
		}
		return modificationByDate.getOrDefault(date, List.of());
	}
}
