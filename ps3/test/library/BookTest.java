package library;

import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for Book ADT.
 */
public class BookTest {

    /*
     * Testing strategy
     * ==================
     * 
     * TODO: your testing strategy for this ADT should go here.
     * Make sure you have partitions.
     * 
     * Testing strategy:
     * - Partition by constructor args: valid/invalid title, authors, year
     * - Test equality: same/different books
     * - Test getters return correct values
     */
	
	private final Book book = new Book("Effective Java", 
	        Arrays.asList("Joshua Bloch"), 2018);
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test
    public void testExampleTest() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals("This Test Is Just An Example", book.getTitle());
    }
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    
    @Test
    public void testGettersReturnCorrectValues() {
        assertEquals("Effective Java", book.getTitle());
        assertEquals(Arrays.asList("Joshua Bloch"), book.getAuthors());
        assertEquals(2018, book.getYear());
    }

    @Test
    public void testEqualBooksAreEqual() {
        Book sameBook = new Book("Effective Java", 
            Arrays.asList("Joshua Bloch"), 2018);
        assertEquals(book, sameBook);
        assertEquals(book.hashCode(), sameBook.hashCode());
    }

    @Test
    public void testDifferentBooksNotEqual() {
        Book different = new Book("Clean Code", 
            Arrays.asList("Robert Martin"), 2008);
        assertNotEquals(book, different);
    }

    @Test(expected=AssertionError.class)
    public void testInvalidTitleThrows() {
        new Book("", Arrays.asList("Author"), 2020);
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
