import {a, div, h5} from "../../js/dom/domTags.js"

/**
 * @typedef PropClub
 * @property {number} id
 * @property {string} name
 */

/**
 * Clubs list component.
 *
 * @param {Object} state - global state
 * @param {Object} props - props with array of clubs
 * @param {PropClub[]} props.clubs
 *
 * @returns Promise<HTMLElement>
 */
async function Clubs(state, props) {
    return div(
        {class: "row justify-content-evenly"},
        ...props.clubs.map(club =>
            div(
                {class: "card user_card col-6"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h5(
                        {class: "card-title"},
                        a({href: `#clubs/${club.id}`}, club.name)
                    )
                )
            )
        )
    )
}

export default Clubs
