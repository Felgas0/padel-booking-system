import {a, div, h1, h3} from "../../js/dom/domTags.js";

/**
 * @param {Object} state
 * @param {{ id: number, name: string, email: string }} props
 */
async function User(state, props) {
    return div(
        {class: "row justify-content-evenly"},



        h1({class: "app_icon"}, `User ${props.name}`),

        div(
            {class: "card user_card col-6"},
            div(
                {class: "card-body"},
                h3("Name: ", props.name),
                h3("Email: ", typeof props.email === 'object' ? props.email.value : props.email)
            )
        ),

        div(
            {class: "col-12 text-center mt-4"},
            a({href: `#users/${props.id}/rentals`, class: "btn btn-primary"}, "View Rentals")
        )
    );
}

export default User;
