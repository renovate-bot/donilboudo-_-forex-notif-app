package com.nsidetech;

import com.mysql.cj.jdbc.result.ResultSetImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CheckOpportunityJob implements Job {
    private static final String DB_NAME = "forex";
    private static final String TABLE_NAME = "alerte";
    private static List<Alerte> toBeUpdate;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Looking in the DB to find new opportunities");
        toBeUpdate = new ArrayList<>();

        try
        {
            // create our mysql database connection
            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost/" + DB_NAME + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "root", "");

            searchForNewOpportunities(conn);

            updateNotifiedRows(conn);

            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    private static void searchForNewOpportunities(Connection conn) throws SQLException {
        // our SQL SELECT query.
        // if you only need a few columns, specify them by name instead of using "*"
        String query = "SELECT * FROM " + TABLE_NAME;

        // create the java statement
        Statement st = conn.createStatement();

        // execute the query, and get a java resultset
        ResultSet rs = st.executeQuery(query);

        // iterate through the java resultset
        while (rs.next())
        {
            boolean isProcessed = rs.getBoolean("isProcessed");

            if (!isProcessed)
            {
                System.out.println("Seems like i found a new opportunity! On my way to be rich");

                String asset = rs.getString(1);
                LocalDateTime time = ((ResultSetImpl) rs).getLocalDateTime(2);
                int timeframe = rs.getInt(3);
                String message = rs.getString(4);

                NotificationHelper.getInstance().sendMessage(asset, message, time);

                Alerte alerte = new Alerte(asset, time, timeframe, message);
                toBeUpdate.add(alerte);
            }
        }
        st.close();
    }

    private static void updateNotifiedRows(Connection conn) {
        toBeUpdate.forEach(alerte -> {
            System.out.println("Updating a row");

            String time = alerte.getTime().toLocalDate() + " " + alerte.getTime().toLocalTime();

            try
            {
                String query = "update " + TABLE_NAME + " set isProcessed = ? where Asset = ? AND Timeframe = ? AND Message = ? ";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setBoolean(1, true);
                preparedStmt.setString(2, alerte.getAsset());
                preparedStmt.setInt(3, alerte.getTimefreame());
                preparedStmt.setString(4, alerte.getMessage());

                preparedStmt.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        });
    }

}