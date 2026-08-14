package com.tennis.broadcaster;

import com.tennis.containers.ScoreBug;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import com.tennis.model.API_Tournament;
import com.tennis.model.Fixture;
import com.tennis.model.Game;
import com.tennis.model.LeagueTable;
import com.tennis.model.LeagueTeam;
import com.tennis.model.LiveMatchStatsAPI;
import com.tennis.model.Match;
import com.tennis.model.NameSuper;
import com.tennis.model.Player;
import com.tennis.model.Result;
import com.tennis.model.Set;
import com.tennis.model.Statistics;
import com.tennis.model.Team;
import com.tennis.model.VariousText;
import com.tennis.service.TennisService;
import com.tennis.util.TennisFunctions;
import com.tennis.util.TennisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennis.containers.Scene;

public class TPL_2023 extends Scene {

	public String session_selected_broadcaster = "TPL_2023";

	public ScoreBug scorebug = new ScoreBug();
	public String which_graphics_onscreen = "",which_gfx="";
	public boolean is_infobar = false;
	public long last_date = 0;
	int pastHomeScore = 0;
	int pastAwayScore = 0;
	int homeWon = 0;
	int awayWon = 0;
	boolean isVisited = false;
	public ObjectMapper objectMapper = new ObjectMapper();
	public String status;
	public String flag_path_viz = "IMAGE*/Default/Flags/";
	public String color1_path = "IMAGE*/Default/Colors01/";
	public String color2_path = "IMAGE*/Default/Colors02/";
	public String textcolor1_path = "IMAGE*/Default/Text01/";
	public String textcolor2_path = "IMAGE*/Default/Text02/";
	public String logo_path = "IMAGE*/Default/Logos/";
	private String left_photo_path = "C:\\\\Images\\\\TPL\\\\Left\\\\";
	private String right_photo_path = "C:\\\\Images\\\\TPL\\\\Right\\\\";
	
	public TPL_2023() {
		super();
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public ScoreBug updateScoreBug(List<Scene> scenes, Match match, TennisService tennisService, PrintWriter print_writer)
			throws InterruptedException, MalformedURLException, IOException, JAXBException {
		if (scorebug.isScorebug_on_screen() == true) {
			scorebug = populateScoreBug(true,scorebug,print_writer,scenes.get(0).getScene_path(),match,
					tennisService,session_selected_broadcaster);
		}
		return scorebug;
	}

	public Object ProcessGraphicOption(String whatToProcess, Match match, TennisService tennisService,PrintWriter print_writer, List<Scene> scenes, String valueToProcess)
			throws Exception {
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-SCOREBUG": case "POPULATE-SCOREBUG_STATS": case "POPULATE-SCOREBUG_SET_STATS": case "POPULATE-SCOREBUG_HEADER": case "POPULATE-SCOREBUG_BAR_STATS":
		case "POPULATE-SCOREBUG_GAME_POINTS":	
		case "POPULATE-LT-MATCH_RESULTSINGLES": case "POPULATE-LT-MATCH_RESULTDOUBLES": case "POPULATE-LT-MATCHID": case "POPULATE-LT-MATCHID_DOUBLE": 
		case "POPULATE-NAMESUPERDB": case "POPULATE-NAMESUPER-SP": case "POPULATE-NAMESUPER-DP": case "POPULATE-NAMESUPER-SP1": case "POPULATE-NAMESUPER-DP1": 
		case "POPULATE-LT-MATCH_SCORESINGLES": case "POPULATE-SINGLE_LT_MATCHPROMO": case "POPULATE-LT_DOUBLE_MATCHPROMO": case "POPULATE-LT-MATCH_SCOREDOUBLES":
		case "POPULATE-MATCHID_DOUBLE": case "POPULATE-MATCHID": case "POPULATE-FF-MATCH_RESULTSINGLES": case "POPULATE-FF-MATCH_RESULTDOUBLES": 
		case "POPULATE-SINGLE_MATCHPROMO": case "POPULATE-DOUBLE_MATCHPROMO": case "POPULATE-MATCH_STATS": case "POPULATE-SPEED": case "POPULATE-LOCATOR": 
		case "POPULATE-TIE_RESULT": case "POPULATE-CROSS": case "POPULATE-ORDER_OF_TIE": case "POPULATE-FF_PLAYERPROFILE": case "POPULATE-POINTS_TABLE": 
		case "POPULATE-ORDER_OF_MATCH": case "POPULATE-POINTS_PROGRESS": case "POPULATE-LT_PLAYERPROFILE": case "POPULATE-LT_TEAM": case "POPULATE-LT_MATCHID_SCORE": 
		case "POPULATE-LT_DOUBLEMATCHID_SCORE": case "POPULATE-LT_SINGLEMATCH_PROMO": case "POPULATE-LT_DOUBLEMATCH_PROMO":
			switch (whatToProcess.toUpperCase()) {
			
			case "POPULATE-SCOREBUG_STATS": case "POPULATE-SCOREBUG_SET_STATS": case "POPULATE-SCOREBUG_HEADER": case "POPULATE-SCOREBUG_BAR_STATS":
			case "POPULATE-SCOREBUG_GAME_POINTS":
				 break;
			 
			case "POPULATE-SCOREBUG":
				scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
				break;
			default:
				which_gfx = valueToProcess.split(",")[0];
				scenes.get(1).setScene_path(valueToProcess.split(",")[0]);
				scenes.get(1).scene_load(print_writer, session_selected_broadcaster);
				break;
			}
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-SCOREBUG":
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Reaet START \0");
				populateScoreBug(false, scorebug, print_writer, valueToProcess.split(",")[0], match,tennisService,session_selected_broadcaster);
				break;
			case "POPULATE-SCOREBUG_HEADER":
				if(scorebug.getLast_scorebug_stat() != null && !scorebug.getLast_scorebug_stat().trim().isEmpty()) {
					switch(scorebug.getLast_scorebug_stat()) {
					case "aces": case "double_faults": case "winners": case "unforced_errors":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*PopUpOut START \0");
						TimeUnit.MICROSECONDS.sleep(600);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*PopUpIn SHOW 0.0 \0");
						break;
					}
					scorebug.setScorebug_stat("");
					scorebug.setLast_scorebug_stat("");
				}
				
				populateScoreBugHeader(false,scorebug,print_writer,valueToProcess.split(",")[0],match,session_selected_broadcaster);
				break;
			case "POPULATE-SCOREBUG_GAME_POINTS":
				System.out.println(valueToProcess);
				populateScoreBugGamePoints(false,scorebug,print_writer,valueToProcess.split(",")[0],valueToProcess.split(",")[1],match,session_selected_broadcaster);
				break;
				
			case "POPULATE-SCOREBUG_SET_STATS":
				if(scorebug.isGame_header_on_screen() == true) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ExtraInfoOut START \0");
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ExtraInfoIn SHOW 0.0 \0");
					scorebug.setGame_header_on_screen(false);
					
					TimeUnit.MILLISECONDS.sleep(500);
					scorebug.setScorebug_stat(valueToProcess);
					populateScoreBugStatsSet(false,scorebug,print_writer,match,session_selected_broadcaster);
				}
				else if(scorebug.getLast_scorebug_stat() != null && !scorebug.getLast_scorebug_stat().trim().isEmpty()) {
					switch(scorebug.getLast_scorebug_stat()) {
					case "aces": case "double_faults": case "winners": case "unforced_errors":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*PopUpOut START \0");
						TimeUnit.MICROSECONDS.sleep(600);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*PopUpIn SHOW 0.0 \0");
						break;
					}
					TimeUnit.MILLISECONDS.sleep(500);
					scorebug.setScorebug_stat(valueToProcess);
					populateScoreBugStatsSet(false,scorebug,print_writer,match,session_selected_broadcaster);
				}else {
					System.out.println(valueToProcess);
					scorebug.setScorebug_stat(valueToProcess);
					populateScoreBugStatsSet(false,scorebug,print_writer,match,session_selected_broadcaster);
				}
				break;
			case "POPULATE-LT-MATCH_SCORESINGLES":
				populateLtMatchScoreSingles(print_writer, valueToProcess.split(",")[0], match,session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCH_SCOREDOUBLES":
				populateLtMatchScoreDoubles(print_writer, valueToProcess.split(",")[0], match,session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCH_RESULTSINGLES":
				populateLtMatchResultSingles(print_writer, valueToProcess.split(",")[0], match, tennisService,session_selected_broadcaster);
				break;
			case "POPULATE-FF-MATCH_RESULTSINGLES":
				populateFFMatchResultSingles(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],tennisService,match,session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCH_RESULTDOUBLES":
				populateLtMatchResultDoubles(print_writer, valueToProcess.split(",")[0],match,session_selected_broadcaster);
				break;
			case "POPULATE-FF-MATCH_RESULTDOUBLES":
				populateFFMatchResultDoubles(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],tennisService,match,session_selected_broadcaster);
				break;
			case "POPULATE-MATCHID_DOUBLE":
				populateMatchIdDouble(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],tennisService,match, session_selected_broadcaster);
				break;
			case "POPULATE-MATCHID":
				populateMatchId(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],tennisService,match,session_selected_broadcaster);
				break;
				
			case "POPULATE-LT_MATCHID_SCORE": case "POPULATE-LT_DOUBLEMATCHID_SCORE": case "POPULATE-LT_SINGLEMATCH_PROMO": case "POPULATE-LT_DOUBLEMATCH_PROMO":
				populateLtMatchIdScoreOrPromo(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],valueToProcess.split(",")[2],
						tennisService, TennisFunctions.processAllFixtures(tennisService),match,session_selected_broadcaster);
				break;
				
			case "POPULATE-LOCATOR":
				populateLocator(print_writer, valueToProcess.split(",")[0],tennisService,match,tennisService.getFixtures(),session_selected_broadcaster);
				break;
			case "POPULATE-POINTS_TABLE":
				LeagueTable league_table = null;
				if(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.LEAGUE_TABLE_DIRECTORY + TennisUtil.LEAGUETABLE_XML).exists()) {
					league_table = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
							new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.LEAGUE_TABLE_DIRECTORY + TennisUtil.LEAGUETABLE_XML));
				}
				
				populatePointsTable(print_writer,valueToProcess.split(",")[0],tennisService,match,league_table.getLeagueTeams(),tennisService.getAllTeams(),session_selected_broadcaster);
				break;
				
			case "POPULATE-LT_TEAM":
				populateLtTeam(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]), tennisService,
						tennisService.getAllTeams(), match, session_selected_broadcaster);
				break;
				
			case "POPULATE-ORDER_OF_TIE":
				populateOrderOfTie(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService,tennisService.getResults(), tennisService.getAllTeams(), tennisService.getVariousTexts(),match,session_selected_broadcaster);
				break;
			case "POPULATE-ORDER_OF_MATCH":
				populateOrderOfMatch(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService,
						tennisService.getFixtures(),tennisService.getAllPlayer(), tennisService.getAllTeams(), tennisService.getResults(),match,session_selected_broadcaster);
				break;
			case "POPULATE-TIE_RESULT":
				populateTieResult(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService,tennisService.getResults(),
						tennisService.getAllTeams(),tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-SINGLE_MATCHPROMO":
				populateMatchPromo(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),valueToProcess.split(",")[2],
						tennisService,tennisService.getFixtures(),tennisService.getAllPlayer(),tennisService.getAllTeams(),tennisService.getVariousTexts(),
						match, session_selected_broadcaster);
				break;
			case "POPULATE-SINGLE_LT_MATCHPROMO":
				populateLtMatchPromo(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getFixtures(),
						tennisService.getAllPlayer(),tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-LT_DOUBLE_MATCHPROMO":
				populateLtMatchDoublePromo(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getFixtures(),
						tennisService.getAllPlayer(),tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-DOUBLE_MATCHPROMO":
				populateMatchDoublePromo(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),valueToProcess.split(",")[2],
						tennisService,tennisService.getFixtures(),tennisService.getAllPlayer(),tennisService.getAllTeams(),tennisService.getVariousTexts(),
						match, session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCHID":
				populateltMatchId(print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCHID_DOUBLE":
				populateltMatchIdDouble(print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
				break;
			case "POPULATE-NAMESUPERDB":
				populateNameSuperDB(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getNameSupers(),
						tennisService.getAllTeams(),match, session_selected_broadcaster);
				break;
			case "POPULATE-NAMESUPER-SP":
				populateNameSuperSP(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getAllPlayer(),
						tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-NAMESUPER-SP1":
				populateNameSuperSP1(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),valueToProcess.split(",")[2],tennisService.getAllPlayer(),
						tennisService.getAllTeams(),tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
//			case "POPULATE-NAMESUPER-DP":
//				populateNameSuperDP(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],tennisService.getAllPlayer(),
//						tennisService.getVariousTexts(),match, session_selected_broadcaster);
//				break;
//			case "POPULATE-NAMESUPER-DP1":
//				populateNameSuperDP1(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),Integer.valueOf(valueToProcess.split(",")[2]),
//						tennisService.getAllPlayer(),tennisService.getVariousTexts(),match, session_selected_broadcaster);
//				break;
			case "POPULATE-CROSS":
				populateCross(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],match,tennisService.getAllTeams(), session_selected_broadcaster);
				break;
			case "POPULATE-SPEED":
				populateSpeed(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),match, session_selected_broadcaster);
				break;
			case "POPULATE-MATCH_STATS":
//				WorkBook workbook = null;
//				if(new File(TennisUtil.TENNIS_DIRECTORY + "matchStats.xml").exists()) {
//					workbook = (WorkBook)JAXBContext.newInstance(WorkBook.class).createUnmarshaller().unmarshal(new File(TennisUtil.TENNIS_DIRECTORY + 
//							"matchStats.xml"));
//				}
//				populateMatchStats(print_writer, valueToProcess.split(",")[0],workbook,tennisService , tennisService.getAllTeams(),match, session_selected_broadcaster);

				API_Tournament  ApiMatch = TennisFunctions.getMatchStatApi("https://tplsport.net.in/tpl6/json.php?mid="+ match.getMatchId());

				populateMatchStats(print_writer, valueToProcess.split(",")[0],ApiMatch,tennisService,tennisService.getAllTeams(),match, session_selected_broadcaster);
				break;
			case "POPULATE-FF_PLAYERPROFILE":
				populateFFPlayerProfile(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getAllPlayer(),
						tennisService.getAllTeams(),tennisService.getStatistics(),match, session_selected_broadcaster);
				break;
			case "POPULATE-POINTS_PROGRESS":
				populatePointsProgress(print_writer, valueToProcess.split(",")[0],tennisService,tennisService.getResults(),
						tennisService.getAllTeams(), match, session_selected_broadcaster);
				break;
			case "POPULATE-LT_PLAYERPROFILE":
				populateLtPlayerProfile(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getAllPlayer(),
						tennisService.getAllTeams(),tennisService.getStatistics(),match, session_selected_broadcaster);
				break;
			}
			
		case "NAMESUPER_GRAPHICS-OPTIONS": 
			return objectMapper.writeValueAsString(tennisService.getNameSupers());
		case "NAMESUPER-SP_GRAPHICS-OPTIONS": case "NAMESUPER-SP1_GRAPHICS-OPTIONS": case "NAMESUPER-DP1_GRAPHICS-OPTIONS":
			return objectMapper.writeValueAsString(tennisService.getAllPlayer());
		case "FF_PLAYERPROFILE_GRAPHICS-OPTIONS": case "LT_PLAYERPROFILE_GRAPHICS-OPTIONS":
			return objectMapper.writeValueAsString(TennisFunctions.processMatchPlayers(tennisService, match));
		case "SINGLE-MATCHPROMO_GRAPHICS-OPTIONS": case "DOUBLE-MATCHPROMO_GRAPHICS-OPTIONS": case "SINGLE-LT_MATCHPROMO_GRAPHICS-OPTIONS": 
		case "DOUBLE-LT_MATCHPROMO_GRAPHICS-OPTIONS": case "LT_SINGLE-MATCHPROMO_GRAPHICS-OPTIONS": case "LT_DOUBLE-MATCHPROMO_GRAPHICS-OPTIONS":
			return objectMapper.writeValueAsString(TennisFunctions.processAllFixtures(tennisService));
		case "TIE_RESULT_GRAPHICS-OPTIONS": case "ORDER_OF_MATCH_GRAPHICS-OPTIONS":
			return objectMapper.writeValueAsString(TennisFunctions.processAllResults(tennisService));
		case "LT-TEAM_GRAPHICS-OPTIONS":
			return objectMapper.writeValueAsString(TennisFunctions.processMatchTeams(tennisService, match));

		case "ANIMATE-IN-SCOREBUG": case "ANIMATE-IN-SCOREBUG_GAMEINFO":
			
		case "ANIMATE-LT-MATCH_RESULTSINGLES": case "ANIMATE-LT-MATCH_RESULTDOUBLES": case "ANIMATE-IN-LT_MATCHID": case "ANIMATE-IN-LT-MATCHID_DOUBLE": case "ANIMATE-LT-NAMESUPERDB":
		case "ANIMATE-LT-NAMESUPER_SP": case "ANIMATE-LT-NAMESUPER_DP": case "ANIMATE-LT-NAMESUPER_SP1": case "ANIMATE-LT-NAMESUPER_DP1":
		case "ANIMATE-LT-MATCH_SCORESINGLES": case "ANIMATE-LT-MATCH_SCOREDOUBLES": case "ANIMATE-LT-SINGLE_LT_MATCHPROMO": case "ANIMATE-LT-DOUBLE_LT_MATCHPROMO":
		case "ANIMATE-IN-MATCHID_DOUBLE": case "ANIMATE-IN-MATCHID": case "ANIMATE-FF-MATCH_RESULTSINGLES": case "ANIMATE-FF-MATCH_RESULTDOUBLES":
		case "ANIMATE-IN-SINGLE_MATCHPROMO": case "ANIMATE-IN-DOUBLE_MATCHPROMO": case "ANIMATE-MATCH_STATS": case "ANIMATE-IN-LOCATOR": case "ANIMATE-IN-TIE_RESULT":
		case "ANIMATE-LT-CROSS": case "ANIMATE-SPEED": case "ANIMATE-IN-ORDER_OF_TIE": case "ANIMATE-FF_PLAYERPROFILE": case "ANIMATE-IN-POINTS_TABLE":
		case "ANIMATE-IN-ORDER_OF_MATCH": case "ANIMATE-IN-POINTS_PROGRESS": case "ANIMATE-LT_PLAYERPROFILE": case "ANIMATE-IN-LT_TEAM": case "ANIMATE-IN-LT_MATCHID_SCORE":
		case "ANIMATE-IN-LT_DOUBLEMATCHID_SCORE": case "ANIMATE-IN-LT_SINGLEMATCH_PROMO": case "ANIMATE-IN-LT_DOUBLEMATCH_PROMO":
			
		case "CLEAR-ALL":
		case "ANIMATE-OUT-SCOREBUG": case "ANIMATE-OUT-SCOREBUG_STAT": case "ANIMATE-OUT-SCOREBUG_GAMEINFO": case "ANIMATE-OUT-SCOREBUG_GAME_POINTS":
			
		case "ANIMATE-OUT":
			switch (whatToProcess.toUpperCase()) {
			case "ANIMATE-IN-SCOREBUG":
				AnimateInGraphics(print_writer, "SCOREBUG");
				TimeUnit.MILLISECONDS.sleep(1000);
				if((scorebug.getHomeTotalScore() > 0 || scorebug.getAwayTotalScore() > 0)) {
					if(scorebug.isGame_in() == false) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*TeamScoreIn START \0");
						scorebug.setGame_in(true);
					}
				}
				which_graphics_onscreen = "SCOREBUG";
				is_infobar = true;
				scorebug.setScorebug_on_screen(true);
				break;
			case "ANIMATE-IN-SCOREBUG_GAMEINFO":
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameInfo_in START \0");
				break;
			case "ANIMATE-MATCH_STATS":
				AnimateInGraphics(print_writer, "MATCH_STATS");
				which_graphics_onscreen = "MATCH_STATS";
				break;
			case "ANIMATE-LT_PLAYERPROFILE":
				AnimateInGraphics(print_writer, "LT_PLAYERPROFILE");
				which_graphics_onscreen = "LT_PLAYERPROFILE";
				break;
			case "ANIMATE-IN-POINTS_PROGRESS":
				AnimateInGraphics(print_writer, "POINTS_PROGRESS");
				which_graphics_onscreen = "POINTS_PROGRESS";
				break;
			case "ANIMATE-FF_PLAYERPROFILE":
				AnimateInGraphics(print_writer, "FF_PLAYERPROFILE");
				which_graphics_onscreen = "FF_PLAYERPROFILE";
				break;
			case "ANIMATE-LT-MATCH_RESULTSINGLES":
				AnimateInGraphics(print_writer, "LT-MATCH_RESULTSINGLES");
				which_graphics_onscreen = "LT-MATCH_RESULTSINGLES";
				break;
			case "ANIMATE-FF-MATCH_RESULTSINGLES":
				AnimateInGraphics(print_writer, "FF-MATCH_RESULTSINGLES");
				which_graphics_onscreen = "FF-MATCH_RESULTSINGLES";
				break;
			case "ANIMATE-LT-MATCH_RESULTDOUBLES":
				AnimateInGraphics(print_writer, "LT-MATCH_RESULTDOUBLES");
				which_graphics_onscreen = "LT-MATCH_RESULTDOUBLES";
				break;
			case "ANIMATE-FF-MATCH_RESULTDOUBLES":
				AnimateInGraphics(print_writer, "FF-MATCH_RESULTDOUBLES");
				which_graphics_onscreen = "FF-MATCH_RESULTDOUBLES";
				break;
			case "ANIMATE-IN-MATCHID_DOUBLE":
				AnimateInGraphics(print_writer, "MATCHID_DOUBLE");
				which_graphics_onscreen = "MATCHID_DOUBLE";
				break;
			case "ANIMATE-IN-MATCHID":
				AnimateInGraphics(print_writer, "MATCHID");
				which_graphics_onscreen = "MATCHID";
				break;
				
			case "ANIMATE-IN-LT_MATCHID_SCORE":
				AnimateInGraphics(print_writer, "LT_MATCHID_SCORE");
				which_graphics_onscreen = "LT_MATCHID_SCORE";
				break;
			case "ANIMATE-IN-LT_DOUBLEMATCHID_SCORE":
				AnimateInGraphics(print_writer, "LT_DOUBLEMATCHID_SCORE");
				which_graphics_onscreen = "LT_DOUBLEMATCHID_SCORE";
				break;
			case "ANIMATE-IN-LT_SINGLEMATCH_PROMO":
				AnimateInGraphics(print_writer, "LT_SINGLEMATCH_PROMO");
				which_graphics_onscreen = "LT_SINGLEMATCH_PROMO";
				break;
			case "ANIMATE-IN-LT_DOUBLEMATCH_PROMO":
				AnimateInGraphics(print_writer, "LT_DOUBLEMATCH_PROMO");
				which_graphics_onscreen = "LT_DOUBLEMATCH_PROMO";
				break;
				
			case "ANIMATE-IN-LOCATOR":
				AnimateInGraphics(print_writer, "LOCATOR");
				which_graphics_onscreen = "LOCATOR";
				break;
			case "ANIMATE-IN-POINTS_TABLE":
				AnimateInGraphics(print_writer, "POINTS_TABLE");
				which_graphics_onscreen = "POINTS_TABLE";
				break;
			case "ANIMATE-IN-LT_MATCHID":
				AnimateInGraphics(print_writer, "LT_MATCHID");
				which_graphics_onscreen = "LT_MATCHID";
				break;
			case "ANIMATE-IN-LT-MATCHID_DOUBLE":
				AnimateInGraphics(print_writer, "LT-MATCHID_DOUBLE");
				which_graphics_onscreen = "LT-MATCHID_DOUBLE";
				break;
			case "ANIMATE-LT-NAMESUPERDB":
				AnimateInGraphics(print_writer, "NAMESUPERDB");
				which_graphics_onscreen = "NAMESUPERDB";
				break;
			case "ANIMATE-LT-NAMESUPER_SP":
				AnimateInGraphics(print_writer, "NAMESUPER_SP");
				which_graphics_onscreen = "NAMESUPER_SP";
				break;
			case "ANIMATE-LT-NAMESUPER_DP":
				AnimateInGraphics(print_writer, "NAMESUPER_DP");
				which_graphics_onscreen = "NAMESUPER_DP";
				break;
			case "ANIMATE-LT-NAMESUPER_SP1":
				AnimateInGraphics(print_writer, "NAMESUPER_SP1");
				which_graphics_onscreen = "NAMESUPER_SP1";
				break;
			case "ANIMATE-LT-NAMESUPER_DP1":
				AnimateInGraphics(print_writer, "NAMESUPER_DP1");
				which_graphics_onscreen = "NAMESUPER_DP1";
				break;
			case "ANIMATE-LT-CROSS":
				AnimateInGraphics(print_writer, "CROSS");
				which_graphics_onscreen = "CROSS";
				break;
			case "ANIMATE-LT-MATCH_SCORESINGLES":
				AnimateInGraphics(print_writer, "MATCH_SCORESINGLES");
				which_graphics_onscreen = "MATCH_SCORESINGLES";
				break;
			case "ANIMATE-IN-LT_TEAM":
				AnimateInGraphics(print_writer, "LT_TEAM");
				which_graphics_onscreen = "LT_TEAM";
				break;
			case "ANIMATE-IN-ORDER_OF_TIE":
				AnimateInGraphics(print_writer, "ORDER_OF_TIE");
				which_graphics_onscreen = "ORDER_OF_TIE";
				break;
			case "ANIMATE-IN-ORDER_OF_MATCH":
				AnimateInGraphics(print_writer, "ORDER_OF_MATCH");
				which_graphics_onscreen = "ORDER_OF_MATCH";
				break;
			case "ANIMATE-IN-SINGLE_MATCHPROMO":
				AnimateInGraphics(print_writer, "SINGLE_MATCHPROMO");
				which_graphics_onscreen = "SINGLE_MATCHPROMO";
				break;
			case "ANIMATE-IN-TIE_RESULT":
				AnimateInGraphics(print_writer, "TIE_RESULT");
				which_graphics_onscreen = "TIE_RESULT";
				break;
			case "ANIMATE-IN-DOUBLE_MATCHPROMO":
				AnimateInGraphics(print_writer, "DOUBLE_MATCHPROMO");
				which_graphics_onscreen = "DOUBLE_MATCHPROMO";
				break;
			case "ANIMATE-LT-SINGLE_LT_MATCHPROMO":
				AnimateInGraphics(print_writer, "SINGLE_LT_MATCHPROMO");
				which_graphics_onscreen = "SINGLE_LT_MATCHPROMO";
				break;
			case "ANIMATE-LT-DOUBLE_LT_MATCHPROMO":
				AnimateInGraphics(print_writer, "DOUBLE_LT_MATCHPROMO");
				which_graphics_onscreen = "DOUBLE_LT_MATCHPROMO";
				break;
			case "ANIMATE-LT-MATCH_SCOREDOUBLES":
				AnimateInGraphics(print_writer, "MATCH_SCOREDOUBLES");
				which_graphics_onscreen = "MATCH_SCOREDOUBLES";
				break;
			case "ANIMATE-SPEED":
				AnimateInGraphics(print_writer, "SPEED");
				which_graphics_onscreen = "SPEED";
				break;
			case "CLEAR-ALL":
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In SHOW 0.0 \0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Reaet START \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ExtraInfoIn SHOW 0.0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*PopUpIn SHOW 0.0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameInfo_in SHOW 0.0 \0");
				print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*ExtraInfoIn SHOW 0.0 \0");

				which_graphics_onscreen = "";
				this.status = "";
				is_infobar = false;
				scorebug.setScorebug_on_screen(false);
				scorebug.setGame_header_on_screen(false);
				scorebug.setGame_in(false);
				which_gfx = "";
				break;
			case "ANIMATE-OUT-SCOREBUG_GAMEINFO":
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameInfo_Out START \0");
				break;
			case "ANIMATE-OUT-SCOREBUG_GAME_POINTS":
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GamePointOut START \0");
				break;
			case "ANIMATE-OUT-SCOREBUG_STAT":
				if(scorebug.isGame_header_on_screen() == true) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ExtraInfoOut START \0");
					TimeUnit.MILLISECONDS.sleep(500);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ExtraInfoIn SHOW 0.0 \0");
					scorebug.setGame_header_on_screen(false);
				}else if(scorebug.getLast_scorebug_stat() != null && !scorebug.getLast_scorebug_stat().trim().isEmpty()) {
					switch(scorebug.getLast_scorebug_stat()) {
					case "aces": case "double_faults": case "winners": case "unforced_errors":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*PopUpOut START \0");
						TimeUnit.MICROSECONDS.sleep(600);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*PopUpIn SHOW 0.0 \0");
						break;
					}
					//print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*TeamScoreIn START \0");
				}
				scorebug.setLast_scorebug_stat("");
				scorebug.setScorebug_stat("");
				break;
			case "ANIMATE-OUT-SCOREBUG":
				if (is_infobar == true) {
					AnimateOutGraphics(print_writer, "SCOREBUG");
					is_infobar = false;
					scorebug.setScorebug_on_screen(false);
					scorebug.setGame_in(false);
				}
				break;
			
			case "ANIMATE-OUT":
				switch (which_graphics_onscreen) {
				case "LT-MATCH_RESULTSINGLES": case "LT-MATCH_RESULTDOUBLES": case "LT_MATCHID": case "LT-MATCHID_DOUBLE": case "NAMESUPERDB": case "NAMESUPER_SP": 
				case "NAMESUPER_DP":case "NAMESUPER_SP1": case "NAMESUPER_DP1": case "MATCH_SCORESINGLES": case "MATCH_SCOREDOUBLES": case "SINGLE_LT_MATCHPROMO": case "DOUBLE_LT_MATCHPROMO":
				case "MATCHID_DOUBLE": case "MATCHID": case "FF-MATCH_RESULTSINGLES": case "FF-MATCH_RESULTDOUBLES": case "SINGLE_MATCHPROMO": case "DOUBLE_MATCHPROMO":
				case "CROSS": case "MATCH_STATS": case "SPEED": case "ORDER_OF_TIE": case "FF_PLAYERPROFILE": case "LOCATOR": case "TIE_RESULT": case "POINTS_TABLE":
				case "ORDER_OF_MATCH": case "POINTS_PROGRESS": case "LT_PLAYERPROFILE": case "LT_TEAM": case "LT_MATCHID_SCORE": case "LT_DOUBLEMATCHID_SCORE":
				case "LT_SINGLEMATCH_PROMO": case "LT_DOUBLEMATCH_PROMO":
					AnimateOutGraphics(print_writer, which_graphics_onscreen);
					which_graphics_onscreen = "";
					break;
				}
				break;
			}
			break;
		}
		return null;
	}

	public void AnimateInGraphics(PrintWriter print_writer, String whichGraphic) throws InterruptedException {

		switch (whichGraphic) {
		case "SCOREBUG":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*In START \0");
			this.status = whichGraphic;
			break;
		case "LT_MATCHID_SCORE": case "LT_DOUBLEMATCHID_SCORE": case "LT_SINGLEMATCH_PROMO": case "LT_DOUBLEMATCH_PROMO":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			TimeUnit.MILLISECONDS.sleep(2000);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*ExtraInfoIn START \0");
			break;
		case "LT-MATCH_RESULTSINGLES": case "LT-MATCH_RESULTDOUBLES": case "LT_MATCHID": case "LT-MATCHID_DOUBLE": case "NAMESUPERDB": case "NAMESUPER_SP": 
		case "NAMESUPER_DP": case "NAMESUPER_SP1": case "NAMESUPER_DP1": case "MATCH_SCORESINGLES": case "MATCH_SCOREDOUBLES":
		case "MATCHID_DOUBLE": case "MATCHID": case "FF-MATCH_RESULTSINGLES": case "FF-MATCH_RESULTDOUBLES": case "SINGLE_MATCHPROMO": case "DOUBLE_MATCHPROMO": case "SINGLE_LT_MATCHPROMO":
		case "DOUBLE_LT_MATCHPROMO": case "MATCH_STATS": case "SPEED": case "ORDER_OF_TIE": case "FF_PLAYERPROFILE": case "LOCATOR": case "TIE_RESULT": case "POINTS_TABLE":
		case "CROSS": case "ORDER_OF_MATCH": case "POINTS_PROGRESS": case "LT_PLAYERPROFILE": case "LT_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In START \0");
			break;
		}
	}

	public void AnimateOutGraphics(PrintWriter print_writer, String whichGraphic) throws InterruptedException {
		switch (whichGraphic.toUpperCase()) {
		case "SCOREBUG":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Out START \0");
			this.status = "";
			break;
		case "LT_MATCHID_SCORE": case "LT_DOUBLEMATCHID_SCORE": case "LT_SINGLEMATCH_PROMO": case "LT_DOUBLEMATCH_PROMO":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*ExtraInfoOut START \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			
			TimeUnit.MILLISECONDS.sleep(1200);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Reaet START \0");
			break;
		case "LT-MATCH_RESULTSINGLES": case "LT-MATCH_RESULTDOUBLES": case "LT_MATCHID": case "LT-MATCHID_DOUBLE": case "NAMESUPERDB": case "NAMESUPER_SP": 
		case "NAMESUPER_DP": case "NAMESUPER_SP1": case "NAMESUPER_DP1": case "MATCH_SCORESINGLES": case "MATCH_SCOREDOUBLES":
		case "MATCHID_DOUBLE": case "MATCHID": case "FF-MATCH_RESULTSINGLES": case "FF-MATCH_RESULTDOUBLES": case "SINGLE_MATCHPROMO": case "DOUBLE_MATCHPROMO": case "SINGLE_LT_MATCHPROMO":
		case "DOUBLE_LT_MATCHPROMO": case "MATCH_STATS": case "SPEED": case "ORDER_OF_TIE": case "FF_PLAYERPROFILE": case "LOCATOR": case "TIE_RESULT": case "POINTS_TABLE":
		case "CROSS": case "ORDER_OF_MATCH": case "POINTS_PROGRESS": case "LT_PLAYERPROFILE": case "LT_TEAM":
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Out START \0");
			
			TimeUnit.MILLISECONDS.sleep(700);
			print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In SHOW 0.0 \0");
			break;
		}
	}
	
	public static List<String> getSpeed(String FileName) throws IOException {
		long last_date = 0;
		String filePath = TennisUtil.TENNIS_DIRECTORY + "/Speeds/" + FileName + ".txt";
        File file = new File(filePath);
        long lastModified = file.lastModified();
		
		List<String> allLines = new ArrayList<String>();
		if(new File(TennisUtil.TENNIS_DIRECTORY + "/Speeds/" + FileName + ".txt").exists()) {
			if(last_date == 0) {
				last_date = lastModified;
				allLines = Files.readAllLines(Paths.get(TennisUtil.TENNIS_DIRECTORY + "/Speeds/" + FileName + ".txt"));
				System.out.println("1 = " + allLines.get(1));
			}else if(last_date != 0 && last_date != lastModified) {
				last_date = lastModified;
				allLines = Files.readAllLines(Paths.get(TennisUtil.TENNIS_DIRECTORY + "/Speeds/" + FileName + ".txt"));
				System.out.println("2 = " + allLines.get(1));
			}
		}
		return allLines;
	}
	
	public ScoreBug populateScoreBug(boolean is_this_updating, ScoreBug scorebug, PrintWriter print_writer,String viz_sence_path, Match match, TennisService tennisService, String selectedbroadcaster) throws IOException, JAXBException {
		if (match == null) {
			System.out.println("ERROR: ScoreBug -> Match is null");
		} else {
			int currHomeScore = 0;
			int currAwayScore = 0;
			if (is_this_updating == false) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamName1"+ " SET " + 
						match.getHomeFirstPlayer().getTeam().getTeamName1() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamName2"+ " SET " + 
						match.getAwayFirstPlayer().getTeam().getTeamName1() + "\0");
				
				//Logos
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgHomeTeamLogo" + " SET " + logo_path + 
						match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgAwayTeamLogo" + " SET " + logo_path + 
						match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
				
				//Colors
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgHomeTeamColor" + " SET " + color1_path + 
						match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgAwayTeamColor" + " SET " + color1_path + 
						match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$Scorebug$MainData_Grp$TOTAL$HomeTeam$DataGRp$Rectangle*TEXTURE*IMAGE SET " 
						+ color2_path + match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$Scorebug$MainData_Grp$TOTAL$AwayTeam$DataGRp$Rectangle*TEXTURE*IMAGE SET " 
						+ color2_path + match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
				
				if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.SINGLES)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1"+ " SET " + 
							match.getHomeFirstPlayer().getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2"+ " SET " + 
							match.getAwayFirstPlayer().getTicker_name() + "\0");
				} else if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.DOUBLES)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " + 
							match.getHomeFirstPlayer().getTicker_name()+ " / " + match.getHomeSecondPlayer().getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " + 
							match.getAwayFirstPlayer().getTicker_name() + " / " + match.getAwaySecondPlayer().getTicker_name() + "\0");
				}
				
				List<Fixture> all_db_fixture;
				List<File> all_match_files;
				File this_file = null;
				Match this_match = null;
				//Match curr_match = null;
				
				all_match_files = Arrays.asList(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						String name = pathname.getName().toLowerCase();
						return name.endsWith(".json") && pathname.isFile();
					}
				}));
				all_db_fixture = tennisService.getFixtures();
				
				if(all_db_fixture != null) {
					Fixture curr_fixture = all_db_fixture.stream().filter(fix -> 
					fix.getMatchfilename().equalsIgnoreCase(match.getMatchFileName())).findAny().orElse(null);		
					if(curr_fixture != null) {
						pastHomeScore = 0;
						pastAwayScore = 0;
						for (Fixture fixture : all_db_fixture.stream().filter(fix -> fix.getMatchNumber()==curr_fixture.getMatchNumber()).collect(Collectors.toList())) {
							this_file = all_match_files.stream().filter(fil -> fil.getName().equalsIgnoreCase(fixture.getMatchfilename())).findAny().orElse(null);
							if(this_file != null) {
								if(!this_file.getName().equalsIgnoreCase(match.getMatchFileName())) {
									this_match = TennisFunctions.populateMatchVariables(tennisService, new ObjectMapper().readValue(
											new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY + this_file.getName()), Match.class));
									if(match.getHomeFirstPlayer().getTeamId()==this_match.getHomeFirstPlayer().getTeamId()
											|| match.getHomeFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
											|| match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
											|| match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId())
										{
											
											if(this_match.getSets() != null) {
												for (Set set : this_match.getSets()) {
													for (Game game : set.getGames()) {
														if(is_this_updating == false) {
															pastHomeScore = pastHomeScore + Integer.valueOf(game.getHome_score());
															pastAwayScore = pastAwayScore + Integer.valueOf(game.getAway_score());
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					if(match.getMatchType().equalsIgnoreCase("SINGLES") && match.getCategoryType().equalsIgnoreCase("MENS")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + " tGameType " + " SET " + " MS " + "\0");
					}else if(match.getMatchType().equalsIgnoreCase("SINGLES") && match.getCategoryType().equalsIgnoreCase("WOMENS")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + " tGameType " + " SET " + " WS " + "\0");
					}else if(match.getMatchType().equalsIgnoreCase("DOUBLES") && match.getCategoryType().equalsIgnoreCase("MENS")) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + " tGameType " + " SET " + " MD " + "\0");
					}else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + " tGameType " + " SET " + " XD " + "\0");
					}
					
			}
			if(match.getSets() != null) {
				if(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getServing_player() == match.getHomeFirstPlayer().getPlayerId() ||
						match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getServing_player() == match.getHomeSecondPlayer().getPlayerId()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vServe1"+ " SET " + "1" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vServe2"+ " SET " + "0" + "\0");
				}
				else if(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getServing_player() == match.getAwayFirstPlayer().getPlayerId() || 
						match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getServing_player() == match.getAwaySecondPlayer().getPlayerId()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vServe1"+ " SET " + "0" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vServe2"+ " SET " + "1" + "\0");
				}
				else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vServe1"+ " SET " + "0" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vServe2"+ " SET " + "0" + "\0");
				}
				
				for (Set set : match.getSets()) {
					for (Game game : set.getGames()) {
						 currHomeScore = Integer.valueOf(game.getHome_score());
						 currAwayScore = Integer.valueOf(game.getAway_score());
						 
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + " tSetValue1 " + " SET "+  currHomeScore + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + " tSetValue2 " +" SET "+ currAwayScore + "\0");
					}
				}
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGameValue1" + " SET " + (pastHomeScore + currHomeScore) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGameValue2" + " SET " + (pastAwayScore + currAwayScore) + "\0");
				
				scorebug.setHomeTotalScore((pastHomeScore + currHomeScore));
				scorebug.setAwayTotalScore((pastAwayScore + currAwayScore));
				
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGameValue1" + " SET " + (pastHomeScore + currHomeScore) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGameValue2" + " SET " + (pastAwayScore + currAwayScore) + "\0");
			
				scorebug.setHomeTotalScore((pastHomeScore + currHomeScore));
				scorebug.setAwayTotalScore((pastAwayScore + currAwayScore));
			}
		}
		return scorebug;
	}
	public ScoreBug populateGameScore(boolean is_this_updating,ScoreBug scorebug, PrintWriter print_writer, Match match, String selectedbroadcaster) {
		
		if(is_this_updating == false && scorebug.isGame_in() == false) {
			if(match.getSets().get(0).getGames().get(0).getGame_status().equalsIgnoreCase(TennisUtil.START)) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*SoreAllIn START \0");
			}
			if(match.getSets().size() < 3) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameIn START \0");
			}else if(match.getSets().size() == 3) {
				if(!match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameIn START \0");
				}
			}
			scorebug.setGame_in(true);
		}
		
		if (match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getGame_status().
				equalsIgnoreCase(TennisUtil.START)) {
			
			if (match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getHome_score().
					toUpperCase().equalsIgnoreCase(TennisUtil.GAME)) {
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopGameScore" + " SET " + "40" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomGameScore" + " SET " + 
						match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getAway_score() + "\0");
				
			}else if (match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).
					getAway_score().toUpperCase().equalsIgnoreCase(TennisUtil.GAME)) {
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopGameScore" + " SET "+ 
						match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getHome_score() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomGameScore" + " SET " + "40" + "\0");
				
			}else if (match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getHome_score().
					toUpperCase().equalsIgnoreCase(TennisUtil.ADVANTAGE)) {
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopGameScore" + " SET " + "AD" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomGameScore" + " SET " + "" + "\0");
				
			}else if (match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).
					getAway_score().toUpperCase().equalsIgnoreCase(TennisUtil.ADVANTAGE)) {
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopGameScore" + " SET "+ "" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomGameScore" + " SET " + "AD" + "\0");
				
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopGameScore" + " SET " + 
						match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getHome_score() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomGameScore" + " SET "+ 
						match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getAway_score() + "\0");
			}

		}else if(match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getGame_status().
				equalsIgnoreCase(TennisUtil.END)) {
			if(scorebug.isGame_in() == true) {
				if(match.getSets().size() < 3) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameOut START \0");
				}else if(match.getSets().size() == 3) {
					if(!match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameOut START \0");
					}
				}
				scorebug.setGame_in(false);
			}
			
		}
		
		return scorebug;
	}
	public ScoreBug populateScoreBugStats(boolean is_this_updating,ScoreBug scorebug, PrintWriter print_writer, Match match, String selectedbroadcaster) 
			throws MalformedURLException, IOException, InterruptedException {
		
		String link = "https://api.protennislive.com/feeds/MatchStats/" + match.getMatchId();
		LiveMatchStatsAPI ApiMatch = TennisFunctions.getMatchStatsApi(link);
		
		if(scorebug.getScorebug_stat().equalsIgnoreCase("firstServeWon")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "1st SERVE POINTS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getFirstServePointsWon().getDividend() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getFirstServePointsWon().getDividend() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("secondServeWon")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "2nd SERVE POINTS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getSecondServePointsWon().getDividend() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getSecondServePointsWon().getDividend() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase(TennisUtil.ACE)) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "ACES" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getAces().getNumber() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getAces().getNumber() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase(TennisUtil.DOUBLE_FAULT)) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "DOUBLE FAULTS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("breakPointWon")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "BREAK POINTS WON" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
		}
		
		if(is_this_updating == false) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Top$ScoreBg$LoseBg*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Bottom$ScoreBg$LoseBg*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Top$ScoreBg$WinBg*ACTIVE SET 1 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Bottom$ScoreBg$WinBg*ACTIVE SET 1 \0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1HeadIn START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1In START \0");
			TimeUnit.MILLISECONDS.sleep(4);
		}
		scorebug.setLast_scorebug_stat(scorebug.getScorebug_stat());
		return scorebug;
	}
	public ScoreBug populateScoreBugStatsSet(boolean is_this_updating,ScoreBug scorebug, PrintWriter print_writer, Match match, String selectedbroadcaster) 
			throws Exception {
		
		API_Tournament  ApiMatch = TennisFunctions.getMatchStatApi("https://tplsport.net.in/tpl6/json.php?mid="+ match.getMatchId());
		
		if(scorebug.getScorebug_stat().equalsIgnoreCase("aces")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPopInfo"+ " SET " + "ACES" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomePopValue"+ " SET " + 
					ApiMatch.getTeamwiseStat().getTeamA().get(0).getAces() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayPopValue"+ " SET " + 
					ApiMatch.getTeamwiseStat().getTeamB().get(0).getAces() + "\0");
			
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("winners")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPopInfo"+ " SET " + "WINNERS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomePopValue"+ " SET " + 
					ApiMatch.getTeamwiseStat().getTeamA().get(0).getWinners() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayPopValue"+ " SET " + 
					ApiMatch.getTeamwiseStat().getTeamB().get(0).getWinners() + "\0");
			
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("unforced_errors")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPopInfo"+ " SET " + "UNFORCED ERRORS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomePopValue"+ " SET " + 
					ApiMatch.getTeamwiseStat().getTeamA().get(0).getUnforcedErrors() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayPopValue"+ " SET " + 
					ApiMatch.getTeamwiseStat().getTeamB().get(0).getUnforcedErrors() + "\0");
			
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("double_faults")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPopInfo"+ " SET " + "DOUBLE FAULTS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomePopValue"+ " SET " + 
					ApiMatch.getTeamwiseStat().getTeamA().get(0).getDoubleFaults() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayPopValue"+ " SET " + 
					ApiMatch.getTeamwiseStat().getTeamB().get(0).getDoubleFaults() + "\0");
		}
		
		if(is_this_updating == false) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*PopUpIn START \0");
			TimeUnit.MILLISECONDS.sleep(400);
		}
		
		scorebug.setLast_scorebug_stat(scorebug.getScorebug_stat());
		return scorebug;
	}
	
	public ScoreBug populateScoreBugStatsBar(boolean is_this_updating,ScoreBug scorebug, PrintWriter print_writer, Match match, String selectedbroadcaster) throws JsonMappingException, JsonProcessingException, InterruptedException {
		String link = "https://api.protennislive.com/feeds/MatchStats/" + match.getMatchId();
		LiveMatchStatsAPI ApiMatch = TennisFunctions.getMatchStatsApi(link);
		
		if(ApiMatch.getPlayerTeam1().getSets().size() == 2) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "vSetNumber" + " SET " + "1" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tSetNumber1" + " SET " + "SET 1" + "\0");
		}else if(ApiMatch.getPlayerTeam1().getSets().size() == 3) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "vSetNumber" + " SET " + "2" + "\0");
		}else if(ApiMatch.getPlayerTeam1().getSets().size() == 4) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "vSetNumber" + " SET " + "3" + "\0");
			
			if(scorebug.getScorebug_stat().equalsIgnoreCase("firstServePoints")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp1$BarGrp$noname*FUNCTION*BarValues*Bar_Value__1 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp1$BarGrp$group*FUNCTION*BarValues*Bar_Value__1 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
										Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp1$BarGrp$noname*FUNCTION*BarValues*Bar_Value__2 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp1$BarGrp$group*FUNCTION*BarValues*Bar_Value__2 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp2$BarGrp$noname*FUNCTION*BarValues*Bar_Value__1 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp2$BarGrp$group*FUNCTION*BarValues*Bar_Value__1 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
										Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp2$BarGrp$noname*FUNCTION*BarValues*Bar_Value__2 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp2$BarGrp$group*FUNCTION*BarValues*Bar_Value__2 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp3$BarGrp$noname*FUNCTION*BarValues*Bar_Value__1 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp3$BarGrp$group*FUNCTION*BarValues*Bar_Value__1 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
										Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp3$BarGrp$noname*FUNCTION*BarValues*Bar_Value__2 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp3$BarGrp$group*FUNCTION*BarValues*Bar_Value__2 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
			}
			
			
		}
		
		if(is_this_updating == false) {
			//print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2HeadIn START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2In START \0");
			TimeUnit.MILLISECONDS.sleep(4);
		}
		scorebug.setLast_scorebug_stat(scorebug.getScorebug_stat());
		return scorebug;
		
	}
	
	public void populateScoreBugHeader(boolean is_this_updating,ScoreBug scorebug,PrintWriter print_writer,String value, Match match,String selectedbroadcaster) {
		
		if(value.equalsIgnoreCase("match_game")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tExtraInfo"+ " SET " + match.getMatchIdent() + "\0");
		}else if(value.equalsIgnoreCase("match_score")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tExtraInfo"+ " SET " + "MATCH SCORE" + "\0");
		}
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*ExtraInfoIn START \0");
		scorebug.setGame_header_on_screen(true);
	}
	public void populateScoreBugGamePoints(boolean is_this_updating,ScoreBug scorebug,PrintWriter print_writer,String value,String which_team, Match match,String selectedbroadcaster) {
		
		if(value.equalsIgnoreCase("match_point")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGamePointInfo"+ " SET " + "MATCH POINT" + "\0");
		}else if(value.equalsIgnoreCase("set_point")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGamePointInfo"+ " SET " + "SET POINT" + "\0");
		}else if(value.equalsIgnoreCase("break_point")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGamePointInfo"+ " SET " + "BREAK POINT" + "\0");
		}else if(value.equalsIgnoreCase("tie_break")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGamePointInfo"+ " SET " + "TIE-BREAK" + "\0");
		}else if(value.equalsIgnoreCase("deuce")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGamePointInfo"+ " SET " + "DEUCE" + "\0");
		}else if(value.equalsIgnoreCase("match_tie_break")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGamePointInfo"+ " SET " + "MATCH TIE-BREAK" + "\0");
		}else if(value.equalsIgnoreCase("game_point")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGamePointInfo"+ " SET " + "GAME POINT" + "\0");
		}
		
		if(which_team.equalsIgnoreCase("home")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "0" + "\0");
		}else if(which_team.equalsIgnoreCase("away")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "1" + "\0");
		}
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GamePointIn START \0");
	}
	
	public void populateLtMatchScoreSingles(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: MATCH RESULT -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			if(match.getHomeFirstPlayer().getRankingSingle() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + match.getHomeFirstPlayer().getRankingSingle() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			if(match.getAwayFirstPlayer().getRankingSingle() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + match.getAwayFirstPlayer().getRankingSingle() + "\0");
			}
			
			List<String> home_data = new ArrayList<String>();
			List<String> away_data = new ArrayList<String>();
			
			if(match.getSets() != null) {
				
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					int home = 0, away = 0;
					for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
							if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								home = home + 1;
							} else if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								away = away + 1;
							}
						}
					}
					home_data.add(i, String.valueOf(home));
					away_data.add(i, String.valueOf(away));
				}		
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + "" + "\0");
			
			if(match.getSets().size() <= 3) {
				if(match.getSets().size()==1) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "6" + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + "" + "\0");
				}else if(match.getSets().size() == 2) {
					
					if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0))) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "7" + "\0");
					}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0))) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "9" + "\0");
					}
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
				}else if(match.getSets().size() == 3) {
					if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) < Integer.valueOf(away_data.get(1))) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "8" + "\0");
					}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) > Integer.valueOf(away_data.get(1))) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "10" + "\0");
					}
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + home_data.get(2) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + away_data.get(2)+ "\0");
				}
			}
			
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.100 \0");
		}
	}
	public void populateLtMatchScoreDoubles(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: MATCH RESULT -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameA1" + " SET " + match.getHomeFirstPlayer().getTicker_name() + "\0");
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			if(match.getHomeFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + match.getHomeFirstPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameA2" + " SET " + match.getHomeSecondPlayer().getTicker_name() + "\0");
			if(match.getHomeSecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + match.getHomeSecondPlayer().getNationality() + "\0");
			}
			if(match.getHomeSecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + match.getHomeSecondPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameB1" + " SET " + match.getAwayFirstPlayer().getTicker_name() + "\0");
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			if(match.getAwayFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + match.getAwayFirstPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameB2" + " SET " + match.getAwaySecondPlayer().getTicker_name() + "\0");
			if(match.getAwaySecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + match.getAwaySecondPlayer().getNationality() + "\0");
			}
			if(match.getAwaySecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + match.getAwaySecondPlayer().getRankingDouble() + "\0");
			}
			
			List<String> home_data = new ArrayList<String>();
			List<String> away_data = new ArrayList<String>();
			
			if(match.getSets() != null) {
				
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					int home = 0, away = 0;
					for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
							if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								home = home + 1;
							} else if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								away = away + 1;
							}
						}
					}
					home_data.add(i, String.valueOf(home));
					away_data.add(i, String.valueOf(away));
				}		
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + "" + "\0");
			
			if(match.getSets().size() <= 3) {
				if(match.getSets().size()==1) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "6" + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + "" + "\0");
				}else if(match.getSets().size() == 2) {
					
					if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0))) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "7" + "\0");
					}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0))) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "9" + "\0");
					}
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
				}else if(match.getSets().size() == 3) {
					if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) < Integer.valueOf(away_data.get(1))) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "8" + "\0");
					}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) > Integer.valueOf(away_data.get(1))) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "10" + "\0");
					}
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
					
					if(match.getMatchType().equalsIgnoreCase(TennisUtil.DOUBLES) && 
							match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + match.getSets().get(2).getGames().get(0).getHome_score() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + match.getSets().get(2).getGames().get(0).getAway_score() + "\0");
						
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + home_data.get(2) + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + away_data.get(2)+ "\0");
					}
	
				}
			}
			
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.220 \0");
		}
	}
	
	public void populateLtMatchResultSingles(PrintWriter print_writer, String viz_sence_path, Match match,TennisService tennisService,String selectedbroadcaster) throws JAXBException, StreamReadException, DatabindException, IOException {
		if (match == null) {
			System.out.println("ERROR: MATCH RESULT -> Match is null");
		} else {
			isVisited = false;
			homeWon = 0;
			awayWon = 0;
			
			int row =1;
			int homeScore = 0;
			int awayScore = 0;
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGameType1" + " SET " + "WS" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGameType2" + " SET " + "MS" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGameType3" + " SET " + "XD" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tGameType4" + " SET " + "MD" + "\0");
			for(int row_id = 1; row_id<=4; row_id++) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValueA"+ row_id +  " SET " + "-" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValueB"+ row_id + " SET " + "-" + "\0");

			}
							
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamName1" + " SET " + match.getHomeFirstPlayer().getTeam().getTeamName1() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamName2" + " SET " + match.getAwayFirstPlayer().getTeam().getTeamName1() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
					match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
					match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
			List<Fixture> all_db_fixture;
			List<File> all_match_files;
			File this_file = null;
			Match this_match = null;
			//Match curr_match = null;
			
			all_match_files = Arrays.asList(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					String name = pathname.getName().toLowerCase();
					return name.endsWith(".json") && pathname.isFile();
				}
			}));
			all_db_fixture = tennisService.getFixtures();
			
			if(all_db_fixture != null) {
				Fixture curr_fixture = all_db_fixture.stream().filter(fix -> 
				fix.getMatchfilename().equalsIgnoreCase(match.getMatchFileName())).findAny().orElse(null);						
				//Match this_match = new Match();
				if(curr_fixture != null) {
					int totalHomeScore = 0;
					int totalAwayScore = 0;
					pastHomeScore = 0;
					pastAwayScore = 0;
					for (Fixture fixture : all_db_fixture.stream().filter(fix -> fix.getMatchNumber()==curr_fixture.getMatchNumber()).collect(Collectors.toList())) {
						this_file = all_match_files.stream().filter(fil -> fil.getName().equalsIgnoreCase(fixture.getMatchfilename())).findAny().orElse(null);
						if(this_file != null) {
							homeScore = 0;
							awayScore = 0;
							
							if(!this_file.getName().equalsIgnoreCase(match.getMatchFileName())) {
								this_match = TennisFunctions.populateMatchVariables(tennisService, new ObjectMapper().readValue(
										new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY + this_file.getName()), Match.class));
								if(match.getHomeFirstPlayer().getTeamId()==this_match.getHomeFirstPlayer().getTeamId()
										|| match.getHomeFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
										|| match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
										|| match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId())
									{
										if(this_match.getSets() != null) {
											for (Set set : this_match.getSets()) {
												for (Game game : set.getGames()) {
													 homeScore = Integer.valueOf(game.getHome_score());
													 awayScore = Integer.valueOf(game.getAway_score());
													 
													 pastHomeScore = pastHomeScore + homeScore;
													 pastAwayScore = pastAwayScore + awayScore;
												}
											}
										}
									}
								if(homeScore>0 || awayScore>0) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValueA"+ row +  " SET " + homeScore + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValueB"+ row + " SET " + awayScore + "\0");
									
								}else {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValueA"+ row +  " SET " + "-" + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValueB"+ row + " SET " + "-" + "\0");
									
								}
								row++;
							}else {
								// Current Match
								if(match.getSets() != null) {
									homeScore = Integer.valueOf(match.getSets().get(0).getGames().get(0).getHome_score());
									awayScore = Integer.valueOf(match.getSets().get(0).getGames().get(0).getAway_score());
									 
									totalHomeScore = (pastHomeScore + homeScore);
									totalAwayScore = (pastAwayScore + awayScore);
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValueA"+ row +  " SET " + homeScore + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValueB"+ row + " SET " + awayScore + "\0");
								}else {
									totalHomeScore = (pastHomeScore + homeScore);
									totalAwayScore = (pastAwayScore + awayScore);
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValueA"+ row +  " SET " + "-" + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValueB"+ row + " SET " + "-" + "\0");
								}
								row++;
							}
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPoints1" + " SET " + totalHomeScore + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPoints2" + " SET " + totalAwayScore + "\0");
							
						}
					}
				}
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.100 \0");
		}
	}
	public void populateLtMatchResultDoubles(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: MATCH RESULT -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameA1" + " SET " + match.getHomeFirstPlayer().getTicker_name() + "\0");
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			if(match.getHomeFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + match.getHomeFirstPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameA2" + " SET " + match.getHomeSecondPlayer().getTicker_name() + "\0");
			if(match.getHomeSecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + match.getHomeSecondPlayer().getNationality() + "\0");
			}
			if(match.getHomeSecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + match.getHomeSecondPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameB1" + " SET " + match.getAwayFirstPlayer().getTicker_name() + "\0");
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			if(match.getAwayFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + match.getAwayFirstPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameB2" + " SET " + match.getAwaySecondPlayer().getTicker_name() + "\0");
			if(match.getAwaySecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + match.getAwaySecondPlayer().getNationality() + "\0");
			}
			if(match.getAwaySecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + match.getAwaySecondPlayer().getRankingDouble() + "\0");
			}
			
			
			List<String> home_data = new ArrayList<String>();
			List<String> away_data = new ArrayList<String>();
			
			if(match.getSets() != null) {
				
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					int home = 0, away = 0;
					for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
							if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								home = home + 1;
							} else if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								away = away + 1;
							}
						}
					}
					home_data.add(i, String.valueOf(home));
					away_data.add(i, String.valueOf(away));
				}
				
				if(match.getSets().size() == 2) {
					if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "0" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "5" + "\0");
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
					
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
				}else if(match.getSets().size() == 3) {
					if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "1" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "2" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "3" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "4" + "\0");
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + "" + "\0");
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					/*if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + 
									match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getAway_score() + "\0");
						}else if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + 
									match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getAway_score() + "\0");
						}
					}*/
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
					if(match.getMatchType().equalsIgnoreCase(TennisUtil.DOUBLES) && 
							match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + match.getSets().get(2).getGames().get(0).getHome_score() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + match.getSets().get(2).getGames().get(0).getAway_score() + "\0");
						
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + home_data.get(2) + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + away_data.get(2)+ "\0");
					}
				}
			}
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.220 \0");
		}
	}
	
	public void populateFFMatchResultSingles(PrintWriter print_writer, String viz_sence_path,String withOrWithoutPhoto,TennisService tennisService,Match match,String selectedbroadcaster) throws InterruptedException {
		if (match == null) {
			System.out.println("ERROR: Lt-Match -> Match is null");
		} else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "www.tplsport.com" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + match.getMatchIdent() + "\0");
			
			if(withOrWithoutPhoto.equalsIgnoreCase("without")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "0" + "\0");
			}else if(withOrWithoutPhoto.equalsIgnoreCase("with")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "1" + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame1FirstName" + " SET " + match.getHomeFirstPlayer().getTeam().getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame2FirstName" + " SET " + match.getAwayFirstPlayer().getTeam().getTeamName2() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vScore"+ " SET " + "1" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tScore" + " SET " + match.getSets().get(0).getGames().get(0).getHome_score() + 
					"-" + match.getSets().get(0).getGames().get(0).getAway_score() + "\0");
			
			//HOME PLAYER
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HomeTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 0 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage1" + " SET " + left_photo_path + 
					match.getHomeFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " + match.getHomeFirstPlayer().getFull_name() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
					match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
			
			//AWAY PLAYER
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$AwayTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 0 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage2" + " SET " + right_photo_path + 
					match.getAwayFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION +"\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " + match.getAwayFirstPlayer().getFull_name() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
					match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + "LIVE FROM " + tennisService.getGround().get(0).getFullname() + "\0");
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.900 \0");
		}
	}
	public void populateFFMatchResultDoubles(PrintWriter print_writer, String viz_sence_path,String withOrWithoutPhoto,TennisService tennisService,Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "www.tplsport.com" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + match.getMatchIdent() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame1FirstName" + " SET " + match.getHomeFirstPlayer().getTeam().getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame2FirstName" + " SET " + match.getAwayFirstPlayer().getTeam().getTeamName2() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vScore"+ " SET " + "1" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tScore" + " SET " + match.getSets().get(0).getGames().get(0).getHome_score() + 
					"-" + match.getSets().get(0).getGames().get(0).getAway_score() + "\0");
			
			if(withOrWithoutPhoto.equalsIgnoreCase("without")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "0" + "\0");
			}else if(withOrWithoutPhoto.equalsIgnoreCase("with")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "1" + "\0");
			}
			
//			HOME PLAYER
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HomeTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 1 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage1" + " SET " + left_photo_path + 
					match.getHomeFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage1B" + " SET " + left_photo_path + 
					match.getHomeSecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
					match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " 
					+ match.getHomeFirstPlayer().getTicker_name() + " / " + match.getHomeSecondPlayer().getTicker_name() + "\0");

//			AWAY PLAYER
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$AwayTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 1 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage2" + " SET " + right_photo_path+ 
					match.getAwayFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage2B" + " SET " + right_photo_path + 
					match.getAwaySecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
					match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " 
					+ match.getAwayFirstPlayer().getTicker_name() + " / " + match.getAwaySecondPlayer().getTicker_name() + "\0");
			
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + "LIVE FROM " + tennisService.getGround().get(0).getFullname() + "\0");
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.900 \0");
		}
	}
	
	public void populateNameSuperDB(PrintWriter print_writer, String viz_sence_path,int namesuperId,List<NameSuper> NameSuper,List<Team> teams,
			Match match,String selectedbroadcaster) {
		
		if(NameSuper.get(namesuperId-1).getFirstname()==null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName" + " SET " + NameSuper.get(namesuperId-1).getSurname() + "\0");
		}else if(NameSuper.get(namesuperId-1).getSurname()==null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName" + " SET " + NameSuper.get(namesuperId-1).getFirstname() + "\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName" + " SET " + NameSuper.get(namesuperId-1).getFirstname() +" "+ NameSuper.get(namesuperId-1).getSurname() + "\0");
		}
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDesignation" + " SET " + NameSuper.get(namesuperId-1).getSubLine() + "\0");
		
		if(NameSuper.get(namesuperId-1).getLogo() != null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LT$Base$GP*ACTIVE SET 1 " + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogo" + " SET " + logo_path + 
					NameSuper.get(namesuperId-1).getLogo() + "\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LT$Base$GP*ACTIVE SET 0 " + "\0");
		}

		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.800 \0");
	}
	public void populateNameSuperSP(PrintWriter print_writer, String viz_sence_path,int playerid,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) throws InterruptedException {
		
		if(Plyr.get(playerid-1).getFirstname()==null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName" + " SET " + Plyr.get(playerid-1).getSurname() + "\0");
		}else if(Plyr.get(playerid-1).getSurname()==null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName" + " SET " + Plyr.get(playerid-1).getFirstname() + "\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName" + " SET " + Plyr.get(playerid-1).getFirstname()+ " " +Plyr.get(playerid-1).getSurname() + "\0");
		}
		
		if(Plyr.get(playerid-1).getNationality()==null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry" + " SET " + "" + "\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogo" + " SET " + flag_path_viz + Plyr.get(playerid-1).getNationality() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry" + " SET " + Plyr.get(playerid-1).getNationality() + "\0");
		}
		if(Plyr.get(playerid-1).getTeamId()==match.getHomeFirstPlayer().getTeamId()) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LT$Base$TLogo*TEXTURE*IMAGE SET " + logo_path + match.getHomeFirstPlayer().getTeam().getTeamBadge() +" \0");
			//print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogo" + " SET " + logo_path + match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDesignation" + " SET " + match.getHomeFirstPlayer().getTeam().getTeamName1() + "\0");
		}else if(Plyr.get(playerid-1).getTeamId()==match.getAwayFirstPlayer().getTeamId()) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LT$Base$TLogo*TEXTURE*IMAGE SET " + logo_path + match.getAwayFirstPlayer().getTeam().getTeamBadge() +" \0");
			//print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogo" + " SET " + logo_path + match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDesignation" + " SET " + match.getAwayFirstPlayer().getTeam().getTeamName1() + "\0");
		}else if(Plyr.get(playerid-1).getTeamId()==match.getHomeSecondPlayer().getTeamId()) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LT$Base$TLogo*TEXTURE*IMAGE SET " + logo_path + match.getHomeSecondPlayer().getTeam().getTeamBadge() +" \0");
			//print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogo" + " SET " + logo_path + match.getHomeSecondPlayer().getTeam().getTeamBadge() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDesignation" + " SET " + match.getHomeSecondPlayer().getTeam().getTeamName1() + "\0");
		}else if(Plyr.get(playerid-1).getTeamId()==match.getAwaySecondPlayer().getTeamId()) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LT$Base$TLogo*TEXTURE*IMAGE SET " + logo_path + match.getAwaySecondPlayer().getTeam().getTeamBadge() +" \0");
			//print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogo" + " SET " + logo_path + match.getAwaySecondPlayer().getTeam().getTeamBadge() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDesignation" + " SET " + match.getAwaySecondPlayer().getTeam().getTeamName1() + "\0");
		}
		TimeUnit.MILLISECONDS.sleep(200);
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.800 \0");
	}
	public void populateNameSuperSP1(PrintWriter print_writer, String viz_sence_path,int playerid, String designation, List<Player> Plyr,List<Team> teams,List<VariousText>vt,Match match,String selectedbroadcaster) {
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName" + " SET " + Plyr.get(playerid-1).getFull_name() + "\0");
		
		if(Plyr.get(playerid-1).getNationality()==null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry" + " SET " + "" + "\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogo" + " SET " + flag_path_viz + Plyr.get(playerid-1).getNationality() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry" + " SET " + 
					Plyr.get(playerid-1).getNationality() + "\0");
		}
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LT$Base$TLogo*TEXTURE*IMAGE SET " + logo_path + teams.get(Plyr.get(playerid-1).getTeamId()-1).getTeamBadge() +" \0");
		
		for(VariousText vtext : vt) {
			if(vtext.getVariousType().equalsIgnoreCase("NameSuperSingle") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDesignation" + " SET " + vtext.getVariousText() + "\0");
				break;
			}else if(designation.equalsIgnoreCase("PLAYEROFTHEMATCH")){
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDesignation" + " SET " + 
						"PLAYER OF THE MATCH" + "\0");
			}else{
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDesignation" + " SET " + 
						teams.get(Plyr.get(playerid-1).getTeamId()-1).getTeamName1() + "\0");
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.800 \0");
	}
//	public void populateNameSuperDP(PrintWriter print_writer, String viz_sence_path,String teamtype,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
//		
//		//lgLogo
//		//tDesignation
//		//tName
//		switch(teamtype.toUpperCase()) {
//		case TennisUtil.HOME:
//			if(Plyr.get(match.getHomeFirstPlayerId()-1).getFirstname()==null) {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName" + " SET " + Plyr.get(match.getHomeFirstPlayerId()-1).getSurname() + "\0");
//			}else if(Plyr.get(match.getHomeFirstPlayerId()-1).getFirstname()==null) {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName" + " SET " + Plyr.get(match.getHomeFirstPlayerId()-1).getFirstname() + "\0");
//			}else {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName" + " SET " + Plyr.get(match.getHomeFirstPlayerId()-1).getFirstname() + " " + Plyr.get(match.getHomeFirstPlayerId()-1).getSurname() + "\0");
//			}
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDesignation" + " SET " + Plyr.get(match.getHomeFirstPlayerId()-1).getTeam().getTeamName1() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogo" + " SET " + flag_path + 
//					Plyr.get(match.getHomeFirstPlayerId()-1).getTeam().getTeamName4() + TennisUtil.PNG_EXTENSION + "\0");
//			
//			if(Plyr.get(match.getHomeFirstPlayerId()-1).getRankingDouble() == 0) {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + "" + "\0");
//			}else {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + Plyr.get(match.getHomeFirstPlayerId()-1).getRankingDouble() + "\0");
//			}
//			
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + Plyr.get(match.getHomeSecondPlayerId()-1).getFirstname() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(match.getHomeSecondPlayerId()-1).getSurname() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag2" + " SET " + flag_path + 
//					Plyr.get(match.getHomeSecondPlayerId()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
//			
//			if(Plyr.get(match.getHomeSecondPlayerId()-1).getRankingDouble() == 0) {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + "" + "\0");
//			}else {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + Plyr.get(match.getHomeSecondPlayerId()-1).getRankingDouble() + "\0");
//			}
//			break;
//		case TennisUtil.AWAY:
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + Plyr.get(match.getAwayFirstPlayerId()-1).getFirstname() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(match.getAwayFirstPlayerId()-1).getSurname() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag1" + " SET " + flag_path + 
//					Plyr.get(match.getAwayFirstPlayerId()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
//			
//			if(Plyr.get(match.getAwayFirstPlayerId()-1).getRankingDouble() == 0) {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + "" + "\0");
//			}else {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + Plyr.get(match.getAwayFirstPlayerId()-1).getRankingDouble() + "\0");
//			}
//			
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + Plyr.get(match.getAwaySecondPlayerId()-1).getFirstname() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(match.getAwaySecondPlayerId()-1).getSurname() + "\0");
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag2" + " SET " + flag_path + 
//					Plyr.get(match.getAwaySecondPlayerId()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
//			
//			if(Plyr.get(match.getAwaySecondPlayerId()-1).getRankingDouble() == 0) {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + "" + "\0");
//			}else {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + Plyr.get(match.getAwaySecondPlayerId()-1).getRankingDouble() + "\0");
//			}
//			break;
//		}
//		
//		for(VariousText vtext : vt) {
//			if(vtext.getVariousType().equalsIgnoreCase("NameSuperDouble") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + vtext.getVariousText() + "\0");
//				break;
//			}else {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + "TATA OPEN MAHARASHTRA 2024" + "\0");
//			}
//		}
//		
//		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.980 \0");
//	}
//	public void populateNameSuperDP1(PrintWriter print_writer, String viz_sence_path,int firstPlayerId,int secondPlayerId,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
//		
//		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + Plyr.get(firstPlayerId-1).getFirstname() + "\0");
//		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(firstPlayerId-1).getSurname() + "\0");
//		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag1" + " SET " + flag_path + 
//				Plyr.get(firstPlayerId-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
//		
//		if(Plyr.get(firstPlayerId-1).getRankingDouble() == 0) {
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + "" + "\0");
//		}else {
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + Plyr.get(firstPlayerId-1).getRankingDouble() + "\0");
//		}
//		
//		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + Plyr.get(secondPlayerId-1).getFirstname() + "\0");
//		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(secondPlayerId-1).getSurname() + "\0");
//		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag2" + " SET " + flag_path + 
//				Plyr.get(secondPlayerId-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
//		
//		if(Plyr.get(secondPlayerId-1).getRankingDouble() == 0) {
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + "" + "\0");
//		}else {
//			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + Plyr.get(secondPlayerId-1).getRankingDouble() + "\0");
//		}
//		
//		for(VariousText vtext : vt) {
//			if(vtext.getVariousType().equalsIgnoreCase("NameSuperDouble") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + vtext.getVariousText() + "\0");
//				break;
//			}else {
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + "TATA OPEN MAHARASHTRA 2024" + "\0");
//			}
//		}
//		
//		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.980 \0");
//	}
	
	public void populateSpeed(PrintWriter print_writer, String viz_sence_path,int speed,Match match,String selectedbroadcaster) {
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-SPEEDHEAD" + " SET " + "SERVE SPEED" + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-SPEED" + " SET " + speed + " KPH" + "\0");
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.980 \0");
	}

	public void populateCross(PrintWriter print_writer, String viz_sence_path,String type,Match match,List<Team> teams,String selectedbroadcaster) throws InterruptedException {
		
		switch(match.getMatchType().toUpperCase()) {
		case TennisUtil.SINGLES:
			switch(type.toUpperCase()) {
			
			case TennisUtil.HOME:
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogoTop" + " SET " + logo_path + match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$TopRight$Header_Sub_Grp$Header*GEOM*TEXT SET " + match.getHomeFirstPlayer().getFull_name() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$TopRight$Header_Sub_Grp$SubHeader2*GEOM*TEXT SET " + 
						teams.get(match.getHomeFirstPlayer().getTeamId()-1).getTeamName1() + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogoBottom" + " SET " + logo_path + match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BottomLeft$Header_Sub_Grp$Header*GEOM*TEXT SET " + match.getAwayFirstPlayer().getFull_name() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BottomLeft$Header_Sub_Grp$SubHeader2*GEOM*TEXT SET " + 
						teams.get(match.getAwayFirstPlayer().getTeamId()-1).getTeamName1() + "\0");
				break;
			case TennisUtil.AWAY:
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogoTop" + " SET " + logo_path + match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$TopRight$Header_Sub_Grp$Header*GEOM*TEXT SET " + match.getAwayFirstPlayer().getFull_name() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$TopRight$Header_Sub_Grp$SubHeader2*GEOM*TEXT SET " + 
						teams.get(match.getAwayFirstPlayer().getTeamId()-1).getTeamName1() + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogoBottom" + " SET " + logo_path + match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BottomLeft$Header_Sub_Grp$Header*GEOM*TEXT SET " + match.getHomeFirstPlayer().getFull_name() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BottomLeft$Header_Sub_Grp$SubHeader2*GEOM*TEXT SET " + 
						teams.get(match.getHomeFirstPlayer().getTeamId()-1).getTeamName1() + "\0");
				break;
			}
			break;
		case TennisUtil.DOUBLES:
			switch(type.toUpperCase()) {
			case TennisUtil.HOME:
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogoTop" + " SET " + logo_path + match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$TopRight$Header_Sub_Grp$Header*GEOM*TEXT SET " + match.getHomeFirstPlayer().getTicker_name() + 
						" / " + match.getHomeSecondPlayer().getTicker_name() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$TopRight$Header_Sub_Grp$SubHeader2*GEOM*TEXT SET " + 
						teams.get(match.getHomeFirstPlayer().getTeamId()-1).getTeamName1() + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogoBottom" + " SET " + logo_path + match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BottomLeft$Header_Sub_Grp$Header*GEOM*TEXT SET " + match.getAwayFirstPlayer().getTicker_name() + 
						" / " + match.getAwaySecondPlayer().getTicker_name() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BottomLeft$Header_Sub_Grp$SubHeader2*GEOM*TEXT SET " + 
						teams.get(match.getAwayFirstPlayer().getTeamId()-1).getTeamName1() + "\0");
				break;
			case TennisUtil.AWAY:
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogoTop" + " SET " + logo_path + match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$TopRight$Header_Sub_Grp$Header*GEOM*TEXT SET " + match.getAwayFirstPlayer().getTicker_name() + 
						" / " + match.getAwaySecondPlayer().getTicker_name() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$TopRight$Header_Sub_Grp$SubHeader2*GEOM*TEXT SET " + 
						teams.get(match.getAwayFirstPlayer().getTeamId()-1).getTeamName1() + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogoTop" + " SET " + logo_path + match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BottomLeft$Header_Sub_Grp$Header*GEOM*TEXT SET " + match.getHomeFirstPlayer().getTicker_name() + 
						" / " + match.getHomeSecondPlayer().getTicker_name() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BottomLeft$Header_Sub_Grp$SubHeader2*GEOM*TEXT SET " + 
						teams.get(match.getHomeFirstPlayer().getTeamId()-1).getTeamName1() + "\0");
				break;
			}
			break;
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.800 \0");
	}
	
	public void populateMatchIdDouble(PrintWriter print_writer, String viz_sence_path,String withOrWithoutPhoto,TennisService tennisService,Match match,String selectedbroadcaster) throws InterruptedException {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "www.tplsport.com" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + match.getMatchIdent() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame1FirstName" + " SET " + match.getHomeFirstPlayer().getTeam().getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame2FirstName" + " SET " + match.getAwayFirstPlayer().getTeam().getTeamName2() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vScore"+ " SET " + "0" + "\0");

			if(withOrWithoutPhoto.equalsIgnoreCase("without")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "0" + "\0");
			}else if(withOrWithoutPhoto.equalsIgnoreCase("with")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "1" + "\0");
			}
			
//			HOME PLAYER
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HomeTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 1 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage1" + " SET " + left_photo_path + 
					match.getHomeFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage1B" + " SET " + left_photo_path + 
					match.getHomeSecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
					match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " 
					+ match.getHomeFirstPlayer().getTicker_name() + " / " + match.getHomeSecondPlayer().getTicker_name() + "\0");

//			AWAY PLAYER
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$AwayTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 1 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage2B" + " SET " + right_photo_path+ 
					match.getAwayFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage2" + " SET " + right_photo_path + 
					match.getAwaySecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
					match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " 
					+ match.getAwayFirstPlayer().getTicker_name() + " / " + match.getAwaySecondPlayer().getTicker_name() + "\0");
			
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + "LIVE FROM " + tennisService.getGround().get(0).getFullname() + "\0");
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.900 \0");
		}
	}
	public void populateMatchId(PrintWriter print_writer, String viz_sence_path,String withOrWithoutPhoto,TennisService tennisService,Match match,String selectedbroadcaster) throws InterruptedException {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "www.tplsport.com" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + match.getMatchIdent() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame1FirstName" + " SET " + 
					match.getHomeFirstPlayer().getTeam().getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame2FirstName" + " SET " + 
					match.getAwayFirstPlayer().getTeam().getTeamName2() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vScore"+ " SET " + "0" + "\0");
			
			if(withOrWithoutPhoto.equalsIgnoreCase("without")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "0" + "\0");
			}else if(withOrWithoutPhoto.equalsIgnoreCase("with")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "1" + "\0");
			}
			
			//HOME PLAYER
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HomeTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 0 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage1" + " SET " + left_photo_path + 
					match.getHomeFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " + match.getHomeFirstPlayer().getFull_name() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
					match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
			
			//AWAY PLAYER
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$AwayTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 0 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage2" + " SET " + right_photo_path + 
					match.getAwayFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION +"\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " + match.getAwayFirstPlayer().getFull_name() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
					match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + "LIVE FROM " + tennisService.getGround().get(0).getFullname() + "\0");
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.900 \0");
		}
	}
	
	public void populateLtMatchIdScoreOrPromo(PrintWriter print_writer, String viz_sence_path,String type,String withOrWithoutPhoto,TennisService tennisService,
			List<Fixture> fixtures,Match match,String selectedbroadcaster) throws InterruptedException {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			switch (type) {
			case "Ident": case "Score":
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tExtraInfo" + " SET " + match.getMatchIdent() + "\0");
				
				if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.SINGLES)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " + 
							match.getHomeFirstPlayer().getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " + 
							match.getAwayFirstPlayer().getTicker_name() + "\0");
				} else if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.DOUBLES)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " + 
							match.getHomeFirstPlayer().getTicker_name()+ " / " + match.getHomeSecondPlayer().getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " + 
							match.getAwayFirstPlayer().getTicker_name() + " / " + match.getAwaySecondPlayer().getTicker_name() + "\0");
				}
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgHomeTeamLogo" + " SET " + logo_path + 
						match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgAwayTeamLogo" + " SET " + logo_path + 
						match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
				
				if(type.equalsIgnoreCase("Ident")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHomeScore" + " SET " + "0" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vAwayScore" + " SET " + "0" + "\0");
				}else if(type.equalsIgnoreCase("Score")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHomeScore" + " SET " + "1" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vAwayScore" + " SET " + "1" + "\0");
					
					for (Set set : match.getSets()) {
						for (Game game : set.getGames()) { 
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomeScore" + " SET " + game.getHome_score() + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayScore" + " SET " + game.getAway_score() + "\0");
						}
					}
				}
				
				if(withOrWithoutPhoto.equalsIgnoreCase("without")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHomePlayer"+ " SET " + "0" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vAwayPlayerServe"+ " SET " + "0" + "\0");
				}else if(withOrWithoutPhoto.equalsIgnoreCase("with")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHomePlayer"+ " SET " + "1" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vAwayPlayerServe"+ " SET " + "1" + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgHomePlayerImage1" + " SET " + left_photo_path + 
							match.getHomeFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgAwayPlayerImage1" + " SET " + right_photo_path + 
							match.getAwayFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION +"\0");
					
					if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.DOUBLES)) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHomePlayer"+ " SET " + "2" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vAwayPlayerServe"+ " SET " + "2" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgHomePlayerImage2" + " SET " + left_photo_path + 
								match.getHomeSecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgAwayPlayerImage2" + " SET " + right_photo_path + 
								match.getAwaySecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION +"\0");
					}
				}
				break;

			default:
				Fixture fixture = fixtures.stream().filter(fix -> fix.getMatchId() == Integer.valueOf(type)).findAny().orElse(null);
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tExtraInfo" + " SET " + "UP NEXT - MATCH " + 
						fixture.getMatchNumber() + " - GAME " + fixture.getGameNumber() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHomeScore" + " SET " + "0" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vAwayScore" + " SET " + "0" + "\0");
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgHomeTeamLogo" + " SET " + logo_path + 
						tennisService.getAllTeams().get(fixture.getHome_FirstPlayer().getTeamId()-1).getTeamBadge() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgAwayTeamLogo" + " SET " + logo_path + 
						tennisService.getAllTeams().get(fixture.getAway_FirstPlayer().getTeamId()-1).getTeamBadge() + "\0");
				
				if(withOrWithoutPhoto.equalsIgnoreCase("without")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHomePlayer"+ " SET " + "0" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vAwayPlayerServe"+ " SET " + "0" + "\0");
				}else if(withOrWithoutPhoto.equalsIgnoreCase("with")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHomePlayer"+ " SET " + "1" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vAwayPlayerServe"+ " SET " + "1" + "\0");
				}
				
				if(fixture.getHomePlayerSecond() != null && fixture.getAwayPlayerSecond() != null) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " + 
							fixture.getHome_FirstPlayer().getTicker_name() + "/" + fixture.getHome_SecondPlayer().getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " + 
							fixture.getAway_FirstPlayer().getTicker_name() + "/" + fixture.getAway_SecondPlayer().getTicker_name() + "\0");
					
					if(withOrWithoutPhoto.equalsIgnoreCase("with")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHomePlayer"+ " SET " + "2" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vAwayPlayerServe"+ " SET " + "2" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgHomePlayerImage1" + " SET " + left_photo_path + 
								fixture.getHome_FirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgAwayPlayerImage1" + " SET " + right_photo_path + 
								fixture.getAway_FirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION +"\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgHomePlayerImage2" + " SET " + left_photo_path + 
								fixture.getHome_SecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgAwayPlayerImage2" + " SET " + right_photo_path + 
								fixture.getAway_SecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION +"\0");
					}
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " + 
							fixture.getHome_FirstPlayer().getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " + 
							fixture.getAway_FirstPlayer().getTicker_name() + "\0");
					
					if(withOrWithoutPhoto.equalsIgnoreCase("with")) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHomePlayer"+ " SET " + "1" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vAwayPlayerServe"+ " SET " + "1" + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgHomePlayerImage1" + " SET " + left_photo_path + 
								fixture.getHome_FirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgAwayPlayerImage1" + " SET " + right_photo_path + 
								fixture.getAway_FirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION +"\0");
					}
				}
				break;
			}
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.640 ExtraInfoIn 0.480 \0");
		}
	}
	
	public void populateLtTeam(PrintWriter print_writer, String viz_scene_path, int teamId,TennisService tennisService, List<Team> teams, Match match, String selectedbroadcaster) throws StreamReadException, DatabindException, IOException {
		if (match == null) {
			System.out.println("ERROR: LtTeam -> Match is null");
		} else {
				
			homeWon = 0;
			awayWon = 0;
//			int homeScore = 0;
//			int awayScore = 0;
			int rowid = 1;
			Team team = teams.stream().filter(tm -> tm.getTeamId() == teamId).findAny().orElse(null);
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLogo" + " SET " + logo_path + 
					team.getTeamBadge() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + 
					team.getTeamName1() + " - " + "SQUADS" + "\0");
			
//			List<Fixture> all_db_fixture;
//			List<File> all_match_files;
//			File this_file = null;
//			Match this_match = null;
//			
//			all_match_files = Arrays.asList(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
//				@Override
//				public boolean accept(File pathname) {
//					String name = pathname.getName().toLowerCase();
//					return name.endsWith(".json") && pathname.isFile();
//				}
//			}));
//			all_db_fixture = tennisService.getFixtures();
//			
//			if(all_db_fixture != null) {
//				Fixture curr_fixture = all_db_fixture.stream().filter(fix -> 
//				fix.getMatchfilename().equalsIgnoreCase(match.getMatchFileName())).findAny().orElse(null);		
//				if(curr_fixture != null) {
//					int totalHomeScore = 0;
//					int totalAwayScore = 0;
//					pastHomeScore = 0;
//					pastAwayScore = 0;
//					for (Fixture fixture : all_db_fixture.stream().filter(fix -> fix.getMatchNumber()==curr_fixture.getMatchNumber()).collect(Collectors.toList())) {
//						this_file = all_match_files.stream().filter(fil -> fil.getName().equalsIgnoreCase(fixture.getMatchfilename())).findAny().orElse(null);
//						if(this_file != null) {
//							homeScore = 0;
//							awayScore = 0;
//							
//							if(!this_file.getName().equalsIgnoreCase(match.getMatchFileName())) {
//								this_match = TennisFunctions.populateMatchVariables(tennisService, new ObjectMapper().readValue(
//										new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY + this_file.getName()), Match.class));
//								if(match.getHomeFirstPlayer().getTeamId()==this_match.getHomeFirstPlayer().getTeamId()
//										|| match.getHomeFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
//										|| match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
//										|| match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId())
//									{
//										if(this_match.getSets() != null) {
//											for (Set set : this_match.getSets()) {
//												for (Game game : set.getGames()) {
//													 homeScore = Integer.valueOf(game.getHome_score());
//													 awayScore = Integer.valueOf(game.getAway_score());
//
//													 pastHomeScore = pastHomeScore + homeScore;
//													 pastAwayScore = pastAwayScore + awayScore;
//												}
//											}
//										}
//									}
//							}else {
//								if(match.getSets() != null) {
//									homeScore = Integer.valueOf(match.getSets().get(0).getGames().get(0).getHome_score());
//									awayScore = Integer.valueOf(match.getSets().get(0).getGames().get(0).getAway_score());
//									totalHomeScore = (pastHomeScore + homeScore);
//									totalAwayScore = (pastAwayScore + awayScore);
//								}else {
//									totalHomeScore = (pastHomeScore + homeScore);
//									totalAwayScore = (pastAwayScore + awayScore);
//								}
//								
//							}
//							
//							if(this_match.getHomeFirstPlayer().getTeamId() == teamId) {
//								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + 
//										team.getTeamName1() + " - " + totalHomeScore + "\0");
//							}else if(this_match.getAwayFirstPlayer().getTeamId() == teamId) {
//								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + 
//										team.getTeamName1() + " - " + totalAwayScore + "\0");
//							}
//							
//							}	
//						}
//					}
//				}
			
			
			for(Player plyr : match.getPlayers()) {
				if(plyr.getTeamId() == teamId) {
					if(rowid <=3) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgImage0" + rowid + " SET " 
								+ left_photo_path + plyr.getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tName0" + rowid + " SET " + 
								plyr.getFull_name() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vCaptain0" + rowid + " SET " + "0" + "\0");
						
						
						if(match.getMatchType().equalsIgnoreCase(TennisUtil.SINGLES)) {
							if(match.getHomeFirstPlayerId() == plyr.getPlayerId() || match.getAwayFirstPlayerId() == plyr.getPlayerId()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight0" + rowid + " SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight0" + rowid + " SET " + "0" + "\0");
							}
						}else if(match.getMatchType().equalsIgnoreCase(TennisUtil.DOUBLES)) {
							if(match.getHomeFirstPlayerId() == plyr.getPlayerId() || match.getAwayFirstPlayerId() == plyr.getPlayerId() || 
									match.getHomeSecondPlayerId() == plyr.getPlayerId() || match.getAwaySecondPlayerId() == plyr.getPlayerId()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight0" + rowid + " SET " + "1" + "\0");
							}else {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vHighlight0" + rowid + " SET " + "0" + "\0");
							}
						}
						rowid = rowid + 1;
					}
				}
			}
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene_path + " C:/Temp/Preview.png In 1.700 \0");
		}
	}
	
	public void populateOrderOfTie(PrintWriter print_writer, String viz_scene_path, int whichDay,TennisService tennisService,List<Result> result,List<Team> team, List<VariousText>vt, Match match, String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: FIX & RES -> Match is null");
		} else {
				
			int row = 0;
			
			for(VariousText vtext : vt) {
				if(vtext.getVariousType().equalsIgnoreCase("ORDEROFTIEHEADER") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + vtext.getVariousText() + "\0");
					break;
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + "FIXTURES" + "\0");
				}
			}
			
			for(VariousText vtext : vt) {
				if(vtext.getVariousType().equalsIgnoreCase("ORDEROFTIESUB") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + vtext.getVariousText() + "\0");
					break;
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + 
							tennisService.getGround().get(0).getShortname() + "\0");
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
					"TLogo" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
					"TLogo" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$First*ACTIVE SET 0 " + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$Second*ACTIVE SET 0 " + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match2$AnimIn$Third*ACTIVE SET 0 " + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match2$AnimIn$Fourth*ACTIVE SET 0 " + "\0");
		
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$TotalScoreGrp*ACTIVE SET 0 " + "\0");
			
				
			for(Result  res : result) {
				if(Integer.valueOf(res.getDay()) == whichDay) {
					row = row + 1 ;
					
					if(res.getMatchNumber() == 21) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDate"+ row + " SET " + "SEMI-FINAL 1" + "\0");
					}else if(res.getMatchNumber() == 22) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDate"+ row + " SET " + "SEMI-FINAL 2" + "\0");
					}else if(res.getMatchNumber() == 23) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDate"+ row + " SET " + "FINAL" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDate"+ row + " SET " + "MATCH - " + res.getMatchNumber() + "\0");
					}
					
					if(row == 4) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$First*ACTIVE SET 1 " + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$Second*ACTIVE SET 1 " + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match2$AnimIn$Third*ACTIVE SET 1 " + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match2$AnimIn$Fourth*ACTIVE SET 1 " + "\0");
					}else if(row == 3) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$First*ACTIVE SET 1 " + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$Second*ACTIVE SET 1 " + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match2$AnimIn$Third*ACTIVE SET 1 " + "\0");
					}else if(row == 2) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$First*ACTIVE SET 1 " + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$Second*ACTIVE SET 1 " + "\0");
					}else if(row == 1) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$First*ACTIVE SET 1 " + "\0");
					}
					
					if(res.getMatchResult()!= null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeparator"+ row + " SET " + res.getMatchResult() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeparator"+ row + " SET " + "v" + "\0");
					}
					
					if(res.getHomeTieBreaker() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamNameA" + row + " SET " + 
								team.get(res.getHomeTeam()-1).getTeamName1() + " (T)" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamNameA" + row + " SET " + 
								team.get(res.getHomeTeam()-1).getTeamName1() + "\0");
					}
					
					if(res.getAwayTieBreaker() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamNameB" + row + " SET " + 
								team.get(res.getAwayTeam()-1).getTeamName1() + " (T)" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamNameB" + row + " SET " + 
								team.get(res.getAwayTeam()-1).getTeamName1() + "\0");
					}
				}
			}
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene_path + " C:/Temp/Preview.png In 2.500 \0");
		}

	}
	
	public void populateOrderOfMatch(PrintWriter print_writer, String viz_scene_path, int whichTie,TennisService tennisService,List<Fixture> fixture,
			List<Player> players,List<Team> team,List<Result> result, Match match, String selectedbroadcaster) throws JAXBException {
		if (match == null) {
			System.out.println("ERROR: FIX & RES -> Match is null");
		} else {
			homeWon = 0;
			awayWon = 0;
			
			int homeScore = 0;
			int awayScore = 0;
			
			int row = 0;
			
			String MatchName="";
			
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 0);
			
			boolean homeTie=false,awayTie=false;
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + "ORDER OF PLAY" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Team$HomeTeamGrp$LogoAll$HomeTeamLogoOverlay*ACTIVE SET 1 " + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Team$HomeTeamGrp$LogoAll$HomeTeamLogo*ACTIVE SET 1 " + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Team$AwayTeamGrp$LogoAll$Away'TeamLogoOverlay*ACTIVE SET 1 " + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$Team$AwayTeamGrp$LogoAll$AwayTeamLogo*ACTIVE SET 1 " + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$First*ACTIVE SET 0 " + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$Second*ACTIVE SET 0 " + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match2$AnimIn$Third*ACTIVE SET 0 " + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match2$AnimIn$Fourth*ACTIVE SET 0 " + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$TotalScoreGrp*ACTIVE SET 1 " + "\0");
			
			for(Result res : result) {
				if(res.getMatchNumber() == whichTie) {
					if(res.getHomeTieBreaker() != null) {
						homeTie = true;
					}
					if(res.getAwayTieBreaker() != null) {
						awayTie = true;
					}
				}
			}
			
			for(Fixture  fixt : fixture) {
				if(fixt.getMatchNumber() == whichTie) {
					row = row + 1 ;
					
					if(whichTie == 21) {
						MatchName = "SEMI-FINAL 1";
					}else if(whichTie == 22) {
						MatchName = "SEMI-FINAL 2";
					}else if(whichTie == 23) {
						MatchName = "FINAL";
					}else {
						MatchName = "MATCH - " + whichTie;
					}
					

					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + MatchName + 
							" | " + team.get(players.get(fixt.getHomePlayerFirst()-1).getTeamId() -1).getTeamName1() + " v " + 
							team.get(players.get(fixt.getAwayPlayerFirst()-1).getTeamId() -1).getTeamName1() + "\0");
					
					if(row == 4) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$First*ACTIVE SET 1 " + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$Second*ACTIVE SET 1 " + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match2$AnimIn$Third*ACTIVE SET 1 " + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match2$AnimIn$Fourth*ACTIVE SET 1 " + "\0");
					}else if(row == 3) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$First*ACTIVE SET 1 " + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$Second*ACTIVE SET 1 " + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match2$AnimIn$Third*ACTIVE SET 1 " + "\0");
					}else if(row == 2) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$First*ACTIVE SET 1 " + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$Second*ACTIVE SET 1 " + "\0");
					}else if(row == 1) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st2$Match1$AnimIn$First*ACTIVE SET 1 " + "\0");
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDate"+ row + " SET " + fixt.getCategary() + "\0");
					if(fixt.getMatchResult()!= null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeparator"+ row + " SET " +
							fixt.getMatchResult() + "\0");
						
						homeScore = homeScore + Integer.valueOf(fixt.getMatchResult().split("-")[0]);
						awayScore = awayScore + Integer.valueOf(fixt.getMatchResult().split("-")[1]);
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeparator"+ row + " SET " + "v" + "\0");
					}
					if(homeScore>0 || awayScore>0) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTotalScore" + " SET " + homeScore +" - "+ awayScore + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTotalScore" + " SET " + "" + "\0");
					}
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
							team.get(players.get(fixt.getHomePlayerFirst()-1).getTeamId() -1).getTeamBadge() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
							team.get(players.get(fixt.getAwayPlayerFirst()-1).getTeamId() -1).getTeamBadge() + "\0");
					
					if(homeTie == true) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomeTeamName" + " SET " + 
								team.get(players.get(fixt.getHomePlayerFirst()-1).getTeamId() -1).getTeamName1() + " (T)" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomeTeamName" + " SET " + 
								team.get(players.get(fixt.getHomePlayerFirst()-1).getTeamId() -1).getTeamName1() + "\0");
					}
					
					if(awayTie == true) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayTeamName" + " SET " + 
								team.get(players.get(fixt.getAwayPlayerFirst()-1).getTeamId() -1).getTeamName1() + " (T)" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayTeamName" + " SET " + 
								team.get(players.get(fixt.getAwayPlayerFirst()-1).getTeamId() -1).getTeamName1() + "\0");
					}
					
					if(fixt.getHomePlayerSecond() != null && fixt.getAwayPlayerSecond() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamNameA" + row + " SET " + 
								players.get(fixt.getHomePlayerFirst()-1).getTicker_name() + " / " + players.get(fixt.getHomePlayerSecond()-1).getTicker_name() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamNameB" + row + " SET " + 
								players.get(fixt.getAwayPlayerFirst()-1).getTicker_name() + " / " + players.get(fixt.getAwayPlayerSecond()-1).getTicker_name() + "\0");
					}
					else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamNameA" + row + " SET " + 
								players.get(fixt.getHomePlayerFirst()-1).getFull_name() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamNameB" + row + " SET " + 
								players.get(fixt.getAwayPlayerFirst()-1).getFull_name() + "\0");
					}

					
				}
			}
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_scene_path + " C:/Temp/Preview.png In 2.500 \0");
		}

	}
	
	public void populateTieResult(PrintWriter print_writer, String viz_sence_path,int tie_id,TennisService tennisService,List<Result> result,List<Team> teams,List<VariousText>vt,Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			
			if(tie_id == 21) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + "SEMI-FINAL 1" + "\0");
			}else if(tie_id == 22) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + "SEMI-FINAL 2" + "\0");
			}else if(tie_id == 23) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + "FINAL" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + "MATCH - " + 
						result.get(tie_id-1).getMatchNumber() + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "0" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HomeTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 0 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$AwayTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 0 \0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + "LIVE FROM " + tennisService.getGround().get(0).getFullname() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "www.tplsport.com" + "\0");
			
			for(Result res : result) {
				if(res.getMatchNumber() == tie_id) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame1FirstName" + " SET " + 
							teams.get(res.getHomeTeam()-1).getTeamName2() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame2FirstName" + " SET " + 
							teams.get(res.getAwayTeam()-1).getTeamName2() + "\0");
					
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
							teams.get(res.getHomeTeam()-1).getTeamBadge() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
							teams.get(res.getAwayTeam()-1).getTeamBadge() + "\0");
					
					if(res.getMatchResult() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vScore"+ " SET " + "1" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tScore" + " SET " + res.getMatchResult() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vScore"+ " SET " + "0" + "\0");
					}
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.900 \0");
		}
	}
	
	public void populateMatchPromo(PrintWriter print_writer, String viz_sence_path,int fixid,String withOrWithoutPhoto,TennisService tennisService,List<Fixture> fixture,List<Player> Plyr,List<Team> teams,List<VariousText>vt,Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + "COMING UP NEXT" + "\0");
			
			if(withOrWithoutPhoto.equalsIgnoreCase("without")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "0" + "\0");
			}else if(withOrWithoutPhoto.equalsIgnoreCase("with")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "1" + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vScore"+ " SET " + "0" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HomeTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 0 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$AwayTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 0 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "www.tplsport.com" + "\0");
			
			for(VariousText vtext : vt) {
				if(vtext.getVariousType().equalsIgnoreCase("AllMatchPromo") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + 
							vtext.getVariousText() + "\0");
					break;
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + "GAME " + 
						fixture.get(fixid-1).getGameNumber() + " - " +fixture.get(fixid-1).getCategary() + "\0");
				}
			}
			
			for(Fixture fix : fixture) {
				if(fix.getMatchId() == fixid) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " + 
							Plyr.get(fix.getHomePlayerFirst()-1).getFull_name() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " + 
							Plyr.get(fix.getAwayPlayerFirst()-1).getFull_name() + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame1FirstName" + " SET " + 
							teams.get(Plyr.get(fix.getHomePlayerFirst()-1).getTeamId()-1).getTeamName2() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame2FirstName" + " SET " + 
							teams.get(Plyr.get(fix.getAwayPlayerFirst()-1).getTeamId()-1).getTeamName2() + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage1" + " SET " + left_photo_path + 
							Plyr.get(fix.getHomePlayerFirst()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage2" + " SET " + right_photo_path + 
							Plyr.get(fix.getAwayPlayerFirst()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
							teams.get(Plyr.get(fix.getHomePlayerFirst()-1).getTeamId()-1).getTeamBadge() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
							teams.get(Plyr.get(fix.getAwayPlayerFirst()-1).getTeamId()-1).getTeamBadge() + "\0");
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.900 \0");
		}
	}
	public void populateMatchDoublePromo(PrintWriter print_writer, String viz_sence_path,int fixid,String withOrWithoutPhoto,TennisService tennisService,List<Fixture> fixture,List<Player> Plyr,List<Team> teams,List<VariousText>vt,Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + "COMING UP NEXT" + "\0");
			if(withOrWithoutPhoto.equalsIgnoreCase("without")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "0" + "\0");
			}else if(withOrWithoutPhoto.equalsIgnoreCase("with")) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "1"+ " SET " + "1" + "\0");
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vScore"+ " SET " + "0" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HomeTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 1 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$AwayTeamGrp$LogoAll$IDType$Logo_PlayerImage$Players*FUNCTION*Omo*vis_con SET 1 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "www.tplsport.com" + "\0");
			
			for(VariousText vtext : vt) {
				if(vtext.getVariousType().equalsIgnoreCase("AllMatchPromo") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + 
							vtext.getVariousText() + "\0");
					break;
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + "GAME " + 
						fixture.get(fixid-1).getGameNumber() + " - " + fixture.get(fixid-1).getCategary() + "\0");
				}
			}
			
			for(Fixture fix : fixture) {
				if(fix.getMatchId() == fixid) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " + 
							Plyr.get(fix.getHomePlayerFirst()-1).getTicker_name() + " / " + Plyr.get(fix.getHomePlayerSecond()-1).getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " + 
							Plyr.get(fix.getAwayPlayerFirst()-1).getTicker_name() + " / " + Plyr.get(fix.getAwayPlayerSecond()-1).getTicker_name() + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame1FirstName" + " SET " + 
							teams.get(Plyr.get(fix.getHomePlayerFirst()-1).getTeamId()-1).getTeamName2() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame2FirstName" + " SET " + 
							teams.get(Plyr.get(fix.getAwayPlayerFirst()-1).getTeamId()-1).getTeamName2() + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
							teams.get(Plyr.get(fix.getHomePlayerFirst()-1).getTeamId()-1).getTeamBadge() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
							teams.get(Plyr.get(fix.getAwayPlayerFirst()-1).getTeamId()-1).getTeamBadge() + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage1" + " SET " + left_photo_path + 
							Plyr.get(fix.getHomePlayerFirst()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage1B" + " SET " + left_photo_path + 
							Plyr.get(fix.getHomePlayerSecond()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage2B" + " SET " + right_photo_path + 
							Plyr.get(fix.getAwayPlayerFirst()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerImage2" + " SET " + right_photo_path + 
							Plyr.get(fix.getAwayPlayerSecond()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.900 \0");
		}
	}
	public void populateLtMatchPromo(PrintWriter print_writer, String viz_sence_path,int fixid,List<Fixture> fixture,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			for(VariousText vtext : vt) {
				if(vtext.getVariousType().equalsIgnoreCase("AllMatchPromo") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + 
							vtext.getVariousText() + "\0");
					break;
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + "COMING UP NEXT - " + 
							fixture.get(fixid-1).getCategary() + "\0");
				}
			}
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vResults" + " SET " + "0" + "\0");
			
			for(Fixture fix : fixture) {
				if(fix.getMatchId() == fixid) {
					if(Plyr.get(fix.getHomePlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getHomePlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getNationality() + "\0");
					}
					
					if(Plyr.get(fix.getHomePlayerFirst()-1).getRankingSingle() == 0) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getRankingSingle() + "\0");
					}
					
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getRankingSingle() == 0) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getRankingSingle() + "\0");
					}
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.000 \0");
		}
	}
	public void populateLtMatchDoublePromo(PrintWriter print_writer, String viz_sence_path,int fixid,List<Fixture> fixture,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			for(VariousText vtext : vt) {
				if(vtext.getVariousType().equalsIgnoreCase("AllMatchPromo") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + 
							vtext.getVariousText() + "\0");
					break;
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + "COMING UP NEXT - " + 
							fixture.get(fixid-1).getCategary() + "\0");
				}
			}
			
			for(Fixture fix : fixture) {
				if(fix.getMatchId() == fixid) {
					if(Plyr.get(fix.getHomePlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA1" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getHomePlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getNationality() + "\0");
					}
					
					if(Plyr.get(fix.getHomePlayerFirst()-1).getRankingDouble() == 0) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getRankingDouble() + "\0");
					}
					
					if(Plyr.get(fix.getHomePlayerSecond()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA2" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getHomePlayerSecond()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getNationality() + "\0");
					}
					
					if(Plyr.get(fix.getHomePlayerSecond()-1).getRankingDouble() == 0) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getRankingDouble() + "\0");
					}
					
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB1" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() + "\0");
					}
					
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getRankingDouble() == 0) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getRankingDouble() + "\0");
					}
					
					if(Plyr.get(fix.getAwayPlayerSecond()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB2" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerSecond()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getNationality() + "\0");
					}
					
					if(Plyr.get(fix.getAwayPlayerSecond()-1).getRankingDouble() == 0) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getRankingDouble() + "\0");
					}
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.000 \0");
		}
	}
	
	public void populateLocator(PrintWriter print_writer, String viz_sence_path,TennisService tennisService,Match match,List<Fixture> fixture,String selectedbroadcaster) throws InterruptedException {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			
			for(Fixture fix : fixture) {
				if(fix.getMatchfilename().equalsIgnoreCase(match.getMatchFileName())) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + "DAY " + fix.getDay() + "\0");
				}
			}
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + "LIVE FROM " + tennisService.getGround().get(0).getFullname() + "\0");
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.500 \0");
		}
	}
	
	public void populatePointsTable(PrintWriter print_writer,String viz_sence_path,TennisService tennisService,Match match,List<LeagueTeam> league_table,List<Team> teams,String selectedbroadcaster) throws InterruptedException {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			int row_id=0;
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + "POINTS TABLE" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + tennisService.getGround().get(0).getFullname() + "\0");
			
			for(LeagueTeam lg : league_table) {
				row_id = row_id + 1;
				if(lg.getQualifiedStatus().trim().equalsIgnoreCase("Q")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tQualify" + row_id + " SET " + "Q" + "\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tQualify" + row_id + " SET " + "" + "\0");
				}
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank" + row_id + " SET " + row_id + "\0");
				
				for(Team tm : teams) {
					if(tm.getTeamName4().equalsIgnoreCase(lg.getTeamName())) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamName" + row_id + " SET " + tm.getTeamName1() + "\0");
					}
				}
				
				if(lg.getTieStatus().equalsIgnoreCase("T")) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vTie" + row_id + " SET " + "1" + "\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vTie" + row_id + " SET " + "0" + "\0");
				}
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tMatchesValue" + row_id + " SET " + lg.getPlayed() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPointsValue" + row_id + " SET " + lg.getPoints() + "\0");
				
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 3.000 \0");
		}
	}
	
	public void populateltMatchId(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: Lt-Match -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vResults" + " SET " + "0" + "\0");
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			if(match.getHomeFirstPlayer().getRankingSingle() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + match.getHomeFirstPlayer().getRankingSingle() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			if(match.getAwayFirstPlayer().getRankingSingle() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + match.getAwayFirstPlayer().getRankingSingle() + "\0");
			}
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.000 \0");
		}
	}
	public void populateltMatchIdDouble(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: Lt-Match -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			if(match.getHomeFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + match.getHomeFirstPlayer().getRankingDouble() + "\0");
			}
			
			if(match.getHomeSecondPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA2" + " SET " + match.getHomeSecondPlayer().getSurname() + "\0");
			}
			if(match.getHomeSecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + match.getHomeSecondPlayer().getNationality() + "\0");
			}
			
			if(match.getHomeSecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + match.getHomeSecondPlayer().getRankingDouble() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB1" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + match.getAwayFirstPlayer().getRankingDouble() + "\0");
			}
			
			if(match.getAwaySecondPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameAB2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB2" + " SET " + match.getAwaySecondPlayer().getSurname() + "\0");
			}
			if(match.getAwaySecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + match.getAwaySecondPlayer().getNationality() + "\0");
			}
			
			if(match.getAwaySecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + match.getAwaySecondPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.000 \0");
		}
	}
	
	public void populateMatchStats(PrintWriter print_writer, String viz_sence_path, API_Tournament apiMatch,TennisService tennisService,List<Team> teams,Match match,String selectedbroadcaster) throws InterruptedException, JAXBException, IOException {
		if (match == null) {
			System.out.println("ERROR: Match-Stats -> Match is null");
		} else {
			homeWon = 0;
			awayWon = 0;
			int homeScore = 0;
			int awayScore = 0;
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + "MATCH STATISTICS" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$1st*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$6th*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$CenterGrp$DataAll$7th*ACTIVE SET 0 \0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomeFirstName" + " SET " + 
					teams.get(match.getHomeFirstPlayer().getTeamId()-1).getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomeLastName" + " SET " + 
					teams.get(match.getHomeFirstPlayer().getTeamId()-1).getTeamName3() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
					teams.get(match.getHomeFirstPlayer().getTeamId()-1).getTeamBadge() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayFirstName" + " SET " + 
					teams.get(match.getAwayFirstPlayer().getTeamId()-1).getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayLastName" + " SET " + 
					teams.get(match.getAwayFirstPlayer().getTeamId()-1).getTeamName3() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
					teams.get(match.getAwayFirstPlayer().getTeamId()-1).getTeamBadge() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomeValue02" + " SET " + 
					apiMatch.getTeamwiseStat().getTeamA().get(0).getAces() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead02" + " SET " + 
					"ACES"+ "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayValue02" + " SET " + 
					apiMatch.getTeamwiseStat().getTeamB().get(0).getAces() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomeValue03" + " SET " + 
					apiMatch.getTeamwiseStat().getTeamA().get(0).getDoubleFaults() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead03" + " SET " + 
					"DOUBLE FAULTS"+ "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayValue03" + " SET " + 
					apiMatch.getTeamwiseStat().getTeamB().get(0).getDoubleFaults() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomeValue04" + " SET " + 
					apiMatch.getTeamwiseStat().getTeamA().get(0).getWinners() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead04" + " SET " + 
					"WINNERS"+ "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayValue04" + " SET " + 
					apiMatch.getTeamwiseStat().getTeamB().get(0).getWinners() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomeValue05" + " SET " + 
					apiMatch.getTeamwiseStat().getTeamA().get(0).getUnforcedErrors() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead05" + " SET " + 
					"UNFORCED ERRORS"+ "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayValue05" + " SET " + 
					apiMatch.getTeamwiseStat().getTeamB().get(0).getUnforcedErrors() + "\0");
			
			List<Fixture> all_db_fixture;
			List<File> all_match_files;
			File this_file = null;
			Match this_match = null;
			
			all_match_files = Arrays.asList(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					String name = pathname.getName().toLowerCase();
					return name.endsWith(".json") && pathname.isFile();
				}
			}));
			all_db_fixture = tennisService.getFixtures();
			
			if(all_db_fixture != null) {
				Fixture curr_fixture = all_db_fixture.stream().filter(fix -> 
				fix.getMatchfilename().equalsIgnoreCase(match.getMatchFileName())).findAny().orElse(null);		
				if(curr_fixture != null) {
					int totalHomeScore = 0;
					int totalAwayScore = 0;
					pastHomeScore = 0;
					pastAwayScore = 0;
					for (Fixture fixture : all_db_fixture.stream().filter(fix -> fix.getMatchNumber()==curr_fixture.getMatchNumber()).collect(Collectors.toList())) {
						this_file = all_match_files.stream().filter(fil -> fil.getName().equalsIgnoreCase(fixture.getMatchfilename())).findAny().orElse(null);
						if(this_file != null) {
							homeScore = 0;
							awayScore = 0;
							
							if(!this_file.getName().equalsIgnoreCase(match.getMatchFileName())) {
								this_match = TennisFunctions.populateMatchVariables(tennisService, new ObjectMapper().readValue(
										new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY + this_file.getName()), Match.class));
								if(match.getHomeFirstPlayer().getTeamId()==this_match.getHomeFirstPlayer().getTeamId()
										|| match.getHomeFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
										|| match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
										|| match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId())
									{
										if(this_match.getSets() != null) {
											for (Set set : this_match.getSets()) {
												for (Game game : set.getGames()) {
													 homeScore = Integer.valueOf(game.getHome_score());
													 awayScore = Integer.valueOf(game.getAway_score());

													 pastHomeScore = pastHomeScore + homeScore;
													 pastAwayScore = pastAwayScore + awayScore;
												}
											}
										}
									}
							}else {
								if(match.getSets() != null) {
									homeScore = Integer.valueOf(match.getSets().get(0).getGames().get(0).getHome_score());
									awayScore = Integer.valueOf(match.getSets().get(0).getGames().get(0).getAway_score());
									totalHomeScore = (pastHomeScore + homeScore);
									totalAwayScore = (pastAwayScore + awayScore);
								}else {
									totalHomeScore = (pastHomeScore + homeScore);
									totalAwayScore = (pastAwayScore + awayScore);
								}
								
							}
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tScore" + " SET " + 
									totalHomeScore + "-" + totalAwayScore + "\0");
							
						}	
						}
					}
				}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.900 \0");
		}
	}

	public void populateFFPlayerProfile(PrintWriter print_writer, String viz_sence_path,int Player_id,List<Player> Player, List<Team> teams,List<Statistics> Profile,Match match,String selectedbroadcaster) {
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName" + " SET " + Player.get(Player_id-1).getFirstname() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName" + " SET " + Player.get(Player_id-1).getSurname() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerIamge" + " SET " + left_photo_path + 
				Player.get(Player_id-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
				teams.get(Player.get(Player_id-1).getTeamId()-1).getTeamBadge() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "www.tplsport.com" + "\0");
		
		if(Player.get(Player_id-1).getNationality() == null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry" + " SET " + "" + "\0");
		}else {
			//print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag" + " SET " + flag_path + Player.get(Player_id-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag" + " SET " + flag_path_viz + Player.get(Player_id-1).getNationality() + "\0");
		}
		
		
		for(Statistics PP : Profile) {
			if(PP.getPlayer_id() == Player_id) {
				if(PP.getBestResult1() != null && PP.getBestResult2() != null) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter1" + " SET " + PP.getBestResult1() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter2" + " SET " + PP.getBestResult2() + "\0");
				}else if(PP.getBestResult1() != null && PP.getBestResult2() == null){
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter1" + " SET " + PP.getBestResult1() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter2" + " SET " + "" + "\0");
				}else if(PP.getBestResult1() == null && PP.getBestResult2() != null){
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter1" + " SET " + PP.getBestResult2() + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter2" + " SET " + "" + "\0");
				}else{
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter2" + " SET " + "" + "\0");
				}
				
				if(PP.getAge() != null) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue01" + " SET " + PP.getAge() + "\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue01" + " SET " + "-" + "\0");
				}
				
				if(PP.getHeight() != null) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue02" + " SET " + PP.getHeight() + " cm" + "\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue02" + " SET " + "-" + "\0");
				}
				
				if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.SINGLES)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead03" + " SET " + "RANK - SINGLES" + "\0");
					if(Player.get(Player_id-1).getRankingSingle() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue03" + " SET " + Player.get(Player_id-1).getRankingSingle() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue03" + " SET " + "-" + "\0");
					}
				} else if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.DOUBLES)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead03" + " SET " + "RANK - DOUBLES" + "\0");
					if(Player.get(Player_id-1).getRankingDouble() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue03" + " SET " + Player.get(Player_id-1).getRankingDouble() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue03" + " SET " + "-" + "\0");
					}
				}
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead06" + " SET " + "WIN - LOSS" + "\0");
				if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.SINGLES)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead04" + " SET " + "BEST RANK - SINGLES" + "\0");
					if(PP.getBestRankingSingle()!=null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue04" + " SET " + PP.getBestRankingSingle() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue04" + " SET " + "-" + "\0");
					}
					
					
					if(PP.getWinLossSingles() != null ) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue06" + " SET " + PP.getWinLossSingles() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue06" + " SET " + "-" + "\0");
					}
					
				} else if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.DOUBLES)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead04" + " SET " + "BEST RANK - DOUBLES" + "\0");
					if(PP.getBestRankingSingle() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue04" + " SET " + PP.getBestRankingDouble() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue04" + " SET " + "-" + "\0");
					}
					
					if(PP.getWinLossDoubles()!= null ) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue06" + " SET " + PP.getWinLossDoubles() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue06" + " SET " + "-" + "\0");
					}
				}
			
				if(PP.getTurnedPro() != null) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue05" + " SET " + PP.getTurnedPro() + "\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue05" + " SET " + "-" + "\0");
				}
							
				
				
				if(PP.getBirthPlace() != null) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue07" + " SET " + PP.getBirthPlace() + "\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue07" + " SET " + "" + "\0");
				}		
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.120 \0");
	}

	@SuppressWarnings("unused")
	public void populatePointsProgress(PrintWriter print_writer, String viz_sence_path,TennisService tennisService,List<Result> result,List<Team> teams, Match match,String selectedbroadcaster) throws JAXBException, StreamReadException, DatabindException, IOException {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			homeWon = 0;
			awayWon = 0;
			int row =1;
			int home_id = 1;
			int away_id = 2;
			int homeScore = 0;
			int awayScore = 0;
			int total_value=0;
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + "STORY OF THE MATCH" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame1FirstName" + " SET " + match.getHomeFirstPlayer().getTeam().getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTame2FirstName" + " SET " + match.getAwayFirstPlayer().getTeam().getTeamName2() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
					match.getHomeFirstPlayer().getTeam().getTeamBadge() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo2" + " SET " + logo_path + 
					match.getAwayFirstPlayer().getTeam().getTeamBadge() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamShortName1" + " SET " + match.getHomeFirstPlayer().getTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTeamShortName2" + " SET " + match.getAwayFirstPlayer().getTeam().getTeamName4() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDiscipline1" + " SET " + "WOMEN'S SINGLES" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDiscipline2" + " SET " + "MEN'S SINGLES" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDiscipline3" + " SET " + "MIXED DOUBLES" + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDiscipline4" + " SET " + "MEN'S DOUBLES" + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHomeTeamName" + " SET " + match.getHomeFirstPlayer().getTeam().getTeamName1() + "\0");
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAwayTeamName" + " SET " + match.getAwayFirstPlayer().getTeam().getTeamName1() + "\0");
			
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "www.tplsport.com" + "\0");
			
			
			List<Fixture> all_db_fixture;
			List<File> all_match_files;
			File this_file = null;
			Match this_match = null;
			
			all_match_files = Arrays.asList(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					String name = pathname.getName().toLowerCase();
					return name.endsWith(".json") && pathname.isFile();
				}
			}));
			all_db_fixture = tennisService.getFixtures();
			
			if(all_db_fixture != null) {
				Fixture curr_fixture = all_db_fixture.stream().filter(fix -> 
				fix.getMatchfilename().equalsIgnoreCase(match.getMatchFileName())).findAny().orElse(null);						
				//Match this_match = new Match();
				if(curr_fixture != null) {
					int totalHomeScore = 0;
					int totalAwayScore = 0;
					int omo_num=0;
					pastHomeScore = 0;
					pastAwayScore = 0;
					for (Fixture fixture : all_db_fixture.stream().filter(fix -> fix.getMatchNumber()==curr_fixture.getMatchNumber()).collect(Collectors.toList())) {
						this_file = all_match_files.stream().filter(fil -> fil.getName().equalsIgnoreCase(fixture.getMatchfilename())).findAny().orElse(null);
						if(this_file != null) {
							homeScore = 0;
							awayScore = 0;
							
							if(!this_file.getName().equalsIgnoreCase(match.getMatchFileName())) {
								this_match = TennisFunctions.populateMatchVariables(tennisService, new ObjectMapper().readValue(
										new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY + this_file.getName()), Match.class));
								if(match.getHomeFirstPlayer().getTeamId()==this_match.getHomeFirstPlayer().getTeamId()
										|| match.getHomeFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
										|| match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
										|| match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId())
									{
										if(this_match.getSets() != null) {
											for (Set set : this_match.getSets()) {
												for (Game game : set.getGames()) {
													 homeScore = Integer.valueOf(game.getHome_score());
													 awayScore = Integer.valueOf(game.getAway_score());

													 pastHomeScore = pastHomeScore + homeScore;
													 pastAwayScore = pastAwayScore + awayScore;
												}
											}
										}
									}
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "0"+ home_id +  " SET " + homeScore + "\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "0"+ away_id +  " SET " + awayScore + "\0");
								row++;
								home_id = home_id + 2;
								away_id = away_id + 2;
							}else {
								System.out.println("EQUAL "+"THIS FILE NAME : "+this_file.getName()+"   MATCH FILE NAME "+match.getMatchFileName());
								if(match.getSets() != null) {
									homeScore = Integer.valueOf(match.getSets().get(0).getGames().get(0).getHome_score());
									awayScore = Integer.valueOf(match.getSets().get(0).getGames().get(0).getAway_score());
									totalHomeScore = (pastHomeScore + homeScore);
									totalAwayScore = (pastAwayScore + awayScore);
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "0"+ home_id +  " SET " + homeScore + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "0"+ away_id +  " SET " + awayScore + "\0");
									
									row++;
									home_id = home_id + 2;
									away_id = away_id + 2;
								}else {
									totalHomeScore = (pastHomeScore + homeScore);
									totalAwayScore = (pastAwayScore + awayScore);
								}
							}
							
							if((totalHomeScore+totalAwayScore) > 25 && (totalHomeScore+totalAwayScore) <= 50) {
								omo_num = 2;
								total_value = 25;
							}else if((totalHomeScore+totalAwayScore) > 50 && (totalHomeScore+totalAwayScore) <= 75) {
								omo_num = 3;
								total_value = 25;
							}else if((totalHomeScore+totalAwayScore) > 75 && (totalHomeScore+totalAwayScore) <= 100) {
								omo_num = 4;
								total_value = 25;
							}
							
							for(int i=1;i<=omo_num;i++) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBar" + i + ".max" + " SET " + total_value + "\0");
							}
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vNoOfGames" + " SET " + omo_num + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTotalScore" + " SET " + 
									totalHomeScore + " - " + totalAwayScore + "\0");
							
						}	
						}
					}
				}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.400 \0");
	}
	
	public void populateLtPlayerProfile(PrintWriter print_writer, String viz_sence_path,int Player_id,List<Player> Player, List<Team> teams,List<Statistics> Profile,Match match,String selectedbroadcaster) {
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName" + " SET " + Player.get(Player_id-1).getFirstname() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName" + " SET " + Player.get(Player_id-1).getSurname() + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgPlayerIamge" + " SET " + left_photo_path + 
				Player.get(Player_id-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTeamLogo1" + " SET " + logo_path + 
				teams.get(Player.get(Player_id-1).getTeamId()-1).getTeamBadge() + "\0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$HomeTeamGrp$LogoAll$HomeTeamLogoOverlay*TEXTURE*IMAGE SET " + logo_path +
				teams.get(Player.get(Player_id-1).getTeamId()-1).getTeamBadge() +" \0");
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$All$AwayTeamGrp$LogoAll$Away'TeamLogoOverlay*TEXTURE*IMAGE SET " + logo_path + 
				teams.get(Player.get(Player_id-1).getTeamId()-1).getTeamBadge() +" \0");
		
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tWebInfo" + " SET " + "www.tplsport.com" + "\0");
		
		if(Player.get(Player_id-1).getNationality() != null) {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue02" + " SET " + Player.get(Player_id-1).getNationality() + "\0");
		}else {
			print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue02" + " SET " + "" + "\0");
		}
		print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFooter" + " SET " + "" + "\0");
		
		
		for(Statistics PP : Profile) {
			if(PP.getPlayer_id() == Player_id) {
				
				
				if(PP.getAge() != null) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue01" + " SET " + PP.getAge() + "\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue01" + " SET " + "-" + "\0");
				}
				
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead03" + " SET " + "HEIGHT" + "\0");
				if(PP.getHeight() != null) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue03" + " SET " + PP.getHeight() + " cm" + "\0");
				}else {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue03" + " SET " + "-" + "\0");
				}
				
				if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.SINGLES)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead04" + " SET " + "RANK - SINGLES" + "\0");
					if(Player.get(Player_id-1).getRankingSingle() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue04" + " SET " + Player.get(Player_id-1).getRankingSingle() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue04" + " SET " + "-" + "\0");
					}
				} else if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.DOUBLES)) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHead04" + " SET " + "RANK - DOUBLES" + "\0");
					if(Player.get(Player_id-1).getRankingDouble() != null) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue04" + " SET " + Player.get(Player_id-1).getRankingDouble() + "\0");
					}else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatValue04" + " SET " + "-" + "\0");
					}
				}
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.120 \0");
	}
}
