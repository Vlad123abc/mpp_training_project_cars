package cars;

public interface UserRepository extends Repository<Long, User>
{
    User getByUsername(String username);
}
