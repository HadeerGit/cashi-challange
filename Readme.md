# Fees Workflow API

## Description
This project implements a RESTful API for a fees workflow using Kotlin services to calculate, charge, and record fees for transactions submitted to the public API endpoint. The workflow is orchestrated using Apache Airflow.

## Installation
1. Clone the repository.
2. Install dependencies using `gradle build`.
3. Run the application using `gradle run`.

## Usage
- Endpoint: `/transaction/fee`
- Sample Request:
{
"transaction_id": "txn_001",
"amount": 1000,
"asset": "USD",
"asset_type": "FIAT",
"type": "Mobile Top Up",
"state": "SETTLED - PENDING FEE",
"created_at": "2023-08-30
15:42:17.610059"
}

- Sample Response:
{
"transaction_id": "txn_001",
"amount": 1000,
"asset": "USD",
"type": "Mobile Top Up",
"fee": 1.5,
"rate": 0.0015,
"description": "Standard fee rate of 0.15%",
}


services build
cd services
kotlinc fees-calculator/src/main/kotlin/Main.kt -include-runtime -d ./builds/fees-calculator.jar
kotlinc fees-charger/src/main/kotlin/Main.kt -include-runtime -d ./builds/fees-charger.jar
kotlinc fees-recorder/src/main/kotlin/Main.kt -include-runtime -d ./builds/fees-recorder.jar
