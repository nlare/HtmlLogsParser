import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class Main {

//    private static void writeExcludedFilesToString(String excludeFile)  {
//
//        System.out.println(excludeFile);
//
//        excludeFiles.add(excludeFile);
//
//    }

    private static List<String> excludeFiles = new ArrayList<>();

    private static boolean isAlreadyParsed(String filename)   {

        boolean trigger = false;
        int count = 0;

        for (String nameInList: excludeFiles) {

            count++;

//            System.out.println("File : " + nameInList);

            if (filename.contains(nameInList)) {

                System.out.println("Exclude : " + filename + " : " + nameInList);

                trigger = true;

                break;

            }   else    {

//                System.out.println("New File : " + filename);

                trigger = false;

//                break;

            }

        }

        return trigger;

    }

    public static void main(String[] args) throws IOException, SQLException, ParseException {

        System.out.println("And a program begun!");

        long startTime, endTime, millisecToExecute, secToExecute, minToExecute, modulo;

        startTime = System.currentTimeMillis();

        String pathToLogFiles = null;

        DBConnect db_connection = new DBConnect();

        List<File> listLogFiles = new ArrayList<>();

        List<String> listOfParsedFiles = new ArrayList<>();

        String buffer = new String();
        String delimiters = new String();
        String filename = new String();

//        File FileWithlistOfParsedFiles = new File(".parsed_files");
        Path pathToFileWithListOfParsedFiles = Paths.get(".parsed_files");

        Map <Event, Integer> mapEvents = new HashMap<>();

        long summarySizeOfMap = 0;
        long countOfLogFiles  = 0;

        int count_args = 0;
        int path_count = 0;

        // \\\\ - use backslash (Windows Paths)
        delimiters = "[/\\\\]";

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

        Files.lines(pathToFileWithListOfParsedFiles).forEach(s -> excludeFiles.add(s));

        File folderFile = new File(pathToLogFiles);

        for(final File fileEntry: folderFile.listFiles())   {

            if(fileEntry.isDirectory()) {

                for(final File recurseFile: fileEntry.listFiles())  {

                    if(recurseFile.toString().contains(".htm")) {
                        // Проверяем присутствие файла в уже распарсенных ранее, которые записаны в файл .parsed_files и коллекцию excludeFiles
                        if(!(isAlreadyParsed(recurseFile.toString()))) {

                            if(!(recurseFile.toString().equals(""))) {

                                System.out.println("Added: " + recurseFile.toString());

                                listLogFiles.add(recurseFile);

                                countOfLogFiles++;

                                {

                                    buffer = recurseFile.toString();

                                    String[] dividedFilenamesBySlash = buffer.split(delimiters);

                                    filename = dividedFilenamesBySlash[dividedFilenamesBySlash.length - 1];

                                    listOfParsedFiles.add(filename);

                                }

                            }
                        }

                    }

                }

            }

        }

        final String pathToFileWithListOfParsedFilesString = ".parsed_files";
        // Заведем определение файла
        File FileWithListOfParsedFiles = new File(pathToFileWithListOfParsedFilesString);

        // Запишем в файл список уже прочитанных файлов
        if(!(listOfParsedFiles.isEmpty())) {
            // pathToFileWithListOfParsedFiles - переменная типа Path, путь до файла, в который будем писать список
            //            Files.write(pathToFileWithListOfParsedFiles, listOfParsedFiles, Charset.forName("UTF-8"), EnumSet.of(APPEND));

            // Для записи в файл с флагом APPEND (второй параметр конструктора)
            FileWriter parsedFilesWriter = new FileWriter(FileWithListOfParsedFiles.getAbsoluteFile(),true);

            System.out.println("Filepath: " + FileWithListOfParsedFiles.getAbsoluteFile());
            //Если файл не существует - создадим файл
            if(!(FileWithListOfParsedFiles.exists()))  {

                FileWithListOfParsedFiles.createNewFile();
                System.out.println("Create file with name: " + FileWithListOfParsedFiles.getAbsoluteFile());

            }
            // Буферезированная запись
            BufferedWriter parsedFilesBufferedWriter = new BufferedWriter(parsedFilesWriter);

            for(String parsedFiles: listOfParsedFiles)  {

                try {

                    parsedFilesBufferedWriter.write(parsedFiles);
                    System.out.println("Block of try");

                } catch(Exception e)   {

                    System.out.println("Cannot write (append) to file!");

                }   finally {

                    try {
                        // Сперва нужно закрыть BufferWriter
                        if(parsedFilesBufferedWriter != null)   {

                            System.out.println("Close BufferedFileWriter.");
                            parsedFilesBufferedWriter.close();

                        }
                        // А только потом FileWriter!
                        if(parsedFilesWriter != null) {

                            System.out.println("Close FileWriter.");
                            parsedFilesWriter.close();

                        }
                        // Иначе не запишет

                    }   catch(Exception e) {

                        System.out.println("FileWriter and BufferedFileWriter:" + e);

                    }

                }

                System.out.println("Write: " + parsedFiles);

            }

        }

        System.out.println("-----------------------------------------!");

        int fieldCount = 0;
        int allRecordsCount = 0;

        db_connection.connectToDB();

        List<String> tdTextArray = new ArrayList<>();

        System.out.println("Size of listLogFiles: " + listLogFiles.size());

        for(int i = 0; i < listLogFiles.size(); i++) {

            System.out.println(listLogFiles.get(i).toString());

            File current_file = new File(listLogFiles.get(i).toString());

            Document parsedHtml = Jsoup.parse(current_file, "UTF-16");

            if(parsedHtml != null)  {

                Elements doc = parsedHtml.getElementsByTag("td");

                if(doc != null) {

                    Elements html = doc;

                    int recordCount = 0;

                    String eventDate = new String();
                    String eventUser = new String();
                    String eventPath = new String();

                    String[] excludedNames = new String[10];
                    String[] excludedFiles = new String[10];

                    excludedNames[0] = "AJakovchitc";
                    excludedNames[1] = "VLozhnikov";
                    excludedNames[2] = "AMamonov";
                    excludedNames[3] = "VVelichko";
                    excludedNames[4] = "FS0$";

                    excludedFiles[0] = ".db-journal";

                    for (int j = 0; j < html.size(); j++) {

                        Element element = html.get(j);

                        String tdText = element.text();

                        tdTextArray.add(tdText);

                        // List indexes counted from 0

                        fieldCount += 1;

                        if(fieldCount == 1) eventDate = tdTextArray.get(j);
                        if(fieldCount == 2) eventUser = tdTextArray.get(j);
                        if(fieldCount == 3) eventPath = tdTextArray.get(j);

                        if((fieldCount % 3) == 0)    {

//                            if((!Objects.equals(eventUser, excludedNames[0])) && (!Objects.equals(eventUser, excludedNames[1])) && (!Objects.equals(eventUser, excludedNames[2])) && (!Objects.equals(eventUser, excludedNames[3])) && (!Objects.equals(eventUser, excludedNames[4]))) {

//                                if (!(eventUser.contains(excludedFiles[0]))) {

                                    Event event = new Event();

                                    event.setDate(eventDate);
                                    event.setUser(eventUser);
                                    event.setPath(eventPath);

//                                  System.out.println(recordCount + " : " + fieldCount + " : " + event.getDate() + " : " + event.getUser() + " : " + event.getPath());

                                    recordCount += 1;

                                    allRecordsCount += 1;

                                    if (recordCount % 1000 == 0) {

                                        System.out.print(".");


                                    }

      //                            mapEvents.put(event, recordCount);

//                                    db_connection.commitToDB();


//                                }

                                    fieldCount = 0;

//                                    try {

//                                        db_connection.addToDatabase(eventUser, eventPath, eventDate);

//                                        db_connection.commitToDB();

//                                    }   catch(SQLException e)   {
//
//                                        System.out.println("SQL ERROR! " + e);
//
//                                    }
//
//                                } else {
//
//                                    fieldCount = 0;
//
//                                }
                        }



                    }

//                    System.out.println("Size of mapEvents: " + mapEvents.size());
                    System.out.println("\nSize of mapEvents: " + recordCount);
                    System.out.println("------------------------------------------------------------------------");

//                    summarySizeOfMap += mapEvents.size();

                }

                } else {

                    System.out.println("No any selected element!");

                }

        }

        try {

            db_connection.commitToDB();

            System.out.println("Commited!");

        }   catch(Exception e) {

            System.out.println("Commit Error!");

        }

        endTime = System.currentTimeMillis();

        millisecToExecute = endTime - startTime;

        secToExecute = millisecToExecute / 1000;

        minToExecute = secToExecute / 60;

        modulo = secToExecute - minToExecute * 60;

//        modulo = secToExecute - minToExecute;

        System.out.println("Time to run: " + minToExecute + "'m " + modulo + "'s ; eventsCount: " + allRecordsCount + "; Count of log files: " + countOfLogFiles);

        System.out.println("The end of a program!");

    }

}