public class Word implements Comparable<Word> {
    private String word;
    private String originalWord;
    private String tag;
    private double tfidf;
    private URLparser.Values whereFrom;

    Word(String str, URLparser.Values wF){
        int i = str.indexOf("_");

        this.word = str.substring(0,i); //word that gets compared
        this.originalWord = this.word; //word before stemming
        this.tag = str.substring(i+1); //tag of the word
        this.tfidf = -1; //making sure that the word can have a TFIDF score. this is changed later
        this.whereFrom = wF; //to make sure to keep a reference of where the word came from
    }

    public double getTfidf() {
        return tfidf;
    }

    public void setTfidf(double tfidf) {
        this.tfidf = tfidf;
    }

    public String getTag() {
        return tag;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public URLparser.Values getWhereFrom() {
        return whereFrom;
    }

    public String getOriginalWord() {
        return originalWord;
    }

    @Override
    public String toString() {
        return "From tag: " + whereFrom + " word: " +word +"_"+tag + " original word: " + originalWord;
    }


    @Override
    public boolean equals(Object obj){ // to make sure that each word stored is unique
        Word o = (Word)obj;
        return this.getWhereFrom().equals(o.getWhereFrom()) && this.getTag().equals(o.getTag()) && this.getOriginalWord().equals(o.getOriginalWord());
    }

    @Override
    public int compareTo(Word w) { // this is so that they can be ordered in the list
        if(this.getTfidf()<w.getTfidf())
            return -1;
        else if(this.getTfidf()>w.getTfidf())
            return 1;
        return 0;

    }
}
