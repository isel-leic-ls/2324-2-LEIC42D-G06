import { div, a, ul, h1, li, p } from "../tags.js"


export function homePage(player) {
    const container = div(
        { class: 'home-page' },
        h1({}, "Welcome to Session Finder"),
        p({}, "Session Finder is a platform where you can create games, organize gaming sessions, and join with other players to play sessions with those games."),
        ul(
            {},
            li({}, a({ href: "#games" }, "Search games"), " - Explore and discover new games"),
            li({}, a({ href: "#sessions" }, "Search sessions"), " - Find and join gaming sessions"),
            li({}, a({ href: "#players/" + player.id }, "My profile"), " - View and manage your profile")
        )
    );

    return container;
}
