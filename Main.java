import java.io.*;
import java.util.*;
import org.jsoup.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("And a program begun!");

        String pathToLogFiles = null;

        File[] listLogFiles = new File[10000];

        int count_args = 0;
        int path_count = 0;

        for(String arg: args)   {

            count_args++;
            System.out.println("arg[" + count_args + "]=" + arg);

            if(arg.equals("--path"))  {

                path_count = count_args;

            }

            if(count_args == (path_count+1))    {

                pathToLogFiles = arg;
                System.out.println("Set system log path to " + pathToLogFiles);

            }

        }

        if(count_args == 0) {

            System.out.println("Arguments is empty!");

        }

        int count = 0;

        File folderFile = new File(pathToLogFiles);

        for(final File fileEntry: folderFile.listFiles())   {

            if(fileEntry.isDirectory()) {

                for(final File recurseFile: fileEntry.listFiles())  {

                    if(recurseFile.toString().contains(".htm")) {
                        
                        count++;
                        listLogFiles[count] = recurseFile;

                        System.out.println(listLogFiles[count]);

                    }

                }

            }

        }

        for(final File fileInDir: listLogFiles) {

            try {

                Jsoup.connect(fileInDir.toString()).timeout(0).get();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }


        System.out.println("The end of a program!");

    }

}
