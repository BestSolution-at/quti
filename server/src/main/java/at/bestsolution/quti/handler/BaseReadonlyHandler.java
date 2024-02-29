package at.bestsolution.qutime.handler;

import org.hibernate.Session;

import jakarta.persistence.EntityManager;

public abstract class BaseReadonlyHandler extends BaseHandler {
	// private boolean processed = false;

	public BaseReadonlyHandler(EntityManager em) {
		super(em);
		// Fails with @QuarkusTest
		// this.em.unwrap(Session.class).setDefaultReadOnly(true);
	}

	public EntityManager em() {
		// System.err.println("======> GET THE EM " + this);
		// if (!processed) {
		//	System.err.println("====> MARK IT READONLY" + this);
			this.em.unwrap(Session.class).setDefaultReadOnly(true);
		//	this.processed = true;
		//}
		return this.em;
	}
}
