from flask import Blueprint, render_template, request, redirect, url_for, flash, abort
import requests
from datetime import datetime

proyecto = Blueprint('proyecto', __name__)
URL = 'http://localhost:8090/api/proyecto'

@proyecto.route('/')
def home():
    criterios = charge_options(f'{URL}/list/criteria')
    try:
        r = requests.get(f'{URL}/list')
        r.raise_for_status()
        data = r.json()
        return render_template('index.html', lista=data["data"], opciones = criterios)
    except requests.RequestException as e:
        print(f"Error al obtener la lista de proyectos: {e}")
        return render_template('index.html', lista=[])

@proyecto.route('/<id>')
def proyecto_id(id):
    try:
        # Obtener los detalles del proyecto
        r = requests.get(f'{URL}/get/{id}')
        r.raise_for_status()
        proyecto_data = r.json()["data"]


        return render_template('proyecto.html', proyecto=proyecto_data)
    except requests.RequestException as e:
        print(f"Error al obtener el proyecto o los inversionistas: {e}")
        return abort(404)

@proyecto.route('/<id>/delete')
def delete_proyecto(id):
    try:
        r = requests.delete(f'{URL}/{id}/delete')
        r.raise_for_status()
        return redirect(url_for('proyecto.home'))
    except requests.RequestException as e:
        print(f"Error al eliminar el proyecto: {e}")
        return redirect('/')

@proyecto.route('/create', methods=['GET'])
def save_proyecto():
    tipo = charge_options(f'{URL}/tipos')
    estado = charge_options(f'{URL}/estado')
    provincia = charge_options(f'{URL}/provincia')

    proyecto = {
        "nombre": "",
        "descripcion": "",
        "fechaInicio": "",
        "fechaFin": "",
        "costoEstimadoInicial": "",
        "tipoEnergia": "",
        "estado": "",
        "ubicacion": "",
        "capacidad": "",
        "tiempoDeVida": ""
    }

    return render_template('formproyectos.html', proyecto=proyecto, tipos=tipo, estado=estado, provincia=provincia, is_edit=False)

@proyecto.route('/create', methods=['POST'])
def post_proyecto_form():
    fecha_fin = request.form.get('fecha_fin', '').strip()
    try:
        fecha_inicio = datetime.strptime(request.form.get('fecha_inicio'), "%Y-%m-%d").strftime("%Y-%m-%d")
        if fecha_fin:
            try:
                fecha_fin = datetime.strptime(fecha_fin, "%Y-%m-%d").strftime("%Y-%m-%d")
            except ValueError:
                fecha_fin = None
        else:
            fecha_fin = None
        data = {
            "nombre": request.form.get('nombre'),
            "descripcion": request.form.get('descripcion'),
            "fechaInicio": fecha_inicio,
            "fechaFin" : fecha_fin,
            "costoEstimadoInicial": float(request.form.get('presupuesto', 0)),
            "tipoEnergia": request.form.get('tipo'),
            "estado": request.form.get('estado'),
            "ubicacion": request.form.get('provincia'),
            "capacidad": int(request.form.get('capacidad', 0)),
            "tiempoDeVida": int(request.form.get('tiempo_de_vida', 0)),
            "inversion": float(request.form.get('inversion', 0.0)),
        }

        response = requests.post(f'{URL}/save', json=data)
        response.raise_for_status()

        return redirect(url_for('proyecto.home'))
    except requests.RequestException as e:
        print(f"Error al crear el proyecto: {e}")
        return redirect(url_for('proyecto.save_proyecto'))
    
@proyecto.route('/<id>/edit', methods=['GET'])
def update(id):
    try:
        r = requests.get(f'{URL}/get/{id}')
        r.raise_for_status()
        data = r.json()["data"]

        tipo = charge_options(f'{URL}/tipos')
        estado = charge_options(f'{URL}/estado')
        provincia = charge_options(f'{URL}/provincia')

        return render_template('formproyectos.html', proyecto=data, tipos=tipo, estado=estado, provincia=provincia, is_edit=True)
    except requests.RequestException as e:
        print(f"Error al obtener el proyecto: {e}")
        return redirect(url_for('proyecto.home'))

@proyecto.route('/<id>/edit', methods=['POST'])
def update_proyecto(id):
    fecha_fin = request.form.get('fecha_fin', '').strip()
    try:
        fecha_inicio = datetime.strptime(request.form.get('fecha_inicio'), "%Y-%m-%d").strftime("%Y-%m-%d")
        if fecha_fin:
            try:
                fecha_fin = datetime.strptime(fecha_fin, "%Y-%m-%d").strftime("%Y-%m-%d")
            except ValueError:
                fecha_fin = None
        else:
            fecha_fin = None
        data = {
            "nombre": request.form.get('nombre', ''),
            "descripcion": request.form.get('descripcion', ''),
            "fechaInicio": fecha_inicio if fecha_inicio else '',
            "fechaFin": fecha_fin if fecha_fin else '',
            "costoEstimadoInicial": float(request.form.get('presupuesto', 0)),
            "tipoEnergia": request.form.get('tipo', ''),
            "estado": request.form.get('estado', ''),
            "ubicacion": request.form.get('provincia', ''),
            "capacidad": int(request.form.get('capacidad', 0)),
            "tiempoDeVida": int(request.form.get('tiempo_de_vida', 0)),
            "inversion": float(request.form.get('inversion', 0.0)),
        }
        response = requests.put(f'{URL}/{id}/update', json=data)
        response.raise_for_status()
        return redirect(url_for('proyecto.home'))
    except requests.RequestException as e:
        print(f"Error al crear el proyecto: {e}")
        return redirect(url_for('proyecto.supdate_proyecto(id)'))
    
@proyecto.route('/list/search/<attribute>/<value>', methods=['GET'])
def search_criteria(attribute, value):
    try:
        r = requests.get(f'{URL}/search/{attribute}/{value}')
        r.raise_for_status()
        data = r.json()
        return render_template('index.html', lista=data["data"])
    except requests.RequestException as e:
        print(f"Error al obtener la lista de proyectos: {e}")
        return render_template('index.html', lista=[])
@proyecto.route('list/sort/<attribute>/<type>', methods=['GET'])
def order_list(attribute, type):
    criterios = charge_options(f'{URL}/list/criteria')
    print(criterios)
    try:
        r = requests.get(f'{URL}/list/order/{attribute}/{type}')
        r.raise_for_status()
        data = r.json()
        return render_template('index.html', lista=data["data"], opciones = criterios)
    except requests.RequestException as e:
        print(f"Error al obtener la lista de proyectos: {e}")
        return render_template('index.html', lista=[])
def charge_options(endpoint):
    try:
        response = requests.get(endpoint)
        response.raise_for_status()
        data = response.json()
        return [(item, item) for item in data.get("data", [])]
    except requests.RequestException as e:
        print(f"Error al obtener opciones desde {endpoint}: {e}")
        return []