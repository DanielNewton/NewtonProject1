<%-- 
    Document   : GradeOneQuestion
    Created on : Mar 4, 2018, 4:59:05 PM
    Author     : ryfac
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Project 1</title>

        <style type = "text/css">
            body {font-family: Courier New, sans-serif; font-size: 100%; color: black}
            .keyword {color: #000080; font-weight: normal}
            .comment {color: gray}
            .literal {font-weight: normal; color: #3366FF}
        </style>
        <link rel="stylesheet" type="text/css" href="intro6e.css" />
        <link rel="stylesheet" type="text/css" href="intro6eselftest.css" />
        <style> 
            #question {font-family: Courier New, Courier, Verdana, Helvetica, sans-serif; font-size: 100%; 
                       color: #8000f0; color: black; margin-left: 0.5em}
            #questionstatement {font-family: 
                                    Times New Roman, monospace, Courier, Verdana, Helvetica, sans-serif; font-size: 100%; color: #8000f0; 
                                color: black; margin-left:1.8em; margin-top:0.5em; margin-bottom:0.5em; }
            #choices {font-family: Times New Roman, Helvetica, sans-serif; font-size: 100%; 
                      color: #8000f0; color: black; margin-left:25.0pt; margin-left:0.5em; margin-bottom:0.5em; }
            #choicemargin {font-family: Times New Roman, Helvetica, sans-serif; font-size: 100%; }
            #choicestatement {font-family: Times New Roman, Helvetica, sans-serif; font-size: 100%; 
                              color: #8000f0; color: black; margin-left:25.0pt; margin-left:0.5em; margin-bottom:0.5em; }
            .preBlock {margin-left:0px;text-indent:0.0px; font-family:monospace; font-size: 120%}
            .keyword {color: green;}
            .comment {color: darkred;  }
            .literal {color: darkblue}
            .constant {color: teal}
            #h3style {color: white; font-family: Helvetica, sans-serif;  font-size: 100%; border-color: #6193cb; text-align: center;margin-bottom: 0.5em; background-color: #6193cb;}  </style>
    </head>
    <body>
        <%@ page import ="Project1.Question" %>
        <!-- Create bean for capitals that exists within the session so that the user does not get duplicate questions.-->
        <jsp:useBean id = "question" class = "Project1.Question" scope = "session" ></jsp:useBean>



        <% question.viewQuestionData();%>

        <h3 id="h3style" style = " width: 500px auto; max-width: 620px; margin: 0 auto; ">Multiple-Choice Question <%= question.getTitle()%> </h3>

        <div style="width: 500px auto; max-width: 620px; margin: 0 auto; border: 1px solid #f6912f; font-weight: normal ">


            <div id="question"><div id="questionstatement">
                    <%= question.getQuestionTitle()%>   
                    <span class="preBlock" > <%= question.getQuestionBody()%> </span><br>
                </div></div>
                <%= question.determineAnswers()%>     

            <%= question.checkAnswer(request)%>

        </div>  
    </body>
</html>
