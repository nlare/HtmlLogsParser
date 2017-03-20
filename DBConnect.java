/**
 * Created by nlare on 08.12.16.
 */

import java.sql.*;
import java.text.ParseException;

//import java.sql.*;

public class DBConnect {

    private String connectionURL;
    private String connectionUsername;
    private String connectionPassword;

    private int statusConnection;
    private int oracleConnectClassExist;
    private Connection connection;

    String sqlDateFormat;

//    EventsData data = new EventsData();

    DBConnect() throws SQLException {

//        sqlDateFormat = "yyyy.MM.dd";
//        sqlDateFormat = "MM/dd/yyyy hh:mm aaa";
//        sqlDateFormat = "yyyy-MM-dd'T'HH:mm:ssz";
        connection = null;
//        connection.setAutoCommit(false);

    }

//    List<EventsData> dataList = new List<EventsData>();

    public void setConnectionURL(String connectionURL) {

        this.connectionURL = connectionURL;

    }

    public void setConnectionUsername(String connectionUsername) {

        this.connectionUsername = connectionUsername;
    }

    public void setConnectionPassword(String connectionPassword)    {

        this.connectionPassword = connectionPassword;

    }

    private void setStatusConnection(int statusConnection)   {

        this.statusConnection = statusConnection;

    }

    private void setOracleConnectClassExist(int oracleConnectClassExist)  {

        this.oracleConnectClassExist = oracleConnectClassExist;

    }

    public String getConnectionURL()  {

        return this.connectionURL;

    }

    public String getConnectionUsername()   {

        return  this.connectionUsername;

    }

    public String getConnectionPassword()   {

        return this.connectionPassword;

    }

    public int getStatusConnection()    {

        return this.statusConnection;

    }

    public int getOracleConnectClassExist() {

        return this.oracleConnectClassExist;

    }

    public int connectToDB() {

        setOracleConnectClassExist(0);

        try {

//            System.setProperty("oracle.net.tns_admin", "/etc");

            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();

        }   catch(ClassNotFoundException cne)   {

            System.out.println("Cannot connect Oracle thought JDBC! Class not found!");

            setOracleConnectClassExist(1);

        } catch (InstantiationException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();

        }

        try {

//            connection = DriverManager.getConnection("CISARCH","pseventlog","pwdfromouterspace");
            connection = DriverManager.getConnection("jdbc:oracle:thin:pseventlog/pwdfromouterspace@cisarch:1521:cisarch");

        }   catch(SQLException sqle) {

            System.out.println("Error in getConnection!");

        }

        if(connection == null)  {

            System.out.println("Cannot connect to the current URL!");

            setStatusConnection(1);

        }   else    {

            System.out.println("Connected!");

            setStatusConnection(0);

        }

        return 0;

    }

    public void commitToDB() throws SQLException {

        connection.commit();

    }

    public int addToDatabase(String username, String filename, String eventdate) throws SQLException, ParseException {

//        Statement st = connection.createStatement();

        DateConverter converter = new DateConverter();

        java.sql.Timestamp sqlDate = converter.ConvertToSQLDate(eventdate);

        // Need correction of date type!

//        String sql = "INSERT into PSEVENTLOG.EventsLog (USERNAME,FILENAME,TIME_EVENT) VALUES ('" + username + "','" + filename + "', to_date('" + sqlDate + "','" + sqlDateFormat + "'))";
        String sql = "INSERT into PSEVENTLOG.EventsLog (USERNAME,FILENAME,TIME_EVENT) VALUES (?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, username);
        preparedStatement.setString(2, filename);
        preparedStatement.setTimestamp(3, sqlDate);

//        st.executeUpdate(sql);
        preparedStatement.executeUpdate();
//        st.executeUpdate("INSERT into PSEVENTLOG.EventsLog (USERNAME,FILENAME,TIME_STRING)" + " VALUES ('" + username + "','" + filename + "','" + dateString + "')");
//        st.executeUpdate("INSERT into PSEVENTLOG.EventsLog (USERNAME,FILENAME,TIME_EVENT) VALUES ('testuser2','testfile2',null)");

        preparedStatement.close();

        return 0;

    }

}
