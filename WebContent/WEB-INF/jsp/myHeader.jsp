<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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
		menuFirstLine {
			font-weight: bold;
			font-size: 08;
			font-family: Verdana, Arial, Helvetica, sans-serif;
		}

		</style>
		<script type="text/javascript">
		    function listMe(){
		        document.forms[0].action="ListAll.htm";
				document.forms[0].submit();
		    }

			function goToHome(){
		        document.forms[0].action="ToHome.htm";
				document.forms[0].submit();
		    }

			function LogMeOut(){
		        document.forms[0].action="LogMeOut.htm";
				document.forms[0].submit();
		    }
		</script>
	</head>
    <body>
        <table id="menuimg" border="1"  width="100%" >
            <tr >
                <%
                    String DATE_FORMAT = "dd-MM-yyyy";
                    String TIME_NOW = "HH:mm";
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat(DATE_FORMAT);
                    java.text.SimpleDateFormat fmt_time = new java.text.SimpleDateFormat(TIME_NOW);
                %>
                <td>
						<div style="position:fixed;height:50%;width:50%;left:25%;">
						<label class="menuFirstLine">Welcome </label>
						<label class="menuFirstLine"><c:out value="${userForm.loggedInUser}" /></label>|
						<label class="menuFirstLine"><c:out value="${userForm.loggedInRole}" /></label>|
						<label class="menuFirstLine"><%=fmt.format(cal.getTime()) %></label>|
						<label class="menuFirstLine"><%=fmt_time.format(cal.getTime()) %></label>
						</div>
						<br/>
						<c:set var="roleFinder" value="admin" />
						<c:choose>
							<c:when test="${userForm.loggedInRole eq roleFinder}">
								<div class="menu"onclick="javascript:goToHome();" >
									<label for="Home" style="font-weight: bold;color:white;"  onMouseOver="this.style.cursor='pointer'"  >Home</label>
								</div>
								<div class="menu"  onclick="javascript:listMe();" >
									<label for="User" style="font-weight: bold;color:white;" onMouseOver="this.style.cursor='pointer'" >UserManagement</label>
								</div>
								<div class="menu"  onclick="javascript:LogMeOut();" >
									<label for="Out" style="font-weight: bold;color:white;" onMouseOver="this.style.cursor='pointer'" >LogOut</label>
								</div>
							</c:when>
							<c:otherwise>
								<div class="menu"  >
									<label for="NoMenu" style="font-weight: bold;color:white;" >Guest user has no menus available</label>
									<label for="Out" style="font-weight: bold;color:white;" onMouseOver="this.style.cursor='pointer'"  onclick="javascript:LogMeOut();">LogOut</label>
								</div>

							</c:otherwise>
						</c:choose>
                    </td>
            </tr>
        </table>
    </body>
</html>