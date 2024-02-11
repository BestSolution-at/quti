package at.bestsolution.qutime.handler;

import org.hibernate.Session;

import jakarta.persistence.EntityManager;

public abstract class BaseReadonlyHandler extends BaseHandler {
	private boolean processed = false;

	public BaseReadonlyHandler(EntityManager em) {
		super(em);
		// Fails with @QuarkusTest
		// this.em.unwrap(Session.class).setDefaultReadOnly(true);
	}

	public EntityManager em() {
		if (!processed) {
			this.em.unwrap(Session.class).setDefaultReadOnly(true);
			this.processed = true;
		}
		return this.em;
	}
}
