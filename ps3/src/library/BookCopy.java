package library;

/**
 * BookCopy is a mutable type representing a particular copy of a book that is held in a library's
 * collection.
 */
public class BookCopy {

    // TODO: rep
    
    // TODO: rep invariant
    // TODO: abstraction function
    // TODO: safety from rep exposure argument
    
    public static enum Condition {
        GOOD, DAMAGED
    };
    
    private final Book book;
    private Condition condition;
    
    // Rep invariant:
    //   book != null, condition != null
    // Abstraction function:
    //   Represents a physical copy of a book in the library
    // Safety from rep exposure:
    //   Book is immutable, condition is enum (immutable)
    //   No references to mutable internal state are exposed
    
    /**
     * Make a new BookCopy, initially in good condition.
     * @param book the Book of which this is a copy
     */
    public BookCopy(Book book) {
    	this.book = book;
        this.condition = Condition.GOOD;
        checkRep();
    }
    
    // assert the rep invariant
    private void checkRep() {
    	assert book != null : "Book cannot be null";
        assert condition != null : "Condition cannot be null";
    }
    
    /**
     * @return the Book of which this is a copy
     */
    public Book getBook() {
        return book;
    }
    
    /**
     * @return the condition of this book copy
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Set the condition of a book copy.  This typically happens when a book copy is returned and a librarian inspects it.
     * @param condition the latest condition of the book copy
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
        checkRep();
    }
    
    /**
     * @return human-readable representation of this book that includes book.toString()
     *    and the words "good" or "damaged" depending on its condition
     */
    public String toString() {
    	return book.toString() + " [" + condition + "]";
    }

    // uncomment the following methods if you need to implement equals and hashCode,
    // or delete them if you don't
    // @Override
    // public boolean equals(Object that) {
    //     throw new RuntimeException("not implemented yet");
    // }
    // 
    // @Override
    // public int hashCode() {
    //     throw new RuntimeException("not implemented yet");
    // }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
