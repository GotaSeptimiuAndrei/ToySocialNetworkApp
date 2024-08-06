package java_projects.demo.domain;

import java.io.Serializable;

public abstract class Entity<ID>  implements Serializable {

    private ID id;

    public Entity(ID id){
        this.id = id;
    }

    /**
     * Return the id of the entity
     * @return id of chosen type ID
     */
    public ID getId(){
        return id;
    }

    /**
     * Set the id of the entity
     * @param id of chosen type ID
     */
    public void setId(ID id){
        this.id = id;
    }

}
