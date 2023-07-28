window.addEventListener("DOMContentLoaded", (event) => {
    const forgotPasswordForm = document.getElementById("forgot-password-form");
    const emailInput = document.getElementById("email-input");
    const alertMessage = document.querySelector(".alert-message");
    function showAlert(content, color) {
        alertMessage.innerHTML = content;
        alertMessage.classList.add("show");
        alertMessage.style.color = color;
    }

    async function forgotPassword(e) {
        e.preventDefault();

        showAlert("Please wait for response...", "green");
        let res = await fetch("api/v1/users/forget-password", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username: emailInput.value,
            }),
        });
        if (res.status === 200) {
            res = await res.json();
            showAlert(res.mes, "green");
            window.setTimeout(function(){
                window.location.href = "../reset-password";
            }, 1000);
        } else {
            res = await res.json();
            showAlert(res.mes, "red");
        }
    }

    forgotPasswordForm.addEventListener("submit", forgotPassword);
});
