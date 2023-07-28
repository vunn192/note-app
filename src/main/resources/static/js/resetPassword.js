import { debounce } from "./utils.js";

window.addEventListener("DOMContentLoaded", (event) => {
    const resetPasswordForm = document.getElementById("reset-password-form");
    const passwordInput = document.getElementById("password-input");
    const passwordConfirmInput = document.getElementById(
        "confirm-password-input"
    );
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

    async function resetPassword(e) {
        e.preventDefault();

        if (!checkSamePassword()) return;

        showAlert("Please wait for response...", "green");
        let res = await fetch("/api/v1/users/reset-password", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                password: passwordInput.value,
            }),
        });

        if (res.status === 200) {
            res = await res.json();
            showAlert(res.mes, "green");
            window.setTimeout(function(){
                window.location.href = "../sign-in";
            }, 1000);
        } else {
            res = await res.json();
            showAlert(res.mes, "red");
        }
    }

    resetPasswordForm.addEventListener("submit", resetPassword);
});
