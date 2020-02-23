import java.io.IOException;
import java.util.*;

import static java.lang.System.out;


public class Main {


    public static void main(String[] args) throws IOException {

        String[] url ={"https://sites.google.com/view/siirh2020/",
                "http://www.multimediaeval.org/mediaeval2019/memorability/"};

        List<TFIDF_Basket> baskets = new ArrayList<>();

        for (String URL:url) { // load all the raw data from the URLs supplied, tokenise and stem the words
            TFIDF_Basket i = new TFIDF_Basket(URL);
            loadWordFromDocumentTokenizeAndStemm(i);
            baskets.add(i);
        }

        TFIDF tfidf = new TFIDF();
        tfidf.calculateTFIDF(baskets); //calculate TFIDF score for all the words


        for (TFIDF_Basket b : baskets){
            System.out.println(" ");
            b.printWords(); // print the words in the 'basket'
            System.out.println(" ");
        }


    }

    static void loadWordFromDocumentTokenizeAndStemm(TFIDF_Basket basket) throws IOException{
        URLparser urlParser = new URLparser();
        DataTokenizer tkn = new DataTokenizer();
        PorterStemmer pStem = new PorterStemmer();

        //@testPrint
        System.out.println("\nFrom url: " +basket.getUrl() + "\n");

        basket.addRawData(urlParser.getDocumentData(basket.getUrl())); // add the raw data into the basket

        for (URLparser.Values val:URLparser.Values.values()) {
            if(basket.getRawDara(val) != null  ){ // dont want to do this if the doc doesnt have one of the enums found in urlParser
                //@testPrint
                System.out.println("\nData split into sentences from " + val+ ": \n");

                List<String> listOfSentences = tkn.splitIntoSentences(basket.getRawDara(val)); // splits the doc into sentences

                //@testPrint
                for (String s: listOfSentences){
                    System.out.println(s);
                }

                //@testPrint
                System.out.println("\nSentences split into words from " + val+ " and tagged: \n");
                List<List<Word>> listOfWords = tkn.splitIntoWords(val, listOfSentences); //splits the sentences into words 'List<sentences>'

                //@testPrint
                System.out.println("\nTagged words stemmed using Porter stemmer: \n");
                for (List<Word> words:listOfWords){
                    for(Word w: words){
                        pStem.stemWord(w); //stem each word individualy
                        basket.addSpecificProcessedData(val,w);
                    }
                }
            }
        }
        basket.sumOfAllWords(); // take a sum of all the words. used in TFIDF calculation
    }
}
