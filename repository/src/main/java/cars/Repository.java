package cars;

import java.util.List;

public interface Repository <ID, E extends Entity<ID>>
{
    E getById(ID id);
    List<E> getAll();
    void save(E entity);
    void delete(ID id);
    void update(E entity);
}
