import flask

app = flask.Flask(__name__)
app.config["DEBUG"] = True

@app.route('/data', methods=['GET'])
def home():
    # TODO: get data from algorithm
    data = "Python api - data: unimplemented"
    return data

app.run()