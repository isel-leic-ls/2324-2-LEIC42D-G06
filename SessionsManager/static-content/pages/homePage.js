import { div, a, ul, button, input } from "../tags.js"


function playerSearchByEmail(email) {
    window.location.hash = "players/email/" + email;
}

export function homePage(player) { //this is the home page (the first page the user sees when opening the app)
    const emailInput = input({ type: "text", id: "emailInput", placeHolder: "Email" })

    const searchByEmailButton = button(
        {
            onClick: () => {
                playerSearchByEmail(emailInput.value);
            }
        },
        "Search by email"
    );

    const element =
        div(
            {}, "Session Finder Home Page",
            ul(
                {},
                ""
            ),
            div(
                {},
                a({ href: "#games" }, "Search games")
            ),
            div(
                {},
                a({ href: "#sessions" }, "Search sessions")
            ),
            div(
                {},
                a({ href: "#players/" + player.id }, "My profile")
            ),
            div(
                {},
                "Search player by email",
                emailInput,
                searchByEmailButton
            )
        );

    return element;
}