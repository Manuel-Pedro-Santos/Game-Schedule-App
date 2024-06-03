import {button, checkbox, inputElement, li, loadImage, div, txtDetails, ul} from "../html.js";
import {fetchCreateGame} from "../_fetch/fetches.js";


function homeGamesView(data) {
    return ul(
        li(
            inputElement("text", "GAME NAME", "gameName"),
            inputElement("text", "DEVELOPER ", "gameDev"),
            txtDetails("CHOOSE YOUR GENRES:"), checkbox(data, "gameGenre"),
            inputElement("button", "Search Games!", null, button(function () {
                    const name = document.getElementById("gameName").value;
                    const dev = document.getElementById("gameDev").value;
                    const form = document.getElementById("gameGenre");
                    const checkboxes = form.querySelectorAll('input[type=checkbox]');
                    const selectedValues = Array.from(checkboxes).filter(checkbox => checkbox.checked).map(checkbox => checkbox.value);
                    const gnr = selectedValues.join(",");
                    window.location.replace(
                        `#games/list?genre=${gnr}&gameName=${name}&dev=${dev}&limit=10&skip=0`
                    );

                })
            ),
            inputElement("button", "Create a Game here!", null, button(function () {
                window.location.replace("#games/createGame")
            })),
            inputElement("button", "List All Games!", null, button(function () {
                const allGenres = data.map(g => g).join(",");
                window.location.replace(`#games/list?genre=${allGenres}&gameName=&dev=&limit=10&skip=0`)
            }
        ))
        )
    )
}


function detailsGamesView(game) {
    return ul(
        li(
            div(
                loadImage(getGameImageByName(game.gameName), "gameImage", 250, 250),
                txtDetails("Game Id : ", game.gameId),
                txtDetails("Game Name : ", game.gameName),
                txtDetails("Game Developer : ", game.gameDev),
                txtDetails("Game Genres : ", game.gameGenre),
                inputElement("button", `Session of  ${game.gameName}`, null, button(function () {
                    window.location.replace(`#sessions/listSessions/${game.gameId}?date=&state=&nickname=&skip=0&limit=10`)
                }))
            )
        )
    )
}


function listGamesView(game) {
    return ul(
        li(div(
                loadImage(getGameImageByName(game.gameName), "gameImage", 250, 250),
                txtDetails("Game Name : ", game.gameName),
                inputElement("button", `Game Details of ${game.gameName}`, null, button(function () {
                    window.location.replace(`#games/details/${game.gameId}`)
                }))
            )
        )
    )
}


function createGameView(data) {
    return ul(
        li(inputElement("text", "New Game Name?", "gameName")),
        li(inputElement("text", "Who is the Developer?", "gameDev")),
        li(txtDetails("CHOOSE THE GENRES OF YOUR NEW GAME:"), checkbox(data, "gameGenre"),
            inputElement("button", "Create Game", null, button(function () {
                    const name = document.getElementById("gameName").value;
                    const dev = document.getElementById("gameDev").value;
                    const form = document.getElementById("gameGenre");
                    const checkboxes = form.querySelectorAll('input[type=checkbox]');
                    const selectedValues = Array.from(checkboxes).filter(checkbox => checkbox.checked).map(checkbox => checkbox.value);
                    const gnr = selectedValues.join(",");
                    fetchCreateGame(name, dev, gnr)
                })
            )
        ))

}


function listGamesButtonsView(skip, limit, gnr, dev, size, gName) {
    return ul(
        li(inputElement("button", "Previous", null, button(function () {
                const newSkip = parseInt(skip) - parseInt(limit);
                if (newSkip < 0) {
                    alert("Can't go back. Already at the beginning of the list.")
                    return
                }
                window.location.replace(`#games/list?gameName=${gName}&genre=${gnr}&dev=${dev}&limit=${limit}&skip=${newSkip}`)

            })),
            inputElement("button", "Next", null, button(function () {
                const newSkip = parseInt(skip) + parseInt(limit);
                if (newSkip > size) {
                    alert("Can't go forward. Already at the end of the list.")
                    return
                }
                window.location.replace(`#games/list?gameName=${gName}&genre=${gnr}&dev=${dev}&limit=${limit}&skip=${newSkip}`)

            })))
    );
}

const gamesViews = {
    getGameViewDetails: detailsGamesView,
    getGameViewList: listGamesView,
    getGameViewHome: homeGamesView,
    getGameViewButtons: listGamesButtonsView,
    createGameView

}

export default gamesViews;


export function getGameImageByName(gameName) {
    const formattedGameName = gameName.replace(/ /g, '_');

    return `../gamesImages/${formattedGameName}.png`
}




