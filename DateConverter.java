import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nlare on 15.12.16.
 */
public class DateConverter {

    public java.sql.Timestamp ConvertToSQLDate(String sourceDate) throws ParseException {

//        DateFormat formatter = new SimpleDateFormat(dateFormat);
        DateFormat sourceFormatter = new SimpleDateFormat("M/d/yyyy h:mm:ss a");
        Date parsed = sourceFormatter.parse(sourceDate);

        DateFormat resultFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String resultDateString = resultFormatter.format(parsed);

        Date resultDate = resultFormatter.parse(resultDateString);

//        System.out.println(resultDateString);

        java.sql.Timestamp sqlDate = new java.sql.Timestamp(resultDate.getTime());

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(sourceDateLong);

//        String resultDateInSQLCompatibleFormat = formatter.format(calendar.getTime());

        return sqlDate;

    }

}