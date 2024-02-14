from airflow import DAG
from datetime import datetime
from airflow.operators.python_operator import PythonOperator
import requests

def calculate_fee(**kwargs):
    # Make HTTP request to calculate_fee endpoint in Ktor
    response = requests.post('http://host.docker.internal:8070/transaction/calculate-fee', json={
        "transaction_id": "txn_001",
        "amount": 1000,
        "asset": "USD",
        "asset_type": "FIAT",
        "type": "Mobile Top Up",
        "state": "SETTLED - PENDING FEE",
        "created_at": "2023-08-30 15:42:17.610059"
    })

    if response.status_code == 200:
        
        return response.json()
    else:
        # Handle error case here
        return {"error": "Failed to calculate fee"}


def charge_fee(**kwargs):
    # Make HTTP request to charge_fee endpoint in Ktor
    response = requests.post('http://host.docker.internal:8070/transaction/charge-fee', json={
        "transaction_id": "txn_001",
        "fee": 1.5
    })
    if response.status_code == 200:
        return response.json()
    else:
        # Handle error case here
        return {"error": "Failed to calculate fee"}


def record_fee(**kwargs):
    # Make HTTP request to record_transaction endpoint in Ktor
    response = requests.post('http://host.docker.internal:8070/transaction/record-fee', json={
        "transaction_id": "txn_001",
        "fee": 1.5
    })
    if response.status_code == 200:
        return response.json()
    else:
        # Handle error case here
        return {"error": "Failed to calculate fee"}


default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'start_date': datetime(2023, 8, 30),
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
    'schedule_interval': None,
}

dag = DAG('fee_workflow', default_args=default_args)


calculate_fee_task = PythonOperator(
    task_id='calculate_fee',
    python_callable=calculate_fee,
    provide_context=True,
    dag=dag
)

charge_fee_task = PythonOperator(
    task_id='charge_fee',
    python_callable=charge_fee,
    provide_context=True,
    dag=dag
)

record_fee_task = PythonOperator(
    task_id='record_fee',
    python_callable=record_fee,
    provide_context=True,
    dag=dag
)

calculate_fee_task >> charge_fee_task >> record_fee_task
