window.addEventListener("DOMContentLoaded", (event) => {
    const signInForm = document.getElementById("sign-in-form");
    const usernameInput = document.getElementById("username-input");
    const passwordInput = document.getElementById("password-input");
    const alertMessage = document.querySelector(".alert-message");

    function showAlert(content, color) {
        alertMessage.innerHTML = content;
        alertMessage.classList.add("show");
        alertMessage.style.color = color;
    }

    async function signIn(e) {
        e.preventDefault();

        let res = await fetch("api/v1/auth/authenticate", {
            method: "POST",
            credentials: 'same-origin',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username: usernameInput.value,
                password: passwordInput.value,
            }),
        });
        if (res.status === 200) window.location.href = "../dashboard";
        else {
            res = await res.json();
            showAlert(res.mes, "red");
        }
    }

    signInForm.addEventListener("submit", signIn);
});
