import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

public class TFIDF {
    TFIDF() {
    }

    public double calculateTF(TFIDF_Basket b, Word wordToSearchFor) {
        double result = 0;
        double addWeight;

        for (URLparser.Values val:URLparser.Values.values()) { //for each tag
            if(b.getProcessedData(val) !=null){
                List<Word> doc = b.getProcessedData(val);
                for (Word word : doc) {
                    if (word.getWord().equals(wordToSearchFor.getWord())) {
                        if (word.getTag().equals(wordToSearchFor.getTag())) { // if exactly the same word
                            addWeight = 1;
                        } else { //if its a stemmed word
                            addWeight = 0.5;
                        }
                        result += addWeight;
                    }
                }
            }

        }
        return result / b.getSumOfAllWrds();
    }
    private void adjustWeight ( Word w){ //adjust tfidf score based on what tag the word came from
        URLparser.Values val = w.getWhereFrom();
        Double rtn;
        switch (val){
            case META:rtn = 0.02; break; //most important
            case H1:rtn = 0.016; break;
            case H2:rtn = 0.015; break;
            case LI:rtn = 0.013; break;
            case TD:rtn = 0.013; break;
            default:rtn = 0.0; break;
        }

        if(val != URLparser.Values.WORDS){
            double i = w.getTfidf();
            if(w.getTfidf() < 0.0000001){
                w.setTfidf(i+rtn);
            }else{
                w.setTfidf(i*(rtn*100));
            }
        }


    }
    public double calculateIDF(List<TFIDF_Basket> baskets,Word wordToSearchFor ) {
        double result = 0;
        for (TFIDF_Basket b : baskets) { //for each url
            for (URLparser.Values val:URLparser.Values.values()) { //for each tag
                boolean brk = false;
                if(b.getProcessedData(val) !=null){
                    List<Word> data = b.getProcessedData(val);
                    for (Word word :data) {
                        if (wordToSearchFor.getWord().equals(word.getWord())) { //break out of that doc and move onto next. this is cos we are looking
                            result++;                                           // how many times the word appears in the doc, doent matter what tag it came from.
                            brk = true;
                            break;
                        }
                    }
                }
                if (brk){ //break out of the tag loop
                    break;
                }
            }

        }
        return Math.log10(baskets.size() / result);
    }

    public void calculateTFIDF(List<TFIDF_Basket> baskets){ // calculated the TFIDF of each word for all the whole URLs
        for(TFIDF_Basket b:baskets){ //for each url
            for (URLparser.Values val:URLparser.Values.values()) { //for each tag
                if(b.getProcessedData(val) != null){
                    List<Word> data = b.getProcessedData(val);
                    for (Word word:data){
                        if(word.getTfidf() == -1){ // to check if the words tfidf has already been calculated
                            double tfidf = calculateTF(b, word) * calculateIDF(baskets,word);
                            word.setTfidf(tfidf);
                            adjustWeight(word);
                        }
                    }
                }

            }
        }
    }


}
