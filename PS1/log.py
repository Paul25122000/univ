import requests, serial, time, threading, json
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
# ser = serial.Serial(serial_port, baud_rate, timeout=.1)
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
        global data, light, temperature, water, culture_id
        # ser.write(config_msg.encode())
        # line = ser.readline().strip()
        # line = line.decode("utf-8")
        time_stamp = datetime.now().strftime("%H:%M:%S")
        line = time_stamp+"_161_"+light+"_26.94_"+temperature+"_50_"+water+"_"+culture_id
        if line:
            if len(data) == 10:
                output_file = open(log_file, "a+")
                for record in data:
                    output_file.write(record)
                data = []
                output_file.close()
            else:
                data.append(today + "/" + time_stamp + "_" + line.strip() +
                            "\n")
                line = line.split("_")
                log_object = json.dumps({
                    "cultureId": line[7],
                    "light": line[1],
                    "temperature": line[3],
                    "water": line[5]
                })
                headers = {'Content-type': 'application/json'}
                response = requests.put(url=API + "logs/",
                                        data=log_object,
                                        headers=headers)
                if (response.status_code < 400):
                    print('\033[94m' + response.text + '\033[0m')
                else:
                    print('\033[91m' + response.text + '\033[0m')

    inter = setInterval(3, handleLogs)
    # t = threading.Timer(30, inter.cancel)
    # t.start()