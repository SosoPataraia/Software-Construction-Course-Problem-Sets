package twitter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
    	if (tweets.isEmpty()) {
            throw new IllegalArgumentException("List of tweets cannot be empty");
        }
    	
    	Instant start = tweets.get(0).getTimestamp();
        Instant end = tweets.get(0).getTimestamp();
        
        for (Tweet tweet : tweets) {
            Instant timestamp = tweet.getTimestamp();
            if (timestamp.isBefore(start)) {
                start = timestamp;
            }
            if (timestamp.isAfter(end)) {
                end = timestamp;
            }
        }

        return new Timespan(start, end);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
    	 Set<String> mentionedUsers = new HashSet<>();
         Pattern pattern = Pattern.compile("@([a-zA-Z0-9_-]+)");
         
         for (Tweet tweet : tweets) {
             String text = tweet.getText();
             Matcher matcher = pattern.matcher(text);
             
             while (matcher.find()) {
                 String username = matcher.group(1).toLowerCase();
                 if (isValidMention(matcher, text)) {
                     mentionedUsers.add(username);
                 }
             }
         }
         
         return mentionedUsers;
    }
    
    private static boolean isValidMention(Matcher matcher, String text) {
        int start = matcher.start();
        int end = matcher.end();
        
        // Check character before @
        if (start > 0) {
            char before = text.charAt(start - 1);
            if (Character.isLetterOrDigit(before) || before == '-' || before == '_') {
                return false;
            }
        }
        
        // Check character after username
        if (end < text.length()) {
            char after = text.charAt(end);
            if (Character.isLetterOrDigit(after) || after == '-' || after == '_') {
                return false;
            }
        }
        
        return true;
    }
    

    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
