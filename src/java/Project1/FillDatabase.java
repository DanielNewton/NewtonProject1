/*
Author: Daniel Newton
File: FillDatabase.java
Created on 2/22/

 */
package Project1;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FillDatabase {

    private PreparedStatement preparedInsertStmt;
    private PreparedStatement preparedQuizDataTableStmt;
    private PreparedStatement preparedStudentDataTableStmt;

    public static void main(String[] args) throws IOException {

        //Create a an obect of the class to be able to use the prepared statements
        FillDatabase fd = new FillDatabase();
        fd.initializeDB();

        //Create the table
        try {
            fd.preparedQuizDataTableStmt.execute();
            fd.preparedStudentDataTableStmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Use a counter to move between each of the files
        int fileNum = 1;

        //Declare some patterns that match some of our key inputs, such as the question format, choices format, and key format.
        Pattern questionPattern = Pattern.compile("^\\d+\\.");
        Pattern choicesPattern = Pattern.compile("^[a-e]+\\.");
        Pattern keyPattern = Pattern.compile("^Key:", Pattern.CASE_INSENSITIVE);

        //Whenever we find a "#", we know we need to insert to the database.
        Pattern insertPattern = Pattern.compile("^#");

        //Declare our attributes for each tuple in the database.
        String questionText = "";
        String choiceA = "";
        String choiceB = "";
        String choiceC = "";
        String choiceD = "";
        String choiceE = "";
        String key = "";
        String hint = "";
        int questionNo = 1;
        int choiceCounter = 0;

        //Iterate over each questions.txt file until we have done them all
        while (fileNum < 45) {
            File file = new File("questions/chapter" + fileNum + ".txt");

            //Because Liang's files are saved as ANSI encoding, we need to set the scanner to that specific incoding.
            Scanner in = new Scanner(file, "Cp1252");

            try {
                //While there is file to be read, read the lines
                while (in.hasNextLine()) {
                    //Store each line in a variable called test
                    String test = in.nextLine();

                    //Use a matcher object to detect the start of questions
                    Matcher questionMatcher = questionPattern.matcher(test);

                    //Once we find a question start, replace the question number since it may be not accurate.
                    if (questionMatcher.find()) {
                        test = test.replaceAll("^\\d+\\.", "");
                        questionText += test;
                        test = in.nextLine();
                        Matcher choicesMatcher = choicesPattern.matcher(test);

                        //If the next line does not match the pattern of a choice, then it is a multi line question and we need to store the whole thing
                        while (!choicesMatcher.find()) {
                            questionText += "\n";
                            questionText += test;
                            test = in.nextLine();
                            choicesMatcher = choicesPattern.matcher(test);
                        }

                        //Once we have gathered the question, we need to iterate over the next few lines to get our choices.
                        Matcher keyMatcher = keyPattern.matcher(test);
                        //If we have not hit the key pattern, then we still have more choices to obtain
                        while (!keyMatcher.find()) {
                            //Use a switch statement and counter to determine which choice we are currently on
                            switch (choiceCounter) {
                                case 0:
                                    choiceA = test.replace("a.", "");
                                    break;
                                case 1:
                                    choiceB = test.replace("b.", "");
                                    break;
                                case 2:
                                    choiceC = test.replace("c.", "");
                                    break;
                                case 3:
                                    choiceD = test.replace("d.", "");
                                    break;
                                case 4:
                                    choiceE = test.replace("e.", "");
                                    break;
                                default:
                                    break;
                            }

                            choiceCounter++;
                            test = in.nextLine();
                            keyMatcher = keyPattern.matcher(test);
                        }

                        //If the key does not have a hint, then we only need to store the key.
                        if (!test.contains(" ")) {
                            key = test.replace("Key:", ""); //Replace the "key" portion because it is not needed now
                            hint = "";
                        } else {
                            //If it has a key and a hint, we need to iterate over the key to break the string into two parts
                            key = test.substring(4, test.indexOf(" "));
                            hint = test.substring(test.indexOf(" "));
                        }
                    }
                    //If we have reached a "#" or we have reached the end of the file in the case of the last question per file, then it is time to insert
                    Matcher insertMatcher = insertPattern.matcher(test);
                    if (insertMatcher.find() || (!in.hasNextLine())) {
                        try {
                            //Update our prepared statement with the new values
                            fd.preparedInsertStmt.setInt(1, fileNum);
                            fd.preparedInsertStmt.setInt(2, questionNo);
                            fd.preparedInsertStmt.setString(3, questionText);
                            fd.preparedInsertStmt.setString(4, choiceA);
                            fd.preparedInsertStmt.setString(5, choiceB);
                            fd.preparedInsertStmt.setString(6, choiceC);
                            fd.preparedInsertStmt.setString(7, choiceD);
                            fd.preparedInsertStmt.setString(8, choiceE);
                            fd.preparedInsertStmt.setString(9, key);
                            fd.preparedInsertStmt.setString(10, hint);

                            if (fd.preparedInsertStmt.executeUpdate() == 1) {
                                System.out.println("Update Successful");
                            } else {
                                System.out.println("Update Failed");
                            }

                        } catch (Exception e) {
                            
                        }
                        //Reset the values for each of the attributes
                        questionText = "";
                        choiceA = "";
                        choiceB = "";
                        choiceC = "";
                        choiceD = "";
                        choiceE = "";
                        key = "";
                        hint = "";
                        questionNo++;
                        choiceCounter = 0;
                    }
                }

            } catch (Exception e) {

            }
            //If we have reached the end of the file, we need to start the question counter over and move to next file.
            questionNo = 1;
            fileNum++;
        }
    }

    //Use similar Initalize DB method to what we have done in previous exercises.
    private void initializeDB() {
        try {
            // Load the JDBC driver 
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            // Establish a connection 
            Connection connection = DriverManager.getConnection("jdbc:mysql://35.185.94.191:3306/newton", "newton", "tiger");
            System.out.println("Database connected");

            //Create possible queries
            String insertQuery = "insert into intro11equiz(chapterNo, questionNo, question, choiceA, choiceB, choiceC, choiceD, choiceE, answerKey, hint) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            // Create a Prepared Statement with the view query 
            preparedInsertStmt = connection.prepareStatement(insertQuery);

            //Create the following tables if they do not yet exist.
            String createQuizDataTableQuery = "create table if not exists intro11equiz (\n"
                    + "chapterNo int(11),\n"
                    + "questionNo int(11),\n"
                    + "question text,\n"
                    + "choiceA varchar(1000),\n"
                    + "choiceB varchar(1000),\n"
                    + "choiceC varchar(1000),\n"
                    + "choiceD varchar(1000),\n"
                    + "choiceE varchar(1000),\n"
                    + "answerKey varchar(5),\n"
                    + "hint text\n"
                    + ");";

            preparedQuizDataTableStmt = connection.prepareStatement(createQuizDataTableQuery);

            String createStudentDataTableQuery = "create table if not exists intro11e (\n"
                    + "  chapterNo int(11), \n"
                    + "  questionNo int(11), \n"
                    + "  isCorrect bit(1) default 0,\n"
                    + "  time timestamp default current_timestamp,\n"
                    + "  hostname varchar(100),\n"
                    + "  answerA bit(1) default 0,\n"
                    + "  answerB bit(1) default 0,\n"
                    + "  answerC bit(1) default 0,\n"
                    + "  answerD bit(1) default 0,\n"
                    + "  answerE bit(1) default 0,\n"
                    + "  username varchar(100)\n"
                    + ");";
            
            preparedStudentDataTableStmt = connection.prepareStatement(createStudentDataTableQuery);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

