import java.io.*;
import java.util.*;
import org.jsoup.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("And a program begun!");

        String pathToLogFiles;

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

        System.out.println("The end of a program!");

    }
}
