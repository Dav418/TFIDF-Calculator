import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.Reader;
import java.io.StringReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataTokenizer {
    private String[] stopwords = {"i","me","my","myself","we","our","ours","ourselves","you","your","yours","yourself",
            "yourselves","he","him","his","himself","she","her","hers","herself","it","its","itself","they","them",
            "their","theirs","themselves","what","which","who","whom","this","that","these","those","am","is","are",
            "was","were","be","been","being","have","has","had","having","do","does","did","doing","a","an","the",
            "and","but","if","or","because","as","until","while","of","at","by","for","with","about","against",
            "between","into","through","during","before","after","above","below","to","from","up","down","in",
            "out","on","off","over","under","again","further","then","once","here","there","when","where","why",
            "how","all","any","both","each","few","more","most","other","some","such","no","nor","not","only","own"
            ,"same","so","than","too","very","s","t","can","will","just","don","should","now","_","-","","-rrb-",
            "-lrb-","-lsb-","-rsb-","''","``"};// words not relevent to a search


    DataTokenizer(){
    }

    public List<String> splitIntoSentences(List<String> list){
        List<String> returnList = new ArrayList<>();
        for(String str:list){ //for each sentence
            Reader reader = new StringReader(str);
            DocumentPreprocessor dp = new DocumentPreprocessor(reader); //seperates each sentance into a list of words
            for (List<HasWord> str2 : dp) {
                String sentenceString = SentenceUtils.listToString(str2);
                sentenceString = normalise(sentenceString); //get rid of accepts
                returnList.add(sentenceString);
            }
        }
        return returnList;
    }
    
    public List<List<Word>> splitIntoWords(URLparser.Values val, List<String> list){ // tag each word in the sentence
        List<List<Word>> returnList = new ArrayList<>();
        MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
        for (String sentence: list) {
            returnList.add(splitSentenceIntoWords(val, tagger.tagString(sentence)));
        }
        return returnList;
    }

    private List<Word> splitSentenceIntoWords(URLparser.Values val, String sentence){
        List<Word> returnList = new ArrayList<>();
        sentence=sentence.toLowerCase();
        sentence=sentence.replaceAll("[.,?!:]",""); //get rid of punctuation and make words lowercase for later comparison
        
        String[] words = sentence.split("\\s+");
        for (String word:words) {
            if(!checkIfWordIsStopWord(word)){
                Word w = new Word(word,val); //store tagged String and what element it came from
                //@testPrint
                System.out.println("Split word from sentence: "+word);
                returnList.add(w);
            }
        }
        return returnList;
    }

    private boolean checkIfWordIsStopWord(String wordToCheck){
        int i = wordToCheck.indexOf("_"); //check if its tagged, since each word will look like 'example_ee'
        if(i==-1){
            return true;
        }else{
            String wordNoTag = wordToCheck.substring(0,i); // get rid of tag to compare with stopwords array
            return Arrays.asList(stopwords).contains(wordNoTag);
        }

    }

    private String normalise(String str){ // get rid of accent chars
        str = Normalizer.normalize(str,Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]]","");
        return str;
    }
}
