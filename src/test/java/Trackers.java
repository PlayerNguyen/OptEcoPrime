import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class Trackers {

    @Test
    public void shouldReturnValue() throws Exception {
        Callable<?> value = () -> 5 + 5;
        Assert.assertEquals(10, value.call());
    }

}
