
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    com.andreistraut.gaps.controller.dispatchers.GetGraphMessageDispatcherTest.class,
    com.andreistraut.gaps.controller.dispatchers.ComputePathMessageDispatcherTest.class,
    com.andreistraut.gaps.controller.dispatchers.EvolveMessageDispatcherTest.class,
    com.andreistraut.gaps.controller.dispatchers.MessageDispatcherFactoryTest.class
    })
public class ControllerDispatchers {
}
