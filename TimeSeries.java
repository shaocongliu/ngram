package ngordnet;
import java.util.Set;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Collection;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {

    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
    }

    /** Returns the years in which this time series is valid. Doesn't really
      * need to be a NavigableSet. This is a private method and you don't have 
      * to implement it if you don't want to. */
    // private NavigableSet<Integer> validYears(int startYear, int endYear) {
    // }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR. 
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        for (Integer year : ts.keySet()) {
            if ((startYear <= year) && (endYear >= year)) {
                this.put(year, ts.get(year));
            }
        }
    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        this.putAll(ts);
    }

    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> newSeries = new TimeSeries<Double>();
        for (Integer i : this.keySet()) {
            newSeries.put(i, this.get(i).doubleValue());
            if (!ts.containsKey(i)) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer j : ts.keySet()) {
            if (newSeries.containsKey(j)) {
                double newVal = newSeries.get(j) / ts.get(j).doubleValue();
                newSeries.put(j, newVal);
            }
        }
        return newSeries;
    }

    /** Returns the sum of this time series with the given ts. The result is a 
      * a Double time series (for simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> newSeries = new TimeSeries<Double>();
        for (Integer i : this.keySet()) {
            newSeries.put(i, this.get(i).doubleValue());
        }
        for (Integer j : ts.keySet()) {
            if (newSeries.containsKey(j)) {
                double newVal = ts.get(j).doubleValue() + newSeries.get(j);
                newSeries.put(j, newVal);
            } else {
                newSeries.put(j, ts.get(j).doubleValue());
            }
        }
        return newSeries;
    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        Collection<Number> collection = new TreeSet<Number>();
        Set<Integer> yearSets = this.keySet();
        Integer[] years = yearSets.toArray(new Integer[2]);
        for (int i = 0; i < years.length; i++) {
            collection.add(years[i]);
        }
        return collection;
    }

    /** Returns all data for this time series. 
      * Must be in the same order as years(). */
    public Collection<Number> data() {
        return (Collection<Number>) this.values();
    }
}
