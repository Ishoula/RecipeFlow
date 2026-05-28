# RecipeFlow

A full-stack web application for sharing, discovering, and managing recipes. RecipeFlow combines a Spring Boot REST API backend with a modern Next.js frontend, providing users with a seamless experience for recipe creation, browsing, and interaction.

## 🌟 Features

- **User Authentication**: Secure JWT-based authentication with registration and login
- **Recipe Management**: Create, read, update, and delete recipes with rich details
- **Social Interactions**: Like and dislike recipes to build a personalized feed
- **User Profiles**: Manage user information and view recipe collections
- **RESTful API**: Well-documented REST endpoints for all operations
- **Responsive UI**: Modern, responsive design built with Next.js and Tailwind CSS

## 🛠️ Tech Stack

### Backend

- **Java 21** with Spring Boot 4.0.6
- **Spring Data JPA** for database operations
- **PostgreSQL** for data persistence
- **JWT (JSON Web Tokens)** for authentication
- **Maven** for build management

### Frontend

- **Next.js 14** (TypeScript)
- **Tailwind CSS** for styling
- **Zustand** for state management
- **Axios** for HTTP requests

## 📁 Project Structure

```
recipeflow/
├── src/
│   ├── main/
│   │   ├── java/com/food/recipeflow/
│   │   │   ├── config/          # Spring configuration (Security, etc.)
│   │   │   ├── controller/      # REST endpoints
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities (User, Recipe)
│   │   │   ├── repository/      # Data access layer
│   │   │   ├── security/        # JWT and authentication logic
│   │   │   └── service/         # Business logic
│   │   └── resources/           # Configuration files
│   └── test/                    # Unit tests
├── pom.xml                      # Maven configuration
└── README.md
```

## 📋 Prerequisites

### Backend

- Java 21 JDK
- Maven 3.8+
- PostgreSQL 12+ running on localhost:5432

### Frontend

- Node.js 18+ and npm/yarn
- Git

## 🚀 Getting Started

### Backend Setup

1. **Clone the repository** (if applicable)

   ```bash
   git clone <repository-url>
   cd recipeflow
   ```

2. **Create PostgreSQL database**

   ```sql
   CREATE DATABASE recipeflow;
   ```

3. **Configure database credentials** (if different from defaults)
   - Edit `src/main/resources/application.properties`
   - Default: username `postgres`, password `admin`

4. **Build the project**

   ```bash
   mvn clean install
   ```

5. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:1027`

### Frontend Setup

1. **Navigate to frontend directory**

   ```bash
   cd ../recipeflow-frontend
   ```

2. **Install dependencies**

   ```bash
   npm install
   ```

3. **Run development server**

   ```bash
   npm run dev
   ```

   The frontend will be available at `http://localhost:3000`

## 📡 API Endpoints

### Authentication

- `POST /api/v1/auth/register` - Register a new user
- `POST /api/v1/auth/login` - Login and receive JWT token

### Recipes

- `GET /api/recipes` - Get all recipes
- `GET /api/recipes/{id}` - Get recipe details
- `POST /api/recipes` - Create a new recipe (authenticated)
- `PUT /api/recipes/{id}` - Update a recipe (authenticated)
- `DELETE /api/recipes/{id}` - Delete a recipe (authenticated)
- `POST /api/recipes/{id}/like` - Like a recipe (authenticated)
- `POST /api/recipes/{id}/dislike` - Dislike a recipe (authenticated)

### Users

- `GET /api/users/{id}` - Get user profile
- `PUT /api/users/{id}` - Update user profile (authenticated)

## 🔐 Authentication

The application uses JWT (JSON Web Tokens) for secure authentication:

1. User registers or logs in via `/api/v1/auth/register` or `/api/v1/auth/login`
2. Backend returns a JWT token
3. Client stores the token and includes it in the `Authorization: Bearer <token>` header for authenticated requests
4. Server validates the token via the `JwtAuthFilter`

## 📝 Configuration

Key configuration options in `application.properties`:

- `spring.datasource.url` - PostgreSQL connection URL
- `spring.datasource.username` - Database username
- `spring.datasource.password` - Database password
- `server.port` - Server port (default: 1027)
- `spring.jpa.hibernate.ddl-auto` - Database schema update strategy

## 🧪 Testing

Run tests with Maven:

```bash
mvn test
```

## 📦 Building for Production

### Backend

```bash
mvn clean package
java -jar target/recipeflow-0.0.1-SNAPSHOT.jar
```

### Frontend

```bash
npm run build
npm start
```

## 🤝 Contributing

1. Create a feature branch (`git checkout -b feature/amazing-feature`)
2. Commit your changes (`git commit -m 'Add amazing feature'`)
3. Push to the branch (`git push origin feature/amazing-feature`)
4. Open a Pull Request

## 📄 License

This project is open source and available under the MIT License.

## 💬 Support

For issues, questions, or suggestions, please open an issue in the repository.

---

**Happy Recipe Sharing! 🍳**
