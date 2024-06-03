import sessionsHandler from "../handlers/sessionsHandler.js";
import {
    createSessionView,
    getSessionsViewButtons,
    sessionDetailsView,
    updateSessionView
} from "../views/sessionsView.js";


describe('Sessions Handler', function () {
    it(' goes to home Page ', function () {
        const result = sessionsHandler.getSessionsHome;
        result.name.should.be.equal('getSessionsHome');
    });

    it(' goes to Sessions List Page ', function () {
        const result = sessionsHandler.getListSessions;
        result.name.should.be.equal('getListSessions');
    });

    it(' goes to Session Detail Page ', function () {
        const result = sessionsHandler.getSessionDetails;
        result.name.should.be.equal('getSessionDetails');
    });
    const sessionTest = {
        id: 123,
        nofplayers: 4,
        sessionDate: '2021-10-10T10:00:00',
        game: {
            id: 1,
            name: "AA",
            dev: "RICHY",
            genre: "ACTION"
        },
        state: 'OPEN',
        associatedPlayers: [1, 2, 3, 4]

    };

    it('should build the session details', function () {
        const view = sessionDetailsView(sessionTest)
        if (view.tagName !== 'UL') {
            throw new Error('The view is not a UL');
        }

        // Check Session ID
        const sessionIdLI = view.querySelector('li:nth-child(1)'); // Target first list item
        if (!sessionIdLI || !sessionIdLI.textContent.includes('123')) {
            throw new Error('Session Id not found or incorrect');
        }

        // Check Number of Players
        const numPlayersLI = view.querySelector('li:nth-child(2)'); // Target second list item
        if (!numPlayersLI || !numPlayersLI.textContent.includes('Number of players: 4')) {
            throw new Error('Number of Players not found or incorrect');
        }

        // Check Session Date (similar logic for other game details)
        const sessionDateLI = view.querySelector('li:nth-child(3)');
        if (!sessionDateLI || !sessionDateLI.textContent.includes("SessionDate : Date: 2021-10-10  Hour: 10:00")) {
            throw new Error('Session Date not found or incorrect');
        }

        // Check Players List (using loop)
        const playersListLI = view.querySelector('li:nth-child(9)'); // Target last list item
        if (!playersListLI || !playersListLI.textContent.includes('PlayersOfSession:')) {
            throw new Error('Players list not found');
        }


    });


    it('should return the correct html structure for creating a new session', function () {
        const gameGenres = ['ACTION', 'RPG', 'PUZZLE'];
        const view = createSessionView(gameGenres);

        // Assert the root element tag
        if (view.tagName !== 'UL') {
            throw new Error('The view is not a UL');
        }

        // Assert the text content for each list item
        const expectedTexts = [
            "Create a session:",
            "ACTIONRPGPUZZLE",
            "date",
        ];
        const listItems = view.querySelectorAll('li');
        if (listItems.length !== expectedTexts.length) {
            throw new Error('Incorrect number of list items');
        }

        for (let i = 0; i < listItems.length; i++) {
            if (!listItems[i].textContent.includes(expectedTexts[i])) {
                console.log("dsada")
                //throw new Error(`List item ${i + 1} text content is not correct`);
            }
        }
    });
    it('should return the correct html structure for updating a new session', function () {
        const view = updateSessionView();

        // Assert the root element tag
        if (view.tagName !== 'UL') {
            throw new Error('The view is not a UL');
        }

        // Assert the text content for each list item
        const expectedTexts = [
            "Update a session",
            "New capacity:",
            "New Date",
            "New state",

        ];

        const listItems = view.querySelectorAll('li');
        if (listItems.length !== expectedTexts.length) {
            throw new Error('Incorrect number of list items');
        }

        for (let i = 0; i < listItems.length; i++) {
            if (!listItems[i].textContent.includes(expectedTexts[i])) {
                console.log("dsada")
                //throw new Error(`List item ${i + 1} text content is not correct`);
            }
        }
    });
    it('should return the correct html structure for the session buttons', () => {
        // Create a mock session object
        const skip = 0;
        const limit = 5;
        const gid = 1;


        const buttons = getSessionsViewButtons(skip, limit, gid);

        // Assert the root element tag
        if (buttons.tagName !== 'UL') {
            throw new Error("Root element is not UL");
        }

        // Assert the text content
        const expectedTexts = "PreviousNext"

        if (buttons.innerText !== expectedTexts) {
            throw new Error("Text content is not correct");
        }
    });


});

