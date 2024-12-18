from flask import Blueprint, render_template, request, redirect, url_for, flash, abort
import requests
from datetime import datetime

inversionista = Blueprint('inversionista', __name__)
URL = 'http://localhost:8090/api/inversionista'

@inversionista.route('/')
def home():
    criterios = charge_options(f'{URL}/list/criteria')   
    try:
        r = requests.get(f'{URL}/list')
        r.raise_for_status()
        data = r.json()  
        lista = data.get("data", []) 
        print("Lista enviada al template:", lista)  
        return render_template('inversionistas.html', lista=lista, opciones=criterios)
    
    except requests.exceptions.RequestException as e:
        print(f"Error al obtener la lista de inversionistas: {e}")
        return render_template('inversionistas.html', lista=[], error_message="Error inesperado al obtener los datos de la API.")

@inversionista.route('/<id>')
def inversionista_id(id):
    try:
        r = requests.get(f'{URL}/get/{id}')
        r.raise_for_status()  
        
        inversionista_data = r.json().get("data")

        if inversionista_data is None or inversionista_data == "No se encontro el inversionista con id":
            return render_template('404.html')
        
        return render_template('inversionista.html', inversionista=inversionista_data)

    except requests.RequestException as e:
        print(f"Error al obtener el inversionista: {e}")
        return render_template('404.html') 
    
@inversionista.route('/<id>/delete')
def delete_inversionista(id):
    try:
        r = requests.delete(f'{URL}/{id}/delete')
        r.raise_for_status()  
        
        if r.status_code == 200:
            return redirect(url_for('inversionista.home'))
        else:
            error_message = r.json().get('data', 'Error desconocido')
            print(f"Error al eliminar el inversionista: {error_message}")
            return redirect('/') 
    except requests.RequestException as e:
        print(f"Error al eliminar el inversionista: {e}")
        return redirect('404.html') 

@inversionista.route('/create', methods=['GET'])
def save_inversionista():
    sector = charge_options(f'{URL}/sector')
    provincia = charge_options(f'{URL}/provincia')

    inversionista = {
        "nombre": "",
        "registro": "",
        "sector": "",
        "ubicacion": ""
    }

    return render_template('forminversionistas.html', inversionista=inversionista, sector=sector, provincia=provincia, is_edit=False)

@inversionista.route('/create', methods=['POST'])
def post_inversionista_form():
    try:
        data = {
            "nombre": request.form.get('nombre'),
            "registro": request.form.get('registro'),
            "sector": request.form.get('sector'),
            "ubicacion": request.form.get('provincia')
        }

        response = requests.post(f'{URL}/save', json=data)
        response.raise_for_status()

        return redirect(url_for('inversionista.home'))

    except requests.HTTPError as e:
        if e.response.status_code == 400: 
            error_msg = e.response.json().get("error", "Error de validacion.")
            flash(f"Error al crear el inversionista: {error_msg}", "error")
        else:
            flash(f"Error inesperado al comunicarse con la API: {e}", "error")
        return redirect(url_for('inversionista.save_inversionista'))

    except Exception as e:
        flash(f"Ocurrió un error inesperado: {str(e)}", "error")
        return redirect(url_for('inversionista.save_inversionista'))
    
@inversionista.route('/<id>/edit', methods=['GET'])
def update(id):
    try:
        r = requests.get(f'{URL}/get/{id}')
        r.raise_for_status()
        response = r.json()
        
        if response.get("msg") != "OK":
            print(f"Error desde la API: {response.get('error', 'No especificado')}")
            return redirect(url_for('inversionista.home'))
        
        data = response["data"]
        
        provincia = charge_options(f'{URL}/provincia')
        sector = charge_options(f'{URL}/sector')
        
        return render_template(
            'forminversionistas.html', inversionista=data, sector=sector, provincia=provincia, is_edit=True
        )
    except requests.RequestException as e:
        print(f"Error al conectar con la API: {e}")
        return redirect(url_for('inversionista.home'))

@inversionista.route('/<id>/edit', methods=['POST'])
def update_inversionista(id):
    try:
        data = {
            "id": request.form.get('id', ''),
            "nombre": request.form.get('nombre', ''),
            "registro": request.form.get('registro', ''),
            "sector": request.form.get('sector', ''),
            "ubicacion": request.form.get('provincia', '')
        }
        response = requests.post(f'{URL}/update', json=data)
        response.raise_for_status()
        return redirect(url_for('inversionista.home'))
    except requests.RequestException as e:
        print(f"Error al crear el inversionista: {e}")
        return redirect(url_for('inversionista.update_inversionista(id)'))
    
@inversionista.route('list/sort/<attribute>/<type>/<metodo>', methods=['GET'])
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
        return render_template('inversionistas.html', lista=data["data"], opciones=criterios)
    
    except requests.RequestException as e:
        print(f"Error al obtener la lista de inversionistas: {e}")
        return render_template('inversionistas.html', lista=[])

@inversionista.route('/list/search/<attribute>/<value>', methods=['GET'])
def search_criteria(attribute, value):
    criterios = charge_options(f'{URL}/list/criteria')  
    try:
        r = requests.get(f'{URL}/list/search/{attribute}/{value}')
        r.raise_for_status()
        response = r.json()  
        
        if response.get("status") == "ERROR":
            print(f"Error desde la API: {response.get('msg', 'Error desconocido')}")
            return render_template('inversionistas.html', lista=[], opciones=criterios, error=response.get('msg'))
        
        data = response.get("data", [])
        
        if not isinstance(data, list):
            data = [data]
        
        return render_template('inversionistas.html', lista=data, opciones=criterios)
    
    except requests.RequestException as e:
        print(f"Error al conectar con la API: {e}")
        return render_template('inversionistas.html', lista=[], opciones=criterios, error="Error")
    
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