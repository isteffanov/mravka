package transducer;

import dev.most.common.Dictionary;
import dev.most.common.TransducerEntry;
import dev.most.transducer.TransducerStructure;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransducerStructureStaticTest {

    @Test
    public void givenEmptyTransducerMap_thenSingleNonFinalStateIsPresent() {
        TransducerStructure map = TransducerStructure.createEmpty();

        assertEquals(1, map.getNumberOfStates());
    }

    @Test
    public void givenTransducerMapWithSingleEntry_thenStatesAreChainedAndOutputIsPulledToTheBeginning() {
        Dictionary dictionary = Dictionary.of(TransducerEntry.of("key", "value"));
        TransducerStructure map = TransducerStructure.createFromOrderedDictionary(dictionary);

        assertEquals(1, map.getDegreeOfSpread());
        assertEquals("value", map.getPrefixOutput());
        assertEquals(4, map.getNumberOfStates());
    }

    @Test
    public void givenTransducerMapWithTwoEntries_whenTheyHaveCommonPrefix_thenOutputIsPrefixedAtTheBeginning() {
        Dictionary dictionary = Dictionary.of(
                TransducerEntry.of("key1", "value1"),
                TransducerEntry.of("key2", "value2"));
        TransducerStructure map = TransducerStructure.createFromOrderedDictionary(dictionary);

        assertEquals(2, map.getDegreeOfSpread());
        assertEquals("value", map.getPrefixOutput());
        assertEquals(5, map.getNumberOfStates());
    }
}
