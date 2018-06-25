package com.github.rmee.boot.utils.jpa.event;

/**
 * JPA event dispatcher for all the possible entity manager listener events available.
 */
public interface JpaEventListener {

	void prePersist(Object entity);

	void postPersist(Object entity);

	void preUpdate(Object entity);

	void postUpdate(Object entity);

	void preRemove(Object entity);

	void postRemove(Object entity);

	void postLoad(Object entity);
}
