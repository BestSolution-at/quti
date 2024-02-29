package at.bestsolution.qutime.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity(name = "Calendar")
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "calendar_uq_key", columnNames = { "ca_key" })
})
public class CalendarEntity {
	@Id
	@SequenceGenerator(name = "calendar_seq", sequenceName = "calendar_seq_id", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "calendar_seq")
	@Column(name = "ca_id")
	public Long id;

	@Version
	@Column(name = "ca_version")
	public long version;

	@Column(name = "ca_key", nullable = false)
	public UUID key;

	@Column(name = "ca_name", nullable = false)
	public String name;

	@Column(name = "ca_owner")
	public String owner;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "calendar", orphanRemoval = true)
	public List<EventEntity> events;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "calendar", orphanRemoval = true)
	public List<EventReferenceEntity> eventReferences;
}
