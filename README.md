# Todo-app
Todo-app van Edwin Abbasian
## Getting Started
1. Build board-service:
```
cd board-service
mvn clean install -DskipTests
```

2. Build board-service:
```
cd user-service
mvn clean install -DskipTests
```

3. Build and Run the Backend with Docker Compose:
```
docker-compose up --build
```

4. Build and Run the Frontend:
```
cd frontend
npm install
npm run build
npm start
```

5. Open the Frontend in your browser:
```
http://localhost:3000
```