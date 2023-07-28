import { debounce } from "./utils.js";

window.addEventListener("DOMContentLoaded", (event) => {
    const signUpForm = document.getElementById("sign-up-form");
    const usernameInput = document.getElementById("username-input");
    const emailInput = document.getElementById("email-input");
    const passwordInput = document.getElementById("password-input");
    const passwordConfirmInput = document.getElementById(
        "confirm-password-input"
    );
    const nameInput = document.getElementById("name-input");
    const alertMessage = document.querySelector(".alert-message");
    function showAlert(content, color) {
        alertMessage.innerHTML = content;
        alertMessage.classList.add("show");
        alertMessage.style.color = color;
    }

    function hideAlert() {
        alertMessage.classList.remove("show");
    }

    function checkSamePassword() {
        if (passwordInput.value != passwordConfirmInput.value) {
            showAlert("Password does not match, please check again!", "red");
            passwordConfirmInput.setCustomValidity(
                "Password does not match, please check again!"
            );
            return false;
        } else {
            hideAlert();
            passwordConfirmInput.setCustomValidity("");
            return true;
        }
    }

    passwordConfirmInput.addEventListener(
        "input",
        debounce(checkSamePassword, 200)
    );
    passwordInput.addEventListener("blur", checkSamePassword);
    passwordConfirmInput.addEventListener("focus", checkSamePassword);

    async function signUp(e) {
        e.preventDefault();

        if (!checkSamePassword()) return;

        showAlert("Please wait for response...", "green");
        let res = await fetch("/api/v1/users/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: nameInput.value,
                username: usernameInput.value,
                password: passwordInput.value,
            }),
        });

        if (res.status === 201) {
            res = await res.json();
            window.setTimeout(function(){
                window.location.href = "../sign-in";
            }, 1000);
            showAlert(res.mes, "green");
        } else {
            res = await res.json();
            showAlert(res.mes, "red");
        }
    }

    signUpForm.addEventListener("submit", signUp);
});
