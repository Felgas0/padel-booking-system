import Clubs from "../../components/clubs/Clubs.js"
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js"
import {div, h1} from "../../js/dom/domTags.js"

/**
 * Clubs list page.
 * @param {Object} state - application state
 * @returns Promise<HTMLElement>
 */
export default async function ClubsPage(state) {
    return div(
        h1({class: "app_icon"}, "Clubs"),
        FetchedPaginatedCollection(state, {
            defaultSkip: 0,
            defaultLimit: 10,
            collectionComponent: Clubs,
            collectionEndpoint: "/clubs",
            collectionName: "clubs",
        })
    )
}
