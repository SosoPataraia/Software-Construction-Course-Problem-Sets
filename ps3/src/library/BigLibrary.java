package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * BigLibrary represents a large collection of books that might be held by a city or
 * university library system -- millions of books.
 * 
 * In particular, every operation needs to run faster than linear time (as a function of the number of books
 * in the library).
 */
public class BigLibrary implements Library {
	// Rep:
    //   bookRecords: maps each Book to a BookRecord containing:
    //     - allCopies: all copies of this book in the library
    //     - availableCopies: currently available copies
    //   exactMatchIndex: maps exact strings (title/author) to matching Books
    //   wordIndex: maps individual words to matching Books for full-text search
	private final Map<Book, BookRecord> bookRecords = new HashMap<>();
    private final Map<String, Set<Book>> exactMatchIndex = new HashMap<>();
    
    private final Map<String, Set<Book>> wordIndex = new HashMap<>();

    
    private static class BookRecord {
        final Set<BookCopy> allCopies = new HashSet<>();
        final Set<BookCopy> availableCopies = new HashSet<>();
    }

    // TODO: rep
    
    // TODO: rep invariant
    // TODO: abstraction function
    // TODO: safety from rep exposure argument
    // Rep invariant:
    //   - bookRecords, exactMatchIndex, wordIndex are non-null
    //   - For each BookRecord:
    //     * allCopies and availableCopies are non-null
    //     * availableCopies is a subset of allCopies
    //     * allCopies is non-empty while book is in bookRecords
    //   - For each string in exactMatchIndex and wordIndex:
    //     * The mapped Set<Book> is non-null and contains no nulls
    //     * All books in the sets exist as keys in bookRecords
    //   - For each Book in bookRecords:
    //     * It appears under its title and all authors in exactMatchIndex
    //     * It appears under all words from title and authors in wordIndex
    //
    // Abstraction function:
    //   AF(bookRecords, exactMatchIndex, wordIndex) = 
    //     a library containing all book copies in ∪(r.allCopies for r in bookRecords.values()),
    //     where copies in ∪(r.availableCopies) are available and others are checked out,
    //     with efficient lookup capabilities for exact matches and word-based search
    //
    // Safety from rep exposure:
    //   - All fields are private and final
    //   - All mutable collections are never returned directly to clients
    //   - Defensive copies or unmodifiable wrappers are used when returning collections
    //   - Book is immutable, BookCopy references are protected by library control
    
    public BigLibrary() {
        checkRep();
    }
    
    // assert the rep invariant
    private void checkRep() {
    	for (Book book : bookRecords.keySet()) {
            BookRecord record = bookRecords.get(book);
            assert !record.allCopies.isEmpty() : "Book has no copies";
            assert record.availableCopies.size() <= record.allCopies.size();
            for (BookCopy copy : record.availableCopies) {
                assert record.allCopies.contains(copy) : "Available copy not in all copies";
            }
        }
    }

    @Override
    public BookCopy buy(Book book) {
        BookRecord record = bookRecords.computeIfAbsent(book, k -> {
            indexBook(book);
            return new BookRecord();
        });
        
        BookCopy copy = new BookCopy(book);
        record.allCopies.add(copy);
        record.availableCopies.add(copy);
        checkRep();
        return copy;
    }
    
    @Override
    public List<Book> find(String query) {
        List<String> tokens = tokenizeQuery(query);
        Map<Book, Integer> scores = new HashMap<>();
        
        for (String token : tokens) {
            if (token.contains(" ")) {
                // Phrase match
                Set<Book> candidateBooks = getBooksForPhrase(token);
                for (Book book : candidateBooks) {
                    scores.put(book, scores.getOrDefault(book, 0) + 3);
                }
            } else {
                // Word match
                Set<Book> books = wordIndex.getOrDefault(token, Set.of());
                for (Book book : books) {
                    scores.put(book, scores.getOrDefault(book, 0) + 1);
                }
            }
        }
        
        List<Book> result = new ArrayList<>(scores.keySet());
        result.sort((b1, b2) -> {
            int scoreCompare = scores.get(b2).compareTo(scores.get(b1));
            return scoreCompare != 0 ? scoreCompare : 
                   Integer.compare(b2.getYear(), b1.getYear());
        });
        return result;
    }

    private List<String> tokenizeQuery(String query) {
        List<String> tokens = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder token = new StringBuilder();
        
        for (char c : query.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (Character.isWhitespace(c) && !inQuotes) {
                if (token.length() > 0) {
                    tokens.add(token.toString().toLowerCase());
                    token.setLength(0);
                }
            } else {
                token.append(c);
            }
        }
        if (token.length() > 0) {
            tokens.add(token.toString().toLowerCase());
        }
        return tokens;
    }

    private Set<Book> getBooksForPhrase(String phrase) {
        String[] words = phrase.split("\\s+");
        if (words.length == 0) return Set.of();
        
        Set<Book> candidateBooks = new HashSet<>(
            wordIndex.getOrDefault(words[0].toLowerCase(), Set.of()));
        
        for (int i = 1; i < words.length; i++) {
            Set<Book> nextBooks = wordIndex.getOrDefault(words[i].toLowerCase(), Set.of());
            candidateBooks.retainAll(nextBooks);
        }
        
        Set<Book> results = new HashSet<>();
        for (Book book : candidateBooks) {
            String lcTitle = book.getTitle().toLowerCase();
            if (lcTitle.contains(phrase)) {
                results.add(book);
                continue;
            }
            for (String author : book.getAuthors()) {
                if (author.toLowerCase().contains(phrase)) {
                    results.add(book);
                    break;
                }
            }
        }
        return results;
    }

    // Update indexBook method to include word indexing
    private void indexBook(Book book) {
        // Existing exact match indexing
        indexString(book.getTitle(), book);
        for (String author : book.getAuthors()) {
            indexString(author, book);
        }
        
        // New word indexing
        indexWords(book.getTitle(), book);
        for (String author : book.getAuthors()) {
            indexWords(author, book);
        }
    }

    private void indexWords(String text, Book book) {
        for (String word : text.split("\\W+")) {
            if (!word.isEmpty()) {
                String lcWord = word.toLowerCase();
                wordIndex.computeIfAbsent(lcWord, k -> new HashSet<>()).add(book);
            }
        }
    }

    // Update unindexBook to remove words
    private void unindexBook(Book book) {
        // Existing exact match removal
        unindexString(book.getTitle(), book);
        for (String author : book.getAuthors()) {
            unindexString(author, book);
        }
        
        // New word index removal
        for (Set<Book> bookSet : wordIndex.values()) {
            bookSet.remove(book);
        }
    }
    
    private void indexString(String text, Book book) {
        exactMatchIndex.computeIfAbsent(text, k -> new HashSet<>()).add(book);
    }
    
    @Override
    public void checkout(BookCopy copy) {
        Book book = copy.getBook();
        BookRecord record = bookRecords.get(book);
        if (record != null && record.availableCopies.remove(copy)) {
            checkRep();
        }
    }
    
    @Override
    public void checkin(BookCopy copy) {
        Book book = copy.getBook();
        BookRecord record = bookRecords.get(book);
        if (record != null) {
            record.availableCopies.add(copy);
            checkRep();
        }
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        BookRecord record = bookRecords.get(copy.getBook());
        return record != null && record.availableCopies.contains(copy);
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        BookRecord record = bookRecords.get(book);
        return record == null ? Set.of() : Collections.unmodifiableSet(record.allCopies);
    }
    
    @Override
    public Set<BookCopy> availableCopies(Book book) {
        BookRecord record = bookRecords.get(book);
        return record == null ? Set.of() : Collections.unmodifiableSet(record.availableCopies);
    }

    
    
    @Override
    public void lose(BookCopy copy) {
        Book book = copy.getBook();
        BookRecord record = bookRecords.get(book);
        if (record != null) {
            record.allCopies.remove(copy);
            record.availableCopies.remove(copy);
            if (record.allCopies.isEmpty()) {
                bookRecords.remove(book);
                unindexBook(book);
            }
            checkRep();
        }
    }
    
    
    
    private void unindexString(String text, Book book) {
        Set<Book> books = exactMatchIndex.get(text);
        if (books != null) {
            books.remove(book);
            if (books.isEmpty()) {
                exactMatchIndex.remove(text);
            }
        }
    }

    // uncomment the following methods if you need to implement equals and hashCode,
    // or delete them if you don't
    @Override
    public boolean equals(Object that) {
    	if (this == that) return true;
        if (!(that instanceof BigLibrary)) return false;
        BigLibrary other = (BigLibrary) that;
        return this.bookRecords.equals(other.bookRecords);
    }
    
    @Override
    public int hashCode() {
    	return bookRecords.hashCode();
    }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
