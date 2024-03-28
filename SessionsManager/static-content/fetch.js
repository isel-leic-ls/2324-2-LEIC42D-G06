const API_BASE_URL = "http://localhost:9000/api";

export async function fetcher(path, method, body) {

    const response = await fetch(API_BASE_URL + path, {
        method,
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(body)
    });
    return await response.json();
}