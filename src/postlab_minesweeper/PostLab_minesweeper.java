/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postlab_minesweeper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author emani
 */
public class PostLab_minesweeper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Board b = new Board(9,9);
        MsAccess ms = new MsAccess(b);
        b.setMSAcess(ms);
    }
    
}
class MsAccess {
        private Board board;
	MsAccess(Board b) {
            board = b;
            Connection connection = null;
            Statement statement = null;
            ResultSet data = null;
            int gamesP=0;
            int gamesW=0;
            int bestT=0;

            try {

                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            }
            catch(ClassNotFoundException cnfex) {

                System.out.println("Problem in loading or "
                        + "registering MS Access JDBC driver");
                cnfex.printStackTrace();
            }

            try {

                String msAccDB = System.getProperty("user.dir")+"//DB//mine1.accdb";
                String dbURL = "jdbc:ucanaccess://" + msAccDB; 

                connection = DriverManager.getConnection(dbURL); 
                statement = connection.createStatement();
                data = statement.executeQuery("SELECT * FROM Statistics");
               while(data.next())
               {
                    int id = data.getInt(1);
                    gamesP = data.getInt(2);
                    gamesW = data.getInt(3);
                    bestT = data.getInt(4);
               }
               board.loadStatsAtStart(gamesP, gamesW); // loads gameplayedCount, gameWonCount, Besttime from db
 
            }
            catch(SQLException sqlex){
                sqlex.printStackTrace();
            }
            finally {
                
                try {
                    if(null != connection) {

                        // cleanup resources, once after processing
                        data.close();
                        statement.close();

                        // and then finally close connection
                        connection.close();
                    }
                }
                catch (SQLException sqlex) {
                    sqlex.printStackTrace();
                }
            }
    }
    public void setDataInDB()
    {
        
        System.out.println("in setDataInDB");
        int dataArr[][] = new int[3][1];
        dataArr = board.setStatsAtEnd();
        Connection connection = null;
        Statement statement = null;
        ResultSet bestTime = null;
        ResultSet GameWon = null;
        String bt = null;
        int gw=0;
        
            try {

                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            }
            catch(ClassNotFoundException cnfex) {

                System.out.println("Problem in loading or "
                        + "registering MS Access JDBC driver");
                cnfex.printStackTrace();
            }

            // Step 2: Opening database connection
            try {

                String msAccDB = System.getProperty("user.dir")+"//DB//mine1.accdb";
                String dbURL = "jdbc:ucanaccess://" + msAccDB; 
                connection = DriverManager.getConnection(dbURL); 
                statement = connection.createStatement();
                statement.execute("SELECT BestTime from Statistics");
                bestTime = statement.getResultSet();
                
                statement.execute("SELECT GameWon FROM statistics");
                GameWon = statement.getResultSet();
                if(GameWon.next()) // if game won then get number of games won in gw
                {
                    gw = GameWon.getInt(1);
                }
                String time = board.get_tf_time().getText(); // time taken by player in current game
                if(bestTime.next())
                {
                    bt = bestTime.getString(1);  // gettin best time stored in DB 
                }
                if(board.getGameWon() && gw  > 0 ) //  game won current time and had won before
                {
                      int t = Integer.parseInt(time);  
                      int t1 = Integer.parseInt(bt);
                       if(t < t1 )  // current game time is less than previous best time so set current time as best time
                       {
                                board.setBestTime(t);
              //                  System.out.println("win and gw > 0  and time < besttime");
                       }
                       else
                       {
                               board.setBestTime(t1);   // previous best time  stored is still best score now 
                 //              System.out.println("win and gw > 0  and time> besttime");
                           
                       }
                }
                if( !board.getGameWon() && gw  > 0)  // game not won but won before
                {
                        board.setBestTime(Integer.parseInt(bt));  // previous best time  stored is still best score now 
                  //      System.out.println("not win and gw > 0");
                    
                    System.out.println(board.getBestTime());
                }
                if(board.getGameWon() &&  gw  == 0) // game win for first time
                { 
                     board.setBestTime(Integer.parseInt(time));  // current time will be best time 
                //     System.out.println(" win and gw = 0");
                }
                if(!board.getGameWon() &&  gw  == 0)  // game not won current time and never win before
                {
                    board.setBestTime(0);
                //    System.out.println("not win and gw = 0");
                    
                }
                

//               statement.execute("DELETE FROM statistics");
//               statement = connection.createStatement();
//                statement.execute("INSERT INTO Statistics"
//                + " (ID, GamePlayed, GameWon, BestTime)"+ " VALUES (1 ,"+ dataArr[0][0] +","+ dataArr[1][0]+","+ board.getBestTime()+ ")");
               statement.execute("UPDATE Statistics set GamePlayed = "+dataArr[0][0]+", GameWon = "+dataArr[1][0]+", BestTime = "+board.getBestTime()+" WHERE ID = 1" );
                   
            }
            catch(SQLException sqlex){
                sqlex.printStackTrace();
            }
            finally {
                try {
                    if(null != connection) {
                       bestTime.close();
                       GameWon.close();
                        statement.close();

                        // and then finally close connection
                        connection.close();
                    }
                }
                catch (SQLException sqlex) {
                    sqlex.printStackTrace();
                }
            }

        
    }

}
