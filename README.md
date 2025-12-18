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

    %% Day02 Structure
    D --> D1[UML Diagram.pdf]
    D --> D2[Day02.pdf]
    
    %% Root Level Files
    A --> A1[README.md]
    A --> A2[LICENSE]
    
    %% Styling
    classDef folder fill:#2b2b2b,stroke:#4a4a4a,color:#e0e0e0,stroke-width:2px
    classDef file fill:#1e3a8a,stroke:#3b82f6,color:#e0e0e0,stroke-width:2px
    classDef doc fill:#1e3a1e,stroke:#10b981,color:#e0e0e0,stroke-width:2px
    classDef readme fill:#5b21b6,stroke:#8b5cf6,color:#e0e0e0,stroke-width:2px
    classDef license fill:#9d174d,stroke:#ec4899,color:#e0e0e0,stroke-width:2px
    
    class A,C1,C11,C12,B1 folder
    class B11,B12,C111,C112,C121,C122 file
    class B2,C2,D1,D2 doc
    class A1 readme
    class A2 license
```


| Day | Topic | 
|-----|-------|
| 00  | [Basic Video Player System Design](Video-Player/) |
| 01  | [OOPs & SOLID Principles](Day01/) |
| 02  | [UML Diagram & Types of Design Patterns](Day02/) |

## 🚀 Getting Started


1. **Clone the repository**
   ```bash
   git clone https://github.com/Shree2604/LL-System-Design.git
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
