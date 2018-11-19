package testcasegen;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.zip.*;

public class main {
    static HashMap<String, Range> ranges = new HashMap<>();
    public static void main(String[] args) {
        file file = new file();
        file.deletewithzip();
        file.checkdir();
        addRange("n", 5L, (long)Math.pow(10, 3));
        addRange("arr", -100L, 100L);
        generate(20);
        //file.zip();
        //file.delete();
    }

    static void generate(int cases) {
        for(int i=0; i<cases; i++){
            out out = new out(i);
            in in = new in(i);
//            for(String s : ranges.keySet()){
//                Range tmp = ranges.get(s);
//                long c = (long)Math.pow(2, cases);
//                tmp.start = Math.max(((tmp.realend - tmp.realstart)/c)*((long)Math.pow(2, i)), tmp.realstart);
//                tmp.end = Math.min(((tmp.realend - tmp.realstart)/c)*((long)Math.pow(2, i+1)), tmp.realend);
//                System.out.println(s + " " + tmp.start + " " + tmp.end);
//                ranges.put(s, tmp);
//            }
            code(out, in);
            out.close();
            in.close();
        }
    }

    static void code(out out, in in) {
        int n = in.nextInt("n","\n");

        int max = in.nextInt("arr", " ");
        for(int i=0; i<n; i++){
            int elem = in.nextInt("arr", " ");
            if(max<elem){
                max = elem;
            }
        }

        out.println(max);
    }

    static void addRange(String var, long start, long end){
        Range r = new Range();
        r.start = start;
        r.end = end;
        r.realend = end;
        r.realstart = start;

        ranges.put(var, r);
    }
}

class Range{
    long start;
    long end;
    long realend;
    long realstart;
}

class file{
    String dir = "src/testcasegen/";
    void createFile(String type, int number, String value){
        String fileName = "output";
        if(type.equals("input"))
            fileName = "input";
        if (number >= 0 && number < 10)
            fileName += "0";
        fileName += Integer.toString(number);
        fileName += ".txt";

        File file = new File(dir + type + "/" + fileName);
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(value);
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    void createFile(String type, int number){
        String fileName = "input";
        if (number >= 0 && number < 10)
            fileName += "0";
        fileName += Integer.toString(number);
        fileName += ".txt";

        File file = new File(dir + type + "/" + fileName);
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("");
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    void checkdir() {
        String PATH = dir + "input";

        File directory = new File(PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }

        PATH = dir + "output";
        directory = new File(PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    void delete(){
        String PATH = dir + "input";
        File directory = new File(PATH);
        deleteutil(directory);

        PATH = dir + "output";
        directory = new File(PATH);
        deleteutil(directory);
    }

    void deletewithzip(){

        String PATH = dir + "input";
        File directory = new File(PATH);
        deleteutil(directory);

        PATH = dir + "output";
        directory = new File(PATH);
        deleteutil(directory);

        PATH = dir + "testcase.zip";
        directory = new File(PATH);
        deleteutil(directory);
    }

    void deleteutil(File file){

        if(file.isDirectory()){

            //directory is empty, then delete it
            if(file.list().length==0){

                file.delete();
                //System.out.println("Directory is deleted : " + file.getAbsolutePath());

            }else{

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    deleteutil(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    //System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            file.delete();
            //System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    void zip(){
        try {
            FileOutputStream fos = new FileOutputStream("testcase.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            String file1Name = dir + "input";
            String file2Name = dir + "output";

            addToZipFile(file1Name, zos);
            addToZipFile(file2Name, zos);

            zos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

        System.out.println("Writing '" + fileName + "' to zip file");

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }
}

class in{
    int current;
    StringBuilder sb = new StringBuilder();
    in(int current){
        this.current = current;
    }

    int nextInt(String end){
        Random r = new Random();
        int num = r.nextInt();
        sb.append(num);
        if(end=="\n")
            sb.toString().trim();
        sb.append(end);
        return num;
    }

    int nextInt(String var, String end){
        if(main.ranges.containsKey(var)){
            int start = (int)main.ranges.get(var).start;
            int stop = (int)main.ranges.get(var).end;
            Random r = new Random();

            int num = r.nextInt(stop+1-start)+start;
            //file file = new file();
            //file.createFile("input", current, Integer.toString(num)+end);
            sb.append(num);
            if(end=="\n")
                sb.toString().trim();
            sb.append(end);
            return num;
        }
        return nextInt(end);
    }

    long nextLong(String end){
        Random r = new Random();
        long num = r.nextLong();
        sb.append(num);
        if(end=="\n")
            sb.toString().trim();
        sb.append(end);
        return num;
    }

    long nextLong(String var, String end){
        if(main.ranges.containsKey(var)){
            long start = main.ranges.get(var).start;
            long stop = main.ranges.get(var).end;
            Random r = new Random();

            long num = start + (r.nextLong() % (stop - start));

            while(!(num>=start && num<=stop)) {
                num = start + (r.nextLong() % (stop - start));
                //System.out.println(start + " "+stop + " " + num);
            }
            System.out.println(num);
            //file file = new file();
            //file.createFile("input", current, Integer.toString(num)+end);
            sb.append(num);
            if(end=="\n")
                sb.toString().trim();
            sb.append(end);
            return num;
        }
        return nextInt(end);
    }

    void close(){
        file file = new file();
        file.createFile("input", current, sb.toString().trim());
    }
}

class out{
    int current;
    StringBuilder sb = new StringBuilder();
    out(int current){
        this.current = current;
    }

    void println(Object... objects){
        //file file = new file();
        //file.createFile("output", current, Objects.toString(objects)+"\n");
        for (int i = 0; i < objects.length; i++)
            sb.append(objects[i]);
        sb.toString().trim();
        sb.append("\n");
    }

    void print(Object... objects){
        //file file = new file();
        //file.createFile("output", current, Objects.toString(objects));
        sb.append(objects);
    }

    void close(){
        file file = new file();
        file.createFile("output", current, sb.toString().trim());
    }
}