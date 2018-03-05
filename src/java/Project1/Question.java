/*
Author: Daniel Newton
File: Question.java
Created on 3/5/18

 */
package Project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

public class Question {

    private int chapterNo = 0;
    private int questionNo = 0;
    private String question = "";
    private String questionTitle = "";
    private String questionBody = "";
    private String choiceA = "";
    private String choiceB = "";
    private String choiceC = "";
    private String choiceD = "";
    private String choiceE = "";
    private String answerKey = "";
    private String hint = "";
    private String title = "";
    private PreparedStatement preparedRetrivalStmt;
    private PreparedStatement preparedInsertStmt;

    public Question() {
        try {
            // Load the JDBC driver 
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            // Establish a connection 
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/project1", "scott", "tiger");
            System.out.println("Database connected");

            //Create possible queries
            String query = "select question, choiceA, choiceB, choiceC, choiceD, choiceE, answerKey, hint "
                    + "from intro11equiz "
                    + "where chapterNo = ? and questionNo = ? ";

            // Create a Prepared Statement with the retrival query listed above 
            preparedRetrivalStmt = connection.prepareStatement(query);

            //Create a Prepared Statement with the insert query
            String insertQuery = "insert into intro11e(chapterNo, questionNo, isCorrect, hostname, answerA, answerB, answerC, answerD, answerE)"
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            preparedInsertStmt = connection.prepareStatement(insertQuery);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void viewQuestionData() {
        //If the QuestionNo or ChapterNo are zero, then the user inputted an incorrect question
        if (chapterNo == 0 || questionNo == 0) {
            return;
        }
        try {
            preparedRetrivalStmt.setInt(1, chapterNo);
            preparedRetrivalStmt.setInt(2, questionNo);

            ResultSet rset = preparedRetrivalStmt.executeQuery();

            //get values from result set
            if (rset.next()) {
                question = rset.getString(1);
                choiceA = rset.getString(2);
                choiceB = rset.getString(3);
                choiceC = rset.getString(4);
                choiceD = rset.getString(5);
                choiceE = rset.getString(6);
                answerKey = rset.getString(7);
                hint = rset.getString(8);

                //If the question text has a line break, we know it is made up of a code section and a title section
                if (question.contains("\n")) {
                    setQuestionTitle(question.substring(0, question.indexOf("\n")));
                    questionBody = question.substring(question.indexOf("\n"));
                    questionBody = questionBody.replaceAll("\n", "<br>");
                } else {
                    questionTitle = question;
                    questionBody = "";
                }
                //Call determineKeywords for correct coloring
                determineKeywords();
            } else {
                question = "";
                choiceA = "";
                choiceB = "";
                choiceC = "";
                choiceD = "";
                choiceE = "";
                answerKey = "";
                hint = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String determineAnswers() {
        //Check how many choices the question has
        int choiceCounter = 0;
        String output;

        if (!choiceA.equals("")) {
            choiceCounter++;
        }
        if (!choiceB.equals("")) {
            choiceCounter++;
        }
        if (!choiceC.equals("")) {
            choiceCounter++;
        }
        if (!choiceD.equals("")) {
            choiceCounter++;
        }
        if (!choiceE.equals("")) {
            choiceCounter++;
        }
        
        //If the question only has one answer, only give one choice, if it only has two, only give two choices, etc.
        
        //If the key length is longer than one, then we know we must allow for checkboxes rather than radio buttons
        if (answerKey.length() > 1) {
            switch (choiceCounter) {
                case 1:
                    output = "<div id=\"choices\">\n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"A\" name=\"A\"> \n"
                            + "                        <span id=\"choicelabel\">A.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceA + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    ";
                    break;
                case 2:
                    output = "<div id=\"choices\">\n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"A\" name=\"A\"> \n"
                            + "                        <span id=\"choicelabel\">A.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceA + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"B\" name=\"B\"> \n"
                            + "                        <span id=\"choicelabel\">B.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceB + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    ";
                    break;
                case 3:
                    output = "<div id=\"choices\">\n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"A\" name=\"A\"> \n"
                            + "                        <span id=\"choicelabel\">A.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceA + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"B\" name=\"B\"> \n"
                            + "                        <span id=\"choicelabel\">B.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceB + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"C\" name=\"C\"> \n"
                            + "                        <span id=\"choicelabel\">C.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceC + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    ";
                    break;
                case 4:
                    output = "<div id=\"choices\">\n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"A\" name=\"A\"> \n"
                            + "                        <span id=\"choicelabel\">A.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceA + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"B\" name=\"B\"> \n"
                            + "                        <span id=\"choicelabel\">B.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceB + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"C\" name=\"C\"> \n"
                            + "                        <span id=\"choicelabel\">C.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceC + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"D\" name=\"D\"> \n"
                            + "                        <span id=\"choicelabel\">D.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceD + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    ";
                    break;
                case 5:
                    output = "<div id=\"choices\">\n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"A\" name=\"A\"> \n"
                            + "                        <span id=\"choicelabel\">A.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceA + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"B\" name=\"B\"> \n"
                            + "                        <span id=\"choicelabel\">B.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceB + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"C\" name=\"C\"> \n"
                            + "                        <span id=\"choicelabel\">C.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceC + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"D\" name=\"D\"> \n"
                            + "                        <span id=\"choicelabel\">D.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceD + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"checkbox\" value=\"E\" name=\"E\"> \n"
                            + "                        <span id=\"choicelabel\">E.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceE + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    ";
                    break;
                default:
                    output = "";
            }
        } else {
            switch (choiceCounter) {
                case 1:
                    output = "<div id=\"choices\">\n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"A\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">A.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceA + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    ";
                    break;
                case 2:
                    output = "<div id=\"choices\">\n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"A\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">A.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceA + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"B\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">B.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceB + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    ";
                    break;
                case 3:
                    output = "<div id=\"choices\">\n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"A\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">A.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceA + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"B\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">B.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceB + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"C\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">C.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceC + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    ";
                    break;
                case 4:
                    output = "<div id=\"choices\">\n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"A\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">A.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceA + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"B\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">B.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceB + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"C\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">C.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceC + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"D\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">D.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceD + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    ";
                    break;
                case 5:
                    output = "<div id=\"choices\">\n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"A\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">A.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceA + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"B\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">B.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceB + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"C\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">C.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceC + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"D\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">D.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceD + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    <div id = \"choicemargin\">\n"
                            + "                        <input type=\"radio\" value=\"E\" name=\"Q\"> \n"
                            + "                        <span id=\"choicelabel\">E.</span>"
                            + "                        <span id=\"choicestatement\">" + choiceE + "</span>\n"
                            + "                        <br>\n"
                            + "                    </div> \n"
                            + "                    ";
                    break;
                default:
                    output = "";
            }
        }
        return output;
    }

    public void determineKeywords() {
        String test = "";
        
        //First go through the string and look for literals
        for(int i=0; i<questionBody.length(); i++){
            if(questionBody.charAt(i) == '"'){
                i++;
                test +="<span class=\"literal\" >\"";
                while(questionBody.charAt(i) != '"'){
                    test += questionBody.charAt(i);
                    i++;
                }
            test+= "\"</span>";
            }else
                test+=questionBody.charAt(i);     
        }
        
        questionBody =test;
        test="";
        
        //Create a list of known keywords and check to see if any of the words in our question match the keywords
        String[] keywords = {"absract", "assert", "boolean", "break", "byte", "case", "catch", "char",
            "do", "default", "continue", "double", "else", "enum", "extends", "final",
            "finally", "float", "for", "if", "implements", "import", "instanceof", "int",
            "interface", "long", "native", "new", "package", "private", "protected", "public",
            "return", "short", "static", "super", "switch", "this", "throws", "try", "void", "while"};

        String[] words = questionBody.split(" |\"\"");

        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < keywords.length; j++) {
                if (words[i].contains(keywords[j]) && !words[i].contains("System.out.print")) {
                    test += words[i].replace(keywords[j], "<span class =\"keyword\"> " + keywords[j] + " </span>");
                    break;
                } else if (words[i].contains("//")) { //Checks for comments and colors them accordingly
                    test += " <span class = \"comment\"> " + words[i] + " </span>";
                    break;
                }
                if (j == keywords.length - 1) {
                    test += " " + words[i];
                }
            }
        }

        questionBody = test;
        test = "";
        
        //Finally checks for constants and colors them accordingly
        for (int i = 0; i < questionBody.length(); i++) {
            if (Character.isDigit(questionBody.charAt(i))) {
                test += "<span class=\"constant\"> " + questionBody.charAt(i) + "</span>";
                if (Character.isDigit(questionBody.charAt(i + 1))) {
                    test += "<span class=\"constant\">" + questionBody.charAt(i + 1) + "</span>";
                    i++;
                }
            } else {
                test += questionBody.charAt(i);
            }
        }

        questionBody = test;
        
    }

    public void storeData(HttpServletRequest request, String userAnswer, boolean isCorrect) {
        //Store the user's answers in the database 
        try {
            preparedInsertStmt.setInt(1, chapterNo);
            preparedInsertStmt.setInt(2, questionNo);
            preparedInsertStmt.setBoolean(3, isCorrect);
            preparedInsertStmt.setString(4, request.getLocalName());
            if (userAnswer.contains("A")) {
                preparedInsertStmt.setInt(5, 0b1);
            } else {
                preparedInsertStmt.setBoolean(5, false);
            }
            if (userAnswer.contains("B")) {
                preparedInsertStmt.setBoolean(6, true);
            } else {
                preparedInsertStmt.setBoolean(6, false);
            }
            if (userAnswer.contains("C")) {
                preparedInsertStmt.setBoolean(7, true);
            } else {
                preparedInsertStmt.setBoolean(7, false);
            }
            if (userAnswer.contains("D")) {
                preparedInsertStmt.setBoolean(8, true);
            } else {
                preparedInsertStmt.setBoolean(8, false);
            }
            if (userAnswer.contains("E")) {
                preparedInsertStmt.setBoolean(9, true);
            } else {
                preparedInsertStmt.setBoolean(9, false);
            }

            preparedInsertStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String checkAnswer(HttpServletRequest request) {
        String output;
        String userAnswer = "";
        boolean isCorrect = false;
        
        //If the parameter is q, then the question was a checkbox, otherwise, the user submitted radio buttons
        if (request.getParameter("Q") != null) {
            userAnswer = request.getParameter("Q");
        } else {
            if (request.getParameter("A") != null) {
                userAnswer += "A";
            }
            if (request.getParameter("B") != null) {
                userAnswer += "B";
            }
            if (request.getParameter("C") != null) {
                userAnswer += "C";
            }
            if (request.getParameter("D") != null) {
                userAnswer += "D";
            }
            if (request.getParameter("E") != null) {
                userAnswer += "E";
            }
        }

        //If the answer is correct, print the appropriate response and set the isCorrect flag to true
        if (userAnswer.equalsIgnoreCase(answerKey)) {
            output = "<span style = \"color: green\"> Your answer is correct  <img border=\"0\" src=\"https://liveexample.pearsoncmg.com/selftest/js/correct.jpg\" width=\"42\" height=\"30\"></span><br></div>";
            //If there is a hint, allow the user to see the hint
            if (!hint.equals("")) {
                output += "\n <div id=\"hint\" style=\"color:green; font-family: Times New Roman;\">Click here to show an explanation</div>\n"
                        + "\n"
                        + "<script>\n"
                        + "document.getElementById(\"hint\").onclick = function() {showHint()};\n"
                        + "\n"
                        + "function showHint() {\n"
                        + "    document.getElementById(\"hint\").innerHTML = \"Explanation: " + hint + "\";\n"
                        + "    document.getElementById(\"hint\").style.color = \"purple \" ;\n"
                        + "}\n"
                        + "\n"
                        + "</script>";
            }
            isCorrect = true;
        } else { //If the answer is incorrect, print the appropriate response.
            output = "<span style = \"color: red\">Your answer " + userAnswer + " is incorrect <img border=\"0\" src=\"https://liveexample.pearsoncmg.com/selftest/js/wrong.jpg\" width=\"28\" height=\"28\"></span><br>" + "\n";
            if (!hint.equals("")) {
                output += "<div id=\"answer\" style=\"color:green\"></div> "
                        + "\n <div id=\"hint\" style=\"color:green; font-family: Times New Roman;\">Click here to show the correct answer and an explanation</div>\n"
                        + "\n"
                        + "<script>\n"
                        + "document.getElementById(\"hint\").onclick = function() {showHint()};\n"
                        + "\n"
                        + "function showHint() {\n"
                        + "    document.getElementById(\"answer\").innerHTML = \"The correct Answer is " + answerKey.toUpperCase() + "\";\n"
                        + "    document.getElementById(\"hint\").innerHTML = \"Explanation: " + hint + "\";\n"
                        + "    document.getElementById(\"hint\").style.color = \"purple \" ;\n"
                        + "}\n"
                        + "\n"
                        + "</script>";
            } else {
                output += "<div id=\"answer\" style=\"color:green\">Click here to show the answer</div>\n"
                        + "\n"
                        + "<script>\n"
                        + "document.getElementById(\"answer\").onclick = function() {myFunction()};\n"
                        + "\n"
                        + "function myFunction() {\n"
                        + "    document.getElementById(\"answer\").innerHTML = \"The correct Answer is " + answerKey.toUpperCase() + "\";\n"
                        + "}\n"
                        + "\n"
                        + "</script>";
            }
        }

        //Store the user's data in the database
        storeData(request, userAnswer, isCorrect);
        return output;
    }

    /**
     * @return the chapterNo
     */
    public int getChapterNo() {
        return chapterNo;
    }

    /**
     * @param chapterNo the chapterNo to set
     */
    public void setChapterNo(int chapterNo) {
        this.chapterNo = chapterNo;
    }

    /**
     * @return the questionNo
     */
    public int getQuestionNo() {
        return questionNo;
    }

    /**
     * @param questionNo the questionNo to set
     */
    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return the choiceA
     */
    public String getChoiceA() {
        return choiceA;
    }

    /**
     * @param choiceA the choiceA to set
     */
    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    /**
     * @return the choiceB
     */
    public String getChoiceB() {
        return choiceB;
    }

    /**
     * @param choiceB the choiceB to set
     */
    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    /**
     * @return the choiceC
     */
    public String getChoiceC() {
        return choiceC;
    }

    /**
     * @param choiceC the choiceC to set
     */
    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    /**
     * @return the choiceD
     */
    public String getChoiceD() {
        return choiceD;
    }

    /**
     * @param choiceD the choiceD to set
     */
    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    /**
     * @return the choiceE
     */
    public String getChoiceE() {
        return choiceE;
    }

    /**
     * @param choiceE the choiceE to set
     */
    public void setChoiceE(String choiceE) {
        this.choiceE = choiceE;
    }

    /**
     * @return the key
     */
    public String getAnswerKey() {
        return answerKey;
    }

    /**
     * @param key the key to set
     */
    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    /**
     * @return the hint
     */
    public String getHint() {
        return hint;
    }

    /**
     * @param hint the hint to set
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the questionBody
     */
    public String getQuestionBody() {
        return questionBody;
    }

    /**
     * @param questionBody the questionBody to set
     */
    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    /**
     * @return the questionTitle
     */
    public String getQuestionTitle() {
        return questionTitle;
    }

    /**
     * @param questionTitle the questionTitle to set
     */
    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }
}
