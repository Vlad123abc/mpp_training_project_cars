package cars;

public interface IService
{
    void login(User user, IObserver client) throws Exception;
}
