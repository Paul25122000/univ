import requests, serial, time, threading
from datetime import datetime

API = "http://192.168.1.102:3000"
preferences_endpoint = API+"/preferences/0"
sensor = "DH11"
serial_port = '/dev/cu.wchusbserialfa130'
baud_rate = 115200
today = datetime.now()
today = today.strftime("%b %d, %Y")
log_file = "Logs/log_"+today+".txt"
ser = serial.Serial(serial_port, baud_rate, timeout=.1)
time.sleep(1)  # give the connection a second to settle)
data = []

r = requests.get(url=preferences_endpoint)
current = r.json()
config_msg = current['light']+"e"+current['temperature']+"e"+current['water']+"ef"
StartTime = time.time()

class setInterval:
    def __init__(self, interval, action):
        self.interval = interval
        self.action = action
        self.stopEvent = threading.Event()
        thread = threading.Thread(target=self.__setInterval)
        thread.start()

    def __setInterval(self):
        nextTime = time.time()+self.interval
        while not self.stopEvent.wait(nextTime-time.time()):
            nextTime += self.interval
            self.action()

    def cancel(self):
        self.stopEvent.set()

def handleLogs():
    global data
    ser.write(config_msg.encode())
    line = ser.readline().strip()
    line = line.decode("utf-8")
    if line:
        if len(data) == 10:
            output_file = open(log_file, "a+")
            for record in data:
                output_file.write(record)
            data = []
            output_file.close()
        else:
            time_stamp = datetime.now().strftime("%H:%M:%S")
            data.append(today+"/"+time_stamp+"-"+line.strip()+"\n")
            line = line.split("-")
            log_light = {
                "date": today,
                "time": time_stamp,
                "value": line[0],
                "set": line[1]
            }
            log_temperature = {
                "date": today,
                "time": time_stamp,
                "value": line[2],
                "set": line[3]
            }
            log_water = {
                "date": today,
                "time": time_stamp,
                "value": line[4],
                "set": line[5]
            }
            rsp_light = requests.post(url=API+"/light", data=log_light)
            time.sleep(.5)  # give the connection a second to settle)
            rsp_water = requests.post(url=API+"/water", data=log_water)
            time.sleep(.5)  # give the connection a second to settle)
            rsp_temperature = requests.post(url=API+"/temperature", data=log_temperature)
            print(line, rsp_light.text, rsp_temperature.text, rsp_water.text)

inter = setInterval(3, handleLogs)
# t = threading.Timer(30, inter.cancel)
# t.start()