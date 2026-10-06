document.addEventListener('click', async (event) => {
    const link = event.target.closest('.report-link');
    if (!link) {
        return;
    }

    event.preventDefault();
    const targetId = link.dataset.target;
    const target = document.getElementById(targetId);
    if (!target) {
        window.location.href = link.href;
        return;
    }

    const response = await fetch(link.href, {
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        },
        credentials: 'same-origin'
    });

    if (!response.ok) {
        window.location.href = link.href;
        return;
    }

    const html = await response.text();
    const parser = new DOMParser();
    const doc = parser.parseFromString(html, 'text/html');
    const nextBody = doc.getElementById('report-body');
    target.innerHTML = nextBody ? nextBody.innerHTML : html;
});
