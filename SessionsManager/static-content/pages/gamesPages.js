import {div, a, ul, li, label, input, button, form, p, h2} from "../tags.js"
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

export function gamesSearchPage(createGame) {
    const genresInput = input({type: "text", id: "genresInput", placeHolder: "Action, Adventure"});
    const developerInput = input({type: "text", id: "developerInput", placeHolder: "Ubisoft, EA"});
    const nameInput = input({type: "text", id: "nameInput", placeHolder: "FIFA 22"});

    const form = gameForm(createGame);

    const createGameButton = button(
       {
           class: 'create-game-button',
           onClick: () => {
               openModal(form);
           }
       }, "Create game"
   );

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

   const homeButton = returnHomeButton();

   return div(
       { class: 'game-search-page' },
       p({}, "Search for games by entering genre(s) and developer or search by game name."),
       p({}, "You can also create a new game."),
       createGameButton,
       div(
           { class: 'search-section' },
           div(
               {},
               label({}, "Genres:"),
               genresInput,
               label({}, "Developer:"),
               developerInput,
               searchButton
           )
       ),
       div(
           { class: 'search-section' },
           div(
               {},
               label({}, "Name:"),
               nameInput,
               secondSearchButton
           )
       ),
       div({}, homeButton)
   );
}


function gameForm(createGame) {
    const form = document.createElement('form');
    form.className = 'form-container';
    form.appendChild(label({}, "Game name:"));
    form.appendChild(input({ class : 'form-input', type: "text", id: "gameName", required: true }));
    form.appendChild(label({}, "Game developer:"));
    form.appendChild(input({ class : 'form-input', type: "text", id: "gameDeveloper", required: true }));
    form.appendChild(label({}, "Game genres:"));
    form.appendChild(input({ class : 'form-input', type: "text", id: "gameGenres", required: true }));
    form.appendChild(
        button({
            class : 'form-button',
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
      { class: 'games-page' },
      p({}, "This page displays the games that were queried."),
      div({}, buttons),
      p({}, "Here are the games that were queried:"),
      ul(
        {},
        ...games.map(game =>
          li(
            {},
            a(
              {href: "#games/" + game.id },
              game.name
            )
          )
        )
      ),
      homeButton
    );
}


export function gameDetailsPage(game, createSession) {
    const homeButton = returnHomeButton();
    const form = sessionForm(createSession, game.id);

    return div(
        { class: 'game-details-page' }, // Use the class for styling
        h2({}, "Game Details"), // Use h2 for the heading
        div(
            {},
            ul(
                {},
                li({}, "Name: " + game.name),
                li({}, "Developer: " + game.dev),
                li({}, "Genres: " + game.genres.join(", ")),
            ),
            button(
                {
                    onClick: () => {
                        openModal(form)
                    }
                }, "Create a session with this game"
            )
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
    form.className = 'form-container';

        form.appendChild(label({}, "Session capacity:"));
        form.appendChild(input({class : 'form-input', type: "number", id: "sessionCapacity", min: 1, max: 100, required: true}));
        form.appendChild(label({}, "Session date:"));
        form.appendChild(input({class : 'form-input', type: "text", id: "sessionDate", required: true}));
        form.appendChild(
            button({
                class : 'form-button',
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