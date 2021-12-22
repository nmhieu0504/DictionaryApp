package source;

import java.io.*;
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
                if (tmp.length == 1) { // case "\n" in slang.txt
                    this.slangDictionary.get(prevKey).add(tmp[0]);
                    continue;
                }
                else if(this.slangDictionary.get(tmp[0]) != null){ // case duplicate slang word
                    this.slangDictionary.get(tmp[0]).add(tmp[1]);
                    continue;
                }
                ArrayList<String> slagMean = new ArrayList<>();
                String[] meaning = tmp[1].split("\\| ");
                Collections.addAll(slagMean, meaning);
                this.slangDictionary.put(tmp[0], slagMean);
                prevKey = tmp[0];
            }
            bufferedReader.close();
            this.slangDictionary.remove("Slag");
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
            ArrayList<String> tmp = new ArrayList<>();
            for (String s : meaning)
                if (s.toLowerCase().contains(definition.toLowerCase()))
                    tmp.add(s);
            if(tmp.size() > 0)
                result.put(entry.getKey(), tmp);
        }
        return result;
    }

    public void addSlangWord(String word, String meaning){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(meaning);
        this.slangDictionary.put(word, arrayList);
        saveToFile("data/slang.txt");
    }

    public void overwriteSlangWord(String word, String meaning){
        this.slangDictionary.remove(word);
        addSlangWord(word, meaning);
        saveToFile("data/slang.txt");
    }

    public void duplicateSlangWord(String word, String meaning){
        this.slangDictionary.get(word).add(meaning);
        saveToFile("data/slang.txt");
    }

    public void saveToFile(String filename){
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
            for (Map.Entry<String, ArrayList<String>> entry : this.slangDictionary.entrySet()) {
                StringBuilder lines = new StringBuilder(entry.getKey() + "`");
                ArrayList<String> arrayList = entry.getValue();
                for(int i = 0; i < arrayList.size(); i++)
                    if(i == arrayList.size() - 1)
                        lines.append(arrayList.get(i));
                    else
                        lines.append(arrayList.get(i)).append("|").append(" ");

                bufferedWriter.write(lines + "\n");
            }
            bufferedWriter.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void editSlangWord(String originWord,String newWord, ArrayList<String> meaning){
        this.slangDictionary.remove(originWord);
        this.slangDictionary.put(newWord, meaning);
        saveToFile("data/slang.txt");
    }

    public void deleteSlangWord(String word){
        this.slangDictionary.remove(word);
        saveToFile("data/slang.txt");
    }

    public String randomSlangWord(){
        int randomNumber = (int) Math.floor(Math.random() * (this.slangDictionary.size())), idx = 0;
        for (Map.Entry<String, ArrayList<String>> entry : this.slangDictionary.entrySet()) {
            if(idx++ == randomNumber)
                return entry.getKey();
        }
        return "";
    }
    public String getMeaning(String word){
        int randomNumber = (int) Math.floor(Math.random() * this.slangDictionary.get(word).size());
        return this.slangDictionary.get(word).get(randomNumber);
    }
}