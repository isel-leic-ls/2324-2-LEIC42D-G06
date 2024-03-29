import { div, input, button, label, p, ul, li } from "../tags.js"


export function sessionsSearchPage() { // search sessions by date and state

    const dateInput = input({ type: "text", placeholder: "2034-04-04 18:00:00", disabled : true });
    const dateCheckBox = input({ type: "checkbox", onChange : () => {
        dateInput.disabled = !dateInput.disabled;
        dateInput.value = ""
    } });

    const stateInput = input({ type: "text", placeholder: "open", disabled : true });
    const stateCheckBox = input({ type: "checkbox", onChange : () => {
        stateInput.disabled = !stateInput.disabled;
        stateInput.value = ""
    } });

    const searchButton = button(
        {
            onClick: () => {
                const date = (dateInput.disabled) ? null : dateInput.value
                const state = (stateInput.disabled) ? null : stateInput.value
                window.location.hash = "sessions/list?date=" + date + "&state=" + state + "&skip=0&limit=1"
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
        searchButton
    );

    return element;
}


export function sessionsListPage(sessions, buttons) { // list of sessions
    const elements = sessions.map(session =>
        ul({},
            li({}, "ID: " + session.id),
            li({}, "Capacity: " + session.capacity),
            li({}, "Date: " + session.date),
            li({}, "Game ID: " + session.game),
            li({}, "Status: " + session.closed),
            li({}, "Players: " + session.players)
        )
    );

    const element = div(
        {},
        "Sessions",
        div({}, buttons),
        ...elements
    );

    return element;
}