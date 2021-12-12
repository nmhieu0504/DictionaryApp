package source;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
                slagMean.add(tmp[1]);
                this.slangDictionary.put(tmp[0], slagMean);
                prevKey = tmp[0];
            }
        } catch (
                IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public ArrayList<String> getter(String key) {return this.slangDictionary.get(key);}
}
