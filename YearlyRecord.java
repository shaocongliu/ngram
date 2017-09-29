package ngordnet;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Collection;

public class YearlyRecord {
    
    private TreeMap records;
    private TreeMap wordsToNum;
    private TreeMap<Double, String> numToWords;
    private TreeMap doubleToInt;
    private TreeMap<String, Integer> wordToRank;
    private int size = 0;
    private Boolean sorted = false;

    /** Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        records = new TreeMap();
        wordsToNum = new TreeMap();
        numToWords = new TreeMap<Double, String>();
        doubleToInt = new TreeMap();
        wordToRank = new TreeMap<String, Integer>();
    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        records = new TreeMap();
        wordsToNum = new TreeMap();
        numToWords = new TreeMap<Double, String>();
        doubleToInt = new TreeMap();
        wordToRank = new TreeMap<String, Integer>();
        for (String s : otherCountMap.keySet()) {
            this.put(s, otherCountMap.get(s));
        }
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
        return (int) records.get(word);
    }

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        sorted = false;
        size += 1;
        records.put(word, count);
        double newCount = (double) count + Math.random();
        if (numToWords.containsKey((double) count)) {
            while (numToWords.containsKey(newCount)) {
                newCount = (double) count + Math.random();
            }
            wordsToNum.put(word, newCount);
            numToWords.put(newCount, word);
            doubleToInt.put(newCount, count);
        } else {
            doubleToInt.put((double) count, count);
            wordsToNum.put(word, (double) count);
            numToWords.put((double) count, word);
        }
    }

    /** Returns the number of words recorded this year. */
    public int size() { 
        return records.size();
    }
    
    /** Returns all words in ascending order of count. */
    public Collection<String> words() {
        return numToWords.values();
    }

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        return doubleToInt.values();
    }

    /** Returns rank of WORD. Most common word is rank 1. 
      * If two words have the same rank, break ties arbitrarily. 
      * No two words should have the same rank.
      */
    public int rank(String word) {
        if (!sorted) {
            int rank = size;
            for (Double num : numToWords.keySet()) {
                wordToRank.put(numToWords.get(num), rank);
                rank -= 1;
            }
            sorted = true;
        }
        return wordToRank.get(word);
    }
}
