import cars.Car;
import cars.IObserver;
import cars.IService;
import cars.jsonProtocol.ServiceProxy;

public class NetworkingTest
{
    private static int defaultChatPort = 55556;
    private static String defaultServer = "localhost";

    private IService service = new ServiceProxy(defaultServer, defaultChatPort);
    private IObserver observer = new IObserver()
    {
        @Override
        public void carSaved(Car car) throws Exception
        {
            System.out.println("Observer Car Saved");
        }

        @Override
        public void carDeleted(Long id) throws Exception
        {
            System.out.println("Observer Car Deleted");
        }
    };
}
