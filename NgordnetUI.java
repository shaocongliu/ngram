package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

public class NgordnetUI {
    public static void main(String[] args) {
        In in = new In("./ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");
        String wordFile = in.readString(); String countFile = in.readString();
        String synsetFile = in.readString(); String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: " + wordFile + ", " 
                           + countFile + ", " + synsetFile + ", and " + hyponymFile + ".");
        System.out.println("\nFor tips on implementing NgordnetUI, see ExampleUI.java.");
        YearlyRecordProcessor yrp = new WordLengthProcessor();
        NGramMap ngm = new NGramMap(wordFile, countFile);
        WordNet wnt = new WordNet(synsetFile, hyponymFile);
        final int start = 1505; final int end = 2008; int startYear = start; int endYear = end;
        while (true) {
            System.out.print("> "); String line = StdIn.readLine();
            String[] rawTokens = line.split(" "); String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1]; 
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            switch (command) {
                case "quit": 
                    return;
                case "help":
                    In helper = new In("help.txt");
                    String helpStr = helper.readAll();
                    break;  
                case "range":
                    try {
                        startYear = Integer.parseInt(tokens[0]); 
                        endYear = Integer.parseInt(tokens[1]);                       
                    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                        System.out.println("Error");
                    }
                    break;
                case "count":
                    try {
                        System.out.println(ngm.countInYear(tokens[0], Integer.parseInt(tokens[1])));
                    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                        System.out.println("Error");
                    }
                    break;
                case "hyponyms":
                    try {
                        System.out.println(wnt.hyponyms(tokens[0]));
                    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                        System.out.println("Error");
                    }
                    break;
                case "history": 
                    try {
                        Plotter.plotAllWords(ngm, tokens, startYear, endYear);
                    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                        System.out.println("Error");
                    }
                    break;
                case "hypohist":
                    try {
                        Plotter.plotCategoryWeights(ngm, wnt, tokens, startYear, endYear);
                    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                        System.out.println("Error");
                    }
                    break;
                case "wordlength":
                    try {
                        Plotter.plotProcessedHistory(ngm, startYear, endYear, yrp);
                    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                        System.out.println("Error");
                    }
                    break;
                case "zipf":
                    try {
                        Plotter.plotZipfsLaw(ngm, Integer.parseInt(tokens[0]));
                    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                        System.out.println("Error");
                    }
                    break;
                default:
                    System.out.println("Invalid command.");  
                    break;
            }
        }
    }
}
