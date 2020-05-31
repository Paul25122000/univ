import serial, time
arduino = serial.Serial('/dev/cu.wchusbserialfa130', 115200, timeout=.1)
time.sleep(1) #give the connection a second to settle
while True:
    arduino.write("123e234e345ef".encode())
    data = arduino.readline().strip()
    if data:
        print (data) #strip out the new lines for now
		# (better to do .read() in the long run for this reason