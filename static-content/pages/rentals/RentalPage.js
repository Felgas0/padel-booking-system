import apiFetch from "../../js/apiFetch.js";
import Rental from "../../components/rentals/Rental.js";
import {LogError} from "../../js/errorUtils.js";

/**
 * Rental details page.
 *
 * @param {Object} state - application state
 * @returns Promise<HTMLElement>
 */
export default async function RentalPage(state) {
    if (state.params.id === undefined)
        throw new LogError("Rental id must be defined");

    const id = state.params.id;

    const rental = await apiFetch(`/rentals/${id}`);

    return Rental(state, {
        id: id,
        date: rental.date,
        duration: rental.duration,
        userId: rental.userId,
        userName: rental.userName,
        clubId: rental.clubId,
        clubName: rental.clubName,
        courtId: rental.courtId,
        courtName: rental.courtName
    });
}