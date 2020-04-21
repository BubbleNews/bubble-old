from subprocess import check_output

import requests


def npl_is_server_on(host='localhost'):
    try:
        response = requests.head('http://' + host + ':9000/')
        return response.status_code == 200

    except:
        print('failed to verify is server is on.')
        return False


def nlp_start_server():
    try:
        if npl_is_server_on():
            print('NLP server already on - no-op')
        else:
            print('starting server')
            # docker run --rm -d -p 9000:9000 --name stanfordcorenlp anwala/stanfordcorenlp
            check_output(['docker', 'run', '--rm', '-d', '-p', '9000:9000', '--name', 'stanfordcorenlp',
                          'anwala/stanfordcorenlp'])

            # warm up server (preload libraries, so subsequent responses are quicker)
            nlpGetEntitiesFromText('A quick brown fox jumped over the lazy dog')
    except:
        print("couldn't start server")


def nlp_stop_server():
    try:
        check_output(['docker', 'rm', '-f', 'stanfordcorenlp'])
    except:
        print('failed to stop server')
