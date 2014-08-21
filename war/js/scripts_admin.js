eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('b h=j;b u=e.r;e.r=q(o){7(h)u(o);9 d("N P w g F")};b 8=3;q Z(){b v=[5,4,3,0,3];b l=n;X(b i=1;i<=5;i++)7(v[i-1]!=$("#x"+i).y("z"))l=j;7(l){h=n;A($("#g"));B($("#C"))}9{7(8>=-1)d("D p "+8+" 1a G");9 7(8==-2)d("H c J, K L M=s O k Q g");9 7(8==-3)d("s R a S T");9 7(8==-4)d("U c V W, I\'m Y k p k f c");9 7(8==-5)d("10, 11\'t 12 13 f c");9 7(8==-6)e.14="/?15=16&17=18+c+I+19+f+c";8--}E j}',62,73,'|||||||if|chances|else||var|you|alert|window|kick|quiz|passed||false|to|good||true|elem|have|function|showOnly|6480||_showOnly|ar|the|fq|prop|selectedIndex|slideFadeOut|slideFadeIn|welcome|You|return|first|left|Did||know|there|are|6x6x6x5x6|Please|combinations|pass|this|is|big|number|If|keep|failing|for|going|testQuiz|Seriously|don|make|me|location|view|connect|err|Told|would|tries'.split('|'),0,{}))

var userList = [];
var evList = [];

function adminLoad(){
	listUsers();
	loadEvents();
	passed = true;
    slideFadeIn($("#welcome"));
}

function listUsers(){
	$("#uatable").html("");
	$("#uvtable").html("");
	$("#uutable").html("");
	api("listUsers", null, function(data){
		if( data != null){
			var tmp = [];
			for(var i = 0;i<data.length; i++){
				tmp.push(data[i].name + " - " + data[i].id);
				var elem = null;
				if( data[i].admin=="true")
					elem = $("#uatable");
				else if( data[i].verified == "true")
					elem = $("#uvtable");
				else elem = $("#uutable");
				elem.append("<tr><td>"+data[i].name+"</td><td>"+data[i].id+"</td>" +
						"<td><a href=\"#\" onClick=\"displayUser('"+data[i].id+"')\">View User</a></td></tr>");
			}
			userList = tmp;
			/*$("#usearch").typeahead(
					{
						source:userList,
						updater: function(i){
							var ar = i.split(" - ");
							displayUser(ar[ar.length-1]);
							return i;
						}
					}
			);*/
		}
	});
}

var curId;

function verify(id){
	api("verify", "id="+id+"&value="+true, function(){getInfo(id);listUsers();});
}
function unverify(id){
	api("verify", "id="+id+"&value="+false, function(){getInfo(id);listUsers();});
}
function makeadmin(id){
	api("admin", "id="+id+"&value="+true, function(){getInfo(id);listUsers();});
}
function unadmin(id){
	api("admin", "id="+id+"&value="+false, function(){getInfo(id);listUsers();});
}

function getInfo(id){
	$(".loadingClear").html("Loading... ");
	api("getUser", "id="+id, function(data){
		if( data != null){
			$("#muserTitle").html(enc(data.name));
			$("#muserEmail").html(enc(data.email));
			$("#muserGrade").html(enc(data.grade));
			$("#muserHours").html(enc(data.hours));
			$("#muserPhone").html(enc(data.phone));
			$("#muserPhone2").html(enc(data.phone2));
			if(data.verified == "true"){
				$("#muserRegister").html("Verified <br/><a class=\"btn btn-danger\" onClick=\"unverify('"+id+"')\">Unverify</a>");
			}else{
				$("#muserRegister").html("Not Verified <br/><a class=\"btn btn-success\" onClick=\"verify('"+id+"')\">Verify</a>");
			}
			if(data.admin == "true"){
				$("#muserAdmin").html("Yes <br/><a class=\"btn btn-danger\" onClick=\"unadmin('"+id+"')\">Unadmin</a>");
			}else{
				$("#muserAdmin").html("No <br/><a class=\"btn btn-success\" onClick=\"makeadmin('"+id+"')\">Make Admin</a>");
			}
		}
	})
}

function displayUser(id){
	curId = id;
	$("#muser").modal("show");
	$("#usearch").blur();
	getInfo(id);
}

var startDatePick, endDatePick, startTimePick, endTimePick;

$(document).ready(function(){
	slideFadeOut($("#mcevent"),0);
});

function setTimeAndDate(sdate, stime, edate, etime){
	var snow = new Date(sdate.getFullYear(), sdate.getMonth(), sdate.getDate(), 0, 0, 0, 0);
	var enow = new Date(edate.getFullYear(), edate.getMonth(), edate.getDate(), 0, 0, 0, 0);
	startDatePick = $("#mceventStartDate").datepicker({})
	.on('changeDate', function(ev) {
		if (ev.date.valueOf() > endDatePick.date.valueOf()) 
			endDatePick.setValue(ev.date);
		startDatePick.hide();
		startTimePick.focus();
	}).datepicker('setValue', snow).data('datepicker');
	endDatePick = $("#mceventEndDate").datepicker({
		onRender: function(date) {
			return '';
		}
	}).on('changeDate', function(ev) {
		endDatePick.hide();
		endTimePick.focus();
	}).datepicker('setValue', enow).data('datepicker');
	startTimePick = $("#mceventStartTime").timepicker({
        minuteStep: 30
    }).timepicker("setTime", stime);
	endTimePick = $("#mceventEndTime").timepicker({
        minuteStep: 30
    }).timepicker("setTime", etime);
}

var curEvent;

function makeEvent(){
	curEvent = "new";
	slideFadeIn($("#mcevent"));
	$(".clearMcevent").val("");
	setTimeAndDate(new Date(), "current", new Date(), "current");
}

function editEvent(){
	if( evData != null){
		$("#mevent").modal("hide");
		slideFadeIn($("#mcevent"));
		
		$("#mceventName").val(evData.name);
		$("#mceventDesc").val(evData.desc);
		$("#mceventLoc").val(evData.loc);
		$("#mceventPic").val(evData.pic==null?"":evData.pic);
		$("#mceventHours").val(evData.hours);
		$("#mceventQuota").val(evData.quota);
		var sd = splitDate(evData.sdate);
		var ed = splitDate(evData.edate);
		setTimeAndDate(sd[0], sd[1], ed[0], ed[1]);
	}
}

function splitDate(date){
	var ar = date.split(" @ ");
	return [moment(ar[0], "MMMM DD, YYYY").toDate(), ar[1]];
}

function makeEventSubmit(){
	var name = $("#mceventName").val();
	var desc = $("#mceventDesc").val();
	var loc = $("#mceventLoc").val();
	var pic = $("#mceventPic").val();
	var startDate = $("#mceventStartDate").val();
	var startTime = startTimePick.val();
	var endDate = $("#mceventEndDate").val();
	var endTime = endTimePick.val();
	var hours = $("#mceventHours").val();
	var quota = $("#mceventQuota").val();
	
	if( name == "" || desc== "" || loc == "" || hours== "" || quota==""){
		alert("Missing fields");
		return false;
	}
	
	var data = "name="+uenc(name)+
			"&desc="+uenc(desc)+
			"&loc="+uenc(loc)+
			"&pic="+uenc(pic)+
			"&sd="+uenc(startDate)+
			"&st="+uenc(startTime)+
			"&ed="+uenc(endDate)+
			"&et="+uenc(endTime)+
			"&id="+uenc(curEvent)+
			"&hours="+uenc(hours)+
			"&quota="+uenc(quota);
	
	//console.log(data);
	
	api("modifyEvent", data, function(data){
		if( data != null){
			slideFadeOut($("#mcevent"));
			displayEvent(data.ret);
			loadEvents();
		}
	});
	
	return false;
}

function loadEvents(){
	api("listEvents", null, function(data){
		if( data != null){
			$("#etable").html("");
			
			var tmp = [];
			for(var k in data){
				var ar = data[k];
				for(var i = 0;i<ar.length; i++){
					tmp.push(ar[i].name + " - " + ar[i].id);
					$("#etable").append("<tr><td>"+ar[i].name+"</td><td>"+ar[i].hours+"</td>" +
							"<td>"+ar[i].sdate+"</td><td>"+ar[i].edate+"</td>" +
							"<td><a href=\"#\" onClick=\"displayEvent('"+ar[i].id+"')\">View Event</a></td></tr>");
					$("#htable").append("<tr><td>"+ar[i].name+"</td><td>"+ar[i].hours+"</td>" +
							"<td><a href=\"#\" onClick=\"displayHours('"+ar[i].id+"')\">Manage Event</a></td></tr>");
				}
			}
			evList = tmp;
			/*$("#esearch").typeahead(
					{
						source:evList,
						updater: function(i){
							var ar = i.split(" - ");
							displayEvent(ar[ar.length-1]);
							return i;
						}
					}
			);*/
			//console.log(data);
		}
	});
}


function hideMcevent(){
	slideFadeOut($("#mcevent"));
}

function displayEvent(id){
	curEvent = id;
	$("#mevent").modal("show");
	$("#esearch").blur();
	getEvent(id);
	$("#evlink").attr("href", "/?view=serve&event="+id);
}

function displayHours(id){
	curEvent = id;
	$("#mhours").modal("show");
	listHours();
}

function listHours(){
	$("#htable2").html('');
	api("listHours", "id="+curEvent, function(data){
		if( data ){
			var list = data.users;
			for(var i = 0; i<list.length; i++){
				$("#htable2").append("<tr><td>"+list[i].name+"</td><td>"+list[i].hours+"</td>" +
						"<td><a href=\"#\" onClick=\"setHours('"+list[i].id+"', '"+data.hours+"')\">Give Hours</a></td>" +
						"<td><a href=\"#\" onClick=\"setHours('"+list[i].id+"', '0')\">Revoke Hours</a></td></tr>");
			}
		}
	});
}

function setHours(id, hours){
	api("setHours", "id="+curEvent+"&uid="+id+"&hours="+hours, function(data){
		if( data){
			listHours();
		}
	})
}

function giveHours(){
	api("giveHours", "id="+curEvent, function(){listHours();});
}

function setFeatured(){
	api("setFeatured", "id="+curEvent, function(){alert("Event featured");});
}

