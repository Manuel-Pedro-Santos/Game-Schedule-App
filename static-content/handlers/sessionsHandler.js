import {fetchAddToSession, fetchCheckSession, fetchDeleteSession, fetchGames} from "../_fetch/fetches.js";
import {
    createSessionView,
    getSessionsList,
    getSessionsViewButtons,
    sessionDetailsView,
    updateSessionView,
    getSessionsHomeView,
    alterButton
} from "../views/sessionsView.js";
import {API_BASE_URL, dateRefactor} from "../html.js";


function getSessionsHome(mainContent) {
    if (!sessionStorage.getItem("playerId") && !window.location.pathname.includes('http-tests/mochaTests.html')) {
        alert("You must be logged in to access this page")
        window.location.hash = "#login"
        return
    }

    fetchGames().then(data => {
        const ulStd = getSessionsHomeView(data)
        mainContent.replaceChildren(ulStd);
    });
}


function getSessionDetails(mainContent) {
    const sessionId = window.location.hash.split("/")[2]
    if (sessionId === "") {
        alert("Session Id must be a number")
        window.location.hash = "sessions"
        return
    }
    fetch(API_BASE_URL + "sessions/details/" + sessionId)
        .then(res => res.json())
        .then(sessionsDetails => {
            if (sessionsDetails != null) {
                const ulstd = sessionDetailsView(sessionsDetails)
                mainContent.replaceChildren(ulstd)
            } else {
                alert("Session does not exist")
            }
        })
}

function createSessionHandler(mainContent) {
    const gamesData = fetchGames().then(data => {
        const ulStd = createSessionView(data)
        mainContent.replaceChildren(ulStd)

    })
    mainContent.replaceChildren(gamesData)
}


function getListSessions(mainContent, params) {
    const gid = params['gid']
    const skip = params['skip']
    const limit = params['limit']
    let date = params['date']
    let state = params['state'].toUpperCase()
    let nickname = params['nick']
    if (isNaN(skip) || isNaN(limit) || skip === "" || limit === "" || gid === "" || isNaN(gid)) {
        alert("Skip and limit must be numbers or invalid GameId");
        window.location.hash = "sessions";
        return;
    }
    fetch(API_BASE_URL + "sessions/listSessions/" + gid + "/?date=" + date + "&state=" + state + "&nick=" + nickname + "&skip=" + skip + "&limit=" + limit)
        .then(res => res.json())
        .then(sessionsList => {
            mainContent.innerHTML = "";
            if (sessionsList.length === 0) {
                alert("No sessions found");
                window.location.hash = "#session";
                return;
            }
            let index = 0;

            sessionsList.forEach(session => {
                const ulstd = getSessionsList(session.id, session.game.gameName, dateRefactor(session.sessionDate), session.game.gameId, index)
                mainContent.appendChild(ulstd)
                const observed = index
                index += 1
                fetchCheckSession(sessionStorage.getItem("playerId"), session.id).then(data => {
                    alterButton(data, observed)
                })
            })
            const buttons = getSessionsViewButtons(skip, limit, gid)
            mainContent.appendChild(buttons)
        })

}

function deleteSession() {
    const sessionId = window.location.hash.split("/")[2]
    fetchDeleteSession(sessionId)
    window.location.replace("#sessions")
}

function addPlayerToSession(mainContent, params) {
    const sessionId = params['sid']
    fetchAddToSession(sessionStorage.getItem("playerId"), sessionId).then(data => {
        window.location.hash = `sessions/details/${data.id}`
    })
}

export function updateSession(mainContent) {
    //  const sessionId = window.location.hash.split("/")[2]
    const ulStd = updateSessionView()
    mainContent.replaceChildren(ulStd)
}

const sessionsHandler = {
    getSessionsHome,
    getSessionDetails,
    getListSessions,
    createSessionHandler,
    addPlayerToSession,
    deleteSession,
    updateSession

}

export default sessionsHandler
