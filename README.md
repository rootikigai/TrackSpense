# TrackSpense

TrackSpense is a Full-Stack Expense Tracker Application.  
It allows multiple users to register, log in, and track their expenses across categories with a simple and clean web interface.

---

## Features

### User
- Register & Login (with password hashing)  
- Secure user data stored in MongoDB  

### Expenses
- Add, Edit, Delete Expenses  
- Categorize expenses (Food, Transport, Bills, etc.)  
- View all expenses per user  
- Filter expenses by category  
- Expenses stored in MongoDB and retrieved via REST APIs

### Frontend (In progress)
- Simple HTML, CSS, JavaScript interface  
- Responsive design for desktop & mobile  
- Communicates with backend via REST APIs  

### Development
- Built with Test-Driven Development (TDD)  
- Uses Layered Architecture (Model → Repository → Service → Controller → Frontend)  
- Tested with JUnit and Postman  

---

## Tech Stack
- **Backend:** Java 21, Spring Boot 3.3.3  
- **Database:** MongoDB  
- **Build Tool:** Maven  
- **Security:** BCrypt (password hashing)  
- **Testing:** JUnit, TDD  
- **Frontend:** HTML, CSS, JavaScript  
- **API Testing:** Postman
