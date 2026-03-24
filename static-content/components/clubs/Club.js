import { a, div, h1, h3 } from "../../js/dom/domTags.js";


/**
 * Detail card for a club.
 * @param {Object} state
 * @param {{ id: number, name: string, ownerName: string, ownerId: number }} props
 */
async function Club(state, props) {
    return div(
        { class: "row justify-content-evenly" },

        div(
            { class: "col-12 text-start mb-3" },
            a({ href: "#clubs", class: "btn btn-secondary" }, "← Back to Clubs")
        ),

        h1({ class: "app_icon" }, `Club ${props.name}`),

        div(
            { class: "card user_card col-6" },
            div(
                { class: "card-body" },
                h3("Name: ", props.name),
                h3("Owner: ", a({ href: `#users/${props.ownerId}` }, props.ownerName))
            )
        ),

        div(
            { class: "col-12 text-center mt-4" },
            a(
                {
                    href: `#clubs/${props.id}/courts`,
                    class: "btn btn-primary"
                },
                "View Courts"
            )
        )
    );
}

export default Club;
