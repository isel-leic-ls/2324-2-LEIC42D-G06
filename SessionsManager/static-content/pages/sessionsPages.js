import { div, input, button, label, p, ul, li, a } from "../tags.js"
import { returnHomeButton } from "../components/returnHomeButton.js"
import { errorToast } from "../components/errorToast.js"
import { controlledInput } from "../components/controlledInput.js"


const pattern = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/;
const states = ["open", "closed"]

function sessionsSearchPageClick(dateInput, stateInput) {
    const date = (dateInput.disabled) ? null : dateInput.value
    const state = (stateInput.disabled) ? null : stateInput.value
    if( date != null && pattern.test(date) === false || state != null && states.includes(state) === false) {
        errorToast("Please enter a valid date and state")
    }
    else window.location.hash = "sessions/list?date=" + date + "&state=" + state + "&skip=0&limit=1"
    // The limit value is set to 1 for testing purposes
}


export function sessionsSearchPage() { // search sessions by date and state

    const homeButton = returnHomeButton();

    const [dateCheckBox, dateInput] = controlledInput("2025-03-04 18:00:00")
    const [stateCheckBox, stateInput] = controlledInput("open")

    const searchButton = button(
        {
            onClick: () => {
                sessionsSearchPageClick(dateInput, stateInput);
            }
        },
        "Search sessions"
    );

    const element = div(
        {},
        "Search sessions by date and state:",
        div(
            {},
            label({}, "Date:", dateCheckBox, dateInput),
            p({}),
            label({}, "State:", stateCheckBox, stateInput),
        ),
        searchButton,
        homeButton
    );

    return element;
}


export function sessionsListPage(sessions, buttons) { // list of sessions

    const homeButton = returnHomeButton();
    const elements = sessions.map(session =>
        ul({},
            li({}, "ID: ", a({ href: "#sessions/" + session.id }, "" + session.id)),
            li({}, "Date: " + session.date)
        )
    );

    const element = div(
        {},
        "Sessions",
        div({}, buttons),
        ...elements,
        homeButton
    );

    return element;
}

export function sessionDetailsPage(session) {

    const homeButton = returnHomeButton();

    const pAnchors = session.players.map(p => a({ href: "#players/" + p }, "    " + p));
    const element = div(
        {},
        "Session details",
        ul({},
            li({}, "ID: " + session.id),
            li({}, "Capacity: " + session.capacity),
            li({}, "Date: " + session.date),
            li({}, "Game ID: ", a({ href: "#games/" + session.game }, "" + session.game)),
            li({}, "Closed: " + session.closed),
            li({}, "Players: ", ...pAnchors)
        ),
        homeButton
    );

    return element;
}