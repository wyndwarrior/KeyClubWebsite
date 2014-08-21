function validatef(){
	if( $("#fphone").val().trim() == "" ){
		alert("Phone number empty");
		return false;
	}
	if( $("#fgrade").val() == "Select a grade" ){
		alert("Please select a grade");
		return false;
	}
	if( !$("#fagree").prop("checked") ){
		alert("Please agree to the terms");
		return false;
	}
	return true;
}

var DEF_DUR = 750;

function slideFadeIn(elem, dur){
	if( elem.css('display') == 'none' )
		elem.stop(true, true)
			.fadeIn({ duration: dur, queue: false })
			.css('display', 'none')
			.slideDown(dur);
}

function slideFadeOut(elem, dur){
	if( elem.css('display') != 'none' )
		elem.stop(true, true)
			.fadeOut({ duration: dur, queue: false })
			.slideUp(dur);
}

function show(elem, dur){
	if( elem=="#home" )
		slideFadeIn($("#container"), dur);
	slideFadeIn($(elem), dur);
}
function hide(elem, dur){
	if( elem=="#home" )
		slideFadeOut($("#container"), dur);
	slideFadeOut($(elem), dur);
}

function showOnly(elem){
	showOnly(elem, DEF_DUR);
}

var evList;
var myList = [];
var featuredEvent;

function inEvent(ev){
	for(var i = 0; i<myList.length; i++){
		if( myList[i].id == ev)
			return true;
	}
	return false;
}

function add(elem, ar, earn){
	elem.html("");
	for(var i= 0; i<ar.length; i++){
		elem.append("<h4>"+ar[i].name+"</h4>");
		elem.append("<b>When:</b> from <b>" + 
				ar[i].sdate + "</b> to " + ar[i].edate + "<br/>");
		elem.append("<b>Hours: </b>" + ar[i].hours + "<br/>");
		if( inEvent(ar[i].id) && earn){
			elem.append("<b>Hours Earned: </b>" + ar[i].earned + "<br/>");
		}
		if( !inEvent(ar[i].id)){
			elem.append("<center><a href=\"#\" class=\"btn btn-primary\" onClick=\"viewEvent('"+ar[i].id+"')\">View Event</a></center>");
		}else{
			elem.append("<b>Status:</b> Attending<br/>");
			elem.append("<center> <a class=\"btn btn-primary\" onClick=\"viewEvent('"+ar[i].id+"')\">View Event</a> "+ 
					"<a class=\"btn btn-danger\" onClick=\"quitEvent('"+ar[i].id+"')\">Leave Event</a></center>");
		}
		elem.append("<br/><br/>");
	}
	if( ar.length == 0)
		elem.html("None");
}

function viewEvent(id){
	showOnly("serve");
	slideFadeIn($("#eventView"));
	getEvent(id);
}

var evData;

function getEvent(id){
	$(".loadingClear2").html("Loading... ");
	api("getEvent", "id="+id, function(data){
		if( data != null){
			evData = data;
			$("#meventTitle").html(enc(data.name));
			$("#meventDesc").html(enc(data.desc));
			if( data.pic != null){
				$("#meventPic").show(0);
				$("#meventPic").html("<img class=\"img-thumbnail\" src=\""+data.pic+"\" /><br/><br/>");
			}else{
				$("#meventPic").hide(0);
			}
			$("#meventLoc").html(enc(data.loc));
			$("#meventHours").html(enc(data.hours));
			$("#meventSdate").html(enc(data.sdate));
			$("#meventEdate").html(enc(data.edate));
			$("#meventQuota").html(enc(data.quota=="0"?"Unlimited":(data.users.length+"/"+data.quota)));
			$("#meventUsers").html("");
			for(var i = 0; i<data.users.length; i++)
				$("#meventUsers").append(data.users[i].name + (i==data.users.length-1?"":", "));
			if( data.users.length == 0 )
				$("#meventUsers").html("None");
			if( !inEvent(data.id)){
				if( loggedIn)
					$("#meventActions").html("<a class=\"btn btn-success\" onClick=\"joinEvent('"+data.id+"')\">Join Event</a>");
				else
					$("#meventActions").html("<a class=\"btn btn-success\" onClick=\"showOnly('connect')\">Join Event</a>");
			}else{
				$("#meventActions").html("<a class=\"btn btn-danger\" onClick=\"quitEvent('"+data.id+"')\">Leave Event</a>");
			}
		}
	});
}

function joinEvent(id){
	eventAction("joinEvent", id);
}

function quitEvent(id){
	eventAction("quitEvent", id);
}

function eventAction(action, id){
	api(action, "id="+id, function(data){
		if( data != null){
			listMyEvents();
			setTimeout(function(){getEvent(id)}, 300);
		}
	});
}

var loggedIn = false;

function listMyEvents(){
	api("myEvents", null, function(data){
		if( data != null){
			if( data.ret == "false")
				loggedIn = false;
			else{
				loggedIn = true;
				myList = [];
				for(var k in data)
					for(var i = 0; i <data[k].length; i++)
						myList.push(data[k][i]);
				add($("#muevents"), data.upcoming, true);
				if( data.upcoming.length == 0)
					$("#muevents").html("<center><a href=\"#\" class=\"btn btn-primary\" onClick=\"showOnly('serve')\">Sign up for events</a></center>");
				add($("#mpevents"), data.past, true);
			}
			listEvents();
		}
	});
}

function listEvents(){
	api("listEvents", null, function(data){
		if( data != null){
			evList = data;
			add($("#uevents"), data.upcoming, false);
			add($("#pevents"), data.past, false);
		}
	});
	api("getFeatured", null, function(data){
		if( data != null && data != "false"){
			$("#featuredTitle").text(data.name);
			$("#featuredDate").html(data.sdate);
			var s = data.desc;
			if( s.length > 175)
				s = s.substring(0, s.lastIndexOf(" ", 175))+"...";
			$("#featuredDesc").text(s);
			featuredEvent = data.id;
		}
	});
}

function viewFeatured(){
	viewEvent(featuredEvent);
}

function showOnly(elem, dur){
	var ar = ["home", "connect", "discover", "serve", "userman", "eventman", "hourman"];
	for(var i = 0; i<ar.length; i++)
		if( elem == ar[i] ){
			show("#"+ar[i], dur);
			$("#_"+ar[i]).addClass("active");
		}else {
			hide("#"+ar[i], dur);
			$("#_"+ar[i]).removeClass("active");
		}
}


$(document).ready(ready);
$(document).ready(function(){
	listMyEvents();
	slideFadeOut($("#eventView"), 0);
	htmlReady();
});

var scene = new THREE.Scene();
var camera = new THREE.PerspectiveCamera(45, window.innerWidth/window.innerHeight*2, 0.01, 10000);
var wgl = Detector.webgl;
var renderer = wgl? new THREE.WebGLRenderer({ antialias: true}): new THREE.CanvasRenderer();
var cube;
var lcd;
var light;

var mouseX=1, mouseY=1;

$(document).mousemove(function(e) {
	  mouseX = e.pageX/window.innerWidth;
	  mouseY = e.pageY/window.innerHeight;
});

function init(){
	renderer.setSize(window.innerWidth, window.innerHeight/2);
    
	renderer.shadowMapEnabled = true;

    renderer.shadowCameraFov = 50;
    renderer.shadowMapWidth = 1024;
    renderer.shadowMapHeight = 1024;
	
    renderer.setClearColor(0xffffff, 1);
	$("#container").append(renderer.domElement);
	
    camera.position.set(0,0,60);
	
	light = new THREE.SpotLight( 0xffffff);
    light.castShadow = true;
    light.position.set( lcd.px*4, lcd.py, 90 );
    scene.add(light);
    
    var light2 = new THREE.DirectionalLight( 0xffffff);
    if( wgl ) light2.position.set( lcd.px*5, lcd.py, 90 );
    else light2.position.set( lcd.px*2, lcd.py, 70 );
    scene.add(light2);
    
}

var fps = 0;
var last = 0;

function render(t) {
	requestAnimationFrame(render);
	
	TWEEN.update();

    light.position.set( lcd.px*2*(2*mouseX-1) +lcd.px, lcd.py*3*(-2*mouseY+1)+lcd.py, 90 );
	
	renderer.render(scene, camera);
	
	if( Math.floor(t/1000) != last){
		last = Math.floor(t/1000);
		//console.log(fps);
		fps = 0;
	}
	fps++;
	
};



function startAnimation(){
	for(var i = 0; i<lcd.cubes.length; i++)
		new FlyIn(lcd.cubes[i], 0).tween.start();

	var pos = { x: -50, y: 30, z:-20};
	var end = { x: lcd.px, y: -lcd.py, z: 65 };
	var t1 = new TWEEN.Tween(pos).to(end, 8000);
	t1.easing(TWEEN.Easing.Quadratic.Out);
	t1.onUpdate(function(){
		camera.position.set(pos.x, pos.y, pos.z);
		camera.lookAt(new THREE.Vector3(lcd.px,lcd.py,0));
	});
	t1.start();

	var pos2 = { z:-15};
	var end2 = { z:-3 };
	var t2 = new TWEEN.Tween(pos2).to(end2, 7500);
	t2.easing(TWEEN.Easing.Cubic.In);
	t2.onUpdate(function(){
		lcd.plane.position.z = pos2.z;
	});
	t2.start();

}


function matrixAppend(m1, m2){
	for(var i = 0; i<m2.length; i++)
		for(var j = 0; j<m2[i].length; j++)
			m1[i].push( m2[i][j] );
}

var letters = {};
letters['m'] = [[1,0,0,0,1],
                [1,1,0,1,1],
                [1,0,1,0,1],
                [1,0,0,0,1],
                [1,0,0,0,1]];
letters['h'] = [[1,0,1],
               [1,0,1],
               [1,1,1],
               [1,0,1],
               [1,0,1]];
letters['s'] = [[1,1,1],
                [1,0,0],
                [1,1,1],
                [0,0,1],
                [1,1,1]];
letters['k'] = [[1,0,1],
                [1,1,0],
                [1,0,0],
                [1,1,0],
                [1,0,1]];
letters['c'] =[[1,1,1],
               [1,0,0],
               [1,0,0],
               [1,0,0],
               [1,1,1]];

letters[' '] = [[0],[0],[0],[0],[0]];

var fly;

function ready(){
	
	var mat = [[],[],[],[],[]];
	var str = "m k c";
	for(var i = 0; i<str.length; i++)
		matrixAppend(mat, letters[str.charAt(i)]);
	lcd = new LCDMatrix(mat);
	
	init();
	lcd.addToScene(scene);
	light.target = lcd.plane;
	
	startAnimation();
	
	render(0);
}

//class Cube{

var CUBE_SIZE = 5;

function Cube( _x,_y,_z,_color ){
	this.x = _x;
	this.y = _y;
	this.z = _z;
	this.color = _color;
	
	this.cube = new THREE.Mesh(
			new THREE.CubeGeometry(CUBE_SIZE,CUBE_SIZE,CUBE_SIZE),
			new THREE.MeshLambertMaterial({color: this.color})
	);
	this.cube.castShadow = true;
	
	this.setPos(this.x,this.y,this.z);
}

Cube.prototype.setPos = function(_x,_y,_z){
	this.x = _x;
	this.y = _y;
	this.z = _z;
	this.cube.position.set(this.x+CUBE_SIZE/2,
			this.y+CUBE_SIZE/2,
			this.z+CUBE_SIZE/2);
}

Cube.prototype.addToScene = function(scene){
	scene.add(this.cube);
}


//} class Cube;


//class LCDMatrix {

var CUBE_GAP = CUBE_SIZE/5;

function LCDMatrix( mat ){
	this.m = mat;
	this.cubes = [];
	
	var r = this.m.length;
	var c = this.m[0].length;
	
	for(var i = 0; i<r; i++)
		for(var j = 0; j<c; j++)
			if( this.m[i][j] == 1){
				var cube = new Cube(
						j*(CUBE_GAP + CUBE_SIZE),
						(r-i-1) * (CUBE_GAP + CUBE_SIZE),
						0,
						0xC9EDFF
				);
				this.cubes.push(cube);
			}
	
	var pHeight = (r+2)*(CUBE_GAP + CUBE_SIZE) + CUBE_GAP;
	var pWidth  = (c+3)*(CUBE_GAP + CUBE_SIZE) + CUBE_GAP;
	this.px = (c*(CUBE_GAP + CUBE_SIZE) + CUBE_GAP)/2;
	this.py = (r*(CUBE_GAP + CUBE_SIZE) + CUBE_GAP)/2;
	
	this.plane = new THREE.Mesh(
	        new THREE.PlaneGeometry(pWidth, pHeight), 
	        new THREE.MeshLambertMaterial({color: 0x00aaff}));
	
	this.plane.position.set(this.px, this.py, -15);
	this.plane.receiveShadow = true;
	
}

LCDMatrix.prototype.addToScene = function(scene){
	for(var i = 0; i<this.cubes.length; i++)
		this.cubes[i].addToScene(scene);
	scene.add(this.plane);
}

//} class LCDMatrix;


//class FlyIn{

function FlyIn(cube, delay){
	this.cube = cube;
	this.delay = delay;
	var done = false;

	var pos = { x:cube.x+(Math.random()<0.5?1:-1)*(20+50*Math.random()),
				  y:cube.y+(Math.random()<0.5?1:-1)*(20+20*Math.random()), 
				  z:cube.z+20+20*Math.random(),
				  rotx: -3-(Math.random()*3),
				  roty: -3-(Math.random()*3)};
	var end = { x:cube.x, y:cube.y, z:cube.z,rotx:0, roty:0 };
	
	this.tween = new TWEEN.Tween(pos).to(end, 5000);
	this.tween.easing(TWEEN.Easing.Bounce.Out);
	this.tween.delay(Math.random()*1000);
	
	var updating = function(){
		cube.setPos(
				pos.x, 
				pos.y, pos.z);
		cube.cube.rotation.x = pos.rotx;
		cube.cube.rotation.y = pos.roty;
	};
	
	this.tween.onUpdate(updating);
	updating();
}


//} class FlyIn;


function enc(txt){
	return $('<div/>').text(txt).html();
}

function uenc(txt){
	return encodeURIComponent(txt);
}


function api(cmd, data, callb){
    $.ajax({
    		type: 'POST', 
            url: 'api',
            async: true,
            cache: false,
            dataType: 'json',
            data: "action="+cmd + (data!=""&&data!=null ? "&"+data : ""), 
            success: function(data, textStatus, jqXHR){
            	if (typeof data === "undefined" || data == null){
            		callb(null);
            		alert("Request failed");
            		return;
            	}
            	var err = data.error;
            	if (typeof err === "undefined") 
            		callb(data);
            	else{
            		callb(null);
            		alert("Error: " + err);
            	}
            }
        });
}

