from airflow import DAG
from datetime import datetime
from airflow.operators.python_operator import PythonOperator
import subprocess

def calculate_fee(**kwargs):
    jar_path = "/opt/builds/fees-calculator.jar"
    amount =  kwargs['dag_run'].conf.get('amount')
    subprocess.run(["java", "-jar", jar_path, "tx_001", "20"])

def charge_fee(**kwargs):
    jar_path = "/opt/builds/fees-charger.jar"
    amount =  kwargs['dag_run'].conf.get('amount')
    subprocess.run(["java", "-jar", jar_path, "tx_001", "20"])

def record_fee(**kwargs):
    jar_path = "/opt/builds/fees-recorder.jar"
    amount =  kwargs['dag_run'].conf.get('amount')
    subprocess.run(["java", "-jar", jar_path, "tx_001", "20"])

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