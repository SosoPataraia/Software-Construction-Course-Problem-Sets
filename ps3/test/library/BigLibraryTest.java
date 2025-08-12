package library;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
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
     *
     * Phrase matching:
     *   - quoted phrases in title
     *   - quoted phrases in author
     *   - multiple quoted phrases
     * 
     * Keyword matching:
     *   - single keyword
     *   - multiple keywords
     *   - partial word matches
     * 
     * Ranking:
     *   - phrase matches ranked higher than keyword
     *   - more matches ranked higher
     *   - newer books before older when scores tie
     *   - popular books before less popular when year ties
     * 
     * Edge cases:
     *   - mixed case queries
     *   - punctuation in queries
     *   - empty queries
     */
	
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    private BigLibrary lib2;
    private Book knuthBook, gofBook, javaBook;
    
    @Before
    public void setUp() {
        lib2 = new BigLibrary();
        knuthBook = new Book("The Art of Computer Programming", 
                          Arrays.asList("Donald Knuth"), 1968);
        gofBook = new Book("Design Patterns: Elements of Reusable Object-Oriented Software",
                         Arrays.asList("Erich Gamma", "Richard Helm"), 1994);
        javaBook = new Book("Effective Java", Arrays.asList("Joshua Bloch"), 2018);
        
        lib2.buy(knuthBook);
        lib2.buy(gofBook);
        lib2.buy(javaBook);
        
        // Make javaBook popular by checking out multiple times
        BookCopy copy = lib2.buy(javaBook);
        for (int i = 0; i < 5; i++) {
            lib2.checkout(copy);
            lib2.checkin(copy);
        }
    }
    
    @Test
    public void testImprovedFind_PhraseInTitle() {
        List<Book> results = lib2.find("\"Design Patterns\"");
        assertEquals("should find phrase match", 1, results.size());
        assertEquals(gofBook, results.get(0));
    }
    
    @Test
    public void testImprovedFind_RankingPriorities() {
        // Add another book that matches two keywords
        Book javaPatterns = new Book("Java Design Patterns", 
                                   Arrays.asList("James Smith"), 2010);
        lib2.buy(javaPatterns);
        
        List<Book> results = lib2.find("Java Patterns");
        // Order should be:
        // 1. javaPatterns (matches 2 keywords)
        // 2. javaBook (matches 1 keyword but newer)
        // 3. gofBook (matches 1 keyword)
        assertEquals(3, results.size());
        assertEquals(javaPatterns, results.get(0));
        assertEquals(javaBook, results.get(1));
        assertEquals(gofBook, results.get(2));
    }

    @Test
    public void testImprovedFind_PopularityTiebreaker() {
        Book javaBook2 = new Book("Java Concurrency", 
                               Arrays.asList("Brian Goetz"), 2018);
        lib2.buy(javaBook2);
        
        List<Book> results = lib2.find("Java");
        // Both javaBook and javaBook2 from 2018, but javaBook is more popular
        assertTrue(results.indexOf(javaBook) < results.indexOf(javaBook2));
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
