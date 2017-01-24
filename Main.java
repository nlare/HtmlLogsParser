import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.text.html.parser.Parser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.jsoup.parser.Parser.htmlParser;

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

//            String testStr = "<html><head><title>First parse</title></head>" + "<body><p>Parsed HTML into a doc.</p></body></html>";

            File current_file = new File(listLogFiles.get(i).toString());

//            String testStr = new String(Files.readAllBytes(Paths.get(listLogFiles.get(i).toString())));

//            System.out.println(testStr);

            Document parsedHtml = Jsoup.parse(current_file, "UTF-16");

            if(parsedHtml != null)  {

                Elements doc = parsedHtml.getElementsByTag("tr");

//                System.out.println("---------------");
//                System.out.println(text);
//                System.out.println("---------------");
//                Elements doc = parsedHtml.select("tr");

                System.out.println(doc.size());
//                System.out.println("td:" + );
                if(doc != null) {

                    for (int j = 0; j < doc.size(); j++) {

                        System.out.println("---------------");
                        System.out.println(doc.get(j));
                        System.out.println("---------------");

                        Document parsedTags = Jsoup.parse(doc.text());


                    }

                }

                } else  {

                    System.out.println("No any selected element!");

                }

        }

        System.out.println("The end of a program!");

    }

}