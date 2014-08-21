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
	if( kuser == null || !kuser.isAdmin())
		MHSKeyClubServlet.error(response, "It looks like you're not an admin", "connect");
%>
<!DOCTYPE html>
<html>
<head>
<title>Admin page</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/datepicker.css">
<link rel="stylesheet" type="text/css" href="css/bootstrap-timepicker.min.css">
<script src="js/jquery-1.10.1.min.js"></script>
<script src="js/jquery-migrate-1.2.1.min.js"></script>
<script src="js/moment.min.js"></script>
<script src="js/three.min.js"></script>
<script src="js/Detector.js"></script>
<script src="js/tween.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-datepicker.js"></script>
<script src="js/bootstrap-timepicker.min.js"></script>
<script src="js/scripts.js"></script>
<script src="js/scripts_admin.js"></script>
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
		slideFadeOut($("#welcome"),0);
		<%
		String viewReq = request.getParameter("view");
					if (viewReq == null)
						viewReq = "home";
		%>
		_showOnly('<%=viewReq%>', 0);
		adminLoad();
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
					<li id="_userman"><a href="#" onClick="showOnly('userman')">Manage Users</a></li>
					<li id="_eventman"><a href="#" onClick="showOnly('eventman')">Manage Events</a></li>
					<li id="_hourman"><a href="#" onClick="showOnly('hourman')">Manage Hours</a></li>
				</ul>
				<%
						pageContext.setAttribute("name", u.getName());
				if( kuser != null && kuser.getEmail()!= null)
					pageContext.setAttribute("email", kuser.getEmail());
				else
					pageContext.setAttribute("email", u.getEmail());
				%>
				<ul class="nav navbar-nav navbar-right">
					<li id="_connect"><a>Hello,
							${fn:escapeXml(name)} </a></li>
				</ul>
			</div>
		</div>
	</div>


	<div class="container" id="content">
	<br/>
	
		<% 
		String msgErr = request.getParameter("err"),
			msgSucc = request.getParameter("succ");
		if( msgErr != null){
			pageContext.setAttribute("msgErr", msgErr);
		%>
		<div class="alert alert-error">${fn:escapeXml(msgErr)}</div>
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
			<!--div id="quiz" class="span5 center box">
				<form class="form-horizontal" 
				onSubmit="return testQuiz()">
				<h2>Welcome Admin</h2>

				<p class="leftalign">
					<b>To make sure you're a true admin, you will be required to answer the following questions<br />
				</p>
				
				<div class="control-group">
					<label class="control-label">What is your sat score?</label>
					<div class="controls">
						<select id="fq1">
							<option>Select an answer</option>
							<option>0</option>
							<option>600</option>
							<option>1500</option>
							<option>2400</option>
							<option>Over 9000</option>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">How are you today?</label>
					<div class="controls">
						<select id="fq2" >
							<option>Select an answer</option>
							<option>Good</option>
							<option>Awesome!</option>
							<option>Under the weather</option>
							<option>Very carefully</option>
							<option>Yes</option>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">Who am I?</label>
					<div class="controls">
						<select id="fq3">
							<option>Select an answer</option>
							<option>Magic</option>
							<option>An AI from the future aiming for world domination</option>
							<option>A Potato</option>
							<option>Andrew Liu</option>
							<option>This is also a trick question</option>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">What is the probability that you will answer this question correctly?</label>
					<div class="controls">
						<select id="fq4">
							<option>Select an answer</option>
							<option>25%</option>
							<option>50%</option>
							<option>0%</option>
							<option>25%</option>
						</select>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label">Why is the sky blue?</label>
					<div class="controls">
						<select id="fq5">
							<option>Select an answer</option>
							<option>Chemicals such as N2, O3, and CO2 add to a blue hue</option>
							<option>Hue... huehuehuehue</option>
							<option>The blue sky symbolizes melancholy and the clouds looming ominously foreshadow danger</option>
							<option>H2O particles in the air refract blue light</option>
							<option>I googled the answer</option>
						</select>
					</div>
				</div>

				<button type="submit" class="btn btn-success">Ok, I'm done</button>

			</form>
			</div-->
			
			<div id="welcome" class="col-md-9 center well">
				<h2>Welcome Admin</h2>
				<p>
				To manage users or events, use the tabs at the top of the page.
				</p>
			</div>
		</div>
			
		<div id="userman" class="col-md-9 center well">
			<h2>User Management</h2><br/>
			<br/>
			<h3>Unverified</h3><br/>
			<table class="table table-hover text-left">
				<thead>
					<tr>
						<th>Name</th>
						<th>ID</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody id="uutable">
				</tbody>
			</table>
			<h3>Verified</h3>
			<br/>
			<table class="table table-hover text-left">
				<thead>
					<tr>
						<th>Name</th>
						<th>ID</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody id="uvtable">
				</tbody>
			</table>
			<h3>Admin</h3>
			<br/>
			<table class="table table-hover text-left">
				<thead>
					<tr>
						<th>Name</th>
						<th>ID</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody id="uatable">
				</tbody>
			</table>
		</div>
		
		<div id="eventman" class="col-md-9 center well">
			<h2>Event Management</h2><br/>
			<a onClick="makeEvent()" class="btn btn-primary">Create Event</a><br/><br/>
			
			<div id="mcevent">
				<form class="form-horizontal" role="form" onSubmit="return makeEventSubmit()">
					<div class="form-group">
						<label class="col-sm-3 control-label">Event Name</label>
						<div class="col-sm-9">
							<input class="clearMcevent form-control" id="mceventName" type="text"
								placeholder="Name">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">Event Description</label>
						<div class="col-sm-9">
							<textarea class="clearMcevent form-control" id="mceventDesc" rows="8"></textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">Location</label>
						<div class="col-sm-9">
							<input class="clearMcevent form-control" id="mceventLoc" type="text"
								placeholder="Location">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">Picture URL (optional)</label>
						<div class="col-sm-9">
							<input class="clearMcevent form-control" id="mceventPic" type="text"
								placeholder="Picture">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">Start Date</label>
						<div class="col-sm-9">
							<input class="form-control" id="mceventStartDate" type="text"
								placeholder="Select Date" >
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">Start Time</label>
						<div class="col-sm-9">
							<div class="bootstrap-timepicker">
								<input class="form-control" id="mceventStartTime" type="text" class="input-small">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">End Date</label>
						<div class="col-sm-9">
							<input id="mceventEndDate" class="form-control" type="text"
								placeholder="Select Date">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">End Time</label>
						<div class="col-sm-9">
							<div class="bootstrap-timepicker">
								<input class="form-control" id="mceventEndTime" type="text" class="input-small">
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">Hours</label>
						<div class="col-sm-9">
							<input class="clearMcevent form-control" id="mceventHours" type="text"
								placeholder="Hours">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">Quota (0 for infinite)</label>
						<div class="col-sm-9">
							<input class="clearMcevent form-control" id="mceventQuota" type="text"
								placeholder="Quota">
						</div>
					</div>
					<a class="btn btn-default" onClick="hideMcevent()">Cancel</a>
					<button type="submit" class="btn btn-success">Submit</button>

				</form>
			</div>
			<br/>
			<table class="table table-hover text-left">
				<thead>
					<tr>
						<th>Name</th>
						<th>Hours</th>
						<th>Start Time</th>
						<th>End Time</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody id="etable">
				</tbody>
			</table>
		</div>
		
		<div id="hourman" class="col-md-9 center well">
		<h2>Hours Management</h2><br/>
		<br/>
		<h4>Clear Hours</h4>
		<p>This will clear all hours up to a given date. All hours given before the set date will be revoked. Please use with caution.</p>
		<div class="bootstrap-timepicker">
			<input class="form-control" id="hoursDeleteDate" type="text" class="input-small">
		</div><br/>
		<a href="#" id="hoursDeleteBtn" class="btn btn-danger">CLEAR ALL HOURS BEFORE THIS DATE</a><br/><br/>
		<table class="table table-hover text-left">
			<thead>
				<tr>
					<th>Name</th>
					<th>Hours</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="htable">
			</tbody>
		</table>
	</div>
		
	<div id="mhours" class="modal fade" tabindex="-1" role="dialog" >
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">×</button>
				<h3 id="mhoursTitle" class="loadingClear2" ></h3>
			</div>
			<div class="modal-body">
			<table class="table table-hover text-left">
			<thead>
				<tr>
					<th>Name</th>
					<th>Hours</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="htable2">
			</tbody>
		</table>
			</div>
			<div class="modal-footer">
				<a class="btn btn-success" onClick="giveHours()">Give All Hours</a>
				<button class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
	
		<div id="mevent" class="modal fade" tabindex="-1" role="dialog" >
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">×</button>
						<h3 id="meventTitle" class="loadingClear2" ></h3>
					</div>
					<div class="modal-body">
						<p>
						<h4>Information:</h4>
						<span class="loadingClear2" id="meventDesc"></span><br /> 
						<b>Location: </b> <span class="loadingClear2" id="meventLoc"></span><br />
						<b>Hours: </b> <span class="loadingClear2" id="meventHours"></span><br />
						<b>Start Time: </b> <span class="loadingClear2" id="meventSdate"></span><br />
						<b>End Time: </b> <span class="loadingClear2" id="meventEdate"></span><br />
						<b>Quota: </b> <span class="loadingClear2" id="meventQuota"></span><br />
						<b>Attending: </b> <span class="loadingClear2" id="meventUsers"></span>
						</p>
						<span id="meventPic"></span>
					</div>
					<div class="modal-footer">
						<a class="btn btn-default" onClick="setFeatured()">Set as Featured</a>
						<a class="btn btn-default" target="_blank" id="evlink">Link to event</a>
						<a class="btn btn-primary" onClick="editEvent()">Edit Event</a>
						<button class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>

		<div id="muser" class="modal fade" tabindex="-1" role="dialog" >
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">×</button>
						<h3 id="muserTitle" class="loadingClear" ></h3>
					</div>
					<div class="modal-body">
						<p>
						<h4>Contact Information:</h4>
						<b>Email: </b> <span class="loadingClear" id="muserEmail"></span><br /> 
						<b>Phone: </b> <span class="loadingClear" id="muserPhone"></span><br /> 
						<b>Alt. Phone: </b> <span class="loadingClear" id="muserPhone2"></span><br />
						<br />
						<h4>General Information:</h4>
						<b>Registration Status: </b> <span class="loadingClear" id="muserRegister"></span> <br /> 
						<b>Grade:</b> <span class="loadingClear" id="muserGrade"></span><br /> 
						<b>Hours: </b> <span class="loadingClear" id="muserHours"></span><br />
						<b>Admin: </b> <span class="loadingClear" id="muserAdmin"></span><br />
						</p>
					</div>
					<div class="modal-footer">
						<button class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
		
		<hr>
		<div class="footer">
			<p>&copy; Wynd 2014</p>
		</div>

	</div>
</body>
</html>