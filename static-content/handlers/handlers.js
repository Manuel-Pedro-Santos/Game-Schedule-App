function getHome(mainContent) {
    const ulStd = homeView();
    mainContent.replaceChildren(ulStd);
}

function homeView() {
    sessionStorage.setItem("playerIdhi", "");
    sessionStorage.setItem("playerToken", "");

    const container = document.createElement("div");
    container.style.backgroundColor = "rgba(250,250,250,0.7)"; // Set a dark background with 50% opacity
    container.style.padding = "20px"; // Add some padding inside the container
    container.style.borderRadius = "10px"; // Optional: round the corners of the background
    container.style.maxWidth = "800px"; // Optional: limit the max width
    container.style.margin = "0 auto"; // Center the container

    const h1 = document.createElement("h1");
    h1.style.color = "#2545af"; // Set the color of the text to white
    h1.style.fontSize = "2.3em"; // Increase the font size for the title
    h1.style.fontFamily = "'OCR A Std', sans-serif"; // Set the font family
    h1.style.textAlign = "center"; // Center the title
    h1.style.marginBottom = "20px"; // Add some space below the title
    const text = document.createTextNode("Welcome to Group 5 Game Scheduling App!");

    const p = document.createElement("p");
    p.style.color = "#2545af"; // Set the color of the text to white
    p.style.fontSize = "1.5em"; // Set the font size for the paragraph
    p.style.fontFamily = "'OCR A Std', sans-serif"; // Set the font family
    p.style.textAlign = "justify"; // Justify the text
    p.style.marginTop = "20px"; // Add some space above the paragraph
    const introductionText = document.createTextNode(
        "This is a simple app to schedule game sessions. " +
        "You can see the list of games, the list of players, and the list of sessions. You can also see the details of each game, player, and session. " +
        "You can also see the list of sessions of a game. You can also see the list of players of a session."
    );
    p.appendChild(introductionText);

    h1.appendChild(text);
    container.appendChild(h1);
    container.appendChild(p);

    return container;
}

export const handlers = {
    getHome
};
export default handlers;
