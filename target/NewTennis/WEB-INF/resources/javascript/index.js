var match_data,home_first_player_id,away_first_player_id,home_second_player_id,away_second_player_id;
function processWaitingButtonSpinner(whatToProcess) 
{
	switch (whatToProcess) {
	case 'START_WAIT_TIMER': 
		$('.spinner-border').show();
		$(':button').prop('disabled', true);
		break;	
	case 'END_WAIT_TIMER': 
		$('.spinner-border').hide();
		$(':button').prop('disabled', false);
		break;
	}
}
function secondsTimeSpanToHMS(s) {
  var h = Math.floor(s / 3600); //Get whole hours
  s -= h * 3600;
  var m = Math.floor(s / 60); //Get remaining minutes
  s -= m * 60;
  return h + ":" + (m < 10 ? '0' + m : m) + ":" + (s < 10 ? '0' + s : s); //zero padding on minutes and seconds
}
function displayMatchTime() {
	processTennisProcedures('READ_CLOCK',null);
}
function initialiseForm(whatToProcess, dataToProcess)
{
	switch (whatToProcess) {
	case 'TIME':
		if(match_data) {
			if(document.getElementById('match_time_hdr')) {
				document.getElementById('match_time_hdr').innerHTML = 'MATCH TIME : ' + 
					secondsTimeSpanToHMS(match_data.clock.matchTotalSeconds);
			}
		}
		
		break;
	}
}
function uploadFormDataToSessionObjects(whatToProcess)
{
	var formData = new FormData();
	var url_path;

	$('input, select, textarea').each(
		function(index){  
			if($(this).is("select")) {
				formData.append($(this).attr('id'),$('#' + $(this).attr('id') + ' option:selected').val());  
			} else {
				formData.append($(this).attr('id'),$(this).val());  
			}	
		}
	);
	
	switch(whatToProcess.toUpperCase()) {
	case 'RESET_MATCH':
		url_path = 'reset_and_upload_match_setup_data';
		break;
	case 'SAVE_MATCH':
		url_path = 'upload_match_setup_data';
		break;
	}
	
	$.ajax({    
		headers: {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')},
        url : url_path,     
        data : formData,
        cache: false,
        contentType: false,
        processData: false,
        type: 'POST',     
        success : function(data) {

        	switch(whatToProcess.toUpperCase()) {
        	case 'RESET_MATCH':
        		alert('Match has been reset');
        		processWaitingButtonSpinner('END_WAIT_TIMER');
        		break;
        	case 'SAVE_MATCH':
        		document.setup_form.method = 'post';
        		document.setup_form.action = 'setup_to_match';
        	   	document.setup_form.submit();
        		break;
        	}
        	
        },    
        error : function(e) {    
       	 	console.log('Error occured in uploadFormDataToSessionObjects with error description = ' + e);     
        }    
    });		
	
}
function processUserSelectionData(whatToProcess,dataToProcess){
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		if($('#log_game_undo_btn').val()) { // Ignore keypress when user is working with UNDO
			return false;
		}
		switch (dataToProcess) {
		case ' ':
			processTennisProcedures('CLEAR-ALL');
			break;
		case '-':
			if(confirm('It will Also Delete Your Preview from Directory...\r\n\r\n Are You Sure To Animate Out?') == true){
				processTennisProcedures('ANIMATE-OUT');
			}
			break;	
		case '=':
			processTennisProcedures('ANIMATE-OUT-SCOREBUG');
			break;
		
		case 'F1':
			processTennisProcedures('POPULATE-SCOREBUG');
			break;
		case 'F2':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('FF_MATCHID-OPTIONS',null);
			//processTennisProcedures('POPULATE-MATCHID');
			break;
		case 'F3':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('FF_MATCHID_DOUBLE-OPTIONS',null);
			//processTennisProcedures('POPULATE-MATCHID_DOUBLE');
			break;
		case 'F4':
			processTennisProcedures('POPULATE-LT-MATCHID');
			break;
		case 'F5':
			processTennisProcedures('POPULATE-LT-MATCHID_DOUBLE');
			break;
		case 'F6':
			processTennisProcedures('POPULATE-LT-MATCH_RESULTSINGLES');
			break;
		case 'F7':
			processTennisProcedures('POPULATE-LT-MATCH_RESULTDOUBLES');
			break;
		case 'F8':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('FF_MATCH_RESULTSINGLES-OPTIONS',null);
			//processTennisProcedures('POPULATE-FF-MATCH_RESULTSINGLES');
			break;
		case 'F9':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('FF_MATCH_RESULTDOUBLES-OPTIONS',null);
			//processTennisProcedures('POPULATE-FF-MATCH_RESULTDOUBLES');
			break;
		case 'F10':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('NAMESUPER_GRAPHICS-OPTIONS');
			break;
		case 'F11':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			home_first_player_id = match_data.homeFirstPlayerId;
			away_first_player_id = match_data.awayFirstPlayerId;
			processTennisProcedures('NAMESUPER-SP_GRAPHICS-OPTIONS');
			break;
		case 'F12':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('NAMESUPER-DP-OPTIONS',null);
			break;
		case 'a':
			processTennisProcedures('POPULATE-LT-MATCH_SCORESINGLES');
			break;
		case 'b':
			processTennisProcedures('POPULATE-POINTS_PROGRESS');
			break;
		case 'c':
			processTennisProcedures('POPULATE-LOCATOR');
			break;
		case 'd':
			processTennisProcedures('POPULATE-LT-MATCH_SCOREDOUBLES');
			break;
		case 'e':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('SINGLE-LT_MATCHPROMO_GRAPHICS-OPTIONS');
			break;
		case 'f':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('ORDER_OF_TIE-OPTIONS',null);
			break;
		case 'g':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('ORDER_OF_MATCH_GRAPHICS-OPTIONS');
			break;
		case 'h':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('SCOREBUG-HEADER_OPTION',null);
			//processTennisProcedures('POPULATE-SCOREBUG_HEADER');
			break;
		case 'i':
			switch ($('#selectedBroadcaster').val()) {
			case 'ATP_2022': case 'ATP_CHALLENGERS':
				$("#select_event_div").hide();
				$("#match_configuration").hide();
				$("#tennis_div").hide();
				addItemsToList('SCOREBUG_OPTION',null);
				processTennisProcedures('APIDATA_GRAPHICS-OPTIONS');
				break;
			case 'TPL_2023':
				processTennisProcedures('ANIMATE-IN-SCOREBUG_GAMEINFO');
				//processTennisProcedures('ANIMATE-IN-SCOREBUG_TEAMNAME');
				break;
			}
			break;
		case 'j':
			processTennisProcedures('ANIMATE-OUT-SCOREBUG_GAMEINFO');
			break;	
		case 'k':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('SCOREBUG-SET_OPTION',null); 
			break;
		case 'l':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('LT_PLAYERPROFILE_GRAPHICS-OPTIONS');
			break;
		case 'm':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			home_first_player_id = match_data.homeFirstPlayerId;
			away_first_player_id = match_data.awayFirstPlayerId;
			home_second_player_id = match_data.homeSecondPlayerId
			away_second_player_id = match_data.awaySecondPlayerId
			processTennisProcedures('MATCH_FF_PLAYERPROFILE_GRAPHICS-OPTIONS');
			break;
		case 'n':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			home_first_player_id = match_data.homeFirstPlayerId;
			away_first_player_id = match_data.awayFirstPlayerId;
			home_second_player_id = match_data.homeSecondPlayerId
			away_second_player_id = match_data.awaySecondPlayerId
			processTennisProcedures('MATCH_LT_PLAYERPROFILE_GRAPHICS-OPTIONS');
			break;	
		case 'o':
			switch ($('#selectedBroadcaster').val()) {
			case 'ATP_2022': case 'ATP_CHALLENGERS':
				processTennisProcedures('ANIMATE-OUT-SCOREBUG_STAT');
				break;
			case 'TPL_2023':
				processTennisProcedures('ANIMATE-OUT-SCOREBUG_STAT');
				//processTennisProcedures('ANIMATE-OUT-SCOREBUG_TEAMNAME');
				break;
			}
			break;
		case '0':
			switch ($('#selectedBroadcaster').val()) {
			case 'TPL_2023':
				processTennisProcedures('ANIMATE-OUT-SCOREBUG_GAME_POINTS');
				break;
			}
			break;
		case 'p':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('FF_PLAYERPROFILE_GRAPHICS-OPTIONS');
			break;
		case 'q':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('SINGLE-MATCHPROMO_GRAPHICS-OPTIONS');
			break;
		case 'r':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('DOUBLE-LT_MATCHPROMO_GRAPHICS-OPTIONS');
			break;
		case 's':
			processTennisProcedures('POPULATE-MATCH_STATS');
			break;
		case 't':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('NAMESUPER-SP1_GRAPHICS-OPTIONS');
			break;	
		case 'u':
			processTennisProcedures('POPULATE-POINTS_TABLE');
			break;
		case 'v':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('TIE_RESULT_GRAPHICS-OPTIONS');
			break;	
		case 'w':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('DOUBLE-MATCHPROMO_GRAPHICS-OPTIONS');
			break;
		case 'x':
			addItemsToList('SPEED_OPTION',null);
			break;	
		
		case 'y':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('NAMESUPER-DP1_GRAPHICS-OPTIONS');
			break;
		case 'z':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('CROSS-OPTIONS',null);
			break;
			
		case 'Control_q':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('LT-TEAM_GRAPHICS-OPTIONS');
			break;
		case 'Control_h':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('SCOREBUG-GAME_POINTS_OPTIONS',null);
			break;
			
		case 'Control_s':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('LT-MATCH_ID_SCORE_OPTIONS',null);
			break;
		case 'Control_d':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('LT-DOUBLEMATCH_ID_SCORE_OPTIONS',null);
			break;
			
		case 'Shift_Q':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('LT_SINGLE-MATCHPROMO_GRAPHICS-OPTIONS');
			break;
		case 'Shift_W':
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('LT_DOUBLE-MATCHPROMO_GRAPHICS-OPTIONS');
			break;
		
		/*case 75:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('SCOREBUG_STATS_OPTION',null);
			break;*/
			
		}
		
		break;
	}
}
function processUserSelection(whichInput)
{	
	var error_msg = '';

	switch ($(whichInput).attr('name')) {
	case 'matchType':
		if($('#matchType option:selected').val() == 'singles') {
			document.getElementById('select_double_player_row').style.display = 'none';
		} else {
			document.getElementById('select_double_player_row').style.display = '';
		}
		break;
	case 'load_scene_btn':
		/*if(checkEmpty($('#vizIPAddress'),'IP Address Blank') == false
			|| checkEmpty($('#vizPortNumber'),'Port Number Blank') == false) {
			return false;
		}*/
	  	document.initialise_form.submit();
		break;
	case 'cancel_match_setup_btn':
		document.setup_form.method = 'post';
		document.setup_form.action = 'setup_to_match';
	   	document.setup_form.submit();
		break;
	case 'matchFileName':
		if(document.getElementById('matchFileName').value) {
			document.getElementById('matchFileName').value = 
				document.getElementById('matchFileName').value.replace('.json','') + '.json';
		}
		break;
	case 'load_match_btn':
		processWaitingButtonSpinner('START_WAIT_TIMER');
		processTennisProcedures('LOAD_MATCH',$('#select_tennis_matches option:selected'));
		break;
	case 'cancel_graphics_btn':
		$('#select_graphic_options_div').empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#select_event_div").show();
		$("#match_configuration").show();
		$("#tennis_div").show();
		break;
	case 'select_existing_tennis_matches':
		if(whichInput.value.toLowerCase().includes('new_match')) {
			initialiseForm('SETUP',null);
		} else {
			processWaitingButtonSpinner('START_WAIT_TIMER');
			processTennisProcedures('LOAD_SETUP',$('#select_existing_tennis_matches option:selected'));
		}
		break;
	case 'populate_stats_btn':
		processTennisProcedures('POPULATE-SCOREBUG_STATS');
		break;
	case 'populate_header_btn':
		processTennisProcedures('POPULATE-SCOREBUG_HEADER');
		break;
	case 'populate_points_btn':
		processTennisProcedures('POPULATE-SCOREBUG_GAME_POINTS');
		break;
	case 'populate_speed_btn':
		processTennisProcedures('POPULATE-SPEED');
		break;
	case 'populate_stats_set_btn':
		processTennisProcedures('POPULATE-SCOREBUG_SET_STATS');
		break;
	case 'populate_stats_bar_btn':
		processTennisProcedures('POPULATE-SCOREBUG_BAR_STATS');
		break;
	case 'populate_namesuper_btn':
		processTennisProcedures('POPULATE-NAMESUPERDB');
		break;
	case 'populate_namesupersp_btn':
		processTennisProcedures('POPULATE-NAMESUPER-SP');
		break;
	case 'populate_namesuperdp_btn':
		processTennisProcedures('POPULATE-NAMESUPER-DP');
		break;
	case 'populate_namesuper_sp1_btn':
		processTennisProcedures('POPULATE-NAMESUPER-SP1');
		break;
	case 'populate_namesuper_dp1_btn':
		processTennisProcedures('POPULATE-NAMESUPER-DP1');
		break;
	case 'populate_ff_player_profile_btn': case 'populate_ff_player_profile_match_btn':
		processTennisProcedures('POPULATE-FF_PLAYERPROFILE');
		break;
	case 'populate_lt_player_profile_btn': case 'populate_lt_player_profile_match_btn':
		processTennisProcedures('POPULATE-LT_PLAYERPROFILE');
		break;
	case 'populate_cross_btn':
		processTennisProcedures('POPULATE-CROSS');
		break;
		
	case 'populate_ff_matchId_btn':
		processTennisProcedures('POPULATE-MATCHID');
		break;
	case 'populate_ff_doublematchId_btn':
		processTennisProcedures('POPULATE-MATCHID_DOUBLE');
		break;
	case 'populate_ff_match_result_btn':
		processTennisProcedures('POPULATE-FF-MATCH_RESULTSINGLES');
		break;
	case 'populate_ff_doublematch_result_btn':
		processTennisProcedures('POPULATE-FF-MATCH_RESULTDOUBLES');
		break;
		
	case 'populate_lt_matchId_score_btn':
		processTennisProcedures('POPULATE-LT_MATCHID_SCORE');
		break;
	case 'populate_lt_doublematchId_score_btn':
		processTennisProcedures('POPULATE-LT_DOUBLEMATCHID_SCORE');
		break;
	case 'populate_lt_single_match_promo_btn':
		processTennisProcedures('POPULATE-LT_SINGLEMATCH_PROMO');
		break;
	case 'populate_ltdouble_match_promo_btn':
		processTennisProcedures('POPULATE-LT_DOUBLEMATCH_PROMO');
		break;
		
	case 'populate_tie_results_btn':
		processTennisProcedures('POPULATE-TIE_RESULT');
		break;
	case 'populate_single_match_promo_btn':
		processTennisProcedures('POPULATE-SINGLE_MATCHPROMO');
		break;
	case 'populate_single_ltmatch_promo_btn':
		processTennisProcedures('POPULATE-SINGLE_LT_MATCHPROMO');
		break;
	case 'populate_double_match_promo_btn':
		processTennisProcedures('POPULATE-DOUBLE_MATCHPROMO');
		break;
	case 'populate_lt_double_match_promo_btn':
		processTennisProcedures('POPULATE-LT_DOUBLE_MATCHPROMO');
		break;
	case 'populate_order_of_tie_btn':
		processTennisProcedures('POPULATE-ORDER_OF_TIE');
		break;
	case 'populate_lt_team_btn':
		processTennisProcedures('POPULATE-LT_TEAM');
		break;
	case 'populate_order_of_match_btn':
		processTennisProcedures('POPULATE-ORDER_OF_MATCH');
		break;
	/*default:
		if(whichInput) {
			if(whichInput.id.includes('_score_btn')) { 
				
				error_msg = 'Cannot find any started set. Please start a set first before logging an event';
				match_data.sets.forEach(function(set,set_index,set_arr){
					if(set.set_status.toLowerCase() == 'start') {
						error_msg = '';
					}
				});
				if(error_msg) {
					alert(error_msg);
					return false;			
				} else {
					error_msg = 'Cannot find any started game. Please start a game first before logging an event';
					match_data.sets.forEach(function(set,set_index,set_arr){
						if(set.set_status.toLowerCase() == 'start') {
							set.games.forEach(function(game,game_index,game_arr){
								if(game.game_status.toLowerCase() == 'start') {
									error_msg = '';
								}
							});
						}
					});
					if(error_msg) {
						alert(error_msg);
						return false;			
					} else {
						processWaitingButtonSpinner('START_WAIT_TIMER');
						processTennisProcedures('LOG_SCORE',whichInput);
					}
				}
				
			} else if(whichInput.id.includes('_increment_') || whichInput.id.includes('_decrement_')) {

				if(whichInput.id.includes('_decrement_')) {
					if(parseInt($('#' + whichInput.id.replace('_decrement_','_').replace('_btn','_txt')).val()) <= 0) {
						alert('Cannot use decrement button when the value is zero');
						return false;
					}
				}
				if(whichInput.id.includes('_increment_')) {
					$('#' + whichInput.id.replace('_increment_','_').replace('_btn','_txt')).val(
						parseInt($('#' + whichInput.id.replace('_increment_','_').replace('_btn','_txt')).val()) + parseInt(1));
				}else if(whichInput.id.includes('_decrement_')) {
					$('#' + whichInput.id.replace('_decrement_','_').replace('_btn','_txt')).val(
						parseInt($('#' + whichInput.id.replace('_increment_','_').replace('_btn','_txt')).val()) - parseInt(1));
				}
				
				processWaitingButtonSpinner('START_WAIT_TIMER');
				processTennisProcedures('LOG_STAT',whichInput);
			}
		}
		break;*/
	}
}
function processTennisProcedures(whatToProcess, whichInput)
{
	var value_to_process; 
	
	switch(whatToProcess) {
	case 'READ-MATCH-AND-POPULATE':
		value_to_process = $('#matchFileTimeStamp').val();
		break;
	case 'LOAD_MATCH':
		value_to_process = whichInput.val();
		break;

	case 'POPULATE-SCOREBUG':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/ScoreBug-Single';
			break;
		case 'TPL_2023':
			value_to_process = '/Default/ScoreBug';
			break;
		}
		break;
	case 'POPULATE-SCOREBUG_STATS': case 'POPULATE-SCOREBUG_SET_STATS': case 'POPULATE-SCOREBUG_BAR_STATS':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS': case 'TPL_2023':
			value_to_process = $('#selectScorebugstats option:selected').val() ;
			break;
		}
		break
	case 'POPULATE-SCOREBUG_HEADER':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS': case 'TPL_2023':
			value_to_process = $('#selectScorebugHeader option:selected').val();
			break;
		}
		break
	case 'POPULATE-SCOREBUG_GAME_POINTS':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS': case 'TPL_2023':
			value_to_process = $('#selectScorebugHeader option:selected').val() + ',' + $('#selectteam option:selected').val();
			break;
		}
		break
	case 'POPULATE-MATCH_STATS':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/FF_MatchStats';
			break;
		case 'TPL_2023':
			value_to_process = '/Default/Match_Stats'
		}
		break;
	case 'POPULATE-SPEED':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtServeSpeed_Auto' + ',' + $('#selectSpeed').val();
			break;
		}
		break;
	case 'POPULATE-LOCATOR':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/Locator';
			break;
		}
		break;
	case 'POPULATE-POINTS_TABLE':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/PointsTable';
			break;
		}
		break;
		
	case 'POPULATE-LT_MATCHID_SCORE': case 'POPULATE-LT_DOUBLEMATCHID_SCORE':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/LT_GameIdent' + ',' + $('#selectType option:selected').val() + ',' + $('#selectPhoto option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LT_SINGLEMATCH_PROMO':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/LT_GameIdent' + ',' + $('#selectSingleMatchPromo option:selected').val() + ',' + $('#selectPhoto option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LT_DOUBLEMATCH_PROMO':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/LT_GameIdent' + ',' + $('#selectDoubleMatchPromo option:selected').val() + ',' + $('#selectPhoto option:selected').val();
			break;
		}
		break;
		
		
	case 'POPULATE-MATCHID':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/FF_MatchIdSingles';
			break;
		case 'TPL_2023':
			value_to_process = '/Default/MatchId' + ',' + $('#selectPhoto option:selected').val();
			break;
		}
		break;
	case 'POPULATE-TIE_RESULT':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/MatchId' + ',' + $('#selectTieResult option:selected').val();
			break;
		}
		break;
	case 'POPULATE-SINGLE_MATCHPROMO':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/FF_MatchIdSingles' + ',' + $('#selectSingleMatchPromo option:selected').val();
			break;
		case 'TPL_2023':
			value_to_process = '/Default/MatchId' + ',' + $('#selectSingleMatchPromo option:selected').val() + ',' + $('#selectPhoto option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LT-MATCHID':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtMatchIdentSingles';
			break;
		}
		break;
	case 'POPULATE-SINGLE_LT_MATCHPROMO':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtMatchIdentSingles' + ',' + $('#selectSingleltMatchPromo option:selected').val();
			break;
		}
		break;
	case 'POPULATE-DOUBLE_MATCHPROMO':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/FF_MatchIdDoubles'  + ',' + $('#selectDoubleMatchPromo option:selected').val();
			break;
		case 'TPL_2023':
			value_to_process = '/Default/MatchId' + ',' + $('#selectDoubleMatchPromo option:selected').val() + ',' + $('#selectPhoto option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LT-MATCHID_DOUBLE':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtMatchIdentDoubles';
			break;
		}
		break;
	case 'POPULATE-LT_DOUBLE_MATCHPROMO':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtMatchIdentDoubles' + ',' + $('#selectltDoubleMatchPromo option:selected').val();
			break;
		}
		break;
	case 'POPULATE-MATCHID_DOUBLE':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/FF_MatchIdDoubles';
			break;
		case 'TPL_2023':
			value_to_process = '/Default/MatchId' + ',' + $('#selectPhoto option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LT-MATCH_RESULTSINGLES': case 'POPULATE-LT-MATCH_SCORESINGLES':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtMatchResultsSingles';
			break;
		case 'TPL_2023':
			value_to_process = '/Default/LT_Score';
			break;
		}
		break;
	case 'POPULATE-FF-MATCH_RESULTSINGLES':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/FF_MatchIdSingles';
			break;
		case 'TPL_2023':
			value_to_process = '/Default/MatchId' + ',' + $('#selectPhoto option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LT-MATCH_RESULTDOUBLES': case 'POPULATE-LT-MATCH_SCOREDOUBLES':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtMatchResultsDoubles';
			break;
		}
		break;
	case 'POPULATE-FF-MATCH_RESULTDOUBLES':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/FF_MatchIdDoubles';
			break;
		case 'TPL_2023':
			value_to_process = '/Default/MatchId' + ',' + $('#selectPhoto option:selected').val();
			break;
		}
		break;
	case 'POPULATE-NAMESUPER-DP':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtNameSuper2' + ',' + $('#selectNameSuperdp option:selected').val(); 
			break;
		}
		break;
	case 'POPULATE-NAMESUPER-SP':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtNameSuper1' + ',' + $('#selectNameSupersp option:selected').val();
			break;
		case 'TPL_2023':
			value_to_process = '/Default/LowerThird_Players' + ',' + $('#selectNameSupersp option:selected').val();
			break;
		}
		break;
	case 'POPULATE-NAMESUPER-SP1':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtNameSuper1' + ',' + $('#selectNameSupersp1 option:selected').val();
			break;
		case 'TPL_2023':
			value_to_process = '/Default/LowerThird_Players' + ',' + $('#selectNameSupersp1 option:selected').val() + ',' + $('#selectDesignation option:selected').val();
			break;
		}
		break;
	case 'POPULATE-NAMESUPER-DP1':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtNameSuper2' + ',' + $('#selectNameSuperdphome option:selected').val() + ',' + $('#selectNameSuperdpaway option:selected').val();
			break;
		}
		break;
	case 'POPULATE-FF_PLAYERPROFILE':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/PlayerProfile1' + ',' + $('#selectPlayerProfile option:selected').val();
			break;
		case 'TPL_2023':
			value_to_process = '/Default/PlayerProfile_Big' + ',' + $('#selectPlayerProfile option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LT_PLAYERPROFILE':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS': 
			value_to_process = '/Default/Lt-PlayerProfilePage1' + ',' + $('#selectLtPlayerProfile option:selected').val();
			break;
		case 'TPL_2023':
			value_to_process = '/Default/PlayerProfile' + ',' + $('#selectLtPlayerProfile option:selected').val();
		}
		break;
	case 'POPULATE-CROSS':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			if(match_data.matchType == 'singles'){
				value_to_process = '/Default/LtCross_Court_Single_Auto' + ',' + $('#selectCross1 option:selected').val();
			}else if(match_data.matchType == 'doubles'){
				value_to_process = '/Default/LtCross_Court_Doubles_Auto' + ',' + $('#selectCross1 option:selected').val();
			}
			break;
		case 'TPL_2023':
			value_to_process = '/Default/CrossCourt' + ',' + $('#selectCross1 option:selected').val();
			break;
		}
		break;
	case 'POPULATE-NAMESUPERDB':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS':
			value_to_process = '/Default/LtNameSuperCenter' + ',' + $('#selectNameSuper option:selected').val();
			break;
		case 'TPL_2023':
			value_to_process = '/Default/LowerThird' + ',' + $('#selectNameSuper option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LT_TEAM':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/LowerThird_Team' + ',' + $('#selectLtTeam option:selected').val();
			break;
		}
		break;
	case 'POPULATE-ORDER_OF_TIE':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/Fixtures_Single' + ',' + $('#selectOrderOfTie option:selected').val();
			break;
		}
		break;
	case 'POPULATE-ORDER_OF_MATCH':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/Fixtures_Single' + ',' + $('#selectOrderOfMatch option:selected').val();
			break;
		}
		break;
	case 'POPULATE-POINTS_PROGRESS':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/PointsProgression_Final';
			break;
		}
		break;
	}

	$.ajax({    
        type : 'Get',     
        url : 'processTennisProcedures.html',     
        data : 'whatToProcess=' + whatToProcess + '&valueToProcess=' + value_to_process, 
        dataType : 'json',
        success : function(data) {
			//match_data = data;
        	switch(whatToProcess) {
			case 'READ_MATCH_FOR_STATS': case 'LOG_STAT':
				initialiseForm('LOAD_STAT',data);
				break;
			case 'READ-MATCH-AND-POPULATE':
				if(data){
					if($('#matchFileTimeStamp').val() != data.matchFileTimeStamp) {
						document.getElementById('matchFileTimeStamp').value = data.matchFileTimeStamp;
						addItemsToList('LOAD_MATCH_DETAIL',data);
						addItemsToList('LOAD_EVENTS',data);
						//processTennisProcedures('READ_CLOCK',null);
						match_data = data;
					}
				}
				break;
			case 'READ_CLOCK':
				if(match_data.clock) {
					if(document.getElementById('match_time_hdr')) {
						document.getElementById('match_time_hdr').innerHTML = 'MATCH TIME : ' + 
							secondsTimeSpanToHMS(match_data.clock.matchTotalSeconds);
					}
				}
				break;
			
			case 'LOAD_MATCH':
				addItemsToList('LOAD_EVENTS',data);
				addItemsToList('LOAD_MATCH_DETAIL',data);
				document.getElementById('tennis_div').style.display = '';
				document.getElementById('select_event_div').style.display = '';
				document.getElementById('select_caption_div').style.display = '';
				break;
				
        	case 'POPULATE-SCOREBUG': case 'POPULATE-LT-MATCH_RESULTSINGLES': case 'POPULATE-LT-MATCHID': case 'POPULATE-MATCHID': case 'POPULATE-MATCHID_DOUBLE':
        	case 'POPULATE-LT-MATCHID_DOUBLE': case 'POPULATE-NAMESUPERDB': case 'POPULATE-LT-MATCH_RESULTDOUBLES': case 'POPULATE-FF-MATCH_RESULTSINGLES':
        	case 'POPULATE-FF-MATCH_RESULTDOUBLES': case 'POPULATE-NAMESUPER-SP': case 'POPULATE-NAMESUPER-DP': case 'POPULATE-NAMESUPER-SP1': case 'POPULATE-NAMESUPER-DP1':
        	case 'POPULATE-CROSS': case "POPULATE-LT-MATCH_SCORESINGLES": case 'POPULATE-SINGLE_MATCHPROMO': case 'POPULATE-DOUBLE_MATCHPROMO': case 'POPULATE-SINGLE_LT_MATCHPROMO':
        	case 'POPULATE-LT_DOUBLE_MATCHPROMO': case 'POPULATE-LT-MATCH_SCOREDOUBLES': case 'POPULATE-MATCH_STATS': case 'POPULATE-SPEED': case 'POPULATE-FF_PLAYERPROFILE':
        	case 'POPULATE-LT_PLAYERPROFILE': case 'POPULATE-ORDER_OF_TIE': case 'POPULATE-LOCATOR': case 'POPULATE-TIE_RESULT': case 'POPULATE-POINTS_TABLE':
        	case 'POPULATE-ORDER_OF_MATCH': case 'POPULATE-POINTS_PROGRESS': case 'POPULATE-LT_TEAM': case 'POPULATE-LT_MATCHID_SCORE': case 'POPULATE-LT_DOUBLEMATCHID_SCORE':
        	case 'POPULATE-LT_SINGLEMATCH_PROMO': case 'POPULATE-LT_DOUBLEMATCH_PROMO':
        		console.log(data);
        		if(data.status == 'SCOREBUG'){
					alert('SCOREBUG IS ON AIR');
				}else{
					if(confirm('Animate In?') == true){
						switch(whatToProcess){
						case 'POPULATE-SCOREBUG':
							processTennisProcedures('ANIMATE-IN-SCOREBUG');		
							break;
						case 'POPULATE-MATCHID':
							processTennisProcedures('ANIMATE-IN-MATCHID');				
							break;
						case 'POPULATE-LOCATOR':
							processTennisProcedures('ANIMATE-IN-LOCATOR');				
							break;
							
						case 'POPULATE-LT_MATCHID_SCORE':
							processTennisProcedures('ANIMATE-IN-LT_MATCHID_SCORE');				
							break;
						case 'POPULATE-LT_DOUBLEMATCHID_SCORE':
							processTennisProcedures('ANIMATE-IN-LT_DOUBLEMATCHID_SCORE');
							break;
						case 'POPULATE-LT_SINGLEMATCH_PROMO':
							processTennisProcedures('ANIMATE-IN-LT_SINGLEMATCH_PROMO');				
							break;
						case 'POPULATE-LT_DOUBLEMATCH_PROMO':
							processTennisProcedures('ANIMATE-IN-LT_DOUBLEMATCH_PROMO');				
							break;
							
						case 'POPULATE-POINTS_TABLE':
							processTennisProcedures('ANIMATE-IN-POINTS_TABLE');				
							break;
						case 'POPULATE-TIE_RESULT':
							processTennisProcedures('ANIMATE-IN-TIE_RESULT');				
							break;
						case 'POPULATE-SINGLE_MATCHPROMO':
							processTennisProcedures('ANIMATE-IN-SINGLE_MATCHPROMO');				
							break;
						case 'POPULATE-DOUBLE_MATCHPROMO':
							processTennisProcedures('ANIMATE-IN-DOUBLE_MATCHPROMO');				
							break;
						case 'POPULATE-MATCHID_DOUBLE':
							processTennisProcedures('ANIMATE-IN-MATCHID_DOUBLE');				
							break;
						case 'POPULATE-LT-MATCHID':
							processTennisProcedures('ANIMATE-IN-LT_MATCHID');				
							break;
						case 'POPULATE-LT-MATCHID_DOUBLE':
							processTennisProcedures('ANIMATE-IN-LT-MATCHID_DOUBLE');				
							break;
						case 'POPULATE-LT-MATCH_RESULTSINGLES':
							processTennisProcedures('ANIMATE-LT-MATCH_RESULTSINGLES');				
							break;
						case 'POPULATE-NAMESUPERDB':
							processTennisProcedures('ANIMATE-LT-NAMESUPERDB');				
							break;
						case 'POPULATE-LT-MATCH_RESULTDOUBLES':
							processTennisProcedures('ANIMATE-LT-MATCH_RESULTDOUBLES');				
							break;
						case 'POPULATE-FF-MATCH_RESULTSINGLES':
							processTennisProcedures('ANIMATE-FF-MATCH_RESULTSINGLES');				
							break;
						case 'POPULATE-FF-MATCH_RESULTDOUBLES':
							processTennisProcedures('ANIMATE-FF-MATCH_RESULTDOUBLES');				
							break;
						case 'POPULATE-NAMESUPER-SP':
							processTennisProcedures('ANIMATE-LT-NAMESUPER_SP');				
							break;
						case 'POPULATE-NAMESUPER-DP':
							processTennisProcedures('ANIMATE-LT-NAMESUPER_DP');				
							break;
						case 'POPULATE-NAMESUPER-SP1':
							processTennisProcedures('ANIMATE-LT-NAMESUPER_SP1');				
							break;
						case 'POPULATE-NAMESUPER-DP1':
							processTennisProcedures('ANIMATE-LT-NAMESUPER_DP1');				
							break;
						case 'POPULATE-FF_PLAYERPROFILE':
							processTennisProcedures('ANIMATE-FF_PLAYERPROFILE');				
							break;
						case 'POPULATE-LT_PLAYERPROFILE':
							processTennisProcedures('ANIMATE-LT_PLAYERPROFILE');				
							break;
						case 'POPULATE-CROSS':
							processTennisProcedures('ANIMATE-LT-CROSS');				
							break;
						case "POPULATE-LT-MATCH_SCORESINGLES":
							processTennisProcedures('ANIMATE-LT-MATCH_SCORESINGLES');				
							break;
						case 'POPULATE-LT-MATCH_SCOREDOUBLES':
							processTennisProcedures('ANIMATE-LT-MATCH_SCOREDOUBLES');				
							break;
						case 'POPULATE-SINGLE_LT_MATCHPROMO':
							processTennisProcedures('ANIMATE-LT-SINGLE_LT_MATCHPROMO');				
							break;
						case 'POPULATE-LT_DOUBLE_MATCHPROMO':
							processTennisProcedures('ANIMATE-LT-DOUBLE_LT_MATCHPROMO');				
							break;
						case 'POPULATE-MATCH_STATS':
							processTennisProcedures('ANIMATE-MATCH_STATS');				
							break;
						case 'POPULATE-SPEED':
							processTennisProcedures('ANIMATE-SPEED');				
							break;
						case 'POPULATE-LT_TEAM':
							processTennisProcedures('ANIMATE-IN-LT_TEAM');				
							break;
						case 'POPULATE-ORDER_OF_TIE':
							processTennisProcedures('ANIMATE-IN-ORDER_OF_TIE');				
							break;
						case 'POPULATE-ORDER_OF_MATCH':
							processTennisProcedures('ANIMATE-IN-ORDER_OF_MATCH');				
							break;
						case 'POPULATE-POINTS_PROGRESS':
							processTennisProcedures('ANIMATE-IN-POINTS_PROGRESS');				
							break;
						}
					}
				}
				break;
			case 'TIE_RESULT_GRAPHICS-OPTIONS':
				addItemsToList('TIE_RESULT-OPTIONS',data);
				break;
			case 'ORDER_OF_MATCH_GRAPHICS-OPTIONS':
				addItemsToList('ORDER_OF_MATCH-OPTIONS',data);
				break;
			case 'LT-TEAM_GRAPHICS-OPTIONS':
				addItemsToList('LT_TEAM-OPTIONS',data);
				break;
			case 'SINGLE-MATCHPROMO_GRAPHICS-OPTIONS':
				addItemsToList('SINGLE-MATCHPROMO-OPTIONS',data);
				break;
			case 'LT_SINGLE-MATCHPROMO_GRAPHICS-OPTIONS':
				addItemsToList('LT_SINGLE-MATCHPROMO-OPTIONS',data);
				break;
			case 'LT_DOUBLE-MATCHPROMO_GRAPHICS-OPTIONS':
				addItemsToList('LT_DOUBLE-MATCHPROMO-OPTIONS',data);
				break;
				
			case 'SINGLE-LT_MATCHPROMO_GRAPHICS-OPTIONS':
				addItemsToList('SINGLE-LT_MATCHPROMO-OPTIONS',data);
				break;
			case 'DOUBLE-LT_MATCHPROMO_GRAPHICS-OPTIONS':
				addItemsToList('DOUBLE-LT_MATCHPROMO-OPTIONS',data);
				break;
			case 'DOUBLE-MATCHPROMO_GRAPHICS-OPTIONS':
				addItemsToList('DOUBLE-MATCHPROMO-OPTIONS',data);
				break;
			case 'NAMESUPER_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-OPTIONS',data);
				break;
			case 'NAMESUPER-SP_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-SP-OPTIONS',data);
				break;
			case 'NAMESUPER-SP1_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-SP1-OPTIONS',data);
				break;
			case 'NAMESUPER-DP1_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-DP1-OPTIONS',data);
				break;
			case 'APIDATA_GRAPHICS-OPTIONS':
				addItemsToList('APIDATA-OPTIONS',data);				
				break;
			case 'FF_PLAYERPROFILE_GRAPHICS-OPTIONS':
				addItemsToList('FF_PLAYERPROFILE-OPTIONS',data);
				break;
			case 'MATCH_FF_PLAYERPROFILE_GRAPHICS-OPTIONS':
				addItemsToList('MATCH_FF_PLAYERPROFILE',data);
				break;
			case 'LT_PLAYERPROFILE_GRAPHICS-OPTIONS':
				addItemsToList('LT_PLAYERPROFILE-OPTIONS',data);
				break;
			case 'MATCH_LT_PLAYERPROFILE_GRAPHICS-OPTIONS':
				addItemsToList('MATCH_LT_PLAYERPROFILE',data);
				break;
        	}
    		processWaitingButtonSpinner('END_WAIT_TIMER');
	    },    
	    error : function(e) {    
	  	 	console.log('Error occured in ' + whatToProcess + ' with error description = ' + e);     
	    }    
	});
}
function addItemsToList(whatToProcess, dataToProcess)
{
	var div,row,header_text,select,option,tr,th,thead,text,table,tbody, captionTable, captionTbody,header_text1,header_text2,header_text3;
	var cellCount=0;
	switch (whatToProcess) {
	case 'SCOREBUG_OPTION': case 'SCOREBUG-SET_OPTION': case 'SCOREBUG_STATS_OPTION': case 'SPEED_OPTION': case 'SCOREBUG-HEADER_OPTION': case 'SCOREBUG-GAME_POINTS_OPTIONS':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS': case 'TPL_2023':

			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			switch(whatToProcess){
				case 'SCOREBUG_STATS_OPTION':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectScorebugstats';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'firstServePoints';
					option.text = '1st Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'secondServePoints';
					option.text = '2nd Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'totalPointsWon';
					option.text = 'Total Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'returnPointsWon';
					option.text = 'Return Points Won ';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'breakPoint';
					option.text = 'Break Points Won';
					select.appendChild(option);

					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					break;
				case 'SCOREBUG-HEADER_OPTION': case 'SCOREBUG-GAME_POINTS_OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectScorebugHeader';
					select.name = select.id;
					
					switch(whatToProcess){
						case 'SCOREBUG-GAME_POINTS_OPTIONS':
							option = document.createElement('option');
							option.value = 'game_point';
							option.text = 'Game Point';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'match_point';
							option.text = 'Match Point';
							select.appendChild(option);
							
							/*option = document.createElement('option');
							option.value = 'set_point';
							option.text = 'Set Point';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'break_point';
							option.text = 'Break Point';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'tie_break';
							option.text = 'Tie-Break';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'match_tie_break';
							option.text = 'Match Tie-Break';
							select.appendChild(option);*/
							break;
						
						case 'SCOREBUG-HEADER_OPTION':
							option = document.createElement('option');
							option.value = 'match_game';
							option.text = 'Match Game Number';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'match_score';
							option.text = 'Match Score';
							select.appendChild(option);
							break;
					}
					
					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					if(whatToProcess == 'SCOREBUG-GAME_POINTS_OPTIONS'){
						select = document.createElement('select');
						select.style = 'width:130px';
						select.id = 'selectteam';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = '';
						option.text = '';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'home';
						option.text = 'Home';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'away';
						option.text = 'Away';
						select.appendChild(option);
						
						select.setAttribute('onchange',"processUserSelection(this)");
						row.insertCell(cellCount).appendChild(select);
						cellCount = cellCount + 1;
					}
					break;
				case 'SPEED_OPTION':
					select = document.createElement('input');
					select.type = "text";
					select.id = 'selectSpeed';
					select.value = '';
					
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					break;
				case 'SCOREBUG_OPTION':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectScorebugstats';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'firstServeWon';
					option.text = '1st Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'secondServeWon';
					option.text = '2nd Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'ace';
					option.text = 'Aces';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'doubleFault';
					option.text = 'Double Fault';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'breakPointWon';
					option.text = 'Break Points Won';
					select.appendChild(option);

					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					break;
					
				case 'SCOREBUG-SET_OPTION':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectScorebugstats';
					select.name = select.id;
					
					/*option = document.createElement('option');
					option.value = 'setfirstServeWon';
					option.text = '1st Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'setsecondServeWon';
					option.text = '2nd Serve Points Won';
					select.appendChild(option);*/
					
					option = document.createElement('option');
					option.value = 'aces';
					option.text = 'Aces';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'double_faults';
					option.text = 'Double Fault';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'winners';
					option.text = 'Winners';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'unforced_errors';
					option.text = 'Unforced Errors';
					select.appendChild(option);
					
					/*option = document.createElement('option');
					option.value = 'setbreakPointWon';
					option.text = 'Break Points Won';
					select.appendChild(option);*/

					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					break;
				}
			
			option = document.createElement('input');
		    option.type = 'button';
			switch (whatToProcess) {
			case 'SCOREBUG-HEADER_OPTION':
				option.name = 'populate_header_btn';
			    option.value = 'Populate Header';
				break;
			case 'SCOREBUG-GAME_POINTS_OPTIONS':
				option.name = 'populate_points_btn';
			    option.value = 'Populate Points';
				break;
			case 'SPEED_OPTION':
				option.name = 'populate_speed_btn';
			    option.value = 'Populate Speed';
				break;
			case 'SCOREBUG_OPTION':
			    option.name = 'populate_stats_btn';
			    option.value = 'Populate Stats';
				break;
			case 'SCOREBUG_STATS_OPTION':
				option.name = 'populate_stats_bar_btn';
			    option.value = 'Populate Stats Bar';
				break;
			case 'SCOREBUG-SET_OPTION':
				option.name = 'populate_stats_set_btn';
			    option.value = 'Populate Set Stats';
				break;
			}
		    option.id = option.name;
		    option.setAttribute('onclick',"processUserSelection(this)");
		    
		    div = document.createElement('div');
		    div.append(option);

			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_graphics_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');
	
		    div.append(option);
		    
		    row.insertCell(cellCount).appendChild(div);
		    cellCount = cellCount + 1;
		    
			document.getElementById('select_graphic_options_div').style.display = '';

			break;
		}
		break;
		
	case 'LT-MATCH_ID_SCORE_OPTIONS': case 'LT-DOUBLEMATCH_ID_SCORE_OPTIONS':
		switch ($('#selectedBroadcaster').val().toUpperCase()){
		case 'TPL_2023':
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			select = document.createElement('select');
			select.style = 'width:300px';
			select.id = 'selectType';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'Ident';
			option.text = 'Ident';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Score';
			option.text = 'Score';
			select.appendChild(option);
			
			row.insertCell(0).appendChild(select);
			
			select = document.createElement('select');
			select.style = 'width:300px';
			select.id = 'selectPhoto';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'without';
			option.text = 'Without Photo';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'with';
			option.text = 'With Photo';
			select.appendChild(option);
			
			row.insertCell(1).appendChild(select);
			
			option = document.createElement('input');
	   	 	option.type = 'button';
	   	 	switch(whatToProcess){
			case 'LT-MATCH_ID_SCORE_OPTIONS':
				option.name = 'populate_lt_matchId_score_btn';
		    	option.value = 'Populate LT Match ID/Score';
				break;
			case 'LT-DOUBLEMATCH_ID_SCORE_OPTIONS':
				option.name = 'populate_lt_doublematchId_score_btn';
		    	option.value = 'Populate LT Double Match ID/Score';
				break;
			}
		    option.id = option.name;
		    option.setAttribute('onclick',"processUserSelection(this)");
		    
		    div = document.createElement('div');
		    div.append(option);

			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_graphics_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');
	
		    div.append(option);
		    
		    row.insertCell(2).appendChild(div);
		    
			document.getElementById('select_graphic_options_div').style.display = '';
			break;
		}
		break;
		
	case 'FF_MATCHID-OPTIONS': case 'FF_MATCHID_DOUBLE-OPTIONS': case 'FF_MATCH_RESULTSINGLES-OPTIONS': case 'FF_MATCH_RESULTDOUBLES-OPTIONS':
		switch ($('#selectedBroadcaster').val().toUpperCase()){
			case 'TPL_2023':
				header_text = document.createElement('h6');
				header_text.innerHTML = 'Select Graphic Options';
				document.getElementById('select_graphic_options_div').appendChild(header_text);
				
				table = document.createElement('table');
				table.setAttribute('class', 'table table-bordered');
						
				tbody = document.createElement('tbody');
		
				table.appendChild(tbody);
				document.getElementById('select_graphic_options_div').appendChild(table);
				
				row = tbody.insertRow(tbody.rows.length);
				
				select = document.createElement('select');
				select.style = 'width:300px';
				select.id = 'selectPhoto';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'without';
				option.text = 'Without Photo';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'with';
				option.text = 'With Photo';
				select.appendChild(option);
				
				row.insertCell(0).appendChild(select);
				
				option = document.createElement('input');
		   	 	option.type = 'button';
		   	 	switch(whatToProcess){
				case 'FF_MATCHID-OPTIONS':
					option.name = 'populate_ff_matchId_btn';
			    	option.value = 'Populate FF Match ID';
					break;
				case 'FF_MATCHID_DOUBLE-OPTIONS':
					option.name = 'populate_ff_doublematchId_btn';
			    	option.value = 'Populate FF DoubleMatch ID';
					break
				case 'FF_MATCH_RESULTSINGLES-OPTIONS':
					option.name = 'populate_ff_match_result_btn';
			    	option.value = 'Populate FF DoubleMatch Result';
					break
				case 'FF_MATCH_RESULTDOUBLES-OPTIONS':
					option.name = 'populate_ff_doublematch_result_btn';
			    	option.value = 'Populate FF DoubleMatch Result';
					break
				}
			    option.id = option.name;
			    option.setAttribute('onclick',"processUserSelection(this)");
			    
			    div = document.createElement('div');
			    div.append(option);
	
				option = document.createElement('input');
				option.type = 'button';
				option.name = 'cancel_graphics_btn';
				option.id = option.name;
				option.value = 'Cancel';
				option.setAttribute('onclick','processUserSelection(this)');
		
			    div.append(option);
			    
			    row.insertCell(1).appendChild(div);
			    
				document.getElementById('select_graphic_options_div').style.display = '';
				break;
		}
		break;
		
		
	case 'SINGLE-MATCHPROMO-OPTIONS': case 'DOUBLE-MATCHPROMO-OPTIONS': case 'SINGLE-LT_MATCHPROMO-OPTIONS': case 'DOUBLE-LT_MATCHPROMO-OPTIONS':
	case 'TIE_RESULT-OPTIONS': case 'LT_SINGLE-MATCHPROMO-OPTIONS': case 'LT_DOUBLE-MATCHPROMO-OPTIONS':
		switch ($('#selectedBroadcaster').val().toUpperCase()){
		case 'ATP_2022': case 'ATP_CHALLENGERS': case 'TPL_2023':
			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			switch(whatToProcess){
				case 'TIE_RESULT-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectTieResult';
					select.name = select.id;
					
					dataToProcess.forEach(function(tr,index,arr1){
						option = document.createElement('option');
						option.value = tr.matchNumber;
						option.text = tr.matchNumber + '. ' + tr.home_Team.teamName1 + ' - ' + tr.away_Team.teamName1;
						select.appendChild(option);
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_tie_results_btn';
				    option.value = 'Populate Tie Result';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'SINGLE-MATCHPROMO-OPTIONS': case 'LT_SINGLE-MATCHPROMO-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectSingleMatchPromo';
					select.name = select.id;
					
					dataToProcess.forEach(function(smp,index,arr1){
						if(smp.homePlayerSecond == 0 && smp.awayPlayerSecond == 0){
							option = document.createElement('option');
							option.value = smp.matchId;
							option.text = smp.gameNumber + '. ' + smp.home_FirstPlayer.full_name + ' - ' + smp.away_FirstPlayer.full_name;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectPhoto';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'without';
					option.text = 'Without Photo';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'with';
					option.text = 'With Photo';
					select.appendChild(option);
					
					row.insertCell(1).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
			   	 	switch(whatToProcess){
					case 'SINGLE-MATCHPROMO-OPTIONS':
						option.name = 'populate_single_match_promo_btn';
						break;
					case 'LT_SINGLE-MATCHPROMO-OPTIONS':
						option.name = 'populate_lt_single_match_promo_btn';
						break;
					}
				    option.value = 'Populate Matah Promo';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(2).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'SINGLE-LT_MATCHPROMO-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectSingleltMatchPromo';
					select.name = select.id;
					
					dataToProcess.forEach(function(smp,index,arr1){
						if(smp.homePlayerSecond == 0 && smp.awayPlayerSecond == 0){
							option = document.createElement('option');
							option.value = smp.matchnumber;
							option.text = smp.matchnumber + '. ' +smp.home_FirstPlayer.full_name + ' - ' + smp.away_FirstPlayer.full_name ;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_single_ltmatch_promo_btn';
				    option.value = 'Populate Matah Promo';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'DOUBLE-MATCHPROMO-OPTIONS': case 'LT_DOUBLE-MATCHPROMO-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectDoubleMatchPromo';
					select.name = select.id;
					
					dataToProcess.forEach(function(smp,index,arr1){
						if(smp.homePlayerSecond != 0 && smp.awayPlayerSecond != 0){
							option = document.createElement('option');
							option.value = smp.matchId;
							option.text = smp.gameNumber + '. ' +smp.home_FirstPlayer.ticker_name + ' / ' + smp.home_SecondPlayer.ticker_name + ' - ' + 
								smp.away_FirstPlayer.ticker_name + ' / ' + smp.away_SecondPlayer.ticker_name;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectPhoto';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'without';
					option.text = 'Without Photo';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'with';
					option.text = 'With Photo';
					select.appendChild(option);
					
					row.insertCell(1).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
			   	 	switch(whatToProcess){
					case 'DOUBLE-MATCHPROMO-OPTIONS':
						option.name = 'populate_double_match_promo_btn';
						break;
					case 'LT_DOUBLE-MATCHPROMO-OPTIONS':
						option.name = 'populate_ltdouble_match_promo_btn';
						break;
					}
				    option.value = 'Populate Matah Promo';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(2).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'DOUBLE-LT_MATCHPROMO-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectltDoubleMatchPromo';
					select.name = select.id;
					
					dataToProcess.forEach(function(smp,index,arr1){
						if(smp.homePlayerSecond != 0 && smp.awayPlayerSecond != 0){
							option = document.createElement('option');
							option.value = smp.matchnumber;
							option.text = smp.matchnumber + '. ' +smp.home_FirstPlayer.ticker_name + ' / ' + smp.home_SecondPlayer.ticker_name + ' - ' + 
								smp.away_FirstPlayer.ticker_name + ' / ' + smp.away_SecondPlayer.ticker_name;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_lt_double_match_promo_btn';
				    option.value = 'Populate Matah Promo';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
			}
			break;
		}
		break;
	case 'APIDATA-OPTIONS':
		api_value_home = '';
		api_value_away = '';
		header_text = document.createElement('h6');
		header_text.innerHTML = 'DOAD API DATA';
		document.getElementById('select_graphic_options_div').appendChild(header_text);
		
		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');

		table.appendChild(tbody);
		document.getElementById('select_graphic_options_div').appendChild(table);

		row = tbody.insertRow(tbody.rows.length);
		alert(dataToProcess.playerTeam1.sets[0].stats.servicestats.aces.number);
		header_text = document.createElement('h6');
		if(dataToProcess.playerTeam1.sets.length > 0) {
			for(var i = 0; i <= dataToProcess.apiData.length -1; i++ ) {
				
			}
			//header_text.innerHTML = header_text.innerHTML  + home_name + ' : ' + '[ ' + api_value_home + ' ]' + "<br>" + "<br>" 
									//+ away_name  + ' : ' + '[ ' + api_value_away + ' ]';
			row.insertCell(0).appendChild(header_text);
			
		}
		break;
	case 'ORDER_OF_TIE-OPTIONS': case 'ORDER_OF_MATCH-OPTIONS': case 'LT_TEAM-OPTIONS':
		switch ($('#selectedBroadcaster').val().toUpperCase()){
			case 'TPL_2023':
				$('#select_graphic_options_div').empty();
	
				header_text = document.createElement('h6');
				header_text.innerHTML = 'Select Graphic Options';
				document.getElementById('select_graphic_options_div').appendChild(header_text);
				
				table = document.createElement('table');
				table.setAttribute('class', 'table table-bordered');
						
				tbody = document.createElement('tbody');
		
				table.appendChild(tbody);
				document.getElementById('select_graphic_options_div').appendChild(table);
				
				row = tbody.insertRow(tbody.rows.length);
				switch(whatToProcess){
				case 'LT_TEAM-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:250px';
					select.id = 'selectLtTeam';
					select.name = select.id;
					
					dataToProcess.forEach(function(tm,index,arr1){
						option = document.createElement('option');
						option.value = tm.teamId;
						option.text = tm.teamName1;
						select.appendChild(option);
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_lt_team_btn';
				    option.value = 'Populate LT Team';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
					
				case 'ORDER_OF_TIE-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:250px';
					select.id = 'selectOrderOfTie';
					select.name = select.id;
					
					for(var i=1;i<=7;i++){
						option = document.createElement('option');
						option.value = i;
						option.text = 'DAY ' + i ;
						select.appendChild(option);
					}
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_order_of_tie_btn';
				    option.value = 'Populate Order Of Tie';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
					
				case 'ORDER_OF_MATCH-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectOrderOfMatch';
					select.name = select.id;
					
					dataToProcess.forEach(function(tr,index,arr1){
						option = document.createElement('option');
						option.value = tr.matchNumber;
						option.text = tr.matchNumber + '. ' + tr.home_Team.teamName1 + ' - ' + tr.away_Team.teamName1;
						select.appendChild(option);
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_order_of_match_btn';
				    option.value = 'Populate Order Of Match';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				}
				break;
		}
		break;	
		
	case 'NAMESUPER-OPTIONS': case 'NAMESUPER-SP-OPTIONS': case 'NAMESUPER-DP-OPTIONS': case 'NAMESUPER-SP1-OPTIONS': case 'NAMESUPER-DP1-OPTIONS':
	case 'CROSS-OPTIONS': case 'FF_PLAYERPROFILE-OPTIONS': case 'LT_PLAYERPROFILE-OPTIONS': case 'MATCH_FF_PLAYERPROFILE': case 'MATCH_LT_PLAYERPROFILE':
		switch ($('#selectedBroadcaster').val().toUpperCase()) {
		case 'ATP_2022': case 'ATP_CHALLENGERS': case 'TPL_2023':

			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			switch(whatToProcess){
				case 'CROSS-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:250px';
					select.id = 'selectCross1';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'home';
					option.text = match_data.homeFirstPlayer.full_name ;
					if(match_data.matchType.toLowerCase() == 'doubles' && match_data.homeSecondPlayerId > 0) {
					    option.text = option.text + ' / ' + match_data.homeSecondPlayer.full_name;
					}
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'away';
					option.text = match_data.awayFirstPlayer.full_name ;
					if(match_data.matchType.toLowerCase() == 'doubles' && match_data.awaySecondPlayerId > 0) {
					    option.text = option.text + ' / ' + match_data.awaySecondPlayer.full_name;
					}
					select.appendChild(option);
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_cross_btn';
				    option.value = 'Populate Cross';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					
					break;
				case 'MATCH_FF_PLAYERPROFILE':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectPlayerProfile';
					select.name = select.id;
					
					dataToProcess.forEach(function(mffpp,index,arr1){
						if(home_first_player_id == mffpp.playerId){
							option = document.createElement('option');
							option.value = mffpp.playerId;
							option.text = mffpp.full_name ;
							select.appendChild(option);
						}
						if(match_data.matchType.toLowerCase() == 'doubles' && home_second_player_id == mffpp.playerId){
							option = document.createElement('option');
							option.value = mffpp.playerId;
							option.text = mffpp.full_name ;
							select.appendChild(option);
						}
						if(away_first_player_id == mffpp.playerId){
							option = document.createElement('option');
							option.value = mffpp.playerId;
							option.text = mffpp.full_name ;
							select.appendChild(option);
						}
						if(match_data.matchType.toLowerCase() == 'doubles' && away_second_player_id == mffpp.playerId){
							option = document.createElement('option');
							option.value = mffpp.playerId;
							option.text = mffpp.full_name ;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_ff_player_profile_match_btn';
				    option.value = 'Populate FF Profile';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'MATCH_LT_PLAYERPROFILE':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectLtPlayerProfile';
					select.name = select.id;
					
					dataToProcess.forEach(function(mffpp,index,arr1){
						if(home_first_player_id == mffpp.playerId){
							option = document.createElement('option');
							option.value = mffpp.playerId;
							option.text = mffpp.full_name ;
							select.appendChild(option);
						}
						if(match_data.matchType.toLowerCase() == 'doubles' && home_second_player_id == mffpp.playerId){
							option = document.createElement('option');
							option.value = mffpp.playerId;
							option.text = mffpp.full_name ;
							select.appendChild(option);
						}
						if(away_first_player_id == mffpp.playerId){
							option = document.createElement('option');
							option.value = mffpp.playerId;
							option.text = mffpp.full_name ;
							select.appendChild(option);
						}
						if(match_data.matchType.toLowerCase() == 'doubles' && away_second_player_id == mffpp.playerId){
							option = document.createElement('option');
							option.value = mffpp.playerId;
							option.text = mffpp.full_name ;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_lt_player_profile_match_btn';
				    option.value = 'Populate LT Profile';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'FF_PLAYERPROFILE-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectPlayerProfile';
					select.name = select.id;
					
					dataToProcess.forEach(function(fpp,index,arr1){
						option = document.createElement('option');
						option.value = fpp.playerId;
						option.text = fpp.full_name;
						select.appendChild(option);
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_ff_player_profile_btn';
				    option.value = 'Populate FF Profile';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					
					break;
				case 'LT_PLAYERPROFILE-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectLtPlayerProfile';
					select.name = select.id;
					
					dataToProcess.forEach(function(tpp,index,arr1){
						option = document.createElement('option');
						option.value = tpp.playerId;
						option.text = tpp.full_name;
						select.appendChild(option);
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_lt_player_profile_btn';
				    option.value = 'Populate LT Profile';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					
					break;
				case 'NAMESUPER-DP1-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSuperdphome';
					select.name = select.id;
					
					dataToProcess.forEach(function(nsp,index,arr1){
						option = document.createElement('option');
						option.value = nsp.playerId;
						option.text = nsp.full_name;
						select.appendChild(option);
					});
					
					row.insertCell(0).appendChild(select);
					
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSuperdpaway';
					select.name = select.id;
					
					dataToProcess.forEach(function(nsp,index,arr1){
						option = document.createElement('option');
						option.value = nsp.playerId;
						option.text = nsp.full_name;
						select.appendChild(option);
					});
					
					row.insertCell(1).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_namesuper_dp1_btn';
				    option.value = 'Populate Namesuper';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(2).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					
					break;
				case 'NAMESUPER-SP1-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSupersp1';
					select.name = select.id;
					
					dataToProcess.forEach(function(nsp,index,arr1){
						option = document.createElement('option');
						option.value = nsp.playerId;
						option.text = nsp.full_name;
						select.appendChild(option);
					});
					
					
					row.insertCell(0).appendChild(select);
					
					if($('#selectedBroadcaster').val().toUpperCase() == 'TPL_2023'){
						select = document.createElement('select');
						select.style = 'width:130px';
						select.id = 'selectDesignation';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = 'player';
						option.text = 'PLAYER';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'playerOfTheMatch';
						option.text = 'PLAYER OF THE MATCH';
						select.appendChild(option);
						
						row.insertCell(1).appendChild(select);
					}
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_namesuper_sp1_btn';
				    option.value = 'Populate Namesuper';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(2).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					
					break;
				case 'NAMESUPER-DP-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSuperdp';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'home';
					option.text = 'Home' ;
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'away';
					option.text = 'Away' ;
					select.appendChild(option);
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_namesuperdp_btn';
				    option.value = 'Populate Namesuper';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'NAMESUPER-SP-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSupersp';
					select.name = select.id;
					
					dataToProcess.forEach(function(nsp,index,arr1){
						if(home_first_player_id == nsp.playerId){
							option = document.createElement('option');
							option.value = nsp.playerId;
							option.text = nsp.full_name ;
							select.appendChild(option);
						}
						if(away_first_player_id == nsp.playerId){
							option = document.createElement('option');
							option.value = nsp.playerId;
							option.text = nsp.full_name ;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_namesupersp_btn';
				    option.value = 'Populate Namesuper';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				   
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				
				case'NAMESUPER-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSuper';
					select.name = select.id;
					
					dataToProcess.forEach(function(ns,index,arr1){
						option = document.createElement('option');
						option.value = ns.namesuperId;
						option.text = ns.subHeader ;
						select.appendChild(option);
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_namesuper_btn';
				    option.value = 'Populate Namesuper';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					
					break;
			}
			break;
		}
		break;

	case 'LOAD_EVENTS':
		
		$('#select_event_div').empty();
		
		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
		tbody = document.createElement('tbody');
        row = tbody.insertRow(tbody.rows.length);
		
		header_text = document.createElement('h6');
		var serve;
		if(dataToProcess.sets.length > 0){
		 serve = (dataToProcess.sets && dataToProcess.sets.length > 0 && dataToProcess.sets[dataToProcess.sets.length - 1].games 
		 	&& dataToProcess.sets[dataToProcess.sets[dataToProcess.sets.length - 1].games.length - 1] && 
		 	dataToProcess.sets[dataToProcess.sets.length - 1].games[dataToProcess.sets[dataToProcess.sets.length - 1].games.length - 1].serving_player) || null;
			//alert(serve);
			if(dataToProcess.homeFirstPlayerId == serve){
				header_text.innerHTML = 'Serve : ' + dataToProcess.homeFirstPlayer.full_name;
			}else if(dataToProcess.awayFirstPlayerId == serve){
				header_text.innerHTML = 'Serve : ' + dataToProcess.awayFirstPlayer.full_name;
			}
			
			if(dataToProcess.matchType.toLowerCase() == 'doubles' && dataToProcess.homeSecondPlayerId == serve) {
				header_text.innerHTML = 'Serve : ' + dataToProcess.homeSecondPlayer.full_name;
			}else if(dataToProcess.matchType.toLowerCase() == 'doubles' && dataToProcess.awaySecondPlayerId == serve){
				header_text.innerHTML = 'Serve : ' + dataToProcess.awaySecondPlayer.full_name;
			}
		}
		header_text.style.cssText = 'padding: 1px; text-align: center; font-family: Arial, sans-serif; font-weight: bold; line-height: 2;font-size: 18px;';
		row.insertCell(0).appendChild(header_text);
		table.appendChild(tbody);
		document.getElementById('select_event_div').appendChild(table);
	    
		break;			
	case 'LOAD_MATCH_DETAIL':
		
		$('#tennis_div').empty();
		
		//alert(dataToProcess.homeFirstPlayer.full_name);
		if (dataToProcess)
		{
			 var style = document.createElement('style');
	        style.innerHTML = `
	            th, td {
	                padding: 12px;
	                text-align: center;
	                border: 1px solid #ddd;
	                font-family: Arial, sans-serif;
	                font-weight: bold;
	                font-size: 16px;
	            }
	            table {
	                width: 100%;
	                border-collapse: collapse;
	                margin-top: 20px;
	            }
	            th {
	                background-color: #f2f2f2;
	                color: #333;
	            }
	            td {
	                background-color: #fff;
	                color: #555;
	            }
	            table td, table th {
	                border: 1px solid #ddd;
	            }
	            table tr:nth-child(even) {
	                background-color: #f9f9f9;
	            }
	            table tr:hover {
	                background-color: #f1f1f1;
	            }
	        `;
	        document.head.appendChild(style);
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			tbody = document.createElement('tbody');
			row = tbody.insertRow(tbody.rows.length);
			for (var j = 1; j <= (dataToProcess.sets.length + 1); j++) {
				cell = row.insertCell(row.cells.length);
			    th = document.createElement('th'); //column
			    switch (j) {
				case 1:
				    cell.innerHTML = 'PLAYER NAME'; 
					break;
				default:
				    cell.innerHTML = 'SET -  ' + dataToProcess.sets[j-2].set_number; 
					break;
				}
			}
						
			for (var j = 0; j <= 1; j++) {
				row = tbody.insertRow(tbody.rows.length);
				cell = row.insertCell(row.cells.length);
			    switch (j) {
				case 0:
				    cell.innerHTML = dataToProcess.homeFirstPlayer.full_name;
					if(dataToProcess.matchType.toLowerCase() == 'doubles' && dataToProcess.homeSecondPlayerId > 0) {
					    cell.innerHTML = cell.innerHTML + ' / ' + dataToProcess.homeSecondPlayer.full_name;
					}
					dataToProcess.sets.forEach(function(set,set_index,set_arr){
						text = 0; 
						set.games.forEach(function(game,game_index,game_arr){
							if(game.game_status.toLowerCase() == 'end' && game.game_winner.toLowerCase() == 'home') {
								text = text + 1;
							}
							if(game.game_status.toLowerCase() == 'start') {
								$('#homeScore').val(game.home_score);
							}
							
						});
						cell = row.insertCell(row.cells.length);
						cell.innerHTML = text;
					});
					break;
				case 1:
				    cell.innerHTML = dataToProcess.awayFirstPlayer.full_name;
					if(dataToProcess.matchType.toLowerCase() == 'doubles' && dataToProcess.awaySecondPlayerId > 0) {
					    cell.innerHTML = cell.innerHTML + ' / ' + dataToProcess.awaySecondPlayer.full_name;
					}
					
					dataToProcess.sets.forEach(function(set,set_index,set_arr){
						text = 0; 
						set.games.forEach(function(game,game_index,game_arr){
							if(game.game_status.toLowerCase() == 'end' && game.game_winner.toLowerCase() == 'away') {
								text = text + 1;
							}
							if(game.game_status.toLowerCase() == 'start') {
								$('#awayScore').val(game.away_score);
							}
						});
						cell = row.insertCell(row.cells.length);
						cell.innerHTML = text;
					});
					break;
				}
			}
			table.appendChild(tbody);
			document.getElementById('tennis_div').appendChild(table);
		}
	break;
	}
}
function removeSelectDuplicates(select_id)
{
	var this_list = {};
	$("select[id='" + select_id + "'] > option").each(function () {
	    if(this_list[this.text]) {
	        $(this).remove();
	    } else {
	        this_list[this.text] = this.value;
	    }
	});
}
function checkEmpty(inputBox,textToShow) {

	var name = $(inputBox).attr('id');
	
	document.getElementById(name + '-validation').innerHTML = '';
	document.getElementById(name + '-validation').style.display = 'none';
	$(inputBox).css('border','');
	if(document.getElementById(name).value.trim() == '') {
		$(inputBox).css('border','#E11E26 2px solid');
		document.getElementById(name + '-validation').innerHTML = textToShow + ' required';
		document.getElementById(name + '-validation').style.display = '';
		document.getElementById(name).focus({preventScroll:false});
		return false;
	}
	return true;	
}	
