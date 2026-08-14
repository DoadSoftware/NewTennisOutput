<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Help</title>
    
	<script type="text/javascript" src="<c:url value='/webjars/jquery/3.7.1/jquery.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js' />"></script>
	<script type="text/javascript" src="<c:url value='/resources/javascript/index.js' />"></script>
	<script type="text/javascript" src="<c:url value='/webjars/html2canvas/1.4.1/dist/html2canvas.min.js' />"></script>
	<link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css' />" />
	<link rel="stylesheet" href="<c:url value='/webjars/font-awesome/6.5.1/css/all.min.css' />" />    
    
    <style type="text/css">
        .nav-link {
        	font-weight: bold;
        	} 
        	
        .screenshot-section {
        	padding: 20px; 
        	border: 1px solid #ccc; 
        	margin: 20px;
        }
    
        .nav-link {
            font-weight: bold;
        }
        .screenshot-section {
            padding: 20px;
            border: 1px solid #ccc;
            margin: 20px;
        }
    table {
      width: 100%;
      border-collapse: collapse;
      background-color: #f9f9f9; /* Light background color for the table */
      color: #333; /* Dark text color */
    }

    th, td {
      padding: 12px;
      text-align: center;
      border: 1px solid #ddd;
      font-family: Arial, sans-serif;
      font-weight: bold; /* Bold text */
    }

    /* Styling for the first "CAPTION" column (darker blue) */
    td:nth-child(1),td:nth-child(4),
    td:nth-child(7),td:nth-child(10) {
      background-color: #99c2ff; /* Darker light blue background */
      color: #003366; /* Dark blue text */
    }

    /* Styling for the second "CAPTION" column (darker green) */
    td:nth-child(2),td:nth-child(5),
    td:nth-child(8), td:nth-child(11) {
      background-color: #a8e6a1; /* Darker light green background */
      color: #155724; /* Dark green text */
    }

    /* Styling for the third "CAPTION" column (darker red) */
    td:nth-child(3), td:nth-child(6),
    td:nth-child(9), td:nth-child(12) {
      background-color: #f5c6cb; /* Darker light red background */
      color: #721c24; /* Dark red text */
    }
    </style>
</head>
<body>
    <div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
        <div class="container">
            <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                <li class="nav-item" role="presentation">
                	<button class="nav-link active" id="pills-function-keys-tab" data-bs-toggle="pill" data-bs-target="#pills-function-keys" type="button" role="tab" 
                	aria-controls="pills-function-keys" aria-selected="true">SCOREBUG</button>
                </li>
                <li class="nav-item" role="presentation">
                	<button class="nav-link" id="pills-letters-tab" data-bs-toggle="pill" data-bs-target="#pills-letters" 
                	type="button" role="tab" aria-controls="pills-letters" aria-selected="false">FULL FRAMES
                	</button>
                </li>
                <li class="nav-item" role="presentation">
                	<button class="nav-link" id="pills-CONTROL-tab" data-bs-toggle="pill" data-bs-target="#pills-CONTROL" 
                	type="button" role="tab" aria-controls="pills-CONTROL" aria-selected="false">LOWER THIRD
                	</button>
                </li>
                <li class="nav-item" role="presentation">
                	<button class="nav-link" id="pills-bug-tab" data-bs-toggle="pill" data-bs-target="#pills-bug" 
                	type="button" role="tab" aria-controls="pills-bug" aria-selected="false">BUGS
                	</button>
                </li>
            </ul>
            <div class="tab-content" id="pills-tabContent">
                <div class="tab-pane fade show active" id="pills-function-keys" role="tabpanel" aria-labelledby="pills-function-keys-tab">
                    <button class="screenshot-button" onclick="takeScreenshot('infoTable', 'SCOREBUG')">Take Screenshot</button><br><br>
                    <div class="screenshot-section" id="infoTable">
                        <table class="table table-bordered">
                        <thead>
                        	<tr>
                        		<th colspan="18">SCOREBUG</th>
                        	</tr>
                        	<tr>
                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
                        	</tr>
                        </thead>
                        <tbody>
                        	<tr>
                        		<td>SCOREBUG</td><td>F1</td><td>=</td>
                        		 <td>GAME INFO</td><td>I</td><td>J</td>
                        		<td>TEAM STATS</td><td>K</td><td>O</td>
								<td>SCOREBUG HEADER</td><td>H</td><td>O</td>

                        	</tr>
                        	<tr>
                        		<!-- <td>NEXT MATCH PROMO</td><td>N</td><td>O</td>
                        		<td>MATCH STATS FROM API</td><td>J</td><td>O</td> -->
                        		<td>SCOREBUG GAME POINTS</td><td>CTRL+H</td><td>0</td>
                        	</tr>
                        </tbody>
                       </table>
                    </div>
                </div>
                <div class="tab-pane fade" id="pills-letters" role="tabpanel" aria-labelledby="pills-letters-tab">
                    <button class="screenshot-button" onclick="takeScreenshot('infoTable1', 'FULLFRAME')">Take Screenshot</button><br><br>
                    <div class="screenshot-section" id="infoTable1">
                        <table class="table table-bordered">
                        	<thead>
                        		<tr>
                        			<th colspan="18">FULL FRAMES</th>
                        	   </tr>
                        	   <tr>
                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
                        	</tr>
                        	</thead>
                        <tbody>
                        	<tr>
                        		<td>FF-MatchId-Singles</td><td>F2</td><td>-</td>
					            <td>FF-MatchId-Doubles</td><td>F3</td><td>-</td>
					            <td>FF-MatchResult-Singles</td><td>F8</td><td>-</td>
					            <td>FF-MatchResult-Doubles</td><td>F9</td><td>-</td>
                        	</tr>
                        	<tr>
                        		<td>FF-PLAYERPROFILE</td><td>P</td><td>-</td>
					            <td>FF-MATCH SINGLE PROMO</td><td>Q</td><td>-</td>
					            <td>FF-MATCH DOUBLE PROMO</td> <td>W</td><td>-</td>
								<td>ORDER OF MATCH</td><td>G</td><td>-</td>
					       </tr>
						   <tr>
					            <td>TIE SCORE/TIE LOCATOR</td><td>V</td><td>-</td>
					            <td>FF-POINTS TABLE</td><td>U</td><td>-</td>
					            <td>FF-ORDER OF TIE</td> <td>F</td><td>-</td>
					            <td>FF-ORDER OF MATCH</td> <td>G</td><td>-</td>
					       </tr>
						   <tr>
					            <td>TOURNAMENT LOCATOR</td><td>C</td><td>-</td>
								<td>PLAYER PROFILE OTHER</td><td>L</td><td>-</td>
								<td>POINTS PROGRESS</td><td>B</td><td>-</td>
								<td>MATCH STATS</td><td>S</td><td>-</td>
					       </tr>
                        </tbody>
                      </table>
                    </div>
                </div>
                <div class="tab-pane fade" id="pills-CONTROL" role="tabpanel" aria-labelledby="pills-CONTROL-tab">
                    <button class="screenshot-button" onclick="takeScreenshot('infoTable2', 'LOWERTHIRDS')">Take Screenshot</button><br><br>
                    <div class="screenshot-section" id="infoTable2">
                        <table class="table table-bordered">
                        	<thead>
                        		<tr><th colspan="18">LOWER THIRD</th></tr>
                        		 <tr>
	                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
	                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
	                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
	                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
                        		</tr>
                        	</thead>
                        		<tbody>
                        		<tr>
                        			<td>NAMESUPER DB</td><td>F10</td><td>-</td>
					           		<td>NAMESUPER PLAYER DB</td><td>T</td><td>-</td>
					           		<td>TIE RESULT</td><td>F6</td><td>-</td>
					           		<td>LT TEAMS</td><td>CTRL+Q</td><td>-</td>
					           		<!-- <td>LT-MatchResult-Doubles</td><td>F7</td><td>-</td> -->
					           	</tr>
					           	<tr>
                        			<td>LT MATCH ID/SCORE</td><td>CTRL+S</td><td>-</td>
					           		<td>LT DOUBLE MATCH ID/SCORE</td><td>CTRL+D</td><td>-</td>
					           		<td>LT MATCH PROMO</td><td>SHIFT+Q</td><td>-</td>
					           		<td>LT DOUBLE MATCH PROMO</td><td>SHIFT+W</td><td>-</td>
					           	</tr>
                        		</tbody>
                        	</table>
                    </div>
                </div>
                <div class="tab-pane fade" id="pills-bug" role="tabpanel" aria-labelledby="pills-bug-tab">
                    <button class="screenshot-button" onclick="takeScreenshot('infoTable4', 'BUGS')">Take Screenshot</button><br><br>
                    <div class="screenshot-section" id="infoTable4">
                        <table class="table table-bordered">
                        	<thead>
                        		<tr>
                        			<th colspan="18">BUGS</th>
                        		</tr>
                        		 <tr>
	                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
	                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
	                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
	                        		<th>Graphic</th><th>IN</th> <th> OUT</th>
                        		</tr>
                        	</thead>
                        	<tbody>
                        	<tr>
                        		<!-- <td>Serve Speed</td><td>X</td><td>-</td> -->
                        		<td>Cross(SINGLES/DOUBLES)</td><td>Z</td><td>-</td>
                        	</tr>
							</tbody>
						</table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        function takeScreenshot(tableId, headerText) {
            var element = document.getElementById(tableId);
            if (!element) {console.error(`Element with ID "${tableId}" not found.`); return;}
            html2canvas(element).then(function(canvas) {
                var imgData = canvas.toDataURL('image/png');
                var link = document.createElement('a');
                link.href = imgData;
                link.download = headerText.trim() !== '' ? headerText + '.png' : 'screenshot.png';
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            }).catch(function(error) {console.error('Error capturing screenshot:', error);});
        }
    </script>
</body>
</html>