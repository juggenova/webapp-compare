# How to Run the Doodle Clone Application

This document provides instructions for running the Doodle Clone application, which consists of a Spring Boot backend and a React frontend with Tailwind CSS.

## Prerequisites

- Java 17 or higher
- Node.js and npm
- Maven (optional, as the project includes Maven wrapper)

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

## API Endpoints

The backend provides the following REST API endpoints:

- `GET /api/polls/default` - Get the default poll with its votes
- `POST /api/polls/votes/{voteId}?choice={choice}` - Update a vote
- `GET /api/polls/choices` - Get all possible choice values

## Current Implementation

The application currently implements:

1. Basic poll viewing for the first week of November 2024
2. Poll attributes (title, description, date range, voting deadline)

Future versions will implement the remaining requirements:

3. Preference storage
4. Server-side storage of user preferences
5. Automatic selection of the most voted day after deadline
6. Post-deadline view showing the selected day
7. Login functionality
8. Email notifications
9. Internationalization (Italian/English)
10. Responsive design (already partially implemented)
11. User registration
12. Admin dashboard

## Troubleshooting

- If you encounter issues with the backend, check that port 8080 is available
- If you encounter issues with the frontend, check that port 3000 is available
- Make sure both the backend and frontend are running simultaneously for the application to work properly
