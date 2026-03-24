import {a, div, h1, h3} from "../../js/dom/domTags.js";

/**
 * Rental details component.
 *
 * @param {Object} state - global state
 * @param {Object} props - component properties
 * @param {number} props.id - rental id
 * @param {string} props.date - date of rental
 * @param {number} props.duration - duration in hours
 * @param {number} props.userId - user id
 * @param {string} props.userName - username
 * @param {number} props.clubId - club id
 * @param {string} props.clubName - club name
 * @param {number} props.courtId - court id
 * @param {string} props.courtName - court name
 *
 * @returns Promise<HTMLElement>
 */
async function Rental(state, props) {
    return div(
        {class: "row justify-content-evenly"},
        h1({class: "app_icon"}, "Rental Details"),
        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body"},
                h3("Date: ", props.date),
                h3("Duration: ", `${props.duration}h`),
                h3(
                    "User: ",
                    a({href: `#users/${props.userId}`}, props.userName)
                ),

                h3("Club: ", props.clubName),
                h3(
                    "Court: ",
                    a({href: `#courts/${props.courtId}`}, props.courtName)
                )

            )
        ),
        div(
            {class: "col-12 text-center"},
            a(
                {
                    href: `#rentals/${props.id}/update`,
                    class: "btn btn-primary"
                },
                "Update Rental"
            ),
            a(
                {
                    href: `#rentals/${props.id}/delete`,
                    class: "btn btn-danger"
                },
                "Delete Rental"
            )
        )
    );
}

export default Rental;
