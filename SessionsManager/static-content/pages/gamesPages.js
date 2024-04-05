import { div, a, ul, li, label, input, button } from "../tags.js"
import { returnHomeButton } from "../components/returnHomeButton.js"
import { errorToast } from "../components/errorToast.js"


function gamesSearchPageClick(genres , developer) {
    if(genres === "" && developer === "") errorToast("Please enter at least one genre or developer");
    else window.location.hash = "games/list?genres=" + genres +
    "&developer=" + developer + "&skip=0&limit=5"; // The limit value is set to 1 for testing purposes
}

export function gamesSearchPage() { //this is the search page for games by genre(s) and developer
    const genresInput = input({ type: "text", placeHolder: "Action, Adventure" });
    const developerInput = input({ type: "text", placeHolder: "Ubisoft, EA" });

    const homeButton = returnHomeButton();

    const searchButton = button(
        {
            onClick: () => {
                gamesSearchPageClick(genresInput.value, developerInput.value);
            }
        },
        "Search"
    );

    const element = div(
        {},
        "Search games by genre(s) and developer",
        div(
            {},
            label({}, "Genres:", genresInput),
            label({}, "Developer:", developerInput),
        ),
        searchButton,
        homeButton
    );

    return element;
}

export function gamesListPage(games, buttons) { //this is the list of games that match the search criteria
    const elements = games.map(game =>
        div({},
            a({ href: "#games/" + game.id }, game.name)
        )
    );

    const element = div(
        {},
        "Games",
        div({}, buttons),
        ...elements
    )

    return element
}

export function gameDetailsPage(game) { //this is the details page for a specific game
    const homeButton = returnHomeButton();

    const element =
        div(
            {},
            "Game Details",
            div(
                {},
                ul(
                    {},
                    li({}, "ID: " + game.id),
                    li({}, "Name: " + game.name),
                    li({}, "Developer: " + game.dev),
                    li({}, "Genres: " + game.genres.join(", ")),
                )
            ),
            button(
                {
                    onClick: () => {
                        window.location.hash = "sessions/list?gid=" + game.id + "&skip=0&limit=5"
                    }
                }, "Search sessions with this game"
            ),
            homeButton
        );

    return element;
}