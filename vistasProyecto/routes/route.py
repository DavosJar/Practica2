
from flask import Blueprint, abort, request, render_template, redirect, flash, jsonify
import requests
import json
from forms.forms_proyecto import ProyectoForm

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

@router.route('/proyecto/<id>')
def proyecto(id):
    try:
        r = requests.get(f'http://localhost:8090/api/proyecto/get/{id}')
        r.raise_for_status()
        data = r.json()
        proyecto_data = data["data"]
        
        r_inversionistas = requests.get(f'http://localhost:8090/api/proyecto/inversionistas/{id}')
        r_inversionistas.raise_for_status()
        inversionistas_data = r_inversionistas.json()["data"]
        
        return render_template('proyecto.html', proyecto=proyecto_data, inversionistas=inversionistas_data)
    except requests.RequestException as e:
        print(f"Error al obtener el proyecto o los inversionistas: {e}")
        abort(404)


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


def cargar_opciones(endpoint):
    try:
        response = requests.get(endpoint)
        response.raise_for_status()
        data = response.json()
        return [(str(item), str(item)) for item in data['data']]
    except requests.RequestException as e:
        print(f"Error al cargar opciones desde {endpoint}: {e}")
        return []

@router.route('/proyecto/crear', methods=['GET', 'POST'])
def crear_proyecto():
    form = ProyectoForm()
    
    if request.method == 'GET':
        form.tipo_energia.choices = [(str(item), str(item)) for item in ['SOLAR', 'EOLICA', 'HIDRAULICA', 'GEOTERMICA', 'BIOMASA', 'BIOCOMBUSTIBLE', 'NUCLEAR', 'GAS', 'PETROLEO', 'CARBON', 'HIDROGENO', 'OTRAS']]
        form.ubicacion.choices = [(str(item), str(item)) for item in ['AZUAY', 'BOLIVAR', 'CAÑAR', 'CARCHI', 'CHIMBORAZO', 'COTOPAXI', 'EL_ORO', 'ESMERALDAS', 'GALAPAGOS', 'GUAYAS', 'IMBABURA', 'LOJA', 'LOS_RIOS', 'MANABI', 'MORONA_SANTIAGO', 'NAPO', 'ORELLANA', 'PASTAZA', 'PICHINCHA', 'SANTA_ELENA', 'SANTO_DOMINGO', 'SUCUMBIOS', 'TUNGURAHUA', 'ZAMORA_CHINCHIPE']]
        form.estado.choices = [(str(item), str(item)) for item in ['PLANTEADO', 'EN_DESARROLLO', 'EN_PRUEBAS', 'IMPLEMENTADO', 'EN_OPERACION', 'EN_MANTENIMIENTO', 'EN_REVISION', 'FINALIZADO', 'EN_DESUSO']]
        
        # Confirmación de las opciones cargadas
        print("Opciones tipo_energia:", form.tipo_energia.choices)
        print("Opciones ubicacion:", form.ubicacion.choices)
        print("Opciones estado:", form.estado.choices)
        
        return render_template('formproyectos.html', form=form)
    
    if request.method == 'POST':
        if form.validate_on_submit():
            headers = {'Content-Type': 'application/json'}
            dataF = {
                "nombre": form.nombre.data,
                "costoEstimadoInicial": float(form.costo.data),
                "fechaInicio": form.fecha_inicio.data.strftime('%Y-%m-%d'),
                "tiempoDeVida": int(form.tiempo_vida.data),
                "capacidad": int(form.capacidad.data) if form.capacidad.data is not None else 0,
                "tipoEnergia": form.tipo_energia.data,
                "ubicacion": form.ubicacion.data,
                "descripcion": form.descripcion.data,
                "estado": form.estado.data
            }
            try:
                r = requests.post('http://localhost:8090/api/proyecto/save', json=dataF, headers=headers)
                r.raise_for_status()
                flash('Proyecto creado exitosamente', category='info')
                return redirect('/')
            except requests.RequestException as e:
                print(f"Error al crear el proyecto: {e}")
                flash('Error al crear el proyecto. Intente nuevamente.', 'danger')
        else:
            print("Errores de validación:", form.errors)
    
    return render_template('formproyectos.html', form=form)

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
