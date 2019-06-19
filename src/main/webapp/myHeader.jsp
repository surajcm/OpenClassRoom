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

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/font-awesome.min.css" rel="stylesheet">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="${contextPath}/resources/js/html5shiv.min.js"></script>
    <script src="${contextPath}/resources/js/respond.min.js"></script>
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
<nav class="navbar navbar-static-top navbar-inner">
    <a class="navbar-brand" href="${contextPath}/welcome"><p style="color:white">OpenClassRoom</p></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
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
    </div>
</nav>

<script src="${contextPath}/resources/js/jquery-3.2.1.min.js"></script>
<script src="${contextPath}/resources/js/popper.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>

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