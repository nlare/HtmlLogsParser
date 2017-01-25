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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jsoup.parser.Parser.htmlParser;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("And a program begun!");

        String pathToLogFiles = null;

        List<File> listLogFiles = new ArrayList<>();

        Event event = new Event();

        Map <Integer, Event> mapEvents = new HashMap<>();

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

        int fieldCount = 1;
        int recordCount = 0;

        for(int i = 0; i < listLogFiles.size(); i++) {

            System.out.println(listLogFiles.get(i).toString());

            File current_file = new File(listLogFiles.get(i).toString());

            Document parsedHtml = Jsoup.parse(current_file, "UTF-16");

            if(parsedHtml != null)  {

                Elements doc = parsedHtml.getElementsByTag("tr");

                if(doc != null) {

                    Elements html = doc;

                    System.out.println(html.get("td"));

                    for (int j = 0; j < html.size(); j++) {

                        Element element = html.get(j);

//                        System.out.println(element.text());

                        if(fieldCount == 1) event.setDate(element.text());
                        if(fieldCount == 2) event.setUser(element.text());
                        if(fieldCount == 3) event.setPath(element.text());

                        fieldCount++;
                        recordCount++;

                        if((j+1) % 3 != 0)    {

                            fieldCount = 1;

                            mapEvents.put(recordCount, event);

                        }

                        for(Map.Entry<Integer, Event> entry: mapEvents.entrySet())  {

//                            System.out.println(entry.getKey() + " : " + entry.getValue().getDate());

                        }

                    }
                }

                } else  {

                    System.out.println("No any selected element!");

                }

        }

        System.out.println("The end of a program!");

    }

}