package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Data {
    public static List<String> wordsPool = new ArrayList<String>();

    public Data(){
        try(BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/words.txt")))
        {
            String line;
            while ((line = reader.readLine()) != null){
                if (line.length() <= 8) { wordsPool.add(line); }
            }
            System.out.println("Выбираем одно из " + wordsPool.size() + " слов...");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден.");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла.");
            throw new RuntimeException(e);
        }

    }
}
