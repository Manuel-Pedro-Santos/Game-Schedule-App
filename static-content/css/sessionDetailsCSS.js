export function addSessionsDetailsCss(ulElem) {
    // Inline styles for testing
    ulElem.style.listStyleType = "none";
    ulElem.style.padding = "10px";
    ulElem.style.borderRadius = "8px";
    ulElem.style.boxShadow = "0 2px 4px rgba(0, 0, 0, 0.1)";
    ulElem.style.margin = "20px 0";

    ulElem.querySelectorAll("li").forEach((li, index) => {
        li.style.padding = "20px";
        li.style.color = "black"
        if (index % 2 === 0) {
            li.style.backgroundColor = "#f8f9fa";
        } else {
            li.style.backgroundColor = "#f0f0f0";
        }

        li.addEventListener("mouseover", () => {
            li.style.backgroundColor = "#e9ecef";
            li.style.cursor = "pointer";
        });

        li.addEventListener("mouseout", () => {
            if (index % 2 === 0) {
                li.style.backgroundColor = "#f8f9fa";
            } else {
                li.style.backgroundColor = "#f0f0f0";
            }
        });
    });
}



