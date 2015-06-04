package ro.sapientia2015.story.model;

import org.junit.Test;

import ro.sapientia2015.story.model.AcceptanceTest;
import static junit.framework.Assert.*;

/**
 * @author Csonka Erik
 */
public class AcceptanceTestTest {

    private String TITLE = "title";
    private String DESCRIPTION = "description";

    @Test
    public void buildWithMandatoryInformation() {
    	AcceptanceTest built = AcceptanceTest.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
    	AcceptanceTest built = AcceptanceTest.getBuilder(TITLE)
                .description(DESCRIPTION)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(DESCRIPTION, built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
    	AcceptanceTest atest = new AcceptanceTest();
    	atest.prePersist();

        assertNull(atest.getId());
        assertNotNull(atest.getCreationTime());
        assertNull(atest.getDescription());
        assertNotNull(atest.getModificationTime());
        assertNull(atest.getTitle());
        assertEquals(0L, atest.getVersion());
        assertEquals(atest.getCreationTime(), atest.getModificationTime());
    }

    @Test
    public void preUpdate() {
    	AcceptanceTest atest = new AcceptanceTest();
        atest.prePersist();

        pause(1000);

        atest.preUpdate();

        assertNull(atest.getId());
        assertNotNull(atest.getCreationTime());
        assertNull(atest.getDescription());
        assertNotNull(atest.getModificationTime());
        assertNull(atest.getTitle());
        assertEquals(0L, atest.getVersion());
        assertTrue(atest.getModificationTime().isAfter(atest.getCreationTime()));
    }
    
    @Test
	public void testTitle(){
    	AcceptanceTest atest = AcceptanceTest.getBuilder("Haliho").build();
		assertNotNull(atest.getTitle());
	}
    
    private void pause(long timeInMillis) {
        try {
            Thread.currentThread().sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
}
