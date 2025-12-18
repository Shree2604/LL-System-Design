# Low-Level System Design

## 📚 Overview
This repository is a comprehensive collection of Low-Level System Design patterns, case studies, and best practices. It's designed to help you prepare for system design interviews and build robust, scalable systems in real-world scenarios.

## 🎯 Purpose
- Understand fundamental system design concepts and patterns
- Learn through practical case studies of popular systems
- Prepare for system design interviews with real-world examples
- Implement best practices for building scalable and maintainable systems

## 📂 Repository Structure
```mermaid
graph TD
    %% Main Repository Structure
    A[LL-System-Design] --> B[Video-Player/]
    A --> C[Day01/]
    A --> D[Day02/]
    A --> E[...]
    A --> F[Day15/]
    
    %% Video-Player Structure
    B --> B1[Code/]
    B1 --> B11[Java File]
    B1 --> B12[README.md]
    B --> B2[Video-Player.pdf]
    
    %% Day01 Structure
    C --> C1[Code/]
    C1 --> C11[Polymorphism/]
    C11 --> C111[Java File]
    C11 --> C112[README.md]
    C1 --> C12[SOLID Principles/]
    C12 --> C121[Java File]
    C12 --> C122[README.md]
    C --> C2[Day01.pdf]
    
    %% Styling
    classDef folder fill:#2b2b2b,stroke:#4a4a4a,color:#e0e0e0,stroke-width:2px
    classDef file fill:#1e3a8a,stroke:#3b82f6,color:#e0e0e0,stroke-width:2px
    classDef doc fill:#1e3a1e,stroke:#10b981,color:#e0e0e0,stroke-width:2px
    
    class A,C1,C11,C12,B1 folder
    class B11,B12,C111,C112,C121,C122 file
    class B2,C2 doc
```

## 📅 Daily Progress

```mermaid
gantt
    title Project Timeline
    dateFormat  YYYY-MM-DD
    axisFormat %b %d
    
    section Completed
    Video Player     :done, des1, 2025-12-15, 3d
    OOPs & SOLID     :done, des2, 2025-12-18, 2d
    
    section In Progress
    System Design Basics :active, des3, 2025-12-19, 5d
    
    section Upcoming
    Advanced Patterns    : des4, after des3, 5d
    Real-world Systems   : des5, after des4, 5d
    
    %% Styling - Note: Gantt charts have limited styling in Mermaid
    %% We'll use task states for theming
    class des1,des2 done
    class des3 active
    class des4,des5 critical
```

| Day | Topic | Status |
|-----|-------|--------|
| 00  | [Basic Video Player System Design](#) | ✅ Completed |
| 01  | [OOPs & SOLID Principles](#) | ✅ Completed |

## 🚀 Getting Started

```mermaid
flowchart TD
    A[Clone Repository] --> B[Explore Patterns]
    B --> C[Study Implementations]
    C --> D[Try Your Own Solutions]
    D --> E[Compare & Learn]
    
    %% Styling
    classDef step fill:#2b2b2b,stroke:#4a4a4a,color:#e0e0e0,stroke-width:2px
    class A,B,C,D,E step
```

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/LL-System-Design.git
   cd LL-System-Design
   ```

2. **Explore different design patterns** and case studies
3. **Study the implementation details** in the code
4. **Try to implement your own solutions** before looking at the provided ones
5. **Compare** your solutions with the provided implementations

## 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
