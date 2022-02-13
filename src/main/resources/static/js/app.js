$(document).ready(function () {
    $("#startGameId").click(function () {
        $.ajax({
            type: 'POST',
            url: '/api/v1/nba/init',
            data: JSON.stringify({
                'homeTeam': $("#nbaHomeSelectListId").find(":selected").val(),
                'awayTeam': $("#nbaAwaySelectListId").find(":selected").val()
            }),
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                var homePlayerBoxScores = data.homePlayers;
                updateBoxScoresTable($("#homeTeamBoxScoresId"), homePlayerBoxScores);
                updatePlayerCommandSelector($("#idHomeSelectorPlayerCommand"), homePlayerBoxScores);

                var awayPlayerBoxScores = data.awayPlayers;
                updateBoxScoresTable($("#awayTeamBoxScoresId"), awayPlayerBoxScores);
                updatePlayerCommandSelector($("#idAwaySelectorPlayerCommand"), awayPlayerBoxScores);

                updateGameClock(data.gameEvent);
                updateHomeTeamStats(data.homeTeam);
                updateAwayTeamStats(data.awayTeam);

                $("#idHomeCoachIdModalSettings").text(data.homeCoach.id);
                $("#idAwayCoachIdModalSettings").text(data.awayCoach.id);

                $("#awayTeamNameId").text(data.awayTeam.teamName);
                $("#homeTeamNameId").text(data.homeTeam.teamName);
            },
            error: function (data) {
                alert("error : " + data.error);
            }
        });
    });


    $("#idHomeCommandButton").click(function () {
        var selectedId = $("#idHomeSelectorPlayerCommand").find(":selected").val();
        var selectedName = $("#idHomeSelectorPlayerCommand").find(":selected").text();
        getAndUpdateUIPlayerInfoData(selectedId, selectedName);
    });

    $("#idAwayCommandButton").click(function () {
        var selectedId = $("#idAwaySelectorPlayerCommand").find(":selected").val();
        var selectedName = $("#idAwaySelectorPlayerCommand").find(":selected").text();
        getAndUpdateUIPlayerInfoData(selectedId, selectedName);
    });

    $("#idHomeCoachButton").click(function () {
        getAndUpdateUICoachInfoData($("#idHomeCoachIdModalSettings").text());
    });

    $("#idAwayCoachButton").click(function () {
        getAndUpdateUICoachInfoData($("#idAwayCoachIdModalSettings").text());
    });

    $("#idSavePlayerSettingBtn").click(function () {
        var idCardPlayer = $("#idCardPlayer").text();
        var shootingFocusTendency = $("#idShootingFocusTendency").find(":selected").text();
        var gameFocusTendency = $("#idGameFocusTendency").find(":selected").text();
        var supportFocusTendency = $("#idSupportFocusTendency").find(":selected").text();
        var offensiveFocusTendency = $("#idOffensiveFocusTendency").find(":selected").text();
        var start = $('#idOnCortParameter').is(":checked");
        $.ajax({
            type: 'PUT',
            url: '/api/v1/nba/savePlayerSettings',
            data: JSON.stringify({
                'id': idCardPlayer,
                'insideOutside': shootingFocusTendency,
                'offenseDefense': gameFocusTendency,
                'reboundAssist': supportFocusTendency,
                'screenOpening': offensiveFocusTendency,
                'inStart': start
            }),
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                var homePlayerBoxScores = data.homePlayers;
                updateBoxScoresTable($("#homeTeamBoxScoresId"), homePlayerBoxScores);

                var awayPlayerBoxScores = data.awayPlayers;
                updateBoxScoresTable($("#awayTeamBoxScoresId"), awayPlayerBoxScores);

                $('#idPlayerCardModalSettings').modal('hide');
            },
            error: function (data) {
                alert("error : " + data.error);
            }
        });
    });

    $("#idSaveCoachSettingBtn").click(function () {
        var idCardCoach = $("#idCardCoach").text();
        var coachShootingFocusTendency = $("#idCoachShootingFocusTendency").find(":selected").text();
        var coachGameFocusTendency = $("#idCoachGameFocusTendency").find(":selected").text();
        $.ajax({
            type: 'PUT',
            url: '/api/v1/nba/saveCoachSettings',
            data: JSON.stringify({
                'id': idCardCoach,
                'insideOutside': coachShootingFocusTendency,
                'offenseDefense': coachGameFocusTendency
            }),
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                $('#idCoachModalSettings').modal('hide');
            },
            error: function (data) {
                alert("error : " + data.error);
            }
        });
    });

    $("#idExecuteTurn").click(function () {
        $.ajax({
            type: 'POST',
            url: '/api/v1/nba/execute',
            contentType: 'application/json',
            success: function (data) {
                if (data == null) {
                    alert('The game is OVER!');
                } else {
                    var homePlayerBoxScores = data.homePlayers;
                    updateBoxScoresTable($("#homeTeamBoxScoresId"), homePlayerBoxScores);
                    updatePlayerCommandSelector($("#idHomeSelectorPlayerCommand"), homePlayerBoxScores);

                    var awayPlayerBoxScores = data.awayPlayers;
                    updateBoxScoresTable($("#awayTeamBoxScoresId"), awayPlayerBoxScores);
                    updatePlayerCommandSelector($("#idAwaySelectorPlayerCommand"), awayPlayerBoxScores);

                    updateGameClock(data.gameEvent);
                    updateHomeTeamStats(data.homeTeam);
                    updateAwayTeamStats(data.awayTeam);
                }
            },
            error: function (data) {
                alert("error : " + data.error);
            }
        });
    });

    $("#idTimeOutButton").click(function () {
        var idCardCoach = $("#idCardCoach").text();
        $.ajax({
            type: 'POST',
            url: '/api/v1/nba/timeout',
            data: JSON.stringify({
                'id': idCardCoach
            }),
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                if (data == null) {
                    alert('The game is OVER!');
                } else {
                    var homePlayerBoxScores = data.homePlayers;
                    updateBoxScoresTable($("#homeTeamBoxScoresId"), homePlayerBoxScores);
                    updatePlayerCommandSelector($("#idHomeSelectorPlayerCommand"), homePlayerBoxScores);

                    var awayPlayerBoxScores = data.awayPlayers;
                    updateBoxScoresTable($("#awayTeamBoxScoresId"), awayPlayerBoxScores);
                    updatePlayerCommandSelector($("#idAwaySelectorPlayerCommand"), awayPlayerBoxScores);

                    updateHomeTeamStats(data.homeTeam);
                    updateAwayTeamStats(data.awayTeam);
                }
            },
            error: function (data) {
                alert("error : " + data.error);
            }
        });
    });

});

function updateBoxScoresTable(element, data) {
    element.bootstrapTable("destroy");
    element.bootstrapTable({
        data: data
    });
}

function updatePlayerCommandSelector(jElement, data) {
    jElement.empty();
    $.each(data, function (index, element) {
        var option = $('<option>').attr('value', element.id).text(element.name + ' (' + element.currentGamePosition + ')');
        jElement.append(option);
    });
}

function updateGameClock(clock) {
    $("#idNbaClockQuarter").text(clock.quarter);
    $("#idNbaClockTimer").text(clock.time);
    $("#idNbaOnBall").text(clock.onBall);
    $("#idNbaLastEvent").text(clock.lastEvent);
}

function updateHomeTeamStats(homeTeam) {
    $("#idBoxScoreHomeTeamName").text(homeTeam.teamName);
    $("#idBoxScoreHomePTS").text(homeTeam._PTS);
    $("#idBoxScoreHomeFG").text(homeTeam._FG);
    $("#idBoxScoreHome3Pt").text(homeTeam._3P);
    $("#idBoxScoreHomeFT").text(homeTeam._FT);
    $("#idBoxScoreHomeTR").text(homeTeam._REB);
    $("#idBoxScoreHomeOR").text(homeTeam._OFFR);
    $("#idBoxScoreHomeAst").text(homeTeam._AST);
    $("#idBoxScoreHomeStl").text(homeTeam._ST);
    $("#idBoxScoreHomeBlk").text(homeTeam._BS);
    $("#idBoxScoreHomeTo").text(homeTeam._TO);
    $("#idBoxScoreHomePf").text(homeTeam._PF);
    $("#idBoxScoreHomeTimeOuts").text(homeTeam.timeouts);
}

function updateAwayTeamStats(awayTeam) {
    $("#idBoxScoreAwayTeamName").text(awayTeam.teamName);
    $("#idBoxScoreAwayPTS").text(awayTeam._PTS);
    $("#idBoxScoreAwayFG").text(awayTeam._FG);
    $("#idBoxScoreAway3Pt").text(awayTeam._3P);
    $("#idBoxScoreAwayFT").text(awayTeam._FT);
    $("#idBoxScoreAwayTR").text(awayTeam._REB);
    $("#idBoxScoreAwayOR").text(awayTeam._OFFR);
    $("#idBoxScoreAwayAst").text(awayTeam._AST);
    $("#idBoxScoreAwayStl").text(awayTeam._ST);
    $("#idBoxScoreAwayBlk").text(awayTeam._BS);
    $("#idBoxScoreAwayTo").text(awayTeam._TO);
    $("#idBoxScoreAwayPf").text(awayTeam._PF);
    $("#idBoxScoreAwayTimeOuts").text(awayTeam.timeouts);
}

function getAndUpdateUIPlayerInfoData(selectedId, selectedName) {
    $.ajax({
        type: 'GET',
        url: '/api/v1/nba/playerInfo',
        data: {
            'id': selectedId
        },
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            $("#idCardPlayer").text(selectedId);
            $("#idCardPlayerName").text(selectedName);
            $('#idShootingFocusTendency').val(data.insideOutside);
            $('#idGameFocusTendency').val(data.offenseDefense);
            $('#idSupportFocusTendency').val(data.reboundAssist);
            $('#idOffensiveFocusTendency').val(data.screenOpening);
            $('#idOnCortParameter').prop('checked', data.inStart);
        },
        error: function (data) {
            alert("error : " + data.error);
        }
    });
}

function getAndUpdateUICoachInfoData(selectedId) {
    $.ajax({
        type: 'GET',
        url: '/api/v1/nba/coachInfo',
        data: {
            'id': selectedId
        },
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            $("#idCardCoach").text(selectedId);
            $("#idCardCoachName").text(data.name);
            $('#idCoachShootingFocusTendency').val(data.insideOutside);
            $('#idCoachGameFocusTendency').val(data.offenseDefense);
        },
        error: function (data) {
            alert("error : " + data.error);
        }
    });
}

function rowStyle(row, index) { // function for the table prop
    if (row.inStart) {
        return {
            classes: 'table-success'
        }
    } else {
        return {
            classes: 'table-light'
        }
    }
}


/**
 *
 *
 * var trr = JSON.parse(JSON.stringify(homePlayerBoxScores));
 *
 *  success: function (data) {
 *                 var flattenedData = jQuery.map( data.homePlayers, function(d){ return flattenJson(d) });
 *                 $("#homeTeamBoxScoresId").bootstrapTable({
 *                     data: flattenedData
 *                 });
 *                 $("#testSelectId").empty();
 *                 $.each(data.homePlayers, function(index, element) {
 *                     var option = $('<option>').attr('value', element.id).text(element.playerGamePlan.insideOutside);
 *                     $('#testSelectId').append(option);
 *                 });
 *
 *
 *                 function flattenJson(data) {
 *         var result = {};
 *         function recurse (cur, prop) {
 *             if (Object(cur) !== cur) {
 *                 result[prop] = cur;
 *             } else if (Array.isArray(cur)) {
 *                 for(var i=0, l=cur.length; i<l; i++)
 *                     recurse(cur[i], prop + "[" + i + "]");
 *                 if (l == 0)
 *                     result[prop] = [];
 *             } else {
 *                 var isEmpty = true;
 *                 for (var p in cur) {
 *                     isEmpty = false;
 *                     recurse(cur[p], prop ? prop+"."+p : p);
 *                 }
 *                 if (isEmpty && prop)
 *                     result[prop] = {};
 *             }
 *         }
 *         recurse(data, "");
 *         return result;
 *     }
 *
 */
