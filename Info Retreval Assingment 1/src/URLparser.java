import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class URLparser {

    private EnumMap<Values,List<String>> documentData;

    URLparser(){

    }

    enum Values{
        WORDS,
        META,
        TD,
        LI,
        H1,
        H2,
        IMG
    }


    EnumMap<Values, List<String>> getDocumentData(String URL) throws IOException {
        documentData = new EnumMap<>(Values.class);
        Document doc = Jsoup.connect(URL).get();
        Elements bodyElements = doc.body().select("*");
        Elements headElements = doc.head().select("*");
        Elements imgElements = doc.getElementsByTag("img");

        getBodyData(bodyElements);
        getMetaData(headElements);
        getImgData(imgElements);

        System.out.println("\nOutput from raw HTML data: ");
        for (Map.Entry<URLparser.Values, List<String>> entry :documentData.entrySet()) {
            System.out.println(entry.getKey());
            List<String> val = entry.getValue();
            for (String w:val){
                System.out.println(w);
            }
        }

        return documentData;
    }

    private void getMetaData(Elements headElems){
        for(Element element:headElems){ //get all the relevant metadata information from the head of the file
            String key = element.attr("name");
            key = key.replaceAll("\\s+","");
            if(!key.equals("")){
                String val = element.attr("content");

                List<String> data;
                if(documentData.containsKey(Values.META)){
                    data = documentData.get(Values.META);
                }else{
                    data = new ArrayList<>();
                }
                data.add(key +":"+val);
                documentData.put(Values.META,data);
            }
        }
    }

    private void getImgData(Elements imgElems){
        int i = 1;
        for(Element element:imgElems){ //same as above but for images
            if(element.attr("alt") != null || !element.attr("alt").equals("")) {

                List<String> data;
                if(documentData.containsKey(Values.IMG)){
                    data = documentData.get(Values.IMG);
                }else{
                    data = new ArrayList<>();
                }

                if(element.hasAttr("alt")){
                    data.add(element.attr("alt")+":"+(element.attr("src")));
                    documentData.put(Values.IMG,data);
                }else{
                    data.add("Image #"+i+":"+(element.attr("src")));
                    documentData.put(Values.IMG,data);
                    i++;
                }
            }
        }
    }

    private void getBodyData(Elements bodyElems){
        for(Element element:bodyElems){ //same as above but for metadata
            String str = element.ownText();
            if(str.length() != 0 && str.matches(".*[a-zA-Z0-9]+.*")){ // get rid of empty space
                String tag = element.tagName();
                Values key;
                switch (tag){
                    case "li":
                        key = Values.LI;
                        break;
                    case "td":
                        key = Values.TD;
                        break;
                    case "h1":
                        key = Values.H1;
                        break;
                    case "h2":
                        key = Values.H2;
                        break;
                    default:
                        key = Values.WORDS;
                }

                List<String> val;
                if(documentData.containsKey(key)){
                    val = documentData.get(key);
                }else{
                    val = new ArrayList<>();
                }

                val.add(str);
                documentData.put(key,val);
                }
            }
        }
    }



