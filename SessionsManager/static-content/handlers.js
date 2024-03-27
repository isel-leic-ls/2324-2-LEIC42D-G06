import { div, a, ul, li, label, input, button } from "./tags.js"


const API_PATH = "http://localhost:9000/"

function getHome(mainContent) {
    const element =
        div(
            {}, "Hello LS",
            div(
                {},
                a({ href: "#games" }, "Search games")
            ),
            div(
                {},
                a({ href: "#sessions" }, "Search sessions")
            ),
            div(
                {}, //This should redirect to "My details, we need to implement token first"
                a({ href: "#players/1000" }, "My details - TODO") //HARD-CODED ID
            )
        )
    mainContent.replaceChildren(element);
}

function getGames(mainContent) {
    const genresInput = input({ type: "text", placeHolder: "Action, Adventure" });
    const developerInput = input({ type: "text", placeHolder: "Ubisoft, EA" });

    const searchButton = button(
        {
            onClick: () => {
                const genres = genresInput.value.split(",").map(genre => genre.trim());
                const developer = developerInput.value.trim();

                // Now you can use the 'genres' array and 'developer' string as needed
                console.log("Genres:", genres);
                console.log("Developer:", developer);
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

    mainContent.replaceChildren(element);
}


function getSessions(mainContent) {
    const element = div({}, "Sessions");
    mainContent.replaceChildren(element);
}

async function getPlayer(mainContent) {
    const result = await fetch(API_PATH + "players/1000"); //HARD-CODED ID
    const player = await result.json();
    const element =
        div(
            {},
            "Player 1000 hard-coded", //HARD-CODED ID
            div(
                {},
                ul(
                    {},
                    li({}, "ID: " + player.id), //TODO: Remove this after remove the hard-coded ID
                    li({}, "Name: " + player.name),
                    li({}, "E-mail: " + player.email),
                )
            )
        );
    mainContent.replaceChildren(element);
}


export const handlers = {
    getHome,
    getGames,
    getSessions,
    getPlayer
}

export default handlers