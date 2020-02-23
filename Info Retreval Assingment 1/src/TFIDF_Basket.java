import java.text.DecimalFormat;
import java.util.*;

class TFIDF_Basket {

    private EnumMap<URLparser.Values, List<Word>> processedData; // HTML tag : each word after processing
    private EnumMap<URLparser.Values, List<String>> rawDara; //HTML tag : each word before processing
    private String url; //url the page came from
    private int sumOfAllWrds = 0; // sum of all the words in the doc

    TFIDF_Basket(String url){
        rawDara = new EnumMap<>(URLparser.Values.class);
        processedData = new EnumMap<>(URLparser.Values.class);
        this.url=url;
    }

    String getUrl() {
        return url;
    }

    void addSpecificProcessedData(URLparser.Values key, Word val){
       List<Word> l = getProcessedData(key);

       if(l == null){
           l = new ArrayList<>();
       }
       if (!l.contains(val)){
           l.add(val);
           processedData.put(key,l);
       }
    }



    List<Word> getProcessedData(URLparser.Values key){
        return processedData.get(key);
    }

    void addRawData(EnumMap<URLparser.Values, List<String>> data){
        this.rawDara = data;
    }

    public List<String> getRawDara(URLparser.Values key) {
        return rawDara.get(key);
    }

    public void sumOfAllWords(){
        int i = 0;
        for (Map.Entry<URLparser.Values, List<Word>> entry :processedData.entrySet()) {
            if( entry.getKey() != URLparser.Values.IMG){
                List<Word> val = entry.getValue();
                i += val.size();
            }
        }
        sumOfAllWrds = i;
    }

    public int getSumOfAllWrds() {
        return sumOfAllWrds;
    }

    void printWords(){

        for (Map.Entry<URLparser.Values, List<Word>> entry :processedData.entrySet()) { //sort
            List<Word> val = entry.getValue();
            val.sort(Collections.reverseOrder());
        }


        System.out.println(url + " : ");
            for (Map.Entry<URLparser.Values, List<Word>> entry :processedData.entrySet()) {//print
                if( entry.getKey() != URLparser.Values.IMG){
                    List<Word> val = entry.getValue();
                    System.out.println(" ");
                    for (Word w:val){
                        DecimalFormat df = new DecimalFormat("#");
                        df.setMaximumFractionDigits(8);
                        System.out.println(w + ". TFIDF score: " + df.format(w.getTfidf()));
                    }
                }
            }
    }


}
