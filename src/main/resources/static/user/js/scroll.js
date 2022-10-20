const scrollButton = document.querySelector(".scroll-button");

// when the user scrolled then show the button otherwise hide it
window.addEventListener("scroll", () => {
  window.pageYOffset > 100
    ? scrollButton.classList.add("show-btn")
    : scrollButton.classList.remove("show-btn");
});

// when user click the button take him to the top with smooth behavior
scrollButton.addEventListener("click", () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth" // for smoothly scrolling
  });
});
