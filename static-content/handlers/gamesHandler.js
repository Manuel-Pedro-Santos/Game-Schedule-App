import gameViews from "../views/gameViews.js";
import {fetchGenres} from "../_fetch/fetches.js";
import {API_BASE_URL} from "../html.js";

function getGamesHome(mainContent) {
    if (!sessionStorage.getItem("playerId") && !window.location.pathname.includes('http-tests/mochaTests.html')) {
        alert("You must be logged in to access this page")
        window.location.hash = "#login"
        return
    }
    fetchGenres().then(data => {
        const ulStd = gameViews.getGameViewHome(data)
        mainContent.replaceChildren(ulStd)
    })
}

function getGamesDetails(mainContent, params) {
    const gId = params['gameId']
    fetch(API_BASE_URL + "games/details/" + gId)
        .then(res => res.json())
        .then(details => {
            const ulStd = gameViews.getGameViewDetails(details)
            mainContent.replaceChildren(ulStd)
        }).catch(() => {
            alert("Game not found")
            window.location.hash = "games"
        }
    )
}

function getGamesList(mainContent, params) {
    const name = params['gameName']
    const dev = params['dev']
    const gnr = params['genre']
    const skip = params['skip']
    const limit = params['limit']
    if (!dev && !gnr && !name) {
        alert("Invalid operation. No developer,genre or name selected. Can't list games.")
        return
    }
    fetch(API_BASE_URL + "games/list?dev=" + dev + "&gameName=" + name + "&genre=" + gnr + "&limit=" + limit + "&skip=" + skip)
        .then(res => res.json())
        .then(games => {
            mainContent.innerHTML = '';

            games.forEach(
                game => {
                    const ulStd = gameViews.getGameViewList(game)
                    mainContent.appendChild(ulStd)
                }
            )
            const buttons = gameViews.getGameViewButtons(skip, limit, gnr, dev, games.length, name)
            mainContent.appendChild(buttons)
        }).catch(() => {
        alert("Error fetching games")
    })
}

function createGameHandler(mainContent) {
    fetchGenres().then(data => {
        const ulStd = gameViews.createGameView(data)
        mainContent.replaceChildren(ulStd)
    }).catch(
        () => {
            alert("Error fetching genres")
        }
    )

}

const gamesHandler = {
    getGamesHome,
    getGamesDetails,
    getGamesList,
    createGameHandler
}

export default gamesHandler



