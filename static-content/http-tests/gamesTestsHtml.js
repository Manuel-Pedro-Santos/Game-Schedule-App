import gamesViews from "../views/gameViews.js";
import gameViews from "../views/gameViews.js";

describe('Game views testing', function () {
    it('should return the correct html structure for the Home Page', function () {
        // Create a mock game object
        const genres = [
            "Action",
            "Adventure",
            "Arcade",
            "Fighting",
            "Racing",
            "RPG",
            "Shooter",
            "Simulation",
            "Sports",
            "Strategy",
            "Turn-Based"
        ]

        const gameViewHome = gameViews.getGameViewHome(genres);

        // Assert the root element tag
        if (gameViewHome.tagName !== 'UL') {
            throw new Error("Root element is not UL");
        }

        // Assert the text content
        const expectedTexts = "CHOOSE YOUR GENRES:ActionAdventureArcadeFightingRacingRPGShooterSimulationSportsStrategyTurn-BasedSearch Games!Create a Game here!List All Games!"
        if (gameViewHome.innerText !== expectedTexts) {
            throw new Error("Text content is not correct");
        }
    });
    it('should return the correct html structure for the Game Details', function () {
        // Create a mock game object
        const game = {
            gameId: "1",
            gameName: "Test Game",
            gameDev: "Test Developer",
            gameGenre: "Test Genre"
        };

        const gameViewDetails = gameViews.getGameViewDetails(game);

        // Assert the root element tag
        if (gameViewDetails.tagName !== 'UL') {
            throw new Error("Root element is not UL");
        }

        // Assert the text content
        const expectedTexts = [
            "Game Id : " + game.gameId,
            "Game Name : " + game.gameName,
            "Game Image",
            "Game Developer : " + game.gameDev,
            "Game Genres : " + game.gameGenre,
            `Session of ${game.gameName}`
        ];
        const liElements = Array.from(gameViewDetails.children);
        liElements.forEach((li, index) => {
            if (li.querySelector('img')) {
                return;
            }
            if (li.innerText !== expectedTexts[index]) {
                throw new Error(`Text content at index ${index} is not correct`);
            }
        });
    });


    // lisyings of games

    it('should return the correct html structure for the Listing of Games', function () {
        // Create a mock game object
        const game = {
            gameId: "1",
            gameName: "Test Game",
            gameDev: "Test Developer",
            gameGenre: "Test Genre"
        };

        const gameViewList = gameViews.getGameViewList(game);

        // Assert the root element tag
        if (gameViewList.tagName !== 'UL') {
            throw new Error("Root element is not UL");
        }

        // Assert the text content
        const expectedTexts = [
            "Game Name : " + game.gameName,
            "Game Details of Test Game",
            `Game Details of ${game.gameName}`
        ];

        const liElements = Array.from(gameViewList.children);
        liElements.forEach((li, index) => {
            // Skip the li element if it contains an image
            if (li.querySelector('img')) {
                return;
            }

            // Skip the li element if its innerText is an empty string
            if (li.innerText === "") {
                return;
            }

            if (li.innerText !== expectedTexts[index]) {
                throw new Error(`Text content at index ${index} is not correct`);
            }
        });
    });
    it('should return the correct html structure for the Creation of a Game', () => {
        // Create a mock game object
        const genres = [
            "Action",
            "Adventure",
            "Arcade",
            "Fighting",
            "Racing",
            "RPG",
            "Shooter",
            "Simulation",
            "Sports",
            "Strategy",
            "Turn-Based"
        ]

        const createGameView = gameViews.createGameView(genres);

        // Assert the root element tag
        if (createGameView.tagName !== 'UL') {
            throw new Error("Root element is not UL");
        }

        // Assert the text content
        const expectedTexts = "CHOOSE THE GENRES OF YOUR NEW GAME:ActionAdventureArcadeFightingRacingRPGShooterSimulationSportsStrategyTurn-BasedCreate Game";

        if (createGameView.innerText !== expectedTexts) {
            throw new Error("Text content is not correct");
        }
    });

    it('should return the correct html structure for the buttons', () => {
        // Create a mock game object
        const skip = 0;
        const limit = 5;
        const gnr = "Test Genre";
        const dev = "Test Developer";
        const gamesLength = 1;
        const name = "Test Game";

        const buttons = gameViews.getGameViewButtons(skip, limit, gnr, dev, gamesLength, name);

        // Assert the root element tag
        if (buttons.tagName !== 'UL') {
            throw new Error("Root element is not UL");
        }

        // Assert the text content
        const expectedTexts = "PreviousNext"

        if (buttons.innerText !== expectedTexts) {
            throw new Error("Text content is not correct");
        }
    })
});