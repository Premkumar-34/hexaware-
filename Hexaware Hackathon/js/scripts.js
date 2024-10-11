document.querySelector('button a[href="login.html"]').addEventListener('click', function(event) {
    event.preventDefault();  
    var userType = prompt("Please enter 'Candidate' or 'HR' to proceed with login:");
    userType = userType ? userType.toLowerCase() : '';
    if (userType === 'candidate') {
        window.location.href = 'Candidate-Login.html'; 
    } else if (userType === 'hr') {
        window.location.href = 'HR-Login.html'; 
    } else {
        alert("Invalid input! Please enter either 'Candidate' or 'HR'.");
    }
});