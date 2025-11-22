package andrey.dev.worktelegrambot.enums;

import lombok.Getter;

@Getter
public enum TechStack {
    JAVA("Java"),
    PYTHON("Python"),
    JAVASCRIPT("JavaScript"),
    TYPESCRIPT("TypeScript"),
    C_SHARP("C#"),
    CPP("C++"),
    GO("Go"),
    RUST("Rust"),
    KOTLIN("Kotlin"),
    SWIFT("Swift"),
    PHP("PHP"),
    RUBY("Ruby"),
    SQL("SQL"),
    DART("Dart"),

    SPRING("Spring Framework"),
    REACT("React"),
    DOCKER("Docker"),
    KUBERNETES("Kubernetes"),
    AWS("Amazon Web Services"),
    NODE_JS("Node.js"),
    ANGULAR("Angular"),
    FLUTTER("Flutter"),
    POSTGRESQL("PostgreSQL"),
    MONGO_DB("MongoDB");

    private final String name;

    TechStack(String name) {
        this.name = name;
    }
}
