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
    <!--link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet"-->
    <!--link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css.map" rel="stylesheet"-->
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/font-awesome.min.css" rel="stylesheet">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="${contextPath}/resources/js/html5shiv.min.js"></script>
    <script src="${contextPath}/resources/js/respond.min.js"></script>
    <![endif]-->

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
<nav class="navbar navbar-default navbar-inverse" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${contextPath}/welcome"><p style="color:white">OpenClassRoom</p></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">Home</a></li>
                <li><a href="${contextPath}/plans">Plans</a></li>
                <li><a href="${contextPath}/practice">FAQ</a></li>
            </ul>
            <ul class="navbar-nav my-2 my-lg-0">
                <li class="nav-item dropdown">
                    <c:choose>
                        <c:when test="${pageContext.request.userPrincipal.name != null}">
                            <a href='#' id='dropdownMenu1' class='nav-link dropdown-toggle' role="button" data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>${pageContext.request.userPrincipal.name} </a>
                        </c:when>
                        <c:otherwise>
                            <a href='#' id='dropdownMenu1' class='nav-link dropdown-toggle' role="button" data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>Unknown User</a>
                        </c:otherwise>
                    </c:choose>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenu1">
                        <a href="#" class="dropdown-item" onclick="javascript:LogMeOut();">Log Out</a>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</nav>

<script src="${contextPath}/resources/js/jquery-3.2.1.min.js"></script>
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