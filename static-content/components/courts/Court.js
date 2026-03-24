import { div, h1, h3, a } from "../../js/dom/domTags.js";

/**
 * Court details component.
 * @param {Object} state
 * @param {{ id:number, name:string, clubId:number, clubName:string }} props
 * @returns {Promise<HTMLElement>}
 */
async function Court(state, props) {
    return div(
        { class: "row justify-content-evenly" },

        h1({ class: "app_icon" }, `Court: ${props.name}`),

        div(
            { class: "card user_card col-6" },
            div(
                { class: "card-body" },
                h3("Name: ", props.name),
                h3("Club: ", a({ href: `#clubs/${props.clubId}` }, props.clubName))
            )
        ),

        div(
            { class: "col-12 text-center mt-4" },
            a(
                {
                    href: `#clubs/${props.clubId}/courts/${props.id}/rentals`,
                    class: "btn btn-primary"
                },
                "View Rentals"
            ),

            a(
                {
                    href: `#available-hours?clubId=${props.clubId}&courtId=${props.id}`,
                    class: "btn btn-outline-secondary ms-2"
                },
                "Search Rentals by Date"
            )
        )
    );
}

export default Court;
