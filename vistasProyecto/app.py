from flask import Flask
from flask_wtf.csrf import CSRFProtect

def create_app():
    app = Flask(__name__, instance_relative_config=False);
    csrf = CSRFProtect(app) 
    app.config['SECRET_KEY'] = '2001'
    with app.app_context():
        from routes.route import router
        app.register_blueprint(router)
    return app
