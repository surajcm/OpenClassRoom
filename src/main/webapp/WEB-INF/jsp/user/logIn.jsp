<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Log In</title>
    <link href="../css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="../css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
    <link href="../css/font-awesome.css" rel="stylesheet" type="text/css">
    <link href="../css/font-awesome-ie7.css" rel="stylesheet" type="text/css">
    <!-- HTML5 shim for IE backwards compatibility -->
    <!--[if lt IE 9]>
    <script src="js/html5.js"></script>
    <![endif]-->
    <style>
        body {
            padding-top: 20px;
        }
        .margin-base-vertical {
            margin: 40px 0;
        }
    </style>
    <script type="text/javascript">
        function submitLogIn(){
            document.forms[0].action="LogIn.htm";
            document.forms[0].submit();
        }
    </script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-lg-5 col-offset-3">
            <div class="well">
            <form:form method="POST" commandName="userForm">
                <h1 class="margin-base-vertical">Login</h1>
                <div class="input-group">
                    <span class="input-group-addon"><spring:message code="openclassroom.username" text="User Name:" /></span>
                    <form:input path="name" cssClass="form-control" id="name" /><form:errors path="name" />
                </div>
                <br />
                <div class="input-group">
                    <span class="input-group-addon"><spring:message code="openclassroom.password" text="Password:" />&nbsp;&nbsp;</span>
                    <form:password path="password" cssClass="form-control" id="password" /><form:errors path="password" />
                </div>
                <br />
                <div class="form-actions">
                    <button type="submit" onclick="javascript:submitLogIn()" class="btn btn-success">Login</button>
                </div>
            </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>