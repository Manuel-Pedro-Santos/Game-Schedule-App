import {removePlayerFromSession} from "./_fetch/fetches.js";
import {loginCss, signUpCss} from "./css/playerCss.js";


export const API_BASE_URL = "https://service-ls-2324-2-42d-g05.onrender.com/"
//export const API_BASE_URL = "http://localhost:9001/"
const windowWidth = 800;

export const ul = (...args) => {
    const elem = document.createElement("ul");
    elem.className = "list-group";
    elem.style.margin = "0 auto";
    elem.style.width = `${windowWidth}px`;
    elem.style.padding = "0";
    args.forEach(child => elem.appendChild(child));
    return elem;
}

export const li = (...args) => {
    const elemLi = document.createElement("li");
    elemLi.className = "list-group-item";
    elemLi.style.padding = "10px";
    elemLi.style.width = `${windowWidth}px`;
    elemLi.style.listStyle = "none";
    args.forEach(child => elemLi.appendChild(child));
    return elemLi;
}

export function div(image, ...args) {
    const elem = document.createElement("div");

    elem.style.display = "flex";
    elem.style.alignItems = "center";
    elem.style.width = `${windowWidth}px`;
    elem.style.padding = "10px";
    elem.style.boxSizing = "border-box";


    const textContainer = document.createElement("div");
    textContainer.style.flex = "1"
    textContainer.style.marginTop = "10px";
    textContainer.style.marginBottom = "10px";

    args.forEach(text => {
        const textElem = document.createElement("p");
        textContainer.appendChild(textElem).appendChild(text);
    });

    image.style.margin = "10px";

    elem.appendChild(textContainer);
    elem.appendChild(image);

    return elem;
}


export function login(buttonAction) {
    const formElem = document.createElement("form");
    const userElem = document.createElement("input");
    const passwordElem = document.createElement("input");

    userElem.type = "text";
    userElem.placeholder = "Username";
    userElem.id = "username";
    passwordElem.type = "password";
    passwordElem.placeholder = "Password";
    passwordElem.id = "password";


    const submitButton = document.createElement("input");
    submitButton.type = "submit";
    submitButton.value = "Login";

    submitButton.onclick = function (event) {
        event.preventDefault();

        if (!userElem.value || !passwordElem.value) {
            alert("Please fill all fields before logging in.");
            return;
        }
        buttonAction();
    };

    formElem.appendChild(userElem);
    formElem.appendChild(passwordElem);
    formElem.appendChild(submitButton);
    formElem.className = "login-form";

    // Add stylesheet link (optional)
    const style = document.createElement('style');
    style.textContent = loginCss();
    formElem.appendChild(style);

    return formElem;
}

export function signUp(buttonAction) {
    const formElem = document.createElement("form");
    const nameElem = document.createElement("input");
    const userElem = document.createElement("input");
    const emailElem = document.createElement("input");
    const passwordElem = document.createElement("input");

    nameElem.type = "text";
    nameElem.placeholder = "Name";
    nameElem.id = "name";
    userElem.type = "text";
    userElem.placeholder = "Username";
    userElem.id = "username";
    emailElem.type = "email";
    emailElem.placeholder = "Email";
    emailElem.id = "email";
    passwordElem.type = "password";
    passwordElem.placeholder = "Password";
    passwordElem.id = "password";


    const signUpButton = document.createElement("input");
    signUpButton.style.backgroundColor = "#000080";
    signUpButton.type = "button";
    signUpButton.value = "Sign Up";
    signUpButton.style.color = "white";


    signUpButton.onclick = function (event) {
        event.preventDefault();
        if (!nameElem.value || !userElem.value || !emailElem.value || !passwordElem.value) {
            alert("Please fill all fields before signing up.");
            return;
        }
        buttonAction();
    };

    formElem.appendChild(nameElem);
    formElem.appendChild(userElem);
    formElem.appendChild(emailElem);
    formElem.appendChild(passwordElem);
    formElem.appendChild(signUpButton);
    formElem.className = "signup-form";

    // Add stylesheet link (optional)
    const style = document.createElement('style');
    style.textContent = signUpCss();
    formElem.appendChild(style);

    return formElem;
}

export function txt(text) {
    const span = document.createElement("span");
    span.className = "text-secondary"; // Add Bootstrap class
    span.appendChild(document.createTextNode(text));
    return span;
}

export function txtDetails(title, text) {
    const span = document.createElement("span");
    span.className = "text-secondary"; // Add Bootstrap class

    // Create title with bold font
    const titleNode = document.createElement("strong");
    titleNode.textContent = title;
    titleNode.style.fontFamily = "'Arial', sans-serif"; // Set the font for the title
    span.appendChild(titleNode);

    // Create text with normal font
    if (text !== undefined) {
        const textNode = document.createTextNode(text);
        span.appendChild(textNode);
        span.style.fontFamily = "'Times New Roman', serif"; // Set the font for the text
    }


    return span;
}

export function txtArray(initialText, players) {
    const elemUl = document.createElement("ul");
    const elemLiInitial = document.createElement("li");
    elemLiInitial.textContent = initialText;
    elemUl.appendChild(elemLiInitial);

    players.forEach(player => {
        const elemLi = document.createElement("li");
        const elemA = a(player.name, "#players/" + player.id); // Assuming each player object has an 'id' property
        elemLi.appendChild(elemA);
        elemUl.appendChild(elemLi);
    });

    return elemUl;
}

export function inputElement(type, value, id, buttonAction) {
    const elemInput = document.createElement("input");
    elemInput.type = type;
    elemInput.placeholder = value;

    elemInput.value = "";
    if (id)
        elemInput.id = id;
    if (id === "login") {
        elemInput.className = "form-control Login mt-3";
        elemInput.style.borderRadius = "10px";
        elemInput.style.margin = "10px";
        elemInput.style.width = "20px";
        elemInput.style.height = "30px";
        elemInput.classList.add("login-input");
        return elemInput;
    }
    if (type === "button") {
        buttonAction.id = id;
        buttonAction.textContent = value;
        buttonAction.className = "btn btn-primary";
        buttonAction.style.marginRight = "10px";
        buttonAction.style.marginTop = "10px";

        buttonAction.style.width = "170px";
        return buttonAction

    }

    const container = document.createElement("div");

    container.className = "form-control Login mt-3"
    container.style = "display: flex;";
    container.style.flexDirection = "column";
    container.style.alignItems = "flex-start";
    container.append(elemInput);
    return container;
}

export function a(text, href) {
    const elemA = document.createElement("a");
    elemA.href = href;
    elemA.textContent = text; // Set the text content of the <a> element
    elemA.className = "text-primary"; // Add Bootstrap class
    return elemA; // Return the created <a> element
}


export function createInput(type, value, id) {
    const elemInput = document.createElement("input");
    elemInput.type = type;
    elemInput.value = value;
    elemInput.className = type === "button" ? "btn btn-primary" : "form-control";
    if (type === "button") {

        elemInput.onclick = function () {
            if (id === "GetDet") {
                const sessionId = document.getElementById("sessionId").value;
                window.location.hash = `sessions/details/${sessionId}`;
            } else if (id === "ListSessions") {
                const gameId = document.getElementById("opcoes").value;

                window.location.hash = `sessions/listSessions/${gameId}?skip=${0}&limit=${10}`
            } else if (id === "ListPlayers") {
                const skip = document.getElementById("skip").value
                const limit = document.getElementById("limit").value
                window.location.hash = `players/list?skip=${skip}&limit=${limit}`
            } else if (id === "SID") {
                const sessionId = document.getElementById("sessionId").value;
                window.location.hash = `sessions/details/${sessionId}`;
            } else if (id === "SessionsMain") {
                window.location.hash = "session";
            } else if (id === "GamesMain") {
                window.location.hash = "games";
            } else if (id === "playerId") {
                const playerId = document.getElementById("playerId").value;
                window.location.hash = `players/${playerId}`;
            }
        };
    }
    if (id) {
        elemInput.id = id;
    }
    elemInput.style.borderRadius = "10px";
    elemInput.style.margin = "10px";
    return elemInput;
}

export function button(onClick) {
    const elemButton = document.createElement("button");
    elemButton.onclick = onClick
    return elemButton;
}

export function checkbox(data, id) {
    const container = document.createElement('form');
    container.id = id; // Add the id to the form
    container.className = "form-check";
    container.style = `
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 2px; // Reduced gap from 5px to 2px
        marginRight: 10px;
    `; // Use CSS Grid for a two-column layout

    data.forEach(genre => {
        const row = document.createElement('div');
        row.style = "display: flex; align-items: center;"; // Align items in the center vertically
        const elemInput = document.createElement("input");
        elemInput.type = "checkbox";
        elemInput.value = genre;
        elemInput.id = genre;
        elemInput.style.marginRight = "10px"; // Add some space to the right of the checkbox
        const elemLabel = document.createElement("label");
        elemLabel.htmlFor = genre;
        elemLabel.textContent = genre;
        row.append(elemInput, elemLabel);
        container.appendChild(row);
    });
    return container;
}


export function dropdown(allgames) {
    const selectElement = document.createElement('select');
    selectElement.className = "form-select"; // Add Bootstrap class
    selectElement.setAttribute('id', 'opcoes');
    selectElement.setAttribute('name', 'opcoes');
    selectElement.style.width = "200px";

    // Obter um array de pares chave-valor do objeto
    const gamesArray = Object.entries(allgames);
    gamesArray.forEach(([gameId, game]) => {
        const optionElement = document.createElement('option');
        optionElement.setAttribute('value', gameId); // A chave do objeto é o valor do jogo
        optionElement.textContent = game; // Supondo que 'name' seja o nome do jogo
        selectElement.appendChild(optionElement);
    });

    // Adicionar ouvinte de evento 'change' ao elemento 'select'
    selectElement.addEventListener('change', function () {
        // O ID do jogo selecionado é o valor do elemento 'select'
        const selectedGameId = this.value;
        // Guardar o ID do jogo selecionado
        localStorage.setItem('selectedGameId', selectedGameId);
    });

    return selectElement;
}

export function loadImage(imageSrc, altText, width, height, text) {
    // Create a container div for the image
    const container = document.createElement('div');
    container.style.margin = "30px"; // Add margin around the container

    // Create the image element
    const imgElement = document.createElement('img');
    imgElement.src = imageSrc;
    imgElement.alt = altText;
    imgElement.width = width;
    imgElement.height = height;

    // Add CSS styles to the image
    imgElement.style.borderRadius = "10px"; // Rounded corners
    imgElement.style.boxShadow = "0 4px 8px 0 rgba(0, 0, 0, 0.2)"; // Shadow effect
    imgElement.style.transition = "0.3s"; // Transition effect for hover

    // Add hover effect to the image
    imgElement.onmouseover = function () {
        this.style.transform = "scale(1.1)"; // Enlarge the image
        this.style.boxShadow = "0 8px 16px 0 rgba(0, 0, 0, 0.2)"; // Increase shadow
    };
    imgElement.onmouseout = function () {
        this.style.transform = "scale(1)"; // Return to original size
        this.style.boxShadow = "0 4px 8px 0 rgba(0, 0, 0, 0.2)"; // Return to original shadow
    };

    // Append the image element to the container div
    container.appendChild(imgElement);
    if (text !== undefined) {
        const textElement = document.createTextNode(text);
        container.appendChild(textElement);
    }

    // Return the container div
    return container;
}

export function txtArraySessions(initialText, array) {
    const ulElement = document.createElement("ul");

    array.forEach(session => {
        const liElement = createLiElementForSession(session);
        ulElement.appendChild(liElement);
    });

    return ulElement;
}


function createLiElementForSession(session) {
    const liElement = document.createElement("li");
    liElement.className = "d-flex justify-content-between align-items-center"; // Add Bootstrap classes for horizontal layout

    // Create elements for session details
    const gameName = document.createElement("span");
    gameName.textContent = `Game: ${session.game.gameName}`;

    const sessionDate = document.createElement("span");
    sessionDate.textContent = dateRefactor(session.sessionDate);

    const state = document.createElement("span");
    state.textContent = `State: ${session.state}`;

    const joinButton = joinSessionDetails(session);
    joinButton.className = "btn btn-primary"; // Add Bootstrap class
    joinButton.style.margin = "5px"; // Add margin to the button
    // Add the leave button
    const leaveButton = createLeaveButton(session);
    leaveButton.className = "btn btn-primary"; // Add Bootstrap class
    leaveButton.style.margin = "5px"; // Add margin to the button

    // Append all elements to the list item
    liElement.append(gameName, sessionDate, state, joinButton, leaveButton);

    return liElement;
}

function createLeaveButton(session) {
    const leaveButton = document.createElement("button");
    leaveButton.textContent = "Leave Session";
    leaveButton.addEventListener('click', function () {
        const playerID = window.location.hash.split("/")[1];
        removePlayerFromSession(playerID, session.id).then(
            () => window.location.replace(`#sessions/details/${session.id}`)
        );
    });
    return leaveButton;
}

function joinSessionDetails(session) {
    const joinButton = document.createElement("button");
    joinButton.textContent = "View Details";
    joinButton.addEventListener('click', function () {
        window.location.replace(`#sessions/details/${session.id}`)
    });
    return joinButton;
}

export function dateRefactor(date) {
    let dateArray = date.split("T");
    let dateArray2 = dateArray[1].split(":");
    return "Date: " + dateArray[0] + "  " + "Hour: " + dateArray2[0] + ":" + dateArray2[1];
}


