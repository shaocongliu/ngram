package ngordnet;
import java.util.ArrayList;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.ChartBuilder;
import java.util.Collection;
import java.util.Set;

public class Plotter {
    /** Creates a plot of the TimeSeries TS. Labels the graph with the
      * given TITLE, XLABEL, YLABEL, and LEGEND. */
    public static void plotTS(TimeSeries<? extends Number> ts, String title, 
                              String xlabel, String ylabel, String legend) {
        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();
        Collection<Number> tsx = ts.years();
        Collection<Number> tsy = ts.data();
        for (Number n : tsx) {
            xValues.add(n);
        }
        for (Number n : tsy) {
            yValues.add(n);
        }
        Chart chart = QuickChart.getChart(title, xlabel, ylabel, legend, xValues, yValues);
        new SwingWrapper(chart).displayChart();        
    }

    /** Creates a plot of the absolute word counts for WORD from STARTYEAR
      * to ENDYEAR, using NGM as a data source. */
    public static void plotCountHistory(NGramMap ngm, String word, 
                                      int startYear, int endYear) {
        String title = "word's CountHistory";
        String xlabel = "Year";
        String ylabel = "count";
        String legend = word;
        TimeSeries<Integer> countHistory = ngm.countHistory(word, startYear, endYear);
        plotTS(countHistory, title, xlabel, ylabel, legend);
    }

    /** Creates a plot of the normalized weight counts for WORD from STARTYEAR
      * to ENDYEAR, using NGM as a data source. */
    public static void plotWeightHistory(NGramMap ngm, String word, 
                                       int startYear, int endYear) {
        String title = "word's WeightHistory";
        String xlabel = "Year";
        String ylabel = "weightCount";
        String legend = word;
        TimeSeries<Double> weightHistory = ngm.weightHistory(word, startYear, endYear);
        plotTS(weightHistory, title, xlabel, ylabel, legend);
    }

    /** Creates a plot of the processed history from STARTYEAR to ENDYEAR, using
      * NGM as a data source, and the YRP as a yearly record processor. */
    public static void plotProcessedHistory(NGramMap ngm, int startYear, int endYear,
                                            YearlyRecordProcessor yrp) {
        TimeSeries<Double> timeseries = 
                 new TimeSeries(ngm.processedHistory(yrp), startYear, endYear);
        plotTS(timeseries, "Word History", "Year", "Average", "Surprise");
    }

    /** Creates a plot of the total normalized count of every word that is a hyponym
      * of CATEGORYLABEL from STARTYEAR to ENDYEAR using NGM and WN as data sources. */
    public static void plotCategoryWeights(NGramMap ngm, 
                                           WordNet wn, String categoryLabel,
                                            int startYear, int endYear) {
        Set<String> allWords = wn.hyponyms(categoryLabel);
        String title = "Label's category";
        String xlabel = "Year";
        String ylabel = "normalized count";
        String legend = categoryLabel;
        TimeSeries<Double> totalWeightHistory = 
                     ngm.summedWeightHistory(allWords, startYear, endYear);
        plotTS(totalWeightHistory, title, xlabel, ylabel, legend); 
    }

    /** Creates overlaid category weight plots for each category label in CATEGORYLABELS
      * from STARTYEAR to ENDYEAR using NGM and WN as data sources. */
    public static void plotCategoryWeights(NGramMap ngm, 
                                           WordNet wn, String[] categoryLabels,
                                            int startYear, int endYear) {
        Set<String> allWords;
        ArrayList<Number> xValues;
        ArrayList<Number> yValues;
        TimeSeries<Double> totalWeightHistory;

        String title = "Labels' weightscategory";
        String xlabel = "Year";
        String ylabel = "normalized count";
        String legend;
        Chart chart = 
            new ChartBuilder().width(800).height(600).xAxisTitle(xlabel).yAxisTitle(ylabel).build();
        for (int i = 0; i < categoryLabels.length; i++) {
            legend = categoryLabels[i];
            xValues = new ArrayList<Number>();
            yValues = new ArrayList<Number>();
            allWords = wn.hyponyms(categoryLabels[i]);
            totalWeightHistory = ngm.summedWeightHistory(allWords, startYear, endYear);
            for (Integer year : totalWeightHistory.keySet()) {
                xValues.add(year);
                yValues.add(totalWeightHistory.get(year));
            }
            chart.addSeries(legend, xValues, yValues);
        }
        new SwingWrapper(chart).displayChart();  
    }

    /** Makes a plot showing overlaid individual normalized count for every word in WORDS
      * from STARTYEAR to ENDYEAR using NGM as a data source. */
    public static void plotAllWords(NGramMap ngm, 
                                     String[] words, int startYear, int endYear) {
        ArrayList<Number> xValues;
        ArrayList<Number> yValues;
        TimeSeries<Double> weightHistory;

        String title = "count of each word";
        String xlabel = "Year";
        String ylabel = "normalized count";
        String legend;
        Chart chart = new ChartBuilder().width(800).height(600).xAxisTitle(xlabel).yAxisTitle(ylabel).build();
        for (int i = 0; i < words.length; i++) {
            legend = words[i];
            xValues = new ArrayList<Number>();
            yValues = new ArrayList<Number>();
            weightHistory = ngm.weightHistory(words[i], startYear, endYear);
            for (Integer year : weightHistory.keySet()) {
                xValues.add(year);
                yValues.add(weightHistory.get(year));
            }
            chart.addSeries(legend, xValues, yValues);
        }
        new SwingWrapper(chart).displayChart();  
    }

    /** Returns the numbers from max to 1, inclusive in decreasing order. 
      * Private, so you don't have to implement if you don't want to. */
    // private static Collection<Number> downRange(int max) {

    // }

    /** Plots the normalized count of every word against the rank of every word on a
      * log-log plot. Uses data from YEAR, using NGM as a data source. */
    public static void plotZipfsLaw(NGramMap ngm, int year) {
        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        YearlyRecord records = ngm.getRecord(year);
        Collection<String> allwords = records.words();
        for (String word : allwords) {
            xValues.add(records.count(word));
            yValues.add(records.rank(word));
        }

        String title = "count vs. rank";
        String ylabel = "count";
        String xlabel = "rank";
        String legend = "zipf's Law";
        Chart chart = new ChartBuilder().width(800).height(600).xAxisTitle(xlabel).yAxisTitle(ylabel).build();
        chart.getStyleManager().setYAxisLogarithmic(true);
        chart.getStyleManager().setXAxisLogarithmic(true);
        chart.addSeries(legend, xValues, yValues);

        new SwingWrapper(chart).displayChart();
    }
} 
