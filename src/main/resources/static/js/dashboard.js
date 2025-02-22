async function fetchRecentContacts() {
  try {
    const response = await fetch("/api/contacts/recent");
    const contacts = await response.json();

    const activityList = document.getElementById("recentActivity");
    activityList.innerHTML = ""; // Clear existing content

    if (contacts.length === 0) {
      activityList.innerHTML =
        "<li class='text-gray-500 dark:text-gray-300 text-center'>No recent activity</li>";
      return;
    }

    contacts.forEach((contact) => {
      const listItem = document.createElement("li");
      listItem.className =
        "px-2 py-1 bg-gray-100 dark:bg-gray-700 rounded-md shadow-sm text-sm";
      listItem.innerHTML = `<strong>${
        contact.name
      }</strong> added on ${new Date(contact.createdAt).toLocaleDateString()}`;
      activityList.appendChild(listItem);
    });
  } catch (error) {
    console.error("Error fetching recent contacts:", error);
  }
}

fetchRecentContacts();
