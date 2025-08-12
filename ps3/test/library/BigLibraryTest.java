package library;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test suite for BigLibrary's stronger specs.
 */
public class BigLibraryTest {
    
    /* 
     * NOTE: use this file only for tests of BigLibrary.find()'s stronger spec.
     * Tests of all other Library operations should be in LibraryTest.java 
     */

    /*
     * Testing strategy
     * ==================
     * 
     * TODO: your testing strategy for BigLibrary.find() should go here.
     * Make sure you have partitions.
     * 
     * Testing strategy for Problem 4:
     * - Basic operations: buy, checkout, checkin
     * - Find by exact title/author matches
     * - Verify copies management
     * - Test empty library cases
     */
	
	private final BigLibrary lib = new BigLibrary();
    private final Book book = new Book("Test Book", Arrays.asList("Author"), 2020);
	
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testExampleTest() {
        // this is just an example test, you should delete it
        Library library = new BigLibrary();
        assertEquals(Collections.emptyList(), library.find("This Test Is Just An Example"));
    }

    
    
    @Test
    public void testBuyAddsAvailableCopy() {
        BookCopy copy = lib.buy(book);
        assertTrue(lib.isAvailable(copy));
    }

    @Test
    public void testCheckoutMakesUnavailable() {
        BookCopy copy = lib.buy(book);
        lib.checkout(copy);
        assertFalse(lib.isAvailable(copy));
    }

    @Test
    public void testFindByExactTitle() {
        lib.buy(book);
        List<Book> results = lib.find("Test Book");
        assertEquals(1, results.size());
        assertEquals(book, results.get(0));
    }

    @Test
    public void testMultipleCopiesManagement() {
        BookCopy copy1 = lib.buy(book);
        BookCopy copy2 = lib.buy(book);
        assertEquals(2, lib.allCopies(book).size());
        
        lib.checkout(copy1);
        assertEquals(1, lib.availableCopies(book).size());
    }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
