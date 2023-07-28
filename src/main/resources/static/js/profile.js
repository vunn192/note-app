window.addEventListener("DOMContentLoaded", (event) => {
    const logoutBtn = document.getElementById("log-out-btn");

    async function logout() {
        let response = await fetch("api/v1/auth/sign-out", {
            method: "DELETE",
        });
        if (response.status === 200) window.location.href = "../sign-in";
    }

    logoutBtn.addEventListener("click", logout);
});
