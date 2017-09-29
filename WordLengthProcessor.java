package ngordnet;
import java.util.Collection;

public class WordLengthProcessor implements YearlyRecordProcessor {
    public double process(YearlyRecord yearlyRecord) {
        Collection<String> words = yearlyRecord.words();
        double totalCounts = 0;
        double totalLength = 0;
        for (String word : words) {
            int count = yearlyRecord.count(word);
            totalCounts += count;
            totalLength += count * word.length();
        }
        return totalLength / totalCounts;
    }
}
