import {a, div, h5} from "../../js/dom/domTags.js";

/**
 * @typedef PropCourt
 * @property {number} id - court id
 */

/**
 * Courts component.
 *
 * @param state - application state
 * @param {Object} props - component properties
 * @param {PropCourt[]} props.courts - courts list
 *
 * @return Promise<HTMLElement>
 */
async function Courts(state, props) {
    return div(
        {class: "row justify-content-evenly"},
        ...props.courts.map(court =>
            div(
                {class: "card user_card col-6"},
                div(
                    {class: "card-body d-flex justify-content-center"},
                    h5(
                        {class: "card-title"},
                        a({href: `#courts/${court.id}`}, `Court: ${court.name}`)
                    )
                )
            )
        )
    );
}

export default Courts;
