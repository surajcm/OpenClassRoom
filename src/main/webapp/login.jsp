<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>OpenClassRoom - Home</title>
        <link href="${contextPath}/resources/css/login.css" rel="stylesheet">
    </head>
    <body>
        <%@include file="myHeader.jsp" %>
        <div class="mask rgba-black-light d-flex justify-content-center align-items-center">
            <div class="container">
                <div class="row wow fadeIn">
                    <div class="col-md-6 col-xl-5 mb-4">
                        <img id="adv" src="${contextPath}/resources/img/undraw_teaching_f1cm.svg" alt=""></img>
                    </div>
                    <div class="col-md-6 mb-4 white-text text-center text-md-left">
                        <h3>Please Log In, or <a href="#">Sign Up</a></h3>
                        <form class="form" role="form" method="post" action="${contextPath}/login" accept-charset="UTF-8" id="login-nav">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <div class="form-group">
                              <label for="inputUsernameEmail">Username or email</label>
                              <input name="username" type="text" class="form-control" placeholder="Username" id="inputUsernameEmail"
                                                                                  autofocus="true" required/>
                            </div>
                            <div class="form-group">
                              <a class="pull-right" href="#">Forgot password?</a>
                              <label for="inputPassword">Password</label>
                             <input id="inputPassword" name="password" type="password" class="form-control" placeholder="Password" required/>
                            </div>
                            <div class="pull-right">
                              <button type="submit" class="btn btn btn-primary">Log In</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>

</html>