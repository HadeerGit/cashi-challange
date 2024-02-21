from airflow import DAG
from datetime import datetime, timedelta
from airflow.operators.python_operator import PythonOperator
import requests

def calculate_fee(**kwargs):
    conf = kwargs['dag_run'].conf
    response = requests.post(
        'http://host.docker.internal:8070/transaction/calculate-fee',
         json= conf
    )
    if response.status_code == 200:
        response_json = response.json()
        fee = response_json.get('fee', None)
        if fee is not None:
            # If fee is successfully extracted, pass it to the next task
            kwargs['ti'].xcom_push(key='fee', value=fee)
            return response_json
        else:
            return {"error": "Fee not found in response"}
    else:
        # Handle error case here
        return {"error": "Failed to calculate fee"}

def charge_fee(**kwargs):
    # Retrieve fee from XCom
    conf = kwargs['dag_run'].conf
    transaction_id = conf.get('transaction_id', None)
    if transaction_id is None:
        return {"error": "Transaction ID not found in conf"}

    fee = kwargs['ti'].xcom_pull(task_ids='calculate_fee', key='fee')
    if fee is not None:
        # Make HTTP request to charge_fee endpoint in Ktor
        response = requests.post('http://host.docker.internal:8070/transaction/charge-fee', json={
            "transaction_id": transaction_id,
            "fee": fee
        })
        if response.status_code == 200:
            return  response.json()
        else:
            # Handle error case here
            return {"error": "Failed to charge fee"}
    else:
        return {"error": "Fee not found"}

def record_fee(**kwargs):
    conf = kwargs['dag_run'].conf
    conf['fee'] = kwargs['ti'].xcom_pull(task_ids='calculate_fee', key='fee')

# Make HTTP request to record_transaction endpoint in Ktor
    response = requests.post(
        'http://host.docker.internal:8070/transaction/record-fee',
         json = conf
    )
    return conf
    if response.status_code == 200:
        return response.json()
    else:
        # Handle error case here
        return {"error": "Failed to record fee"}


default_args = {
    'owner': 'airflow',
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}

with DAG(
    dag_id = 'fee_workflow',
    default_args = default_args,
    start_date=datetime(2023, 8, 30),
    schedule_interval = None
) as dag:
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
