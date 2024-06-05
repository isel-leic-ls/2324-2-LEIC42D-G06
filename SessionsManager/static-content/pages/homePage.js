import { div, a, ul, h1, li, p, button, input, label } from "../tags.js"


export function homePage(id, logoutFunction) {
    const container = div(
        { class: 'home-page' },
        h1({}, "Welcome to Session Finder"),
        p(
            {},
            "Session Finder is a platform where you can create games, organize gaming sessions," +
            "and join with other players to play sessions with those games."
        ),
        ul(
            {},
            li({}, a({ href: "#games" }, "Search games"), " - Explore and discover new games"),
            li({}, a({ href: "#sessions" }, "Search sessions"), " - Find and join gaming sessions"),
            li({}, a({ href: "#players" }, "Search players"), " - Find and connect with other players"),
            id ? li({}, a({ href: "#players/" + id }, "My profile"), " - View and manage your profile") : '',
            id ? button({ class: 'logout-button', onClick: logoutFunction }, "Logout") : '',
            id ? '' : li({}, a({ href: "#register" }, "Register"), " - Register a new account"),
            id ? '' : li({}, a({ href: "#login" }, "Login"), " - Login to an existing account")
        )
    );

    return container;
}

export function registerPage(registerUser) {
    return div(
        { class: 'register-page' },
        h1({}, "Register"),
        p({}, "Please fill in the form below to register"),
        div(
            { class: 'form-container' },
            label({}, "Username"),
            input({ class: 'form-input', type: 'text', id: 'username', name: 'username' }),
            label({}, "Email"),
            input({ class: 'form-input', type: 'text', id: 'email', name: 'email' }),
            label({}, "Password"),
            input({ class: 'form-input', type: 'password', id: 'password', name: 'password' }),
            button({
                class: 'form-button', onClick: (event) => {
                    event.preventDefault();
                    registerUser(
                        document.getElementById('username').value,
                        document.getElementById('email').value,
                        document.getElementById('password').value
                    )
                }
            }, "Register")
        )
    );
}

export function loginPage(loginUser) {
    return div(
        { class: 'login-page' },
        h1({}, "Login"),
        p({}, "Please fill in the form below to login"),
        div(
            { class: 'form-container' },
            label({}, "Username"),
            input({ class: 'form-input', type: 'text', id: 'username', name: 'username' }),
            label({}, "Password"),
            input({ class: 'form-input', type: 'password', id: 'password', name: 'password' }),
            button({
                class: 'form-button', onClick: (event) => {
                    event.preventDefault();
                    loginUser(
                        document.getElementById('username').value,
                        document.getElementById('password').value
                    )
                }
            }, "Login")
        )
    );
}