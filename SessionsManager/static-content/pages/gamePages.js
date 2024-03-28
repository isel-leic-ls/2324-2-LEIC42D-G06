import { div, a, ul, li, label, input, button } from "../tags.js"

export function gameDetailsPage(game) {
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
           )
       );
    return element;
}

export function gameListPage(games) {

    const elements = games.map(game =>
        div({},
            a({ href: "#games/" + game.id }, game.name)
        )
    );

    const element = div(
        {},
        "Games",
        ...elements
    )

    return element
}

export function gameSearchPage() {
    const genresInput = input({ type: "text", placeHolder: "Action, Adventure" });
    const developerInput = input({ type: "text", placeHolder: "Ubisoft, EA" });

    const searchButton = button(
        {
            onClick: () => {
                const genres = genresInput.value
                const developer = developerInput.value
                window.location.hash = "games/list?genres=" + genres
                + "&developer=" + developer + "&skip=0&limit=10";
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
        searchButton
    );
    return element;
}