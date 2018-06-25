package com.github.rmee.boot.utils.jpa.event;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * JPA event listener for converting JPA events into other type of events
 * <p>
 * Converts to CDI events by default
 * <br>
 * This class would normally be added into orm.xml as a global entity listener
 */
public class SpringJpaEventDispatcher implements Serializable, ApplicationContextAware {

	private static ApplicationContext applicationContext;

	private static Collection<JpaEventListener> getListeners() {
		return applicationContext.getBeansOfType(JpaEventListener.class).values();
	}

	@PrePersist
	public void firePrePersist(Object entity) {
		getListeners().forEach(it -> it.prePersist(entity));
	}

	@PostPersist
	public void firePostPersist(Object entity) {
		getListeners().forEach(it -> it.postPersist(entity));
	}

	@PreUpdate
	public void firePreUpdate(Object entity) {
		getListeners().forEach(it -> it.preUpdate(entity));
	}

	@PostUpdate
	public void firePostUpdate(Object entity) {
		getListeners().forEach(it -> it.postUpdate(entity));
	}

	@PreRemove
	public void firePreRemove(Object entity) {
		getListeners().forEach(it -> it.preRemove(entity));
	}

	@PostRemove
	public void firePostRemove(Object entity) {
		getListeners().forEach(it -> it.postRemove(entity));
	}

	@PostLoad
	public void firePostLoad(Object entity) {
		getListeners().forEach(it -> it.postLoad(entity));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringJpaEventDispatcher.applicationContext = applicationContext;
	}
}
