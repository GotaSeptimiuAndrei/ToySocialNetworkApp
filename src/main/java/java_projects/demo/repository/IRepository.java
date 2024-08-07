package java_projects.demo.repository;

import java_projects.demo.domain.Entity;

public interface IRepository<ID, E extends Entity<ID>> {
    /**
     * Returns the element with the chosen id
     * @param id - must be not null
     * @return the chosen entity
     * @throws IllegalArgumentException if the given id is null
     */
    E findById(ID id);

    /**
     * Returns a list with all elements from repository
     * @return Iterable <E> - entities from repository
     */
    Iterable<E> findAll();

    /**
     * Add a new element in the list
     * @param entity - the entity we add
     * @return one E if the id of entity is already taken
     */
    E add(E entity);

    /**
     * Remove an element from repo with the chosen id
     * @param id - the id we want to delete
     */
    void remove(ID id);

    /**
     * Update the attributes of the entity with the chosen id
     * @param id the id of the entity
     * @param entity the new entity
     */
    void updateEntity(ID id, E entity);

    /**
     * Return true if the id is available and false otherwise
     * @param id - the id we check
     * @return boolean true or false
     */
    boolean availableId(ID id);

    /**
     * Return the number of elements from the repository
     * @return int
     */
    int size();
}
