import {fetchSessions} from "../_fetch/fetches.js"
import {
    getPlayersViewDetails,
    getPlayersViewList,
    getPlayersViewHome,
    createPlayerView,
    signUpPlayerView
} from "../views/playerViews.js";
import {API_BASE_URL} from "../html.js";


function getPlayersHome(mainContent) {
    const ulStd = getPlayersViewHome()
    return mainContent.replaceChildren(ulStd)


}

function getPlayerDetails(mainContent, params) {
    const playerId = params['id']
    if (isNaN(playerId)) {
        alert("Cannot access player details without a login. Please login first.")
        window.location.hash = "login"
        return
    }
    fetch(API_BASE_URL + "players/" + playerId)
        .then(res => res.json())
        .then(playerDetails => {
            fetchSessions(playerId)
                .then(sessions => {
                    const ulStd = getPlayersViewDetails(playerDetails, sessions)
                    mainContent.replaceChildren(ulStd)
                })

        }).catch(() => {
        alert("Player not found")
    })
}

function getListPlayers(mainContent) {
    const skip = document.getElementById("skip").value;
    const limit = document.getElementById("limit").value;
    if (isNaN(skip) || isNaN(limit) || skip === "" || limit === "") {
        alert("Skip and limit must be numbers")
        window.location.hash = "players"
        return
    }

    fetch(API_BASE_URL + "players/list" + "?skip=" + skip + "&limit=" + limit)
        .then(res => res.json())
        .then(playersList => {
            mainContent.innerHTML = "";
            playersList.map(player => {
                    const ulstd = getPlayersViewList(player)
                    mainContent.appendChild(ulstd)
                }
            )
        }).catch(
        () => {
            alert("Error fetching players")
            window.location.hash = "players"
        }
    )
}


function createPlayerHandler(mainContent) {
    const ulStd = createPlayerView()
    mainContent.replaceChildren(ulStd)
}

function signUpPlayerHandler(mainContent) {
    const ulStd = signUpPlayerView()
    mainContent.replaceChildren(ulStd)
}


function logoutHandler(mainContent) {
    mainContent.innerHTML = "";
    sessionStorage.setItem("playerId", "");
    sessionStorage.setItem("playerToken", "");
    const playerDetailsLink1 = document.getElementById("navLink1");
    const playerDetailsLink2 = document.getElementById("navLink2");
    const playerDetailsLink7 = document.getElementById("navLink7");

    playerDetailsLink7.style.display = "none";
    playerDetailsLink1.style.display = "flex";
    playerDetailsLink2.style.display = "flex";
    window.location.hash = "#home"

}


const playersHandler = {
    getPlayersHome,
    getPlayerDetails,
    getListPlayers,
    createPlayerHandler,
    signUpPlayerHandler,
    logoutHandler
}

export default playersHandler



