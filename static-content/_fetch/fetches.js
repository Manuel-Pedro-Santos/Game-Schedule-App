import {API_BASE_URL} from "../html.js";


export function fetchGames() {
    return fetch(API_BASE_URL + "games/listGames")
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(() => {
            return [];
        });
}

export function fetchSessions(playerID) {
    return fetch(API_BASE_URL + "players/sessions/" + playerID)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(() => {
            return [];
        });
}

export function fetchGenres() {
    return fetch(API_BASE_URL + "games/genres")
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(() => {
            return [];
        });
}

export function fetchCheckSession(playerId, sessionId) {
    return fetch(API_BASE_URL + "sessions/checkSession", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("playerToken")
        },
        body: JSON.stringify({
            id: sessionStorage.getItem("playerId"),
            sessionId: sessionId
        })
    }).then(response => {
        // Verifique se a resposta foi bem-sucedida
        if (!response.ok) {
            throw new Error('Erro ao verificar a sessão');
        }
        // Extrair o ID do corpo da resposta JSON
        return response.json();

    }).then(data => {
            return data; // Retornar o ID
        }
    )
}

export async function fetchCreateSession(playerId, gameId, capacity, date) {
    const gameDetails = await fetchGameDetails(gameId);
    const gameName = gameDetails.gameName;
    return  fetch(API_BASE_URL + "sessions/createSession", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("playerToken")
        },
        body: JSON.stringify({
            playerID: sessionStorage.getItem("playerId"),
            capacity: capacity,
            gameName: gameName,
            date: date
        }),
    }).then(response => {
        const data = response.json();
        window.location.hash = `sessions/details/${data}`;
        return data;
    }).catch(() => {
        alert("Error creating session. Please fill all the fields correctly");
    });
}

async function fetchGameDetails(gameId) {
    return fetch(API_BASE_URL + "games/details/" + gameId)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(() => {
            alert("Error fetching game details.");
        });
}

export function fetchAddToSession(playerId, sessionId) {
    return fetch(API_BASE_URL + "sessions/addPlayer", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("playerToken")
        },
        body: JSON.stringify({
            id: sessionStorage.getItem("playerId"),
            sessionId: sessionId
        })
    }).then(response => {
        return response.json();
    }).then(data => {

        return data; // Retornar o ID
    }).catch(() => {
        alert("Error adding player to session or you are already in this session.");
    })
}

export function fetchDeleteSession(sessionId) {
    fetch(API_BASE_URL + "sessions/delete/" + sessionId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("playerToken")
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('Erro ao deletar a sessão');
        }
        window.location.hash = "sessions"
    }).catch(() => {
        alert("Error deleting session. Please fill all the fields correctly");
    })
}

export function removePlayerFromSession(playerId, sessionId) {
    return fetch(API_BASE_URL + "sessions/removePlayer", {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("playerToken")
        },
        body: JSON.stringify({
            id: sessionStorage.getItem("playerId"),
            sessionId: sessionId
        })
    }).then(response => {
        return response.json();
    }).catch(() => {
        alert("Error removing player from session.");
    })

}

export function fetchUpdateSession(sessionId, capacity, date, state) {
    let estado;
    let capacidade;
    let data;
    if (state === "") {
        estado = null
    } else {
        estado = state;
    }
    if (capacity === "") {
        capacidade = null;
    } else {
        capacidade = capacity;
    }

    if (date === "") {
        data = null;
    } else {
        data = date;
    }
    return fetch(API_BASE_URL + "sessions/update/" + sessionId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem("playerToken")
        },
        body: JSON.stringify({
            capacity: capacidade,
            date: data,
            state: estado
        })
    }).then(response => {
        return response.json();
    }).then(data => {
        window.location.hash = `sessions/details/${sessionId}`;
        console.log(data);
        return data; // Retornar o ID
    }).catch(() => {
        alert("Error updating session.");
    })
}

export function fetchPlayer(username, password) {
    return fetch(API_BASE_URL + "players/login/log?username=" + username + "&password=" + password)
        .then(res => {
            return res.json()
        }).then(player => {
                if (player === undefined) {
                    return;
                }
                sessionStorage.setItem("playerId", player.id);
                sessionStorage.setItem("playerToken", player.token);
                const playerDetailsLink1 = document.getElementById("navLink1");
                const playerDetailsLink2 = document.getElementById("navLink2");
                const playerDetailsLink7 = document.getElementById("navLink7");

                playerDetailsLink7.style.display = "flex";
                playerDetailsLink1.style.display = "none";
                playerDetailsLink2.style.display = "none";

                const playerDetailsLink = document.getElementById("navLink4");
                playerDetailsLink.href = `#players/${sessionStorage.getItem("playerId")}`;
                window.location.hash = `#players/${sessionStorage.getItem("playerId")}`
            }
        ).catch(() => {
            alert("Player not found. Try again or Sign Up.")
            window.location.hash = "#signup"
        })
}

export function fetchSignUpPlayer(name, username, email, password) {
    return fetch(API_BASE_URL + "players", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify({
            name: name,
            username: username,
            email: email,
            password: password
        })
    }).then(res => {
        return res.json()
    }).then(player => {
            sessionStorage.setItem("playerId", player.id)
            sessionStorage.setItem("playerToken", player.token)

            const playerDetailsLink1 = document.getElementById("navLink1");
            const playerDetailsLink2 = document.getElementById("navLink2");
            const playerDetailsLink7 = document.getElementById("navLink7");

            playerDetailsLink7.style.display = "flex";
            playerDetailsLink1.style.display = "none";
            playerDetailsLink2.style.display = "none";

            const playerDetailsLink = document.getElementById("navLink4");
            playerDetailsLink.href = `#players/${sessionStorage.getItem("playerId")}`;
            window.location.hash = `#players/${sessionStorage.getItem("playerId")}`
        }
    ).catch(() => {
        alert("Please fill everything correctly!")
        window.location.hash = "#signup"
    })
}

export function fetchCreateGame(gameName, gameDev, gnr) {
    fetch(API_BASE_URL + "games/createGame", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            gameName: gameName,
            gameDev: gameDev,
            gameGenre: gnr
        })
    }).then(res => {
        return res.json()
    }).then(game => {
        window.location.hash = `games/details/${game.gameId}`
    }).catch(() => {
        alert("Please fill everything correctly!")
        window.location.hash = "#games/createGame"
    })

}

