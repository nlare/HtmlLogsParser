import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("And a program begun!");

        String pathToLogFiles = null;

        List<File> listLogFiles = new ArrayList<>();

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

        File folderFile = new File(pathToLogFiles);

        for(final File fileEntry: folderFile.listFiles())   {

            if(fileEntry.isDirectory()) {

                for(final File recurseFile: fileEntry.listFiles())  {

                    if(recurseFile.toString().contains(".htm")) {

                        listLogFiles.add(recurseFile);

                    }

                }

            }

        }

        for(int i = 0; i < listLogFiles.size(); i++) {

            System.out.println(listLogFiles.get(i).toString());

//            File htmlFile = new File(listLogFiles.get(i).toString());

            String testStr = new String(Files.readAllBytes(Paths.get(listLogFiles.get(i).toString())));

            Document parsedHtml = Jsoup.parse(testStr);

            if(parsedHtml != null)  {

                parsedHtml.text();

            }   else    {

                System.out.println(parsedHtml.text());

            }

            Elements trElements = parsedHtml.getElementsByTag("tr");

            String html = trElements.html();

            if(trElements != null) System.out.println("Parsed!");

            if(trElements != null) {

                for (Element th: trElements) {

                    System.out.println(th.text());

                }

            } else  {

                System.out.println("No any selected element!");

            }

        }

        System.out.println("The end of a program!");

    }

}
