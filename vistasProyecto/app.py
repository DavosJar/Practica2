from flask import Flask


def create_app():
    app = Flask(__name__, instance_relative_config=False);
    with app.app_context():
        from routes.route import router
        from routes.proyecto import proyecto
        from routes.inversionista import inversionista
        from routes.participacion import participacion
        app.register_blueprint(router)
        app.register_blueprint(proyecto, url_prefix='/proyecto')
        app.register_blueprint(inversionista, url_prefix='/inversionista')
        app.register_blueprint(participacion, url_prefix='/participacion')
        
    return app
