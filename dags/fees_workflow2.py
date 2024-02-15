from airflow import DAG
from datetime import datetime, timedelta
from airflow.operators.http_operator import SimpleHttpOperator
import json

default_args = {
    'owner': 'airflow',
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}

with DAG(
    dag_id = 'fee_workflow2',
    default_args = default_args,
    start_date=datetime(2023, 8, 30),
    schedule_interval = None
) as dag:
    calculate_fee_task = SimpleHttpOperator(
        task_id='calculate_fee',
        http_conn_id='http_default',  # Connection ID configured in Airflow for HTTP requests
        data=json.dumps({
            "transaction_id":"{{dag_run.conf.transaction_id}}",
            "amount": "{{dag_run.conf.amount}}",
            "asset": "{{{{dag_run.conf.asset}}}}",
            "type": "{{dag_run.conf.type}}"

        }),
        endpoint='transaction/calculate-fee',  # Endpoint to hit in your Spring Boot application
        response_check=lambda response: response.status_code == 200,
        method='POST',
        headers={"Content-Type": "application/json"},
        dag=dag,
    )

    charge_fee_task = SimpleHttpOperator(
        task_id='charge_fee',
        http_conn_id='http_default',  # Connection ID configured in Airflow for HTTP requests
        endpoint='transaction/charge-fee',  # Endpoint to hit in your Spring Boot application
        method='POST',
        data=json.dumps({
            "transaction_id":"{{dag_run.conf.transaction_id}}",
        }),
        response_check=lambda response: response.status_code == 200,
        headers={"Content-Type": "application/json"},
        dag=dag,
    )

    record_fee_task = SimpleHttpOperator(
        task_id='record_fee',
        http_conn_id='http_default',  # Connection ID configured in Airflow for HTTP requests
        endpoint='transaction/record-fee',  # Endpoint to hit in your Spring Boot application
        response_check=lambda response: response.status_code == 200,
        method='PUT',
        data=json.dumps({
            "transaction_id":"{{dag_run.conf.transaction_id}}",
        }),
        headers={"Content-Type": "application/json"},
        dag=dag,
    )

    calculate_fee_task >> charge_fee_task >> record_fee_task
