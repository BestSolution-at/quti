package at.bestsolution.quti.handler;

import jakarta.persistence.EntityManager;

public abstract class BaseHandler {
	final EntityManager em;

	public BaseHandler(EntityManager em) {
		this.em = em;
	}

	public EntityManager em() {
		return this.em;
	}
}
