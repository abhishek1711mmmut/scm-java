console.log("Script loaded");

let currentTheme = getTheme();
// initial -->
document.addEventListener("DOMContentLoaded", () => {
  changeTheme();
});

function changeTheme() {
  // set to web page
  document.querySelector("html").classList.add(currentTheme);

  // set the listener to change theme button
  const changeThemeButton = document.querySelector("#theme_change_btn");
  changeThemeButton.querySelector("span").textContent =
    currentTheme == "light" ? "Dark" : "Light";
  changeThemeButton.addEventListener("click", (event) => {
    const oldTheme = currentTheme;
    console.log("Theme change button clicked");
    if (currentTheme == "dark") {
      currentTheme = "light";
    } else {
      currentTheme = "dark";
    }
    // localstorage mein update kr do
    setTheme(currentTheme);
    // remove the current theme
    document.querySelector("html").classList.remove(oldTheme);
    // add the current theme
    document.querySelector("html").classList.add(currentTheme);
    // change the text of button
    changeThemeButton.querySelector("span").textContent =
      currentTheme == "light" ? "Dark" : "Light";
  });
}

// set theme to localstorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// get theme from localstorage
function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}
