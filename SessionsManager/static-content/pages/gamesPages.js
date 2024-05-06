import {div, a, ul, li, label, input, button, form, p} from "../tags.js"
import {returnHomeButton} from "../components/returnHomeButton.js"
import {openModal, closeModal} from "../components/modal.js"
import {CONSTS} from "../utils.js"


function gamesSearchPageClick(genres, developer) {
    window.location.hash = "games/list?genres=" + genres + "&developer=" + developer
        + "&skip=" + CONSTS.SKIP_DEFAULT + "&limit=" + CONSTS.LIMIT_DEFAULT;
}

function gamesSearchByNameClick(name) {
    window.location.hash = "games/byName?gname=" + name
        + "&skip=" + CONSTS.SKIP_DEFAULT + "&limit=" + CONSTS.LIMIT_DEFAULT;
}

export function gamesSearchPage(createGame) { //this is the search page for games by genre(s) and developer
    const genresInput = input({type: "text", id: "genresInput", placeHolder: "Action, Adventure"});
    const developerInput = input({type: "text", id: "developerInput", placeHolder: "Ubisoft, EA"});
    const nameInput = input({type: "text", id: "nameInput", placeHolder: "FIFA 22"});

    const form = gameForm(createGame);

    const homeButton = returnHomeButton();

    const searchButton = button(
        {
            onClick: () => {
                gamesSearchPageClick(genresInput.value, developerInput.value);
            }
        },
        "Search"
    );

    const secondSearchButton = button(
        {
            onClick: () => {
                gamesSearchByNameClick(nameInput.value);
            }
        },
        "Search"
    );

    return div(
        {},
        button({
            onClick: () => {
                openModal(form)
            }
        }, "Create game"),
        p({}),
        div(
            {},
            "Search games by genre(s) and developer",
            div(
                {},
                label({}, "Genres:", genresInput),
                label({}, "Developer:", developerInput),
            ),
            searchButton,
        ),
        p({}),
        div(
            {},
            "Search games by name",
            div(
                {},
                label({}, "Name:", nameInput),
            ),
            secondSearchButton,
            homeButton
        )
    );

}

function gameForm(createGame) {
    const form = document.createElement('form');
        form.appendChild(label({}, "Game name:"));
        form.appendChild(input({ type: "text", id: "gameName", required: true }));
        form.appendChild(label({}, "Game developer:"));
        form.appendChild(input({ type: "text", id: "gameDeveloper", required: true }));
        form.appendChild(label({}, "Game genres:"));
        form.appendChild(input({ type: "text", id: "gameGenres", required: true }));
        form.appendChild(
            button({
                type: "submit",
                onClick: (event) => {
                    event.preventDefault();
                    createGame(
                        document.getElementById('gameName').value,
                        document.getElementById('gameDeveloper').value,
                        document.getElementById('gameGenres').value
                    );
                    closeModal();
                }
            }, "Create game")
        );

    return form;
}

export function gamesListPage(games, buttons) {
    const homeButton = returnHomeButton();

    return div(
        {},
        "Games",
        div({}, buttons),
        ...games.map(game =>
            div({}, a({href: "#games/" + game.id}, game.name))
        ),
        homeButton
    );
}


export function gameDetailsPage(game, createSession) { //this is the details page for a specific game
    const homeButton = returnHomeButton();
    const form = sessionForm(createSession, game.id);

    return div(
        {id: "gameDetails"},
        "Game Details",
        div(
            {},
            ul(
                {},
                li({}, "Name: " + game.name),
                li({}, "Developer: " + game.dev),
                li({}, "Genres: " + game.genres.join(", ")),
            ),
            button({
                onClick: () => {
                    openModal(form)
                }
            }, "Create a session with this game")
        ),
        button(
            {
                onClick: () => {
                    window.location.hash = "sessions/list?gid=" + game.id +
                        "&skip=" + CONSTS.SKIP_DEFAULT + "&limit=" + CONSTS.LIMIT_DEFAULT
                }
            }, "Search sessions with this game"
        ),
        homeButton
    );
}


function sessionForm(createSession, id) {
    const form = document.createElement('form');
        form.appendChild(label({}, "Session capacity:"));
        form.appendChild(input({type: "number", id: "sessionCapacity", min: 1, max: 100, required: true}));
        form.appendChild(label({}, "Session date:"));
        form.appendChild(input({type: "text", id: "sessionDate", required: true}));
        form.appendChild(
            button({
                type: "submit",
                onClick: (event) => {
                    event.preventDefault();
                    createSession(id, document.getElementById('sessionCapacity').value, document.getElementById('sessionDate').value);
                    closeModal();
                }
            }, "Create session")
        );
    return form;
}