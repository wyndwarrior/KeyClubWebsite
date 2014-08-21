<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="wynd.mhskc.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	Account u = new Account(request);
	KUser kuser = null;
	if( u.loggedIn())
		kuser = KUser.getUser(u.getId());
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Memorial HS Key Club</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Where your heart to serve unites with your call to lead. Memorial High School Key Club is a student-led volunteer organization dedicated to helping schools and communities in the local Houston area.">
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<!--link rel="stylesheet" type="text/css"
	href="css/bootstrap-theme.min.css"-->
<link rel="stylesheet" type="text/css" href="css/style.css">
<script src="js/jquery-1.10.1.min.js"></script>
<script src="js/jquery-migrate-1.2.1.min.js"></script>
<script src="js/moment.min.js"></script>
<script src="js/three.min.js"></script>
<script src="js/Detector.js"></script>
<script src="js/tween.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/scripts.js"></script>
<script>
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

	  ga('create', 'UA-42255528-1', 'memorialkeyclub.appspot.com');
	  ga('send', 'pageview');
	  
	function htmlReady(){
		setTimeout(function(){
			$(".alert").alert('close');
		}, 5000);
<%
String viewReq = request.getParameter("view");
			if (viewReq == null)
				viewReq = "home";
String ev = request.getParameter("event");
if( ev != null){
%>
viewEvent('<%=ev%>');
<%
	}
%>
		showOnly('<%=viewReq%>', 0);
		<%
		if( kuser != null){
		%>
		slideFadeOut($("#infoForm"),0);
		slideFadeIn($("#infoPane"),0);
		<%
		}else{
		%>
		slideFadeIn($("#infoForm"),0);
		slideFadeOut($("#infoPane"),0);
		<%
		}
		%>
	}
</script>
</head>
<body>
	<div class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		            <span class="icon-bar"></span>
		          </button>
				<a class="navbar-brand">Memorial HS KeyClub</a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li class="active" id="_home"><a href="#"
						onClick="showOnly('home')">Home</a></li>
					<li id="_discover"><a href="#" onClick="showOnly('discover')">Discover</a></li>
					<li id="_serve"><a href="#" onClick="showOnly('serve')">Serve</a></li>
				</ul>
				<%
					if (!u.loggedIn()) {
				%>
				<form class="navbar-form navbar-right">
					<a class="btn btn-success" onClick="showOnly('connect')">Sign in &raquo;</a>
				</form>
				<%
					} else {
						pageContext.setAttribute("name", u.getName());
						if( kuser != null && kuser.getEmail()!= null)
							pageContext.setAttribute("email", kuser.getEmail());
						else
							pageContext.setAttribute("email", u.getEmail());
				%>
				<ul class="nav navbar-nav navbar-right">
					<li id="_connect"><a href="#" onClick="showOnly('connect')">My Account </a></li>
				</ul>
				<%
					}
				%>
			</div>
		</div>
	</div>
	<div id="container"></div>


	<div class="container" id="content">

<br/>
		<% 
		String msgErr = request.getParameter("err"),
			msgSucc = request.getParameter("succ");
		if( msgErr != null){
			pageContext.setAttribute("msgErr", msgErr);
		%>
		<div class="alert alert-danger">${fn:escapeXml(msgErr)}</div>
		<%
		}
		if( msgSucc != null){
			pageContext.setAttribute("msgSucc", msgSucc);
		%>
		<div class="alert alert-success">${fn:escapeXml(msgSucc)}</div>
		<%
		}
		%>
	
		<div class="row" id="home">


		<div class="row">
		<div class="col-xs-12 col-sm-7 well">
			<h2>Featured Event</h2>
			<h4 id="featuredTitle"></h4>
			<p><b id="featuredDate"></b><br/><br/>
			<span id="featuredDesc"></span>
			</p>
			<a href="#" onClick="viewFeatured()" class="btn btn-primary">View Event</a>
		</div>
		<div class="col-xs-12 col-sm-5">
			<img class="img-thumbnail" src="img/Battery.png" />
		</div>
		</div>
			<div class="col-xs-12 col-sm-12" >
				<div class="row">
					<div class="col-sm-4">
						<img class="img-thumbnail" src="img/img_Discover.png" />
						<h2>Discover</h2>
						<p class="mediumbox">Memorial High School Key Club is a student-led service
							program dedicated to helping schools and communities.</p>
						<p>
							<a class="btn btn-primary" href="#" onClick="showOnly('discover')">Learn
								More &raquo;</a>
						</p>
					</div>
		
					<div class="col-sm-4">
						<img class="img-thumbnail" src="img/img_Serve.png" />
						<h2>Serve</h2>
						<p class="mediumbox">Help your school and community in service activities such as
							cleaning parks and organizing food drives. Learn more about how MHS
							KeyClub is reaching out to serve those in need.</p>
						<p>
							<a class="btn btn-primary" href="#" onClick="showOnly('serve')">Serve &raquo;</a>
						</p>
					</div>
		
					<div class="col-sm-4">
						<img class="img-thumbnail" src="img/img_Join.png">
						<h2>Connect</h2>
						<p class="mediumbox">Want to start volunteering with Memorial HS KeyClub? Join
							now!</p>
						<p>
							<a class="btn btn-primary" href="#" onClick="showOnly('connect')">Connect
								&raquo;</a>
						</p>
					</div>
				</div>
			</div>
			
		</div>

		<div id="connect">
			<%
				if (!u.loggedIn() || !u.ensurePermissions()) {
			%>
			<div class="row">
			<form class="well col-md-8 center">
				<h2 class="form-signin-heading">Connect</h2>
				<%
					if (u.loggedIn()  && !u.ensurePermissions()) {
				%>
				<div class="alert alert-error">It looks like you didn't allow
					all the permissions necessary. Try logging in again.</div>
				<%
					}
				%>
				<p>MHS Keyclub uses Facebook to connect with you. Don't worry,
					we won't post anything without your permission.</p>
				<a href="api?action=fbconnect" class="btn btn-large btn-primary">Connect
					with Facebook</a> <br /> <br> <small>If you don't use
					Facebook, other options will be availble soon. </small>
			</form>
			</div>
			<%
				} else {

					if (kuser != null) {
						pageContext.setAttribute("grade", kuser.getGrade());
						pageContext.setAttribute("hours",
								String.format("%.2f", kuser.getHours()));
						pageContext.setAttribute("phone", kuser.getPhone());
						pageContext.setAttribute("phone2", kuser.getPhone2());
						String status;
						if( kuser.isVerified() )
							status = "Verified";
						else status = "Not Verified";
						pageContext.setAttribute("status", status);
						
						if( kuser.isAdmin() ){
						%>
						
						<div class="well col-md-10 center">
						Hey, it looks like you're an admin! That means you can access the admin panel<br/><br/>
						<a href="admin" class="btn btn-default">Admin Panel</a>
						</div><br/>
						
						<%
						}
						
						%>
						
						<div class="well col-md-10 center leftalign" id="infoPane">
							<h3>${fn:escapeXml(name)}</h3>
							<h4>Contact Information:</h4>
							<b>Email: </b> ${fn:escapeXml(email)}<br/>
							<b>Phone: </b> ${fn:escapeXml(phone)}<br/>
							<b>Alt. Phone: </b> ${fn:escapeXml(phone2)}<br/><br/>
							<a onClick="slideFadeIn($('#infoForm'))" class="btn btn-default">Edit Information</a><br/>
							<h4>General Information:</h4>
							<b>Registration Status: </b> ${fn:escapeXml(status)}<br/>
							<b>Grade: </b> ${fn:escapeXml(grade)}<br/>
							<b>Hours: </b> ${fn:escapeXml(hours)}<br/>
						</div>
						
						<%
					}
			%>
			<br/>
			
			<div id="infoForm" class="col-md-10 well center">
			<form class="form-horizontal" role="form" action="api"
				onSubmit="return validatef()">
				<input type="hidden" name="action" value="updateInfo" />
				<h2>MHS KeyClub Signup</h2>

				<p class="leftalign">
					<b>I understand that I will be expected to have at least 50
						service hours by the end of the year.</b><br /> <small>We
						will, of course, do our part to make sure you have all the service
						opportunities you need to exceed this requirement and benefit the
						community. Exceptions will be made on a case-by-case basis.</small><br />
					<br /> <b>I understand that I will be expected to pay dues to
						be a member of Key Club.</b><br /> <small>These
						will be used to pay dues to Kiwanis and fund events and conference
						attendance.</small><br /> <br /> <b>I understand that I am expected
						to be dedicated to service and have high moral character.</b><br /> <small>Key
						Club is based on the core values of leadership, character
						building, caring and inclusiveness.</small><br /> <br /> <b>KeyClub
						International Pledge</b><br /> <small><i>I pledge, on my
							honor, to uphold the Objects of Key Club International; to build
							my home, school and community; to serve my nation and God; and
							combat all forces which tend to undermine these institutions.</i></small> <br />
				</p>
				<br /> <br /> <b>Name:</b> ${fn:escapeXml(name)}<br /><br/>
				<div class="form-group">
					<label class="col-sm-3 control-label">Email</label>
					<div class="col-sm-9">
						<input class="form-control" id="femail" type="text" name="email"
							placeholder="Email" value="${fn:escapeXml(email)}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Phone Number</label>
					<div class="col-sm-9">
						<input class="form-control" id="fphone" type="text" name="phone"
							placeholder="Phone Number" value="${fn:escapeXml(phone)}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">Alternative Number (optional)</label>
					<div class="col-sm-9">
						<input class="form-control" type="text" name="phonealt"
							placeholder="Alternative Number (optional)"
							value="${fn:escapeXml(phone2)}">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">Grade</label>
					<div class="col-sm-9">
						<select class="form-control" id="fgrade" name="grade">
							<option>Select a grade</option>
							<option>9</option>
							<option>10</option>
							<option>11</option>
							<option>12</option>
						</select>
					</div>
				</div>

				<%
					if (kuser != null) {
				%>
				<script>
					$("#fgrade").val("${fn:escapeXml(grade)}");
				</script>
				<%
					}
				%>

				<div class="checkbox text-left">
					<label>
						<input id="fagree"
							type="checkbox" name="agree"> I agree to the terms listed
							above
					</label>
				</div><br/>
				<button type="submit" class="btn btn-success">Submit</button>

			</form>
			</div>
			<div class="col-md-10 well center">
				<h2>My Events</h2>
				<p class="leftalign" id="muevents"></p>
				<h2>Past Events</h2>
				<p class="leftalign" id="mpevents"></p>
			</div>
			<%
				}
			%>
		</div>

		<div class="row" id="discover">
			<div class="col-md-10 center">
				<img class="img-thumbnail" src="img/img_BG.png" /><br /> <br />
				<blockquote>
				  <p>Where your heart to serve unites with your call to lead.</p>
				</blockquote>
				<p class="col-md-7 center lead">We are Memorial's local chapter of Key Club
					International, a 100% student-run volunteer organization dedicated
					to providing service and building character.</p>
				<h4>Student Leaders</h4>
				<table class="table table-hover mediumbox text-left">
					<thead>
						<tr>
							<th>Name</th>
							<th>Position</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>Anthony Xu</td>
							<td>President</td>
						</tr>
						<tr>
							<td>Caius Selheimer</td>
							<td>Vice President</td>
						</tr>
						<tr>
							<td>Rajat Mehndiratta</td>
							<td>Secretary</td>
						</tr>
						<tr>
							<td>Sina Ghadiri</td>
							<td>Assistant Secretary</td>
						</tr><tr>
							<td>Rebekah Kim</td>
							<td>Treasurer</td>
						</tr>
						<tr>
							<td>Kasra Ghadiri </td>
							<td>Recruitment Officer</td>
						</tr>
						<tr>
							<td>Phoebe Chi Nguyen</td>
							<td>Human Resources Officer</td>
						</tr>
						<tr>
							<td>Andrew Liu</td>
							<td>Webmaster</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
				
		<div class="row" id="serve">
			<div class="col-md-10 well center leftalign" id="eventView">
				<h2 class="loadingClear2" id="meventTitle"></h4>
				<span id="meventPic"></span><br/>
				<span class="loadingClear2" id="meventDesc"></span><br /> <br/>
				<b>Location: </b> <span class="loadingClear2" id="meventLoc"></span><br />
				<b>Hours: </b> <span class="loadingClear2" id="meventHours"></span><br />
				<b>Start Time: </b> <span class="loadingClear2" id="meventSdate"></span><br />
				<b>End Time: </b> <span class="loadingClear2" id="meventEdate"></span><br />
				<b>Quota: </b> <span class="loadingClear2" id="meventQuota"></span><br />
				<b>Attending: </b> <span class="loadingClear2" id="meventUsers"></span><br />
				<br/>
				<p id="meventActions"></p>
			</div>
			<br/>
			<div class="col-md-10 center">
				<h2>Upcoming Events</h2>
				<p class="leftalign" id="uevents"></p>
				<h2>Past Events</h2>
				<p class="leftalign" id="pevents"></p>
			</div>
		</div>
		<hr>
		<div class="footer">
			<p>&copy; Wynd 2013</p>
		</div>
	</div>
</body>
</html>