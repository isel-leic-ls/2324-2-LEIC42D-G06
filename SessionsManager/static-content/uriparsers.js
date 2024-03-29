export function filterQueryParameters(path) {
    const index = path.indexOf('?')
    return index == -1 ? {} : Object.fromEntries(new URLSearchParams(path.substring(index)))
}

export function filterUriId(path) {
    return path.substring(path.lastIndexOf('/') + 1);
}