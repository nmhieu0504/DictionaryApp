package source;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SlangWord {
    private HashMap<String, ArrayList<String>> slangDictionary;
    public SlangWord(String filename) {
        try {
            String prevKey = "";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String lines;
            this.slangDictionary = new HashMap<>();
            while ((lines = bufferedReader.readLine()) != null) {
                String[] tmp = lines.split("`");
                if (tmp.length == 1) {
                    this.slangDictionary.get(prevKey).add(tmp[0]);
                    continue;
                }
                ArrayList<String> slagMean = new ArrayList<>();
                String[] meaning = tmp[1].split("\\| ");
                Collections.addAll(slagMean, meaning);
                this.slangDictionary.put(tmp[0], slagMean);
                prevKey = tmp[0];
            }
        } catch (
                IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public ArrayList<String> findByWord(String word) {return this.slangDictionary.get(word);}
    public HashMap<String, ArrayList<String>> findByMeaning(String definition){
        HashMap<String, ArrayList<String>> result = new HashMap<>();
        for (Map.Entry<String, ArrayList<String>> entry : this.slangDictionary.entrySet()) {
            ArrayList<String> meaning = entry.getValue();
            for (String s : meaning) {
                if (s.toLowerCase().contains(definition.toLowerCase())) {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(s);
                    result.put(entry.getKey(), tmp);
                }
            }
        }
        return result;
    }
}
