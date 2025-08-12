package library;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Book is an immutable type representing an edition of a book -- not the physical object, 
 * but the combination of words and pictures that make up a book.  Each book is uniquely
 * identified by its title, author list, and publication year.  Alphabetic case and author 
 * order are significant, so a book written by "Fred" is different than a book written by "FRED".
 */
public class Book {

	private final String title;
    private final List<String> authors;
    private final int year;

	// TODO: rep
    
    // TODO: rep invariant
    // TODO: abstraction function
    // TODO: safety from rep exposure argument
	
	// Rep invariant:
    //   title != null, contains at least one non-space character
    //   authors != null, has at least one author, each author has at least one non-space character
    //   year >= 0
    // Abstraction function:
    //   Represents a published book with given title, author list, and publication year
    // Safety from rep exposure:
    //   All fields are private and final
    //   Mutable authors list is defensively copied in constructor and getter
    
    /**
     * Make a Book.
     * @param title Title of the book. Must contain at least one non-space character.
     * @param authors Names of the authors of the book.  Must have at least one name, and each name must contain 
     * at least one non-space character.
     * @param year Year when this edition was published in the conventional (Common Era) calendar.  Must be nonnegative. 
     */
    public Book(String title, List<String> authors, int year) {
    	this.title = title.trim();
        this.authors = List.copyOf(authors);
        this.year = year;
        checkRep();    
    }
    
    // assert the rep invariant
    private void checkRep() {
    	assert title != null && !title.isBlank() : "Title must contain non-space character";
        assert authors != null && !authors.isEmpty() : "Must have at least one author";
        for (String author : authors) {
            assert author != null && !author.isBlank() : "Author names must be non-blank";
        }
        assert year >= 0 : "Publication year must be non-negative";
    }
    
    /**
     * @return the title of this book
     */
    public String getTitle() {
    	return title;
    }
    
    /**
     * @return the authors of this book
     */
    public List<String> getAuthors() {
    	return Collections.unmodifiableList(authors);
    }

    /**
     * @return the year that this book was published
     */
    public int getYear() {
    	return year;
    }

    /**
     * @return human-readable representation of this book that includes its title,
     *    authors, and publication year
     */
    public String toString() {
    	return String.format("\"%s\" by %s (%d)", title, String.join(", ", authors), year);
    }

    // uncomment the following methods if you need to implement equals and hashCode,
    // or delete them if you don't
    @Override
    public boolean equals(Object that) {
    	if (this == that) return true;
        if (!(that instanceof Book)) return false;
        Book other = (Book) that;
        return year == other.year &&
               title.equals(other.title) &&
               authors.equals(other.authors);
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(title, authors, year);
    }



    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
