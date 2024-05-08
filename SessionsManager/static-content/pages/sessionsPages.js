import {div, button, label, p, ul, li, a, radioButton, input, h2} from "../tags.js"
import {returnHomeButton} from "../components/returnHomeButton.js"
import {errorToast} from "../components/errorToast.js"
import {controlledInput} from "../components/controlledInput.js"
import {CONSTS} from "../utils.js"
import {openModal, closeModal} from "../components/modal.js"

export const pattern = /^\d{4}-\d{2}-\d{2}$/;
export const states = ["ALL", "OPEN", "CLOSED"]

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
            class : 'generic-search-button',
            onClick: () => {
                sessionsSearchPageClick(dateInput, document.querySelector('input[name="state"]:checked'));
            },
        },
        "Search sessions"
    );

    return div(
        { class: 'sessions-page' },
        p({}, "Search for sessions by entering date (optional) and closed state"),
        div(
            {},
            label({id: "dateLabel"}, "Date:", dateCheckBox, dateInput),
            p({}),
            label({id: "stateLabel"}, "Session State:", ...stateRadioButtons),
        ),
        searchButton,
        homeButton
    );
}

function sessionsSearchPageClick(dateInput, stateInput) {
    const input_date = (dateInput.disabled) ? null : dateInput.value;
    if (input_date != null && pattern.test(input_date) === false)
        errorToast("Please enter a valid date");
    else {
        const state = (stateInput.value === "ALL" ? null : stateInput.value);
        const url = "sessions/list?date=" + input_date + "&state=" + state +
            "&skip=" + CONSTS.SKIP_DEFAULT + "&limit=" + CONSTS.LIMIT_DEFAULT;
        window.location.hash = url;
    }
}


export function sessionsListPage(sessions, buttons) { //list of sessions
    const homeButton = returnHomeButton();

    return div(
        { class: 'sessions-page' },
         p({}, "This page displays the sessions that were queried."),
         div({}, buttons),
         p({}, "Here are the sessions that were queried:"),
        ...sessions.map((session, index) =>
            ul({},
                li({}, a({ href: "#sessions/" + session.id }, "Session " + (index + 1))),
                li({}, "Date: " + session.date)
            )
        ),
        homeButton
    );
}


export function sessionDetailsPage(session, leaveSession, updateSession, deleteSession, joinSession) {
    const homeButton = returnHomeButton();

    const pAnchors = session.players.map((p, index) =>
        div({}, a({ href: "#players/" + p }, "Player " + (index + 1)))
    );

    return div(
        { class: 'session-details-page' },
        h2({}, "Session Details"),
        ul({},
            li({}, "This session is playing this ", a({href: "#games/" + session.game}, "game")),
            li({}, "Maximum player capacity: " + session.capacity),
            li({}, "Date and time: " + session.date),
            li({}, "Session closed status: " + session.closed),
            li({}, "Players in the session: ", ...pAnchors)
        ),
         div(
            { class: 'buttons-container' },
            isInSession(session, CONSTS.HARDCODED_ID) ? // hardcoded with player 1000 for now
                button({ class: 'leave-button', onClick: () => { openModal(
                    div(
                        {class : 'form-container'},
                        "Are you sure you want to leave this session?",
                        button({ class : 'form-button', onClick : () => { leaveSession(session.id); closeModal() } }, "Yes"),
                    )
                )}}, "Leave session") : div({}),
            isOwner(session, CONSTS.HARDCODED_ID) ? // hardcoded with player 1000 for now
                button({ class: 'update-button', onClick: () => { openModal(
                    div(
                        {class : 'form-container'}, // This is using the 'form-container' class even though it's not a form
                        label({}, "New Capacity:"),
                        input({ class : 'form-input', type: "number", id: "capacityInput", min: 2, max: 100, required: true}),
                        label({}, "New Date:"),
                        input({ class : 'form-input', type: "text", id: "dateInput", required: true}),
                        button({
                            class : 'form-button',
                            onClick: () => {
                                updateSession(
                                    session.id,
                                    document.getElementById('capacityInput').value,
                                    document.getElementById('dateInput').value
                                );
                                closeModal();
                            }
                        }, "Update")
                    )
                )}}, "Update session") : div({}),
            isOwner(session, CONSTS.HARDCODED_ID) ? // hardcoded with player 1000 for now
                button({ class: 'leave-button', onClick: () => { openModal(
                    div(
                        {class : 'form-container'},
                        "Are you sure you want to delete this session?",
                        button({ class : 'form-button', onClick : () => { deleteSession(session.id); closeModal() } }, "Yes")
                    )
                ) }}, "Delete session") : div({}),
        ),
        isInSession(session, CONSTS.HARDCODED_ID) || session.closed ? // hardcoded with player 1000 for now
            div({}) : button({onClick: () => { joinSession(session.id) } }, "Join session"),
        homeButton
    );
}




function isInSession(session, pid) {
    return session.players.includes(pid);
}

function isOwner(session, pid) {
    return session.players[0] == pid;
}