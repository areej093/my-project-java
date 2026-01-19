Tunisian Agricultural Export Price Prediction and Market Intelligence System

📋 Project Overview
A comprehensive Java 21+ application implementing Object-Oriented Programming principles with AI/ML integration for predicting Tunisian agricultural export prices. The system processes real FAO data, converts prices to Tunisian Dinar (TND), and provides interactive market intelligence through an advanced dashboard.

**Course: Object-Oriented Programming  
**Institution:Sesame University  
**Submission Deadline:January 15, 2026  
**Author:BEN HAMOUDA ARIJ

## 🚀 Key Features

### 1. **AI/ML Integration**
- **DJL (Deep Java Library):** Price prediction models
- **ONNX Runtime:** Cross-platform model inference
- **LLM Integration:** Market intelligence report generation
- **Multiple AI Models:** Ensemble approach for better accuracy

### 2. **Data Processing Pipeline**
- **Real FAO Data:** FAOSTAT dataset (2000-2023)
- **Currency Conversion:** USD to Tunisian Dinar (1 USD = 3.1 TND)
- **Data Cleaning & Transformation:** Comprehensive ETL process
- **Synthetic Data Generation:** For testing and demonstration

### 3. **Interactive Dashboard** (4 Complex Functionalities)
- **Interactive Filtering:** By product, confidence, date range
- **Multiple Visualizations:** Bar, Line, Pie, Area charts
- **Report Generation:** Market intelligence, executive summaries
- **Data Export:** CSV, JSON, XML formats with TND prices

### 4. **OOP Principles Demonstrated**
- **Inheritance:** `BaseAIModel` → `DJLPredictionService`, `ONNXRuntimeService`
- **Polymorphism:** `PredictionService` interface implementation
- **Encapsulation:** Records, private fields, getters/setters
- **Design Patterns:** MVC, Strategy, Factory, Observer
- **Java 21+ Features:** Records, Sealed Classes, Pattern Matching

## 📁 Project Structure
src/main/java/tn/isg/economics/
├── ai/ # AI Model Implementations
│ ├── BaseAIModel.java # Abstract base class
│ ├── DJLPredictionService.java # DJL integration
│ ├── ONNXRuntimeService.java # ONNX Runtime integration
│ └── LLMReportService.java # LLM report generation
├── dashboard/ # MVC Dashboard Components
│ ├── AgriculturalDashboard.java # Main dashboard GUI
│ ├── DashboardMain.java # Advanced dashboard launcher
│ ├── controller/ # MVC Controllers
│ ├── view/ # GUI Views
│ ├── service/ # Business Logic
│ ├── components/ # UI Components
│ └── util/ # Chart utilities
├── data/ # Data Processing
│ ├── loader/ # Data loading
│ ├── cleaner/ # Data cleaning
│ ├── generator/ # Synthetic data
│ └── transformer/ # FAO data transformation
├── model/ # Domain Models (Records)
│ ├── ExportData.java # Export data record
│ ├── PricePrediction.java # Prediction record
│ ├── ProductType.java # Enum: OLIVE_OIL, DATES, etc.
│ ├── MarketIndicator.java # Enum: RISING, STABLE, etc.
│ └── PredictionStatus.java # Enum: COMPLETED, FAILED, etc.
├── service/ # Business Services
│ ├── PredictionService.java # Interface
│ ├── ReportGenerator.java # Interface
│ ├── EconomicIntelligenceService.java # Main service
│ └── DataTransformer.java # Functional interface
├── exception/ # Custom Exceptions
│ ├── EconomicIntelligenceException.java
│ ├── ModelException.java
│ └── PredictionException.java
├── util/ # Utilities
│ └── ConfigLoader.java # Configuration management
└── annotation/ # Custom Annotations
└── AIService.java @AIService annotation

datasets/ # Data Files
├── raw/
│ └── FAOSTAT_data_en_12-20-2025.csv # Real FAO data
└── synthetic/
└── tunisia_exports_full.csv # Generated data

src/main/resources/ # Configuration
├── config/
│ ├── application.properties # App configuration
│ └── model-config.json # AI model configuration

text

## 🛠️ Technical Stack

### **Core Technologies**
- **Java 21+**: Records, Sealed Classes, Pattern Matching
- **Maven 3.9+**: Project management and dependencies
- **Swing/AWT**: GUI framework for dashboard

### **AI/ML Libraries**
- **DJL 0.25.0**: Deep learning in Java
- **ONNX Runtime 1.16.0**: Cross-platform inference
- **LangChain4j 0.29.1**: LLM integration framework

### **Data Processing**
- **Apache Commons CSV 1.10.0**: CSV parsing
- **Jackson 2.15.2**: JSON processing
- **SLF4J 2.0.9**: Logging framework
- **Lombok 1.18.30**: Code generation

## 🚀 Getting Started

### **Prerequisites**
1. **Java Development Kit (JDK) 21+**
   ```bash
   java --version
   # Should show: Java version 21+
Apache Maven 3.9+

bash
mvn --version
# Should show: Apache Maven 3.9+
Setup & Compilation
Clone/Download the project

bash
cd "D:\java\tunisian-agri-project\Tunisian Agricultural Project"
Compile the project

bash
mvn clean compile
Run tests

bash
# Test FAO data transformation
mvn compile exec:java -Dexec.mainClass="tn.isg.economics.TestFAOData"

# Test data pipeline
mvn compile exec:java -Dexec.mainClass="tn.isg.economics.DataPipelineTest"
Running the Dashboard
Option 1: Simple Dashboard (Recommended)
bash
java -cp "target/classes;C:/Users/HP/.m2/repository/org/apache/commons/commons-csv/1.10.0/commons-csv-1.10.0.jar;C:/Users/HP/.m2/repository/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar;C:/Users/HP/.m2/repository/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar" tn.isg.economics.dashboard.AgriculturalDashboard
Option 2: Advanced Dashboard with FAO Data
bash
java -cp "target/classes;C:/Users/HP/.m2/repository/org/apache/commons/commons-csv/1.10.0/commons-csv-1.10.0.jar;C:/Users/HP/.m2/repository/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar;C:/Users/HP/.m2/repository/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar" tn.isg.economics.dashboard.DashboardMain
🎯 Dashboard Features
1. Overview Tab
   Real-time Metrics: Total exports, average price, top products

Currency: All prices in Tunisian Dinar (TND)

Charts: Product distribution, country exports

Data Table: Recent exports with TND prices

2. Analytics Tab
   Interactive Charts: Switch between Bar, Line, Pie, Area

Data Views: By Product, Country, Year, Month

Statistics: Mean, median, standard deviation in TND

Real-time Updates: Dynamic data loading

3. Reports Tab
   Market Intelligence: Comprehensive market analysis

Executive Summary: Key metrics and insights

Product Analysis: Detailed product performance

Country Report: Export destination analysis

Export Formats: PDF, CSV, Text

4. Export Tab
   Multiple Formats: CSV, JSON, XML

Filtering Options: By product, date range, fields

Preview: Real-time export preview

Currency: All exports in Tunisian Dinar (TND)

📊 Data Processing
FAO Data Transformation
text
FAOSTAT CSV (USD)
↓
Data Cleaning & Parsing
↓
Currency Conversion (USD → TND)
↓
Monthly Data Generation
↓
ExportData Objects (TND)
Currency Conversion
Base Currency: Tunisian Dinar (TND)

Exchange Rate: 1 USD = 3.1 TND

All Calculations: Done in TND

Display: Prices formatted as 12,825.51 TND

Sample Data
csv
date,product,price_per_ton_TND,volume_ton,destination_country,status
2024-12-15,Olive Oil,11825.51,150.0,France,Shipped
2024-12-10,Dates,7947.41,85.0,Germany,Pending
2024-12-05,Citrus,3976.28,200.0,Italy,Delivered
🔧 Configuration
application.properties
properties
# App Configuration
app.name=Tunisian Agricultural Export AI
app.version=1.0.0
app.environment=development

# Data Configuration
data.synthetic.enabled=true
data.export.csv.path=data/datasets/raw/exports.csv

# AI Configuration
ai.model.default=djl-lstm
llm.provider=ollama
llm.ollama.base.url=http://localhost:11434

# Currency
currency.base=TND
currency.exchange_rate.usd_to_tnd=3.1

🧪 Testing
Unit Tests
java
// Test data transformation
TestFAOData.main()      // Tests FAO → TND conversion
DataPipelineTest.main() // Tests complete data pipeline
TestConfigLoader.main() // Tests configuration loading
Integration Tests
Dashboard GUI functionality

Export feature (creates actual files)

Report generation

Chart rendering

Manual Tests
Export Test: Export data to CSV, verify file creation

Chart Test: Switch between chart types

Filter Test: Apply product/country filters

Report Test: Generate all report types

📈 Performance Metrics
AI Model Performance
DJL Model Accuracy: 82%

ONNX Runtime Accuracy: 78%

Average Confidence: 75-85%

Prediction Time: < 100ms per record

Data Processing
FAO Records Processed: 87 product-year combinations

Monthly Data Generated: 1,044 records (12 months × 87)

Currency Conversion: All prices in TND

Export Speed: < 2 seconds for 100 records

Dashboard Performance
GUI Load Time: < 3 seconds

Chart Rendering: < 1 second

Export Generation: < 500ms

Memory Usage: < 512MB

🔒 Security & Error Handling
Exception Handling
EconomicIntelligenceException - Base exception

ModelException - AI model errors

PredictionException - Prediction errors

IllegalArgumentException - Invalid inputs

Data Validation
Price validation (non-negative)

Date validation (not in future)

Volume validation (positive)

Country validation (non-empty)

File Operations
Safe file writing with try-with-resources

File existence checks before reading

Proper file extension handling

Error messages for failed operations

🌟 Unique Features
Tunisian Dinar Focus: All calculations in local currency

Real FAO Data: Actual Tunisian export statistics

Multiple AI Integration: DJL + ONNX Runtime + LLM

Comprehensive Dashboard: 4 complex functionalities

Export Flexibility: CSV, JSON, XML formats

Interactive Visualizations: Real-time chart updates

Professional Reports: Market intelligence in TND

📞 Support & Contact
For questions about this project submission:
Course: Object-Oriented Programming
Instructor: Chaouki Bayoudhi
Email: chaouki.bayoudhi@sesame.com.tn
Deadline: January 15, 2026