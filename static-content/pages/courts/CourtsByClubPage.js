import apiFetch from "../../js/apiFetch.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";
import Courts from "../../components/courts/Courts.js";
import { a, div, h1 } from "../../js/dom/domTags.js";

/**
 * List of courts belonging to a club.
 */
export default async function CourtsByClubPage(state) {
    const clubId = parseInt(state.params.clubId);
    const { name: clubName } = await apiFetch(`/clubs/${clubId}`);

    return div(
        { class: "row justify-content-evenly" },

        div(
            { class: "col-12 text-start mb-3" },
            a(
                { href: `#clubs/${clubId}`, class: "btn btn-secondary" },
                `← Back to ${clubName}`
            )
        ),

        h1({ class: "app_icon" }, `Courts in ${clubName}`),

        FetchedPaginatedCollection(state, {
            defaultSkip: 0,
            defaultLimit: 10,
            collectionComponent: Courts,
            collectionEndpoint: `/clubs/${clubId}/courts`,
            collectionName: "courts"
        })
    );
}
