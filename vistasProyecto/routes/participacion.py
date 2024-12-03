from flask import Blueprint, render_template, request, redirect, url_for, flash, abort
import requests
from datetime import datetime

participacion = Blueprint('participacion', __name__)
URL = 'http://localhost:8090/api/participacion'

@participacion.route('/')
def home():
    criterios = charge_options(f'{URL}/list/criteria')
    
    try:
        r = requests.get(f'{URL}/list')
        r.raise_for_status()  
        data = r.json()
        
        return render_template('lista_participacions.html', lista=data["data"], opciones=criterios)
    
    except requests.exceptions.RequestException as e:
        print(f"Error al obtener la lista de participacions: {e}")
        return render_template('lista_participacions.html', lista=[], error_message="Error inesperado al obtener los datos de la API.")

@participacion.route('/proyecto/<id>', methods=['GET'])
def proyecto_participacion(id):
    try:
        
        r = requests.get(f'{URL}/proyecto/{id}')
        r.raise_for_status()  
        
        data = r.json().get("data")
        
        return render_template('proyecto.html', lista=data["data"])

    except requests.RequestException as e:
        print(f"Error al obtener el participacion: {e}")
        return render_template('404.html')

@participacion.route('/<id>')
def participacion_id(id):
    try:
        r = requests.get(f'{URL}/get/{id}')
        r.raise_for_status()  
        
        participacion_data = r.json().get("data")

        if participacion_data is None or participacion_data == "No se encontro el participacion con id":
            return render_template('404.html')
        
        return render_template('participacion.html', participacion=participacion_data)

    except requests.RequestException as e:
        print(f"Error al obtener el participacion: {e}")
        return render_template('404.html') 
    
@participacion.route('/<id>/delete')
def delete_participacion(id):
    try:
        r = requests.delete(f'{URL}/{id}/delete')
        r.raise_for_status()  
        
        if r.status_code == 200:
            return redirect(url_for('participacion.home'))
        else:
            error_message = r.json().get('data', 'Error desconocido')
            print(f"Error al eliminar el participacion: {error_message}")
            return redirect('/') 
    except requests.RequestException as e:
        print(f"Error al eliminar el participacion: {e}")
        return redirect('404.html') 

@participacion.route('/create', methods=['GET'])
def save_participacion():
    tipo = charge_options(f'{URL}/tipos')
    estado = charge_options(f'{URL}/estado')
    provincia = charge_options(f'{URL}/provincia')

    participacion = {
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

    return render_template('formparticipacions.html', participacion=participacion, tipos=tipo, estado=estado, provincia=provincia, is_edit=False)

@participacion.route('/create', methods=['POST'])
def post_participacion_form():
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
            "fechaFin": fecha_fin,
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

        return redirect(url_for('participacion.home'))

    except requests.HTTPError as e:
        if e.response.status_code == 400: 
            error_msg = e.response.json().get("error", "Error de validacion.")
            flash(f"Error al crear el participacion: {error_msg}", "error")
        else:
            flash(f"Error inesperado al comunicarse con la API: {e}", "error")
        return redirect(url_for('participacion.save_participacion'))

    except Exception as e:
        flash(f"Ocurrió un error inesperado: {str(e)}", "error")
        return redirect(url_for('participacion.save_participacion'))
    
@participacion.route('/<id>/edit', methods=['GET'])
def update(id):
    try:
        r = requests.get(f'{URL}/get/{id}')
        r.raise_for_status()
        response = r.json()
        
        if response.get("msg") != "OK":
            print(f"Error desde la API: {response.get('error', 'No especificado')}")
            return redirect(url_for('participacion.home'))
        
        data = response["data"]
        
        tipo = charge_options(f'{URL}/tipos')
        estado = charge_options(f'{URL}/estado')
        provincia = charge_options(f'{URL}/provincia')
        
        return render_template(
            'formparticipacions.html', participacion=data, tipos=tipo, estado=estado, provincia=provincia, is_edit=True
        )
    except requests.RequestException as e:
        print(f"Error al conectar con la API: {e}")
        return redirect(url_for('participacion.home'))

@participacion.route('/<id>/edit', methods=['POST'])
def update_participacion(id):
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
            "id": request.form.get('id', ''),
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
        response = requests.post(f'{URL}/update', json=data)
        response.raise_for_status()
        return redirect(url_for('participacion.home'))
    except requests.RequestException as e:
        print(f"Error al crear el participacion: {e}")
        return redirect(url_for('participacion.update_participacion(id)'))
    
@participacion.route('list/sort/<attribute>/<type>/<metodo>', methods=['GET'])
def order_list(attribute, type, metodo):
    criterios = charge_options(f'{URL}/list/criteria')
    try:
        if metodo.lower() == "shell":
            if type == '1':
                type = '0' 
            elif type == '0':
                type = '1' 
        
        r = requests.get(f'{URL}/list/order/{attribute}/{type}/{metodo}')
        r.raise_for_status()
        data = r.json()
        return render_template('lista_participacions.html', lista=data["data"], opciones=criterios)
    
    except requests.RequestException as e:
        print(f"Error al obtener la lista de participacions: {e}")
        return render_template('lista_participacions.html', lista=[])

@participacion.route('/list/search/<attribute>/<value>', methods=['GET'])
def search_criteria(attribute, value):
    criterios = charge_options(f'{URL}/list/criteria')  
    try:
        r = requests.get(f'{URL}/list/search/{attribute}/{value}')
        r.raise_for_status()
        response = r.json()  
        
        if response.get("status") == "ERROR":
            print(f"Error desde la API: {response.get('msg', 'Error desconocido')}")
            return render_template('lista_participacions.html', lista=[], opciones=criterios, error=response.get('msg'))
        
        data = response.get("data", [])
        
        if not isinstance(data, list):
            data = [data]
        
        return render_template('lista_participacions.html', lista=data, opciones=criterios)
    
    except requests.RequestException as e:
        print(f"Error al conectar con la API: {e}")
        return render_template('lista_participacions.html', lista=[], opciones=criterios, error="Error")
    
def charge_options(endpoint):
    try:
        response = requests.get(endpoint)
        response.raise_for_status()
        data = response.json()

        opciones = []
        for item in data.get("data", []):
            nombre_formateado = format_string(item.replace("_", " "), capitalizar_palabras=True)
            opciones.append((item, nombre_formateado))

        return opciones
    except requests.RequestException as e:
        print(f"Error al obtener opciones desde {endpoint}: {e}")
        return []

def format_string(cadena, capitalizar_palabras=False):
    resultado = ''.join([' ' + char if char.isupper() and (i > 0 and not cadena[i - 1].isupper()) else char
                         for i, char in enumerate(cadena)])
    
    if capitalizar_palabras:
        return ' '.join([palabra.capitalize() for palabra in resultado.split()])
    else:
        return resultado.capitalize()