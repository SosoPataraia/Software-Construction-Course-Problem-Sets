package library;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 
 * SmallLibrary represents a small collection of books, like a single person's home collection.
 */
public class SmallLibrary implements Library {

    // This rep is required! 
    // Do not change the types of inLibrary or checkedOut, 
    // and don't add or remove any other fields.
    // (BigLibrary is where you can create your own rep for
    // a Library implementation.)

    // rep
    private Set<BookCopy> inLibrary;
    private Set<BookCopy> checkedOut;
    
    // rep invariant:
    //    the intersection of inLibrary and checkedOut is the empty set
    //
    // abstraction function:
    //    represents the collection of books inLibrary union checkedOut,
    //      where if a book copy is in inLibrary then it is available,
    //      and if a copy is in checkedOut then it is checked out

    // TODO: safety from rep exposure argument
    
    // Safety from rep exposure:
    //   Sets are private and defensively copied in methods
    //   BookCopy is mutable but we control modifications
    
    public SmallLibrary() {
    	inLibrary = new HashSet<>();
        checkedOut = new HashSet<>();
    }
    
    // assert the rep invariant
    private void checkRep() {
    	// Disjoint sets
        for (BookCopy copy : inLibrary) {
            assert !checkedOut.contains(copy);
        }
    }

    @Override
    public BookCopy buy(Book book) {
        BookCopy copy = new BookCopy(book);
        inLibrary.add(copy);
        checkRep();
        return copy;
    }
    
    @Override
    public void checkout(BookCopy copy) {
        if (inLibrary.remove(copy)) {
            checkedOut.add(copy);
        }
        checkRep();
    }
    
    @Override
    public void checkin(BookCopy copy) {
        if (checkedOut.remove(copy)) {
            inLibrary.add(copy);
        }
        checkRep();
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        return inLibrary.contains(copy);
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        Set<BookCopy> copies = new HashSet<>();
        for (BookCopy copy : inLibrary) {
            if (copy.getBook().equals(book)) copies.add(copy);
        }
        for (BookCopy copy : checkedOut) {
            if (copy.getBook().equals(book)) copies.add(copy);
        }
        return copies;
    }
    
    @Override
    public Set<BookCopy> availableCopies(Book book) {
        Set<BookCopy> copies = new HashSet<>();
        for (BookCopy copy : inLibrary) {
            if (copy.getBook().equals(book)) copies.add(copy);
        }
        return copies;
    }

    @Override
    public List<Book> find(String query) {
        Set<Book> books = new HashSet<>();
        for (BookCopy copy : inLibrary) {
            if (copy.getBook().getTitle().contains(query)) {
                books.add(copy.getBook());
            }
        }
        for (BookCopy copy : checkedOut) {
            if (copy.getBook().getTitle().contains(query)) {
                books.add(copy.getBook());
            }
        }
        List<Book> result = new ArrayList<>(books);
        result.sort(Comparator.comparingInt(Book::getYear).reversed());
        return result;
    }
    
    @Override
    public void lose(BookCopy copy) {
        inLibrary.remove(copy);
        checkedOut.remove(copy);
        checkRep();
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
