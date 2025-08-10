package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

	private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "@bbitdiddle what's up?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "@alyssa hey there", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "@charlie thanks", d1);
    private static final Tweet tweet4 = new Tweet(4, "charlie", "just tweeting", d2);
    
    /*
     * TODO: your testing strategies for these methods should go here.
     * Make sure you have partitions.
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
    public void testGuessFollowsGraph() {
        Map<String, Set<String>> followsGraph = 
            SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4));
        
        assertEquals("expected 3 users", 3, followsGraph.size());
        assertTrue("alyssa follows bbitdiddle", followsGraph.get("alyssa").contains("bbitdiddle"));
        assertTrue("alyssa follows charlie", followsGraph.get("alyssa").contains("charlie"));
        assertTrue("bbitdiddle follows alyssa", followsGraph.get("bbitdiddle").contains("alyssa"));
        assertFalse("charlie shouldn't follow anyone", 
            followsGraph.containsKey("charlie") && !followsGraph.get("charlie").isEmpty());
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
