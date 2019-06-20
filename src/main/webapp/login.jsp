<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
                    <div class="col-md-6 mb-4 white-text text-center text-md-left">
                        <img id="adv" style="max-width:100%;" src="${contextPath}/resources/img/abandoned-architecture-blackboard-752395.jpg" alt=""></img>
                    </div>
                    <div class="col-md-6 col-xl-5 mb-4">
                        <h3>Please Log In, or <a href="#">Sign Up</a></h3>
                        <div class="row">
                            <div class="col-xs-6 col-sm-6 col-md-6">
                              <a href="#" class="btn btn-lg btn-primary btn-block">Facebook</a>
                            </div>
                            <div class="col-xs-6 col-sm-6 col-md-6">
                              <a href="#" class="btn btn-lg btn-info btn-block">Google</a>
                            </div>
                        </div>
                        <div class="login-or">
                            <hr class="hr-or">
                            <span class="span-or">or</span>
                        </div>

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
                            <div class="checkbox pull-right">
                              <label>
                                <input type="checkbox">
                                Remember me </label>
                            </div>
                            <button type="submit" class="btn btn btn-primary">
                              Log In
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>

</html>