import { div, input, button, label } from "../tags.js"


export function sessionsSearchPage() { //this is the search page for sessions by gId, date (optional), state (optional), pId (optional)
    const gIdInput = input({ type: "text", placeholder: "Game ID" });
    const dateInput = input({ type: "text", placeholder: "Date" });
    const stateInput = input({ type: "text", placeholder: "State" });
    const pIdInput = input({ type: "text", placeholder: "Player ID" });

    const searchButton = button(
        {
            onClick: () => {
                const gId = gIdInput.value
                const date = dateInput.value
                const state = stateInput.value
                const pId = pIdInput.value
                window.location.hash = "sessions/list?gid=" + gId
                    + "&date=" + date + "&state=" + state + "&pid=" + pId; //TODO check if this is the correct
            }
        },
        "Search TODO good redirect"
    );

    const element = div(
        {},
        "Search sessions by game ID, date, state and player ID",
        div(
            {},
            label({}, "Game ID:", gIdInput),
            label({}, "Date:", dateInput),
            label({}, "State:", stateInput),
            label({}, "Player ID:", pIdInput),
        ),
        searchButton
    );

    return element;
}