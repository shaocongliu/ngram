package ngordnet;
import java.util.HashMap;
import java.util.Collection;
import edu.princeton.cs.introcs.In;

public class NGramMap {

    private HashMap<Integer, HashMap> yrToWrdsCt;
    private HashMap<Integer, Long> yrToTotal;

    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        yrToWrdsCt = new HashMap<Integer, HashMap>();
        yrToTotal = new HashMap<Integer, Long>();
        In wordsFile = new In(wordsFilename);
        In countsFile = new In(countsFilename);
        while (wordsFile.hasNextLine()) {
            String thisLine = wordsFile.readLine();
            String[] splitLine = thisLine.split("\\t");
            if (yrToWrdsCt.containsKey(Integer.parseInt(splitLine[1]))) {
                HashMap curVal2 = yrToWrdsCt.get(Integer.parseInt(splitLine[1]));
                curVal2.put(splitLine[0], Integer.parseInt(splitLine[2]));
                yrToWrdsCt.put(Integer.parseInt(splitLine[1]), curVal2);
            } else {
                HashMap valMap2 = new HashMap();
                valMap2.put(splitLine[0], Integer.parseInt(splitLine[2]));
                yrToWrdsCt.put(Integer.parseInt(splitLine[1]), valMap2);
            }
        }
        while (countsFile.hasNextLine()) {
            String thisLine2 = countsFile.readLine();
            String[] splitLine2 = thisLine2.split(",");
            Integer key = Integer.parseInt(splitLine2[0]);
            Long val = Long.parseLong(splitLine2[1]);
            yrToTotal.put(key, val);
        }    
    }
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {
        HashMap val = (HashMap) yrToWrdsCt.get(year);
        if (val.containsKey(word)) {
            int count = (int) val.get(word);
            return count;
        } else {
            return 0;
        }
    }

    // /** Returns a defensive copy of the YearlyRecord of WORD. */
    public YearlyRecord getRecord(int year) {
        HashMap val = (HashMap) yrToWrdsCt.get(year);
        return new YearlyRecord(val);
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        TimeSeries timeseries = new TimeSeries();
        for (Integer year : yrToTotal.keySet()) {
            timeseries.put(year, yrToTotal.get(year));
        }
        return timeseries;
    }

    // /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> ts = countHistory(word);
        return new TimeSeries<Integer>(ts, startYear, endYear);
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        TimeSeries timeseries = new TimeSeries();
        for (Integer year : yrToWrdsCt.keySet()) {
            HashMap valMap = (HashMap) yrToWrdsCt.get(year);
            if (valMap.containsKey(word)) {
                timeseries.put(year, valMap.get(word));
            }
        }
        return timeseries;
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        TimeSeries<Double> ts = weightHistory(word);
        return new TimeSeries<Double>(ts, startYear, endYear);
    }

    // * Provides the relative frequency of WORD. 
    public TimeSeries<Double> weightHistory(String word) {
        TimeSeries<Integer> counthistory = countHistory(word);
        TimeSeries<Long> totalcounthistory = totalCountHistory();
        TimeSeries<Double> timeseries = counthistory.dividedBy(totalcounthistory);
        return timeseries;
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
                              int startYear, int endYear) {
        TimeSeries<Double> ts = summedWeightHistory(words);
        return new TimeSeries<Double>(ts, startYear, endYear); 
    }

    // /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        TimeSeries<Double> timeseries = new TimeSeries<Double>();
        for (String word : words) {
            TimeSeries<Double> weightOfWord = weightHistory(word);
            for (Integer year : weightOfWord.keySet()) {
                if (timeseries.containsKey(year)) {
                    Double newVal = timeseries.get(year) + weightOfWord.get(year);
                    timeseries.put(year, newVal);
                } else {
                    timeseries.put(year, weightOfWord.get(year));
                }

            }
        }
        return timeseries;
    }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> timeseries = new TimeSeries<Double>();
        for (Integer year : yrToWrdsCt.keySet()) {
            YearlyRecord yearlyrecord = this.getRecord(year);
            double record = yrp.process(yearlyrecord);
            timeseries.put(year, record);
        }
        return timeseries;
    }
}
