import apiFetch from "../../js/apiFetch.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import Rentals from "../../components/rentals/Rentals.js";
import { a, div, h1 } from "../../js/dom/domTags.js";

/**
 * Page for rentals made by a user.
 * @param {Object} state - app state
 * @returns {Promise<HTMLElement>}
 */
export default async function RentalsByUserPage(state) {
    const userId = parseInt(state.params.userId);
    const { name: userName } = await apiFetch(`/users/${userId}`);

    return div(
        { class: "row justify-content-evenly" },

        div(
            { class: "col-12 text-start mb-3" },
            a(
                { href: `#users/${userId}`, class: "btn btn-secondary" },
                `← Back to ${userName}`
            )
        ),

        h1({ class: "app_icon" }, `Rentals by ${userName}`),

        FetchedPaginatedCollection(state, {
            defaultSkip: 0,
            defaultLimit: 10,
            collectionComponent: Rentals,
            collectionEndpoint: `/users/${userId}/rentals`,
            collectionName: "rentals",
        })
    );
}
