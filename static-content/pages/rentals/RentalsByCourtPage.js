import apiFetch from "../../js/apiFetch.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import Rentals from "../../components/rentals/Rentals.js";
import { a, div, h1 } from "../../js/dom/domTags.js";

/**
 * Page for rentals on a specific court.
 * @param {Object} state - app state
 * @returns {Promise<HTMLElement>}
 */
export default async function RentalsByCourtPage(state) {
    const { clubId, courtId } = state.params;

    const { name: courtName } = await apiFetch(`/courts/${courtId}`);

    return div(
        { class: "row justify-content-evenly" },

        div(
            { class: "col-12 text-start mb-3" },
            a(
                { href: `#courts/${courtId}`, class: "btn btn-secondary" },
                `← Back to ${courtName}`
            )
        ),

        h1({ class: "app_icon" }, `Rentals on ${courtName}`),

        FetchedPaginatedCollection(state, {
            defaultSkip: 0,
            defaultLimit: 10,
            collectionComponent: Rentals,
            collectionEndpoint: `/clubs/${clubId}/courts/${courtId}/rentals`,
            collectionName: "rentals"
        })
    );
}
