import {button, createInput, inputElement, li, login, signUp, txtArraySessions, ul, txtDetails} from "../html.js";
import {fetchPlayer, fetchSignUpPlayer} from "../_fetch/fetches.js";


export function getPlayersViewHome() {
    console.log(sessionStorage.getItem("playerId"))
    if (!sessionStorage.getItem("playerId")  && !window.location.pathname.includes('http-tests/mochaTests.html')) {
        alert("You must be logged in to access this page")
        window.location.hash = "#login"
        return
    }
    return ul(
        li(txtDetails("Players : ")),
        li(txtDetails("Players details:  "), createInput("text", "Enter player id", "playerId"), createInput("button", "Get Details", "playerId")),
        li(txtDetails("List Players  : "), createInput("text", "skip?", "skip"), createInput("text", "limit?", "limit"), createInput("button", "Get Players", "ListPlayers")),
        inputElement("button", "Get Players", "ListPlayers", button(function () {
            window.location.hash = "#players/getAllplayers/list"
        }))
    )
}

export function getPlayersViewDetails(playerDetails, sessions) {
    if (!sessionStorage.getItem("playerId")  && !window.location.pathname.includes('http-tests/mochaTests.html')) {
        alert("You must be logged in to access this page")
        window.location.hash = "#login"
        return
    }
    return ul(
        li(txtDetails("Name: ", playerDetails.name)),
        li(txtDetails("Username: ", playerDetails.username.toLocaleLowerCase())),
        li(txtDetails("Email : ", playerDetails.email)),
        li(txtDetails("Sessions that you are in :"), txtArraySessions("", sessions))
    )
}


export function getPlayersViewList(player) {
    return ul(
        li(txtDetails("Player Id : ", player.id)),
        li(txtDetails("Name: ", player.name)),
        li(txtDetails("Username: @", player.username.toLocaleLowerCase())),
        li(txtDetails("Email : ", player.email))
    )
}


export function createPlayerView() {
    return ul(
        login(function () {
            const username = document.getElementById("username").value;
            const password = document.getElementById("password").value;
            fetchPlayer(username, password)
        }),
    )
}

export function signUpPlayerView() {
    return ul(
        signUp(function () {
                const name = document.getElementById("name").value;
                const username = document.getElementById("username").value;
                const email = document.getElementById("email").value;
                const password = document.getElementById("password").value;
                fetchSignUpPlayer(name, username, email, password)

            }
        )
    )
}



