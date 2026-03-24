import {a, div, h5} from "../../js/dom/domTags.js";

/**
 * @typedef PropRental
 * @property {number} rentalId rental id
 */

/**
 * Rentals component.
 *
 * @param {Object} state - application state
 * @param {Object} props
 * @param {PropRental[]} props.rentals
 */
async function Rentals(state, props) {
    return div(
        {class: "row justify-content-evenly"},
        ...props.rentals.map(rental =>
            div(
                {class: "card user_card col-6"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h5(
                        {class: "card-title"},
                        a(
                            { href: `#rentals/${rental.rentalId}` },
                            `${rental.courtName} - ${rental.date.replace("T", " ").substring(0, 16)}`
                        )

                    )
                )
            )
        )
    );
}

export default Rentals;
