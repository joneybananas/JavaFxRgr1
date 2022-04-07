package com.example.demo;

import com.example.account.Gemstone;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JThread extends Thread {
    ObservableList<Gemstone> gemstoneList;

    JThread(String name,  ObservableList<Gemstone> gemstoneList){
        super(name);
        this.gemstoneList = gemstoneList;
    }

    public ObservableList<Gemstone> getAccountList() {
        return gemstoneList;
    }

    public void saveFile(){
        System.out.printf("%s started... \n", Thread.currentThread().getName());
        try{
            PrintWriter pw = new PrintWriter(new FileOutputStream("src\\main\\resources\\com\\example\\demo\\data.txt")); //D:\Study\8_semester\Java\demo\src\main\resources\com\example\demo\
            for (Gemstone gemstone : gemstoneList){
                pw.println(gemstone);
            }
            pw.close();
            System.out.printf("ok");
        }
        catch(IOException e){
            System.out.println("Thread has been interrupted");
        }
        System.out.printf("%s fiished... \n", Thread.currentThread().getName());
    }

    public void loadFile() {
        System.out.printf("%s started... \n", Thread.currentThread().getName());

        try {
            List<String> list = null;
            list = Files.readAllLines(Paths.get("src\\main\\resources\\com\\example\\demo\\data.txt")); //"D:\\Study\\8_semester\\Java\\demo\\src\\main\\resources\\com\\example\\demo\\
            String[] param;
            for (String row : list){
                try{
                    Thread.sleep(500);
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }

                param = row.split(" ");

                gemstoneList.add(new Gemstone(param[0],
                        Double.parseDouble(param[1]),
                        Double.parseDouble(param[2])));
            }
        } catch (IOException e) {
            System.out.println("Thread has been interrupted");
        }
        System.out.printf("%s finished... \n", Thread.currentThread().getName());
    }
}
