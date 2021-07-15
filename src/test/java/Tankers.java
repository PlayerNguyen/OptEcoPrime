import com.playernguyen.optecoprime.tankers.OptEcoTanker;
import org.junit.Assert;
import org.junit.Test;

public class Tankers {

    @Test
    public void shouldContainElement() {
        OptEcoTanker<Integer> tanker = new OptEcoTanker<>();
        tanker.getCollection().add(1);

        Assert.assertArrayEquals(new Integer[]{1}, tanker.getCollection().toArray());
    }

    @Test
    public void shouldBeRemovable() {
        OptEcoTanker<Integer> tanker = new OptEcoTanker<>();
        tanker.getCollection().add(1);
        tanker.getCollection().remove(1);

        Assert.assertArrayEquals(new Integer[]{}, tanker.getCollection().toArray());
    }

}
