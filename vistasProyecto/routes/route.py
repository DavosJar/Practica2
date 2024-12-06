
from flask import Blueprint, abort, request, render_template, redirect, flash, jsonify
import requests
import json
import datetime

router = Blueprint('router', __name__)

@router.route('/')
def home():
    try:
        r = requests.get('http://localhost:8090/api/proyecto/list')
        r.raise_for_status()
        r2 =requests.get('http://localhost:8090/api/inversionista/list')
        r2.raise_for_status()
        r3 = requests.get('http://localhost:8090/api/participacion/list')
        r3.raise_for_status()
        data = r.json()
        data2 = r2.json()
        data3 = r3.json()
        proyectos_count = len(data["data"]) if "data" in data and isinstance(data["data"], list) else 0
        inversionistas_count = len(data2["data"]) if "data" in data2 and isinstance(data2["data"], list) else 0
        participaciones_count = len(data3["data"]) if "data" in data3 and isinstance(data3["data"], list) else 0
        return render_template('index.html',proyectos=proyectos_count,inversionistas=inversionistas_count,participaciones=participaciones_count) 
    except requests.RequestException as e:
        return render_template('index.html', error=str(e))
