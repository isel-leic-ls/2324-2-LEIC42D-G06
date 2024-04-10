import {div, button, label, p, ul, li, a, radioButton} from "../tags.js"
import {returnHomeButton} from "../components/returnHomeButton.js"
import {errorToast} from "../components/errorToast.js"
import {controlledInput} from "../components/controlledInput.js"
import {CONSTS} from "../utils.js"


export const pattern = /^\d{4}-\d{2}-\d{2}$/;
export const states = ["ALL", "OPEN", "CLOSED"]


function sessionsSearchPageClick(dateInput, stateInput) {
    const input_date = (dateInput.disabled) ? null : dateInput.value;
    const input_state = (stateInput.disabled) ? null : stateInput.value;

    if (input_date != null && pattern.test(input_date) === false)
        errorToast("Please enter a valid date");
    else if (input_state !== null && !states.includes(input_state))
        errorToast("Please enter a valid state");
    else {
        const state = (input_state === "ALL" ? null : input_state);
        const url = "sessions/list?date=" + input_date + "&state=" + state +
            "&skip=" + CONSTS.SKIP_DEFAULT + "&limit=" + CONSTS.LIMIT_DEFAULT;
        window.location.hash = url;
    }
}

export function sessionsSearchPage() {
    const homeButton = returnHomeButton();
    const [dateCheckBox, dateInput] = controlledInput("2025-03-04");

    const stateRadioButtons = states.map(state => {
        const radio = radioButton({name: "state", value: state});
        if (state === "ALL") radio.checked = true;

        radio.addEventListener("click", () => {
            stateRadioButtons.forEach(button => {
                button.checked = (button.value === state);
            });
        });

        const stateLabel = label({}, radio, state);
        return div({}, stateLabel);
    });

    const searchButton = button(
        {
            onClick: () => {
                sessionsSearchPageClick(dateInput, document.querySelector('input[name="state"]:checked'));
            }
        },
        "Search sessions"
    );

    const element = div(
        {},
        "Search sessions by date and state:",
        div(
            {},
            label({id: "dateLabel"}, "Date:", dateCheckBox, dateInput),
            p({}),
            label({id: "stateLabel"}, "State:", ...stateRadioButtons),
        ),
        searchButton,
        homeButton
    );

    return element;
}

export function sessionsListPage(sessions, buttons) { //list of sessions

    const homeButton = returnHomeButton();
    const elements = sessions.map((session, index) =>
        ul({},
            li({}, a({ href: "#sessions/" + session.id }, "Session: " + (index + 1)),
            li({}, "Date: " + session.date)
        )
    ));

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

    const pAnchors = session.players.map(p => div({}, a({href: "#players/" + p}, "  " + p)));
    const element = div(
        {},
        "Session details",
        ul({},
            li({}, a({href: "#games/" + session.game}, "Game")),
            li({}, "Capacity: " + session.capacity),
            li({}, "Date: " + session.date),
            li({}, "Closed: " + session.closed),
            li({}, "Players: ", ...pAnchors)
        ),
        homeButton
    );

    return element;
}