package library;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test suite for Library ADT.
 */
@RunWith(Parameterized.class)
public class LibraryTest {

    /*
     * Note: all the tests you write here must be runnable against any
     * Library class that follows the spec.  JUnit will automatically
     * run these tests against both SmallLibrary and BigLibrary.
     */

    /**
     * Implementation classes for the Library ADT.
     * JUnit runs this test suite once for each class name in the returned array.
     * @return array of Java class names, including their full package prefix
     */
    @Parameters(name="{0}")
    public static Object[] allImplementationClassNames() {
        return new Object[] { 
            "library.SmallLibrary", 
            "library.BigLibrary"
        }; 
    }

    /**
     * Implementation class being tested on this run of the test suite.
     * JUnit sets this variable automatically as it iterates through the array returned
     * by allImplementationClassNames.
     */
    @Parameter
    public String implementationClassName;    

    private Library library;
    private Book book1, book2;
    private BookCopy copy1, copy2;
    
    @Before
    public void setUp() {
        library = makeLibrary();
        book1 = new Book("Effective Java", Arrays.asList("Joshua Bloch"), 2018);
        book2 = new Book("Clean Code", Arrays.asList("Robert Martin"), 2008);
        copy1 = library.buy(book1);
        copy2 = library.buy(book1);
    }
    
    /**
     * @return a fresh instance of a Library, constructed from the implementation class specified
     * by implementationClassName.
     */
    public Library makeLibrary() {
        try {
            Class<?> cls = Class.forName(implementationClassName);
            return (Library) cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /*
     * Testing strategy
     * ==================
     * 
     * TODO: your testing strategy for this ADT should go here.
     * Make sure you have partitions.
     * 
     * buy():
     *   - first copy of book
     *   - additional copies of same book
     * 
     * checkout/checkin/isAvailable:
     *   - available -> checked out -> available
     *   - check unavailable copy
     * 
     * allCopies/availableCopies:
     *   - empty set for unknown book
     *   - sets with mixed availability
     * 
     * find():
     *   - exact title match
     *   - exact author match
     *   - no matches
     *   - multiple matches ordered by year
     */
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    
    @Test
    public void testBuyAddsCopy() {
        assertEquals(2, library.allCopies(book1).size());
    }
    
    @Test
    public void testAvailableAfterBuy() {
        assertTrue(library.isAvailable(copy1));
    }
    
    @Test
    public void testCheckoutMakesUnavailable() {
        library.checkout(copy1);
        assertFalse(library.isAvailable(copy1));
    }
    
    @Test
    public void testCheckinMakesAvailable() {
        library.checkout(copy1);
        library.checkin(copy1);
        assertTrue(library.isAvailable(copy1));
    }
    
    @Test
    public void testAllCopiesIncludesCheckedOut() {
        library.checkout(copy1);
        Set<BookCopy> all = library.allCopies(book1);
        assertEquals(2, all.size());
        assertTrue(all.contains(copy1));
        assertTrue(all.contains(copy2));
    }
    
    @Test
    public void testAvailableCopiesOnlyAvailable() {
        library.checkout(copy1);
        Set<BookCopy> available = library.availableCopies(book1);
        assertEquals(1, available.size());
        assertTrue(available.contains(copy2));
    }
    
    @Test
    public void testLoseRemovesCopy() {
        library.lose(copy1);
        assertEquals(1, library.allCopies(book1).size());
        assertFalse(library.isAvailable(copy1));
    }
    
    @Test
    public void testFindByTitle() {
        List<Book> results = library.find("Effective Java");
        assertEquals(1, results.size());
        assertEquals(book1, results.get(0));
    }
    
    @Test
    public void testFindByAuthor() {
        List<Book> results = library.find("Joshua Bloch");
        assertEquals(1, results.size());
        assertEquals(book1, results.get(0));
    }
    
    @Test
    public void testOrderByYear() {
        library.buy(book2);
        List<Book> results = library.find("Code");
        assertEquals(2, results.size());
        assertTrue(results.get(0).getYear() >= results.get(1).getYear());
    }
    
    
    
    
    @Test
    public void testExampleTest() {
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals(Collections.emptySet(), library.availableCopies(book));
    }
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
