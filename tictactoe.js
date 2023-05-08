// Custom Wars Tactics - Tic-Tac-Toe
//
// This will create a single player and server based Tic-Tac-Toe that
// will also be shared with the Java counterpart. Both of these systems
// should identically be able to access the server.
//

// ----------------------
// Server variables
// ----------------------

// Necessary for all server communication
var request;

// -----------------------
// Engine variables
// -----------------------

// We will be able to control whether we are using interval or timeout
var intervalMode = false;
// The updating interval
var interval = null;
// The time it takes to update
var sec = 16;

// The x-axis of the mouse
var mouseX = 0;
// The y-axis of the mouse
var mouseY = 0;
// Processes clicks
var click = false;

// ------------------------
// Game variables
// ------------------------

// Holds the current game state
var gameState = 0;

// Holds the board position and box size
var boardX = 75;
var boardY = 75;//90
var boardBox = 50;

// Holds an int array of tick boxes
var tickBox = [0,0,0,0,0,0,0,0,0];
var tickToggle = true;
var basicNum = 0;

// Holds to see if someone won
var combo = [0,1,2,3,4,5,6,7,8,0,3,6,1,4,7,2,5,8,0,4,8,2,4,6];

// These are variables to handle game state
var startOfGame = true;

// -----------------------
// GAME CODE
// -----------------------

// ----------------------
// Game Loop
// ----------------------

// This is for initializing the game
function init(){
}

// This is for rendering the game
function render(ctx){

  // This shows the Current Mouse Position on the top
  drawMouseText(ctx, 4, 10);

  // DEBUG state
  if(gameState == -1){

    // Let's draw debug on the top
    drawButtonText(ctx, "DEBUG", 250, 10);

    // This draws the board
    drawBoard(ctx);
    drawSelection(ctx);

    // For fun, check the win state
    drawCheckWin(ctx);

    // This draws the button text widgets
    drawButtonText(ctx, "Reset", 0, 290);

    // Handle all click functionality here in a hierarchy
    if(click){
      click = tapResetButton(click);
      tapTestRender(click);
      click = false;
    }
  }

  // SINGLE PLAYER GAME State
  else if(gameState == 0){

    // This draws the board
    drawBoard(ctx);
    drawSelection(ctx);

    // This monitors if you won the game
    drawCheckWin(ctx);

    // This draws the various widgets
    drawButtonText(ctx, "Reset", 0, 290);

    // Handle all click functionality here in a hierarchy
    if(click){
      click = tapResetButton(click);
      click = tapGameBoard(click);
      click = false;
    }
  }
}

// -----------------------
// Game Logic Code
// -----------------------

// A function for handling the game board clicks
function tapGameBoard(click){
  for(var i = 0; i < 9; i++){
    if(mouseX > boardX+boardBox*(i%3) &&
      mouseX < boardX+boardBox*(i%3)+boardBox &&
      mouseY > boardY+boardBox*parseInt(i/3) &&
      mouseY < boardY+boardBox*parseInt(i/3)+boardBox){
        if(tickBox[i] == 0){
            tickBox[i] = tickToggle ? 1 : 2;
            tickToggle = !tickToggle;
            startOfGame = false;
        }
        return false;
    }
  }
  return click;
}

// A function that goes through board empty state, all Xs,
// and finally all Os via left mouse click
function tapTestRender(click){
  if(click){
    basicNum += 1;
    if(basicNum > 2)
      basicNum = 0;

    for(var i = 0; i < tickBox.length; i++){
      tickBox[i] = basicNum;
    }
  }
  return click;
}

// This function handles all the reset game functionality
function tapResetButton(click){

  // Sets up the functionality
  if(click && mouseY > 280 && mouseX < 100){
    // Set the variables to normal
    basicNum = 0;
    tickToggle = true;
    startOfGame = true;

    // Reset the board to normal
    for(var i = 0; i < 9; i++){
      tickBox[i] = 0;
    }

    return false;
  }
  return true;
}

// -----------------------
// Rendering Code
// -----------------------

// This draws the board
function drawBoard(ctx){
	ctx.fillStyle = "#FFFFFF";

	for(var i = 0; i < 9; i++){
		ctx.fillRect((boardX+boardBox*(i%3))+3, boardY+boardBox*(parseInt(i/3)+3), boardBox-7, boardBox-7);
	}

  ctx.beginPath();
	ctx.moveTo(boardX, boardY+boardBox);
	ctx.lineTo(boardX+boardBox*3, boardY+boardBox);
	ctx.moveTo(boardX, boardY+boardBox*2);
	ctx.lineTo(boardX+boardBox*3, boardY+boardBox*2);

	ctx.moveTo(boardX+boardBox, boardY);
	ctx.lineTo(boardX+boardBox, boardY+boardBox*3);
	ctx.moveTo(boardX+boardBox*2, boardY);
	ctx.lineTo(boardX+boardBox*2, boardY+boardBox*3);
	ctx.stroke();
}

// This function draws the selections that is in the tick box
function drawSelection(ctx){
	for(var i = 0; i < tickBox.length; i++){
		if(tickBox[i] == 1)
			drawX(ctx, i);
		else if(tickBox[i] == 2)
			drawO(ctx, i);
	}
}

// The function for drawing an X on the board
function drawX(ctx, index){
	ctx.beginPath();
	ctx.moveTo((boardX+boardBox*(index%3))+5, (boardY+boardBox*parseInt(index/3))+5);
	ctx.lineTo((boardX+boardBox*(index%3))+boardBox-5, (boardY+boardBox*parseInt(index/3))+boardBox-5);
	ctx.moveTo((boardX+boardBox*(index%3))+5, (boardY+boardBox*parseInt(index/3))+boardBox-5);
	ctx.lineTo((boardX+boardBox*(index%3))+boardBox-5, (boardY+boardBox*parseInt(index/3))+5);
	ctx.stroke();
}

// The function for drawing a Y on the board
function drawO(ctx, index){
	ctx.beginPath();
	ctx.arc((boardX+boardBox*(index%3))+(boardBox/2), (boardY+boardBox*parseInt(index/3))+(boardBox/2), (boardBox-10)/2, 0, 2*Math.PI);
	ctx.stroke();
}

// This checks to see if you won the game
function drawCheckWin(ctx){

  // Checks to see if you got a basic win
  for(var i = 0; i < combo.length; i+=3){
		if(tickBox[combo[i]] != 0 && tickBox[combo[i]] != 3 &&
			tickBox[combo[i]] == tickBox[combo[i+1]] &&
			tickBox[combo[i+1]] == tickBox[combo[i+2]]){
			if(tickBox[combo[i]] == 1){
        drawButtonText(ctx, "[X] Wins", 250, 290);
			}else if(tickBox[combo[i]] == 2){
        drawButtonText(ctx, "[O] Wins", 250, 290);
			}
			for(var j = 0; j < 9; j++){
				if(tickBox[j] == 0)
					tickBox[j] = 3;
			}
			drawWinLine(ctx);
			return;
		}
	}

  // Checks to see if you got a Cat's Game
  for(var i = 0; i < tickBox.length; i++){
		if (tickBox[i] == 0 || tickBox[i] == 3){
			break;
		}
		if(i == tickBox.length-1){
      drawButtonText(ctx, "Cat's Game", 225, 290);
		}
	}
}

// This draws a thick line across the game signifying the win
function drawWinLine(ctx){
	for(var i = 0; i < combo.length; i+=3){
		if(tickBox[combo[i]] != 0 && tickBox[combo[i]] != 3 && tickBox[combo[i]] == tickBox[combo[i+1]] && tickBox[combo[i+1]] == tickBox[combo[i+2]]){
			difNum = combo[i+2] - combo[i];

      // Horisontal Matches
			if(difNum == 2){
				ctx.strokeStyle = "#FF0000";
				ctx.beginPath();
				ctx.moveTo((boardX-5), (boardY+boardBox*parseInt(combo[i]/3))+(boardBox/2));
				ctx.lineTo((boardX+boardBox*3)+5, (boardY+boardBox*parseInt(combo[i]/3))+(boardBox/2));
				ctx.stroke();
			}
      // Vertical Matches
      else if(difNum == 6){
				ctx.strokeStyle = "#FF0000";
				ctx.beginPath();
				ctx.moveTo((boardX+boardBox*(combo[i]))+(boardBox/2), boardY-5)
				ctx.lineTo((boardX+boardBox*(combo[i]))+(boardBox/2), (boardY+boardBox*3)+5)
				ctx.stroke();
			}
      // Backslash Matches
      else if(difNum == 8){
				ctx.strokeStyle = "#FF0000";
				ctx.beginPath();
				ctx.moveTo(boardX-5, boardY-5)
				ctx.lineTo((boardX+boardBox*3)+5, (boardY+boardBox*3)+5)
				ctx.stroke();
			}
      // Forward Slash Matches
      else if(difNum == 4){
				ctx.strokeStyle = "#FF0000";
				ctx.beginPath();
				ctx.moveTo((boardX+boardBox*3)+5, boardY-5)
				ctx.lineTo(boardX-5, (boardY+boardBox*3)+5)
				ctx.stroke();
			}
		}
	}
}

// The function for drawing text to the screen
function drawButtonText(ctx, text, posx, posy){
  // Draws the text
  ctx.fillStyle = "#000000";
  ctx.fillText(text, posx, posy);
}

// ---------------------
// Generic Server Code
// ---------------------

// This is for handling the server requests
function respondObj(objt){}

// ----------------------
// ENGINE CODE
// ----------------------

// ---------------------
// Browser Window Code
// ---------------------

// Attempts to react when you load the window
window.onload = function(){
	run(sec);
}

function windowInit(){
  // Variables for making a full sized window
	var w = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
	var h = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;

	// Creates a new canvas if one isn't made already
	var canvas = document.getElementById("myCanvas");
	if(canvas == null){
		canvas = document.createElement("canvas");
		document.body.appendChild(canvas);
	}

	//Sets up the canvas if it isn't set up already
	canvas.setAttribute("id", "myCanvas");
	canvas.setAttribute("onmousemove", "getDimensions(event)");
	canvas.setAttribute("style", "position:absolute; border: 1px solid #d3d3d3;");
	canvas.setAttribute("onclick", "processClick(event)");
	canvas.innerHTML = "Your browser does not support the HTML5 canvas tag.";

  init()
}

// ----------------------
// System Loop
// ----------------------

// Gets the dimensions of the mouse
function getDimensions(event){
	mouseX = event.clientX - 8;
	mouseY = event.clientY - 8;
	if(navigator.userAgent.toLowerCase().indexOf('safari') >= 0){
		mouseX += window.pageXOffset;
		mouseY += window.pageYOffset;
	}
}

// This is the code for click processing
function processClick(event){
	click = true;
}

// ----------------------
// Update & Response code
// ----------------------

function run(){

  // Initialize here
  windowInit()

	// Makes a running loop - Interval Mode
  if(intervalMode){
    if (interval != null)
  		clearInterval(interval);

    interval = setInterval(runGame, sec);
  }else{
    setTimeout(runGame, sec);
  }
}

// This is for the updates
function runGame(){
  var c = document.getElementById("myCanvas");
	var ctx = c.getContext("2d");

	ctx.clearRect(0, 0, c.width, c.height);
	ctx.fillStyle = "#FFFFFF";
	ctx.fillRect(0, 0, c.width, c.height);
	ctx.fillStyle = "#000000";
	ctx.strokeStyle = "#000000";

  // This is the rendering function
	render(ctx);

	if(click)
		click = false;

  if(!intervalMode){
    setTimeout(runGame, sec);
  }
}

// This is for showing the mouse debug text on the window
function drawMouseText(ctx, posx, posy){
  ctx.fillText('Mouse:(' + mouseX + ',' + mouseY + ')' , posx, posy);
}

// ----------------------
// AJAX Communication
// ----------------------

function handleResponse(){
	if(request.readyState == 4){
		if(request.status == 200){

      // --------------------
      // JSON response setup
      // --------------------

      //request.responseText (Gets the response from the server and does something...)
			var resp = request.responseText;
			//Makes a function out of the JSON object
			var func = new Function("return "+resp);
      // Change into global if dealing with ajax, and check for null
			var objt = func();

      // -----------------------
      // JSON Function Response
      // -----------------------
      //respondObj(objt);

		}else
      console.log("A problem occurred with communicating between " +
					"the XMLHttpRequest object and the server program.");
	}//end outer if...
}

// -------------------------
// Server Functionality
// -------------------------

//The code under here is the AJAX transaction code
//Make sure to include a handleResponse somewhere...

// Wrapper on Wrapper on Wrapper code
// Further simplifies the AJAX code to handle GET and POST requests
// In one quick and dirty function
function quickRequest(reqType, url, urlFrag, asynch){
  if(reqType == "POST"){
    httpRequest("POST", url, asynch, urlFrag);
  }else{
    httpRequest("GET", url+"?"+urlFrag, asynch, null);
  }
}

/* Initialize a request object that is already constructed
Parameters:
	reqType: the HTTP request type, such as GET or POST
	url: The URL of the server program
	isAsynch: Whether to send the data asynchronously or not */
function initReq(reqType, url, isAsynch, send){
	//Specify the function that will handle the HTTP response
	request.onreadystatechange = handleResponse;
	request.open(reqType, url, isAsynch);
	// Set the Content-Type header for a POST request
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	request.send(send);
}

/* Wrapper function for constructing a request object
Parameters:
	reqType: the HTTP request type, such as GET or POST
	url: The URL of the server program
	asynch: Whether to send the data asynchronously or not */
function httpRequest(reqType, url, asynch, send){
	//Mozilla-based browsers
	if(window.XMLHttpRequest){
		request = new XMLHttpRequest();
	}else if(window.ActiveXObject){
		request = newActiveXObject("Msxml2.XMLHTTP");
		if(!request)
			request = new ActiveXObject("Microsoft.XMLHTTP");
	}
	if(request)
		initReq(reqType, url, asynch, send);
	else
		console.log("There is a problem with the AJAX features in your browser");
}
