import {
    a,
    button,
    dateRefactor,
    dropdown,
    inputElement,
    li,
    loadImage,
    txt,
    txtArray,
    txtDetails,
    ul
} from "../html.js";
import {addSessionsDetailsCss} from "../css/sessionDetailsCSS.js";
import {fetchCreateSession, fetchUpdateSession} from "../_fetch/fetches.js";
import {getGameImageByName} from "./gameViews.js";

export function getSessionsHomeView(data) {
    return ul(
        li(txtDetails("Which session do you want to enter ?")),
        li(
            txtDetails("Choose a game:  "), dropdown(data),
            inputElement("datetime-local", "datetime-local", "datetime-local"),
            inputElement("text", "STATE", "state"),
            inputElement("text", "NICKNAME", "nickname"),
            inputElement("button", "List Sessions", "ListSessions", button(function () {
                const gid = document.getElementById("opcoes").value;
                const date = document.getElementById("datetime-local").value;
                const state = document.getElementById("state").value;
                const nickname = document.getElementById("nickname").value

                window.location.replace(`#sessions/listSessions/${gid}?date=${date}&state=${state}&nick=${nickname}&skip=0&limit=10`)
            })),
            inputElement("button", "Create a Session", "CreateASession", button(function () {
                window.location.replace("#sessions/createSession")
            }))
        )
    )
}

export function sessionDetailsView(sessionsDetails) {
    const ulElem = ul(
        li(txtDetails("Session Id : ", sessionsDetails.id)),
        li(txtDetails("Number of players: ", sessionsDetails.nofplayers)),
        li(txtDetails("SessionDate : ", dateRefactor(sessionsDetails.sessionDate))),
        li(txtDetails("State : ", sessionsDetails.state)),
        li(a(`${sessionsDetails.game.gameName} Details`, "#games/details/" + sessionsDetails.game.gameId)),
        li(txtDetails("Game name : ", sessionsDetails.game.gameName)),
        li(txtDetails("Game Developer : " , sessionsDetails.game.gameDev)),
        li(txtDetails("Game Genre : ", sessionsDetails.game.gameGenre)),
        li(txtArray("PlayersOfSession:", sessionsDetails.associatedPlayers))
    );

    addSessionsDetailsCss(ulElem, sessionsDetails);

    return ulElem;
}

export function getSessionsViewButtons(skip, limit, gid) {
    return ul(
        li(inputElement("button", "Previous", null, button(function () {
                const newSkip = parseInt(skip) - parseInt(limit);
                if (isNaN(newSkip) || newSkip < 0) {
                    alert("Invalid operation. Can't go to the previous page.");
                    window.location.replace(`#sessions/listSessions/${gid}?skip=${skip}&limit=${limit}`)
                } else {
                    window.location.replace(`#sessions/listSessions/${gid}?skip=${newSkip}&limit=${limit}`)
                }
            })),
            inputElement("button", "Next", null, button(function () {
                const newSkip = parseInt(skip) + parseInt(limit);
                if (isNaN(newSkip)) {
                    alert("Invalid operation. Can't go to the next page.");
                    window.location.replace(`#sessions/listSessions/${gid}?skip=${skip}&limit=${limit}`)
                } else {
                    window.location.replace(`#sessions/listSessions/${gid}?skip=${newSkip}&limit=${limit}`)
                }
            })))
    );

}

export function getSessionsList(sid, gameName, sessionDate, gameId, index) {
    if (sessionStorage.getItem("playerId")=== 0 && !window.location.pathname.includes('http-tests/mochaTests.html')) {
        alert("You must be logged in to access this page")
        return
    }
    return ul(
        li(
            txt("Session of:  "), a(gameName, `#games/details/${gameId}`),
            loadImage(getGameImageByName("hours"), "session", 50, 50,sessionDate) ,
            loadImage(getGameImageByName(gameName), "session", 200, 200),
                inputElement("button", ` Details of Session`, null, button(function () {
                    window.location.replace(`#sessions/details/${sid}`)
                })),

                inputElement("button", `Add me to this Session`, null, button(function () {
                    window.location.replace(`#sessions/addPlayerToSession/${sid}`)
                })),

                inputElement("button", `Delete Session`, index, button(function () {
                    window.location.replace(`#sessions/delete/${sid}`)
                })),

                inputElement("button", `Update Session`, null, button(function () {
                        window.location.replace(`#sessions/update/${sid}`)
                    }
                ))
            )
    )



}

export function createSessionView(data) {
    return ul(
        li(txtDetails("Create a session:"), inputElement("text", "capacity:", "capacity")),
        li(txtDetails("Choose a game:  "), dropdown(data)),
        li(txtDetails("select a date:"), inputElement("datetime-local", "date", "date"),
            inputElement("button", "Create Session", "CreateSession", button(function () {
                        const capacity = document.getElementById("capacity").value;
                        const gName = document.getElementById("opcoes").value;
                        const date = document.getElementById("date").value;
                        fetchCreateSession(sessionStorage.getItem("playerId"), gName, capacity, date)
                    }
                )
            )
        )
    )

}

export function updateSessionView() {
    return ul(
        li(txtDetails("Update a session:")),
        li(txtDetails(" New capacity:"), inputElement("text", "capacity", "capacity")),
        li(txtDetails("New Date"), inputElement("datetime-local", "datetime-local", "datetime-local")),
        li(txtDetails("New state"), inputElement("text", "state", "state")),
        inputElement("button", "Update Session", "UpdateSession", button(function () {
                    const sessionId = window.location.hash.split("/")[2];
                    const capacity = document.getElementById("capacity").value;
                    const date = document.getElementById("datetime-local").value;
                    const state = document.getElementById("state").value;

                    fetchUpdateSession(sessionId, capacity, date, state).then(r => {
                            window.location.replace(`#sessions/details/${r.id}`)
                        }
                    )
                }
            )
        ))
}

export function alterButton(data, id) {
    if (!data) {
        document.getElementById(id).style.display = "none";
    }
}



