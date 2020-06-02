import requests
import serial
import time
import threading
import json
from datetime import datetime

# API = "https://tonu.rocks/school/GreenHouse/api/"
API = 'http://192.168.64.6/univ/PS1/public/api/'
preferences_endpoint = API + "preferences"
sensor = "DH11"
serial_port = '/dev/cu.wchusbserialfa130'
baud_rate = 115200
today = datetime.now()
today = today.strftime("%b %d, %Y")
log_file = "Logs/log_" + today + ".txt"
serial_connection = serial.Serial(serial_port, baud_rate, timeout=0.5)
time.sleep(1)  # give the connection a second to settle)
data = []

response = requests.get(url=preferences_endpoint)
if (response.status_code >= 400):
    print('\033[91m' + response.text + '\033[0m')
else:
    preferences = response.json()
    print(preferences)

    # default preferences
    light = "200"
    temperature = "25"
    water = "40"
    culture_id = "1"
    # get remote preferences
    for preference in preferences:
        if (preference['selected']):
            culture_id = str(preference['id'])
            light = str(preference['light'])
            temperature = str(preference['temperature'])
            water = str(preference['water'])

    # config message for Arduino
    config_msg = light + "e" + temperature + "e" + water + "ef"
    StartTime = time.time()
    serial_connection.write(config_msg.encode())

    line = ''
    class setInterval:
        def __init__(self, interval, action):
            self.interval = interval
            self.action = action
            self.stopEvent = threading.Event()
            thread = threading.Thread(target=self.__setInterval)
            thread.start()

        def __setInterval(self):
            nextTime = time.time() + self.interval
            while not self.stopEvent.wait(nextTime - time.time()):
                nextTime += self.interval
                self.action()

        def cancel(self):
            self.stopEvent.set()

    def handleLogs():
        global data, light, temperature, water, culture_id, line

        line = line.decode("utf-8").strip()
        time_stamp = datetime.now().strftime("%H:%M:%S")
        if line:
            if len(data) == 10:
                output_file = open(log_file, "a+")
                for record in data:
                    output_file.write(record)
                data = []
                output_file.close()
            else:
                data.append(today + "/" + time_stamp + "_" + line +
                            "\n")
                line = line.split("_")
                log_object = json.dumps({
                    "cultureId": culture_id,
                    "light": line[0],
                    "temperature": line[1],
                    "water": line[2]
                })
                headers = {'Content-type': 'application/json'}
                response = requests.put(url=API + "logs/",
                                        data=log_object,
                                        headers=headers)
                if (response.status_code < 400):
                    print('\033[94m' + response.text + '\033[0m')
                else:
                    print('\033[91m' + response.text + '\033[0m')

    state_msg = serial_connection.readline().decode("utf-8").strip()

    if(state_msg == "Ready"):
        print("Connection settled")
        readRoutine = setInterval(3, handleLogs)

        while 1:
            line = serial_connection.readline()
    else:
        print(state_msg)

    # t = threading.Timer(30, readRoutine.cancel)
    # t.start()