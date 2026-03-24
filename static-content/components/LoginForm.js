import {br, button, div, form, h1, hr, input, label} from "../js/dom/domTags.js";

/**
 * LoginForm component.
 *
 * @param {Object} state - application state
 * @param {Object} props - component properties
 * @param {(event: Event) => void} props.onSubmit - onSubmit event callback
 *
 * @return Promise<HTMLElement>
 */
async function LoginForm(state, props) {

    const {onSubmit} = props;

    return div(
        {class: "card card-body w-50 center"},
        h1("Login"),
        hr(),
        form(
            {onSubmit: onSubmit},
            div(
                label({for: "name", class: "form-label"}, "Username"),
                input({
                    type: "text", id: "name", name: "name",
                    class: "form-control",
                    placeholder: "Enter your username", minlength: "3", maxlength: "60",
                    required: true
                }),

                label({for: "password", class: "form-label"}, "Password"),
                input({
                    type: "password", id: "password",
                    class: "form-control",
                    placeholder: "Enter your password",
                    minlength: "5", maxlength: "25",
                    required: true
                }),
            ),
            br(),
            button({type: "submit", class: "btn btn-primary w-100 btn-lg"}, "Login")
        )
    );
}

export default LoginForm;
