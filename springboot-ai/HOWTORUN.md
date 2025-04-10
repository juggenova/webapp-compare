# Doodle Clone - Spring Boot + React + Tailwind CSS

This is a Doodle clone implementation for the JUG Genova webapp-compare project.

## Project Structure

- `src/` - Spring Boot backend
- `frontend/` - React frontend with Tailwind CSS

## Running the Application

### Backend

#### On Windows

To run the Spring Boot backend on Windows:

```
mvnw.cmd spring-boot:run
```

#### On macOS/Linux

To run the Spring Boot backend on macOS/Linux:

```bash
./mvnw spring-boot:run
```

The backend will start on http://localhost:8080

### Frontend

To run the React frontend (same commands for all operating systems):

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will start on http://localhost:3000

## Features

This application implements a Doodle clone with the following features (as per the requirements):

1. View a poll for the first week of November 2024
2. Poll attributes (title, description, date range, voting deadline)
3. Preference storage
4. Server-side storage of user preferences
5. Automatic selection of the most voted day after deadline
6. Post-deadline view showing the selected day
7. Login functionality
8. Email notifications
9. Internationalization (Italian/English)
10. Responsive design
11. User registration
12. Admin dashboard
