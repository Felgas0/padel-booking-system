import apiFetch from "../../js/apiFetch.js"
import Club from "../../components/clubs/Club.js"
import {LogError} from "../../js/errorUtils.js"

/**
 * Club details page.
 * @param {Object} state - application state
 * @returns Promise<HTMLElement>
 */
export default async function ClubPage(state) {
    if (state.params.id === undefined)
        throw new LogError("Club id must be defined")

    const id = state.params.id
    const club = await apiFetch(`/clubs/${id}`)

    const owner = await apiFetch(`/users/${club.ownerId}`);

    return Club(state, {
        id: club.id,
        name: club.name,
        ownerId: club.ownerId,
        ownerName: owner.name,
    })
}
