import apiFetch from "../../js/apiFetch.js";
import Court from "../../components/courts/Court.js";
import {LogError} from "../../js/errorUtils.js";
import {getQuerySkipLimit} from "../../js/utils.js";

/**
 * Court details page.
 * @param {Object} state - application state
 * @returns Promise<HTMLElement>
 */
async function CourtPage(state) {
    if (state.params.id === undefined)
        throw new LogError("Court id must be defined");

    const id = state.params.id;
    const court = await apiFetch(`/courts/${id}`);
    const club = await apiFetch(`/clubs/${court.clubId}`);

    const {skip, limit} = getQuerySkipLimit(state.query, 0, 10);
    const rentalsData = await apiFetch(`/clubs/${court.clubId}/courts/${court.id}/rentals?skip=${skip}&limit=${limit}`);
    rentalsData.skip = skip;
    rentalsData.limit = limit;

    return Court(state, {
        id: court.id,
        name: court.name,
        clubId: club.id,
        clubName: club.name,
        rentalsData
    });
}

export default CourtPage;
