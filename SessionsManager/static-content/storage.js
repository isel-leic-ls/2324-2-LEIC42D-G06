const items = {
    id : "user_id",
    token : "user_token"
}

export class UserStorage {
    setUserInfo(id, token) {
        sessionStorage.setItem(items.id, id)
        sessionStorage.setItem(items.token, "Bearer " + token)
    }

    getUserInfo() {
        return {
            id: sessionStorage.getItem(items.id),
            token: sessionStorage.getItem(items.token)
        }
    }

    clearUserInfo() {
        sessionStorage.removeItem(items.id)
        sessionStorage.removeItem(items.token)
    }
}