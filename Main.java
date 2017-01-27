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

        int fieldCount = 0;

        List<String> tdTextArray = new ArrayList<>();

        for(int i = 0; i < listLogFiles.size(); i++) {

            System.out.println(listLogFiles.get(i).toString());

            File current_file = new File(listLogFiles.get(i).toString());

            Document parsedHtml = Jsoup.parse(current_file, "UTF-16");

            if(parsedHtml != null)  {

                Elements doc = parsedHtml.getElementsByTag("td");

                String docStr = doc.toString();

                if(doc != null) {

                    Elements html = doc;

                    int recordCount = 0;

                    for (int j = 0; j < html.size(); j++) {

                        Element element = html.get(j);

                        String tdText = element.text();

                        tdTextArray.add(tdText);

//                        System.out.println(j + " : " + tdTextArray.get(j));

                        // List indexes counted from 0

                        fieldCount += 1;

                        if(fieldCount == 1) event.setDate(tdTextArray.get(j));
                        if(fieldCount == 2) event.setUser(tdTextArray.get(j));
                        if(fieldCount == 3) event.setPath(tdTextArray.get(j));

                        if((fieldCount % 3) == 0)    {

//                            System.out.println(j + " : " + fieldCount + " : " + event.getDate() + " : " + event.getUser() + " : " + event.getPath());

                            recordCount += 1;

                            mapEvents.put(recordCount, event);

                            fieldCount = 0;

                        }

                    }

                    for(Map.Entry<Integer, Event> entry: mapEvents.entrySet())  {

//                        System.out.println(entry.getValue() + " : " + entry.getKey().getDate() + " : " + entry.getKey().getUser() + " : " + entry.getKey().getPath());
                            System.out.print(entry.getKey() + " : ");
                            System.out.println(entry.getValue().getPath());
//
                    }

                }

                } else  {

                    System.out.println("No any selected element!");

                }

        }

        System.out.println("The end of a program!");

    }

}