import router from '../router.js';
import playersHandler from '../handlers/playersHandler.js';
import handlers from "../handlers/handlers.js";
import {getPlayersViewDetails, getPlayersViewList, getPlayersViewHome} from "../views/playerViews.js";

/**
 * Test suite for playersHandler
 */
describe('Players Handler', function () {

    it('goes to home', function () {

        // Adding route handler for "home"
        router.addRouteHandler("home", handlers.getHome)

        // Getting the handler for "home"
        const handler = router.getRouteHandler("home")

        // Asserting that the handler's name is "getHome"
        handler.name.should.be.equal("getHome")
    })

    /**
     * Test case for finding getPlayersHome
     */
    it('goes to Player Home', function () {

        // Getting the handler for "getPlayersHome"
        const handler = playersHandler.getPlayersHome

        // Asserting that the handler's name is "getPlayersHome"
        handler.name.should.be.equal("getPlayersHome") // Ensure correct function name
    })

    it('goes to Player Details', function () {
        const handler = playersHandler.getPlayerDetails;
        handler.name.should.be.equal('getPlayerDetails');

    })

    it('goes to List of Players', function () {
        const handler = playersHandler.getListPlayers;
        handler.name.should.be.equal('getListPlayers');

    })

    it('goes to create Player', function () {
        const handler = playersHandler.createPlayerHandler;
        handler.name.should.be.equal('createPlayerHandler');

    })

    /**
     * Test case for building HTML for player home view
     */
    it('should build HTML for player home view', function () {
        const view = getPlayersViewHome();

        // Asserting that the UL element is created
        if (view.tagName !== 'UL') {
            throw new Error('Player home view should be a UL element');
        }

        // Asserting that the number of list items is 3
        if (view.querySelectorAll('li').length !== 3) {
            throw new Error('Player home view should have 3 list items');
        }

        // Asserting that the first list item contains "Players : "
        const firstListItemText = view.querySelector('li:nth-child(1)').textContent;
        if (!firstListItemText.includes('Players : ')) {
            throw new Error('Player home view first list item should contain "Players : "');
        }

        // Asserting that the second list item contains "Players details"
        const secondListItemText = view.querySelector('li:nth-child(2)').textContent;
        if (!secondListItemText.includes('Players details')) {
            throw new Error('Player home view second list item should contain "Players details"');
        }

    });

    /**
     * Test case for building HTML for player details view
     */
    it('should build HTML for player details view', function () {
        const playerDetails = {
            id: 123,
            name: "Goncalo Pinto",
            username: "goncalopinto",
            email: "goncalopinto@example.com"
        };
        const mockSessions = [];
        const view = getPlayersViewDetails(playerDetails, mockSessions);

        // Asserting that the UL element is created
        if (view.tagName !== 'UL') {
            throw new Error('Player details view should be a UL element');
        }

        // Asserting that the first list item contains "Name: John Doe"
        const firstListItemText = view.querySelector('li:nth-child(1)').textContent;
        if (!firstListItemText.includes('Name: Goncalo Pinto')) {
            throw new Error('Player details view first list item should contain "Name: Goncalo Pinto"');
        }

        // Asserting that the second list item contains "Username: johndoe"
        const secondListItemText = view.querySelector('li:nth-child(2)').textContent;
        if (!secondListItemText.includes('Username: goncalopinto')) {
            throw new Error('Player details view second list item should contain "Username: @goncalopinto"');
        }

        // Asserting that the third list item contains "Email : johndoe@example.com"
        const thirdListItemText = view.querySelector('li:nth-child(3)').textContent;
        if (!thirdListItemText.includes('Email : goncalopinto@example.com')) {
            throw new Error('Player details view third list item should contain "Email : goncalopinto@example.com"');
        }
    });

    /**
     * Test case for building HTML for player home view
     */
    it('should build HTML for player home view', function () {
        const view = getPlayersViewHome();

        // Asserting that the UL element is created
        if (view.tagName !== 'UL') {
            throw new Error('Player home view should be a UL element');
        }

        // Asserting that the number of list items is 3
        if (view.querySelectorAll('li').length !== 3) {
            throw new Error('Player home view should have 3 list items');
        }

        // Asserting that the view contains a button with id "ListPlayers"
        const listPlayersButton = view.querySelector('button#ListPlayers');
        if (!listPlayersButton) {
            throw new Error('Player home view should contain a button with id "ListPlayers"');
        }
    });

    /**
     * Test case for building HTML for player list view
     */
    it('should build HTML for player list view', function () {
        const player = {
            id: 123,
            name: "Goncalo Pinto",
            username: "goncalopinto",
            email: "goncalopinto@example.com"
        };
        const view = getPlayersViewList(player);

        // Asserting that the UL element is created
        if (view.tagName !== 'UL') {
            throw new Error('Player list view should be a UL element');
        }

        // Asserting that the first list item contains "Player Id : 123"
        const firstListItemText = view.querySelector('li:nth-child(1)').textContent;
        if (!firstListItemText.includes('Player Id : 123')) {
            throw new Error('Player list view first list item should contain "Player Id : 123"');
        }

        // Asserting that the second list item contains "Name: John Doe"
        const secondListItemText = view.querySelector('li:nth-child(2)').textContent;
        if (!secondListItemText.includes('Name: Goncalo Pinto')) {
            throw new Error('Player list view second list item should contain "Name: Goncalo Pinto"');
        }

        // Asserting that the third list item contains "Username: @johndoe"
        const thirdListItemText = view.querySelector('li:nth-child(3)').textContent;
        if (!thirdListItemText.includes('Username: @goncalopinto')) {
            throw new Error('Player list view third list item should contain "Username: @goncalopinto"');
        }

        // Asserting that the fourth list item contains "Email : johndoe@example.com"
        const fourthListItemText = view.querySelector('li:nth-child(4)').textContent;
        if (!fourthListItemText.includes('Email : goncalopinto@example.com')) {
            throw new Error('Player list view fourth list item should contain "Email : goncalopinto@example.com"');
        }
    });

});