import { div, h1 } from "../../js/dom/domTags.js";
import Clubs from "../../components/clubs/Clubs.js";
import FetchedPaginatedCollection from "../../components/pagination/FetchedPaginatedCollection.js";


/**
 * List of clubs whose name contains the fragment provided in ?name=
 * Route: #clubs/search?name=demo&skip=0&limit=10
 */
export default async function ClubsSearchPage(state) {

    const name = state.query.name ?? "";

    return div(
        h1({ class: "app_icon" }, `Results for "${name}"`),
        FetchedPaginatedCollection(state, {
                defaultSkip   : 0,
                defaultLimit  : 10,
                collectionName: "clubs",
                collectionComponent: Clubs,
                collectionEndpoint: "/clubs/search", searchParams: { name }
        })
    );
}

