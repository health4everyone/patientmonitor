package org.health4everyone.patientmonitoring.models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="Physician")
@Table(name="physicians")
public class Physician {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
	
	@ManyToOne
	private Person person;

	public UUID getId() {
		return this.id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	
}
