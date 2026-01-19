# Tunisian Agricultural Export Price Prediction and Market Intelligence System

## Project Overview
Java 21+ application implementing OOP principles with AI/ML integration for predicting Tunisian agricultural export prices.

## Key Features
1. **AI/ML Integration**: DJL and ONNX Runtime for price prediction
2. **LLM Report Generation**: Market intelligence reports
3. **Interactive Dashboard**: 4 complex functionalities
4. **Real FAO Data**: Tunisian Dinar conversion
5. **Complete Data Pipeline**: ETL processing

## Project Structure
\\\
src/main/java/tn/isg/economics/
├── ai/                          # AI model implementations
│   ├── DJLPredictionService.java
│   ├── ONNXRuntimeService.java
│   └── LLMReportService.java
├── dashboard/                   # MVC dashboard
│   ├── controller/
│   ├── view/
│   ├── service/
│   └── components/
├── data/                        # Data processing
│   ├── loader/
│   ├── cleaner/
│   ├── generator/
│   └── transformer/
├── model/                       # Domain models
├── service/                     # Business logic
└── util/                        # Utilities
\\\

## Setup & Execution
1. **Prerequisites**: Java 21+, Maven 3.9+
2. **Compile**: \mvn clean compile\
3. **Run Simple Dashboard**: \java -cp target/classes tn.isg.economics.dashboard.AgriculturalDashboard\
4. **Run Advanced Dashboard**: \java -cp target/classes tn.isg.economics.dashboard.DashboardMain\

## Demo Video Content
- Project structure and compilation
- FAO data transformation to Tunisian Dinar
- AI model predictions (DJL & ONNX Runtime)
- LLM report generation
- Dashboard with 4 complex functionalities:
  1. Interactive filtering
  2. Multiple chart visualizations
  3. Report generation
  4. Data export in CSV/JSON/XML

## Submission Files
1. **Source Code**: Complete Maven project
2. **Datasets**: FAOSTAT CSV with synthetic data
3. **Demo Video**: 10-15 minute demonstration
4. **Documentation**: README and JavaDoc

## OOP Principles Demonstrated
- Inheritance (BaseAIModel)
- Polymorphism (PredictionService interface)
- Encapsulation (Records, private fields)
- Design Patterns (MVC, Strategy, Factory)
- Java 21+ Features (Records, Sealed Classes)

## Author
[Your Name]
Sesame University
Object-Oriented Programming Course
Submission: January 15, 2026
\\\
"@ | Out-File -FilePath ".\README.md" -Encoding UTF8

Write-Host "   README.md created successfully" -ForegroundColor Green

Write-Host ""
Write-Host "=== TEST COMPLETE ===" -ForegroundColor Cyan
Write-Host "✅ Project is READY for submission!" -ForegroundColor Green
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Record 10-15 minute demo video" -ForegroundColor White
Write-Host "2. Test export functionality in dashboard" -ForegroundColor White
Write-Host "3. Package for Moodle submission" -ForegroundColor White
Write-Host "4. Submit by January 15, 2026" -ForegroundColor White
