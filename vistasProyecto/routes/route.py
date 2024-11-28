
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
        data = r.json()
        return render_template('index.html', lista=data["data"])
    except requests.RequestException as e:
        print(f"Error al obtener la lista de proyectos: {e}")
        flash('Error al cargar la lista de proyectos. Intente nuevamente.', 'danger')
        return render_template('index.html', lista=[])


@router.route('/crud-event')
def crud_event():
    try:
        r = requests.get('http://localhost:8090/api/evento/list')
        r.raise_for_status()
        data = r.json()
        return render_template('historial.html', lista=data["data"])
    except requests.RequestException as e:
        print(f"Error al obtener la lista de eventos: {e}")
        flash('Error al cargar la lista de eventos. Intente nuevamente.', 'danger')
    return redirect('/')



@router.route('/inversionistas')
def inversionistas():
    try:
        r = requests.get('http://localhost:8090/api/inversionista/list')
        r.raise_for_status()
        data = r.json()
        print(f"Datos recibidos: {data}")
        return render_template('inversionistas.html', inversionistas=data["data"])
    except requests.RequestException as e:
        print(f"Error al obtener la lista de inversionistas: {e}")
        flash('Error al cargar la lista de inversionistas. Intente nuevamente.', 'danger')
        return redirect('/')




@router.route('/proyecto/eliminar/<id>', methods=['DELETE'])
def eliminar_proyecto(id):
    try:
        r = requests.delete(f'http://localhost:8090/api/proyecto/{id}/delete')
        r.raise_for_status()
        flash('Proyecto eliminado exitosamente', category='info')
    except requests.RequestException as e:
        print(f"Error al eliminar el proyecto: {e}")
        flash('Error al eliminar el proyecto. Intente nuevamente.', 'danger')
    return redirect('/')
