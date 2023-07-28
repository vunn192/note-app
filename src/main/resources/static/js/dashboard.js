import Pagination from "./pagination.js";

window.addEventListener("DOMContentLoaded", (event) => {
    const itemPerPage = 5;
    let currentPage = 0;

    const createForm = document.getElementById("create-form");
    const createContent = document.getElementById("create-content");
    const createName = document.getElementById("create-name");
    const noteList = document.getElementById("note-list");

    const logoutBtn = document.getElementById("log-out-btn");

    function sanitizeHTML(str) {
        return str.replace(/[\u00A0-\u9999<>\&]/g, function (i) {
            return "&#" + i.charCodeAt(0) + ";";
        });
    }

    async function loadDataPage(page) {
        const params = new Proxy(new URLSearchParams(window.location.search), {
            get: (searchParams, prop) => searchParams.get(prop),
        });
        return await fetch(
            `/api/v1/notes?page=${page}&limit=${itemPerPage}${
                params.search ? `&search=${params.search}` : ""
            }`,
            {
                method: "GET",
            }
        ).then((dt) => dt.json());
    }

    async function logout() {
        let response = await fetch("api/v1/auth/sign-out", {
            method: "DELETE",
        });
        if (response.status === 200) window.location.href = "../sign-in";
    }

    function addNoteToList(id, content, name) {
        let li = document.createElement("li");
        li.classList.add("note-item");
        let sanitizedName = sanitizeHTML(name);
        let sanitizedContent = sanitizeHTML(content);
        let liInside = `
            <p class="note-name" title="${sanitizedName}">${sanitizedName}</p>
            <p class="note-content" title="${sanitizedContent}">${sanitizedContent}</p>
            <div class="note-button">
                <i class="fa-solid fa-trash"></i>
                <i class="fa-solid fa-copy"></i>
            </div>
        `;
        li.dataset.id = id;
        li.dataset.content = content;
        li.innerHTML = liInside;
        const removeBtn = li.querySelector(".fa-trash");
        const copyBtn = li.querySelector(".fa-copy");
        copyBtn.addEventListener("click", function (e) {
            navigator.clipboard.writeText(
                this.parentElement.parentElement.dataset.content
            );
        });
        removeBtn.addEventListener("click", async function (e) {
            let res = await fetch(
                `api/v1/notes/${this.parentElement.parentElement.dataset.id}`,
                {
                    method: "DELETE",
                }
            );
            if (res.status === 200) location.reload();
            else {
                res = await res.json();
                alert(res.error);
            }
        });
        noteList.insertAdjacentElement("beforeend", li);
    }

    async function initialize() {
        let res = await loadDataPage(currentPage);
        if (res.error) {
            alert(res.error);
            return;
        }

        let notes = res.data.notes;
        let count = res.data.total;
        for (let note of notes) {
            addNoteToList(note.id, note.content, note.name);
        }
        let pagination = new Pagination({
            paginationSelector: "main .pagination",
            onPageChange: async (page) => {
                if (page === currentPage) return;
                currentPage = page;

                let res = await loadDataPage(page);
                if (res.error) {
                    alert(res.error);
                    return;
                }
                let notes = res.data.notes;
                noteList.innerHTML = "";
                for (let note of notes) {
                    addNoteToList(note.id, note.content, note.name);
                }
            },
            pageSize: itemPerPage,
            totalItems: count,
        });
    }

    async function createNote(e) {
        e.preventDefault();
        let res = await fetch("/api/v1/notes", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                content: createContent.value,
                name: createName.value,
            }),
        });
        if (res.status === 201) location.reload();
        else {
            res = await res.json();
            alert(res.error);
        }
    }

    createForm.addEventListener("submit", createNote);
    logoutBtn.addEventListener("click", logout);

    initialize();
});