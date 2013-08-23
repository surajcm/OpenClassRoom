<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User Add</title>
    <script type="text/javascript">

        //code to add New user
        function save(){
            document.forms[0].action="SaveUser.htm";
            document.forms[0].submit();
        }

        //code to edit a user
        function clear(){
            document.getElementById("name").value ="";
            document.getElementById("psw").value ="";
        }
    </script>
</head>
<body>
<form:form method="POST" commandName="userForm" name="userForm" >
    <%@include file="/WEB-INF/jsp/myHeader.jsp" %>
    <form:hidden name="loggedInUser" path="loggedInUser" />
    <form:hidden name="loggedInRole" path="loggedInRole" />
    <div class="container">
        <div class="panel panel-primary">
            <div class="panel-heading">Add User</div>
            <table style="margin:auto;top:50%;left:50%;">
                <tr>
                    <td>
                        <label for="name" class="control-label"><spring:message code="openclassroom.username" text="User Name" />:</label>
                    </td>
                    <td>
                        <form:input path="user.name" cssClass="form-control" id="name" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label for="password"  class="control-label"><spring:message code="openclassroom.password" text="Password" /></label>
                    </td>
                    <td>
                        <form:password path="user.password" cssClass="form-control" id="psw" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <label	for="role" class="control-label"><spring:message code="openclassroom.role" text="Role" /></label>
                        <label class="mandatory">*</label>
                    </td>
                    <td>
                        <form:select id="role" path="user.role"
                                     onkeypress="handleEnter(event);" cssClass="form-control" >
                            <form:option value=""><spring:message code="common.select" text="<-- Select -->"/></form:option>
                            <form:options items="${userForm.roleList}" />
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td>
                        &nbsp;
                    </td>
                    <td>
                        <input class="btn btn-primary btn-success" value="Save" type="button" onclick="javascript:save();" />
                        <input class="btn btn-primary" value="Clear" type="button" onclick="javascript:clear();" />
                    </td>
                </tr>
            </table>
        </div>
    </div>
</form:form>
</body>
</html>
