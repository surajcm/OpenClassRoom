<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link href="../css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="../css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
    <link href="../css/bootstrap-theme.css" rel="stylesheet" type="text/css">
    <link href="../css/font-awesome.css" rel="stylesheet" type="text/css">
    <link href="../css/font-awesome-ie7.css" rel="stylesheet" type="text/css">
    <!-- HTML5 shim for IE backwards compatibility -->
    <!--[if lt IE 9]>
    <script src="../js/html5.js"></script>
    <![endif]-->
    <style type="text/css">
        #menuimg {
            background-repeat: repeat-x;
            background-color: #A9A9A9;
            margin:auto;
            border-collapse:collapse;
        }

        div.menu {
            float: left;
            margin-left: 10px;
            font-size: 0.5em;
            font-family: Verdana, Arial, Helvetica, sans-serif;
        }

        div.popup a:hover { background-color: #faa; }
        div.menu:hover div:first-child { border-bottom: none; }
        div.menu div.popup { display: none; }
        div.menu:hover div.popup {
            display: block;
            background-color: #99f;
        }


        div.menu div {
            width: 175px;
            background-color: #66f;
            padding: 5px;
            border: solid 2px blue;
        }

        #menuFirstLine {
            font-weight: bold;
            font-size: 08;
            font-family: Verdana, Arial, Helvetica, sans-serif;
        }

    </style>
    <script type="text/javascript">
        function listMe(){
            document.forms[0].action="<%=request.getContextPath()%>"+"/user/ListAll.htm";
            document.forms[0].submit();
        }

        function goToHome(){
            document.forms[0].action="<%=request.getContextPath()%>"+"/user/ToHome.htm";
            document.forms[0].submit();
        }

        function LogMeOut(){
            document.forms[0].action="<%=request.getContextPath()%>"+"/user/LogMeOut.htm";
            document.forms[0].submit();
        }
    </script>
</head>
<body>
<div class="navbar-wrapper">
    <div class="navbar navbar-inverse navbar-static-top">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#"onclick="javascript:goToHome();" >OpenClassRoom</a>
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav">
                <script type="text/javascript">
                    if ( document.forms[0].loggedInRole != null
                            && document.forms[0].loggedInRole.value != null
                            && document.forms[0].loggedInRole.value == 'ADMIN'){
                        document.write("<li id='user'><a href='#' onclick='javascript:listMe();' >User</a></li>");
                    }
                </script>
            </ul>
            <ul class="nav navbar-nav pull-right">
                <li class="dropdown">
                    <script type="text/javascript">
                        if ( document.forms[0].loggedInUser != null
                                && document.forms[0].loggedInUser.value != null
                                && document.forms[0].loggedInUser.value.length > 0){
                            document.write("<a href='#' class='dropdown-toggle' data-toggle='dropdown'>"+document.forms[0].loggedInUser.value+"<b class='caret'></b></a>");
                        }else {
                            document.write("<a href='#' class='dropdown-toggle' data-toggle='dropdown'>Unknown User<b class='caret'></b></a>");
                        }
                    </script>
                    <ul class="dropdown-menu">
                        <li><a href="#" onclick="javascript:LogMeOut();">Log Out</a></li>
                    </ul>
                </li>
            </ul>
        </div><!--/.nav-collapse -->
    </div>
</div>
<script src="../js/jquery-latest.js"></script>
<script src="../js/bootstrap.js"></script>
<script>
    $(document).ready(function()
    {
        //Handles menu drop down
        $('.dropdown-menu').find('form').click(function (e) {
            e.stopPropagation();
        });
    });
</script>
</body>
</html>