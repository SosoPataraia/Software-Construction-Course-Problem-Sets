package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

	private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "@bbitdiddle #mit #java", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "@alyssa #mit #python", d2);
    private static final Tweet tweet3 = new Tweet(3, "charlie", "#mit #java #python", d1);
    private static final Tweet tweet4 = new Tweet(4, "dave", "just tweeting", d2);
    private static final Tweet tweet5 = new Tweet(5, "eve", "@eve #self", d2); // user mentions self
    private static final Tweet tweet6 = new Tweet(6, "frank", "@nonexistentuser #random #random", d2);
    private static final Tweet tweet7 = new Tweet(7, "greg", "#java #python #mit", d1);
    private static final Tweet tweet8 = new Tweet(8, "hank", "#java #python #mit", d2);
    
    /*
     * Testing strategies:
     * 
     * For guessFollowsGraph:
     * - empty tweet list
     * - tweets with mentions (@username)
     * - tweets with no mentions
     * - mentions are case insensitive
     * - user mentions themselves (should not follow self)
     * - users who only appear as mentioned, not authors
     * - hashtags shared by multiple users to test common hashtag logic (at least 2 shared hashtags)
     * 
     * For influencers:
     * - empty follows graph
     * - users with 0 followers
     * - users with multiple followers
     * - ties in follower counts (ensure alphabetical order)
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphMentionsAndHashtags() {
        Map<String, Set<String>> followsGraph =
            SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet7, tweet8));

        // Users from authors: alyssa, bbitdiddle, charlie, dave, greg, hank
        // Mentions: alyssa->bbitdiddle, bbitdiddle->alyssa
        // dave no mentions
        // hashtags: greg and hank share 3 hashtags, so they follow each other
        assertEquals("expected 6 users", 6, followsGraph.size());

        // Mentions
        assertTrue("alyssa follows bbitdiddle", followsGraph.get("alyssa").contains("bbitdiddle"));
        assertTrue("bbitdiddle follows alyssa", followsGraph.get("bbitdiddle").contains("alyssa"));
        assertFalse("dave follows no one", followsGraph.get("dave").iterator().hasNext());

        // Hashtag commonality followers
        assertTrue("greg follows hank", followsGraph.get("greg").contains("hank"));
        assertTrue("hank follows greg", followsGraph.get("hank").contains("greg"));

        // charlie has hashtags but no mentions
        assertTrue("charlie has no follows", followsGraph.get("charlie").isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphSelfMention() {
        Map<String, Set<String>> followsGraph =
            SocialNetwork.guessFollowsGraph(Arrays.asList(tweet5)); // eve mentions self

        assertEquals("expected 1 user", 1, followsGraph.size());
        assertFalse("eve should not follow herself", followsGraph.get("eve").contains("eve"));
        assertTrue("eve has no follows", followsGraph.get("eve").isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphMentionNonAuthor() {
        Map<String, Set<String>> followsGraph =
            SocialNetwork.guessFollowsGraph(Arrays.asList(tweet6)); // frank mentions nonexistentuser

        // "nonexistentuser" should appear as mentioned user only
        assertTrue("frank follows nonexistentuser", followsGraph.get("frank").contains("nonexistentuser"));
        assertFalse("nonexistentuser should not be a key if not author", followsGraph.containsKey("nonexistentuser"));
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alyssa", new HashSet<>(Arrays.asList("bbitdiddle", "charlie")));
        followsGraph.put("bbitdiddle", new HashSet<>(Arrays.asList("alyssa")));
        followsGraph.put("charlie", new HashSet<>());
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected size", 3, influencers.size());
        assertTrue("alyssa should be first", influencers.get(0).equals("alyssa") || influencers.get(0).equals("bbitdiddle"));
        assertTrue("bbitdiddle should be second", influencers.get(1).equals("alyssa") || influencers.get(1).equals("bbitdiddle"));
        assertEquals("charlie should be last", "charlie", influencers.get(2));
    }
    
    @Test
    public void testInfluencersContinued() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        // users with 2 followers each
        followsGraph.put("user1", new HashSet<>(Arrays.asList("user3")));
        followsGraph.put("user2", new HashSet<>(Arrays.asList("user3")));
        followsGraph.put("user3", new HashSet<>(Arrays.asList()));

        // user3 has 2 followers, user1 and user2 have 0 followers
        List<String> influencers = SocialNetwork.influencers(followsGraph);

        assertEquals("expected size", 3, influencers.size());
        assertEquals("user3 should be first", "user3", influencers.get(0));

        // user1 and user2 have zero followers, sorted alphabetically
        assertEquals("user1 should be second", "user1", influencers.get(1));
        assertEquals("user2 should be third", "user2", influencers.get(2));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
