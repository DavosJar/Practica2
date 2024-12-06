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
        
        return render_template('lista_proyectos.html', lista=data["data"], opciones=criterios)
    
    except requests.exceptions.RequestException as e:
        print(f"Error al obtener la lista de proyectos: {e}")
        return render_template('lista_proyectos.html', lista=[], error_message="Error inesperado al obtener los datos de la API.")

@proyecto.route('/<id>', methods=['GET'])
def proyecto_id(id):
    try:
        r_proyecto = requests.get(f'{URL}/get/{id}')
        r_proyecto.raise_for_status()
        proyecto_data = r_proyecto.json().get("data")

        if not proyecto_data:
            return render_template('404.html', error_message="Proyecto no encontrado.")

        r_participaciones = requests.get(f'http://localhost:8090/api/participacion/proyecto/{id}')
        r_participaciones.raise_for_status()
        participaciones = r_participaciones.json().get("data", [])

        participaciones_con_datos = []
        for participacion in participaciones:
            inversionista_id = participacion.get('idInversionista')
            r_inversionista = requests.get(f'http://localhost:8090/api/inversionista/get/{inversionista_id}')
            r_inversionista.raise_for_status()
            inversionista_data = r_inversionista.json().get("data", {})

            proyecto_id = participacion.get('idProyecto')
            r_proyecto_detalle = requests.get(f'{URL}/get/{proyecto_id}')
            r_proyecto_detalle.raise_for_status()
            proyecto_detalle_data = r_proyecto_detalle.json().get("data", {})

            nuevo_item = {
                'nombre_inversionista': inversionista_data.get('nombre', 'Desconocido'),  
                'registro_inversionista': inversionista_data.get('registro'),
                'nombre_proyecto': proyecto_detalle_data.get('nombre', 'Desconocido'), 
                'montoInvertido': participacion.get('montoInvertido'),
                'fechaRegistro': participacion.get('fechaRegistro')
            }
            participaciones_con_datos.append(nuevo_item)

        return render_template(
            'proyecto.html',
            proyecto=proyecto_data,
            lista=participaciones_con_datos
        )

    except requests.RequestException as e:
        print(f"Error al obtener los datos: {e}")
        return render_template('404.html', error_message="Error al conectarse con la API.")
    
@proyecto.route('/<id>/delete')
def delete_proyecto(id):
    try:
        r = requests.delete(f'{URL}/{id}/delete')
        r.raise_for_status()  
        
        if r.status_code == 200:
            return redirect(url_for('proyecto.home'))
        else:
            error_message = r.json().get('data', 'Error desconocido')
            print(f"Error al eliminar el proyecto: {error_message}")
            return redirect('/') 
    except requests.RequestException as e:
        print(f"Error al eliminar el proyecto: {e}")
        return redirect('404.html') 

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

        return redirect(url_for('proyecto.home'))

    except requests.HTTPError as e:
        if e.response.status_code == 400: 
            error_msg = e.response.json().get("error", "Error de validacion.")
            flash(f"Error al crear el proyecto: {error_msg}", "error")
        else:
            flash(f"Error inesperado al comunicarse con la API: {e}", "error")
        return redirect(url_for('proyecto.save_proyecto'))

    except Exception as e:
        flash(f"Ocurrió un error inesperado: {str(e)}", "error")
        return redirect(url_for('proyecto.save_proyecto'))
    
@proyecto.route('/<id>/edit', methods=['GET'])
def update(id):
    try:
        r = requests.get(f'{URL}/get/{id}')
        r.raise_for_status()
        response = r.json()
        
        if response.get("msg") != "OK":
            print(f"Error desde la API: {response.get('error', 'No especificado')}")
            return redirect(url_for('proyecto.home'))
        
        data = response["data"]
        
        tipo = charge_options(f'{URL}/tipos')
        estado = charge_options(f'{URL}/estado')
        provincia = charge_options(f'{URL}/provincia')
        
        return render_template(
            'formproyectos.html', proyecto=data, tipos=tipo, estado=estado, provincia=provincia, is_edit=True
        )
    except requests.RequestException as e:
        print(f"Error al conectar con la API: {e}")
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
        return redirect(url_for('proyecto.home'))
    except requests.RequestException as e:
        print(f"Error al crear el proyecto: {e}")
        return redirect(url_for('proyecto.update_proyecto(id)'))
    
@proyecto.route('list/sort/<attribute>/<type>/<metodo>', methods=['GET'])
def order_list(attribute, type, metodo):
    criterios = charge_options(f'{URL}/list/criteria')
    try:
        r = requests.get(f'{URL}/list/order/{attribute}/{type}/{metodo}')
        r.raise_for_status()
        data = r.json()
        return render_template('lista_proyectos.html', lista=data["data"], opciones=criterios)
    
    except requests.RequestException as e:
        print(f"Error al obtener la lista de proyectos: {e}")
        return render_template('lista_proyectos.html', lista=[])

@proyecto.route('/list/search/<attribute>/<value>', methods=['GET'])
def search_criteria(attribute, value):
    criterios = charge_options(f'{URL}/list/criteria')
    r = requests.get(f'{URL}/list/search/{attribute}/{value}')
    response = r.json()
    
    data = response.get("data", [])
    # Diferenciar si es un diccionario o lista
    if isinstance(data, dict):
        data = [data] 
        print("Datos recuperados:", data)
    return render_template('lista_proyectos.html', lista=data, opciones=criterios)
    
    
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