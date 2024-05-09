package cars;

public interface IObserver
{
    void carSaved(Car car) throws Exception;
    void carDeleted(Long id) throws Exception;
}
