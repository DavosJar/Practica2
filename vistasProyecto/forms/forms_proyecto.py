from flask_wtf import FlaskForm
from wtforms import StringField, IntegerField, DateField, SelectField, TextAreaField, SubmitField
from wtforms.validators import DataRequired

class ProyectoForm(FlaskForm):
    nombre = StringField('Nombre del Proyecto', validators=[DataRequired()])
    costo = IntegerField('Costo Inicial', validators=[DataRequired()])
    fecha_inicio = DateField('Fecha de Inicio', validators=[DataRequired()])
    tiempo_vida = IntegerField('Tiempo de Vida (años)', validators=[DataRequired()])
    capacidad = IntegerField('Capacidad (MW)', validators=[DataRequired()])
    tipo_energia = SelectField('Tipo de Energía', choices=[],)
    ubicacion = SelectField('Ubicación', choices=[])
    descripcion = TextAreaField('Descripción')
    estado = SelectField('Estado', choices=[])
    submit = SubmitField('Crear Proyecto')