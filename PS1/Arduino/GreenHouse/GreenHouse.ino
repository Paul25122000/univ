// required libraries
#include <OneWire.h>
#include <DallasTemperature.h>
#include <Arduino.h>
#include "TM1637Display.h"
#include <PID_v1.h>

// define pinout macros
#define ONE_WIRE_BUS 7     // temperature sensor data
#define CLK 2              // 4 rang 7 segment display clock
#define DIO 6              // 4 rang 7 segment display data I/O
#define pResistor 11       // photoresistor digital input for controller using mapping
#define pResistorAnalog A0 // photoresistor analog input for PID controller
#define lightOut 10        // LED
#define trigPin 9          // ultrasonic sensor trigger
#define echoPin 8          // ultrasonic sensor echo
#define enA 3              // enable fan DC motor
#define in1 5              // L298N driver in1
#define in2 4              // L298N driver in2
#define in3 12             // L298N driver in3
#define in4 13             // L298N driver in4

double lightSet;   // will be the desired value for brightness
double lightValue; // photoresistor value
double lightPWM;   // PWM output to LED

int waterSet,   // will be the desired value for humidity
    waterValue; // humidity value (not really)

float temperatureSet, // will be the desired value for temperature
    temperatureValue; // temperature sensor input

char data;                 // message received from the serial
double buff[3] = {0};      // splitted message
int loaded = 0, index = 0; // serial receive state flags

// setup PID parameters
double Kp = 0, Ki = 3, Kd = 0;
PID lightPID(&lightValue, &lightPWM, &lightSet, Kp, Ki, Kd, DIRECT);

// temperature sensor utilities
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);

// 4 rang 7 segment utility
TM1637Display display(CLK, DIO);

// degree Celsius symbol
const uint8_t degreeCelsius[] = {
    SEG_G,                         // -
    SEG_G,                         // -
    SEG_A | SEG_B | SEG_F | SEG_G, // o
    SEG_A | SEG_D | SEG_E | SEG_F  // C
};

// store received characters from the serial in a buffer array
void recieveSerial()
{
  data = Serial.read(); // read 1 byte
  switch (data)         // check what kind of byte has arrived
  {
  case 'f':     // emitor finished serial transmit
    loaded = 1; // set loaded flag true
    break;
  case 'e':                  // [e]nd of a value
    index = (index + 1) % 3; // select index from buffer array
    break;
  default: // a integer byte arrived, push it at the end
    buff[i] *= 10;
    buff[i] += (data - 48);
    break;
  }
}

// update configuration
void updateConfig()
{
  while (!loaded) // while serial receiving is not finished
    if (Serial.available() > 0)
      recieveSerial(); // call helper

  // update configuration variables
  lightSet = buff[0];
  temperatureSet = buff[1];
  waterSet = buff[2];
}

void setup()
{
  // set pinout
  pinMode(pResistor, INPUT);
  pinMode(lightOut, OUTPUT);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(enA, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);

  // setup DC Driver
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);

  // setup display
  display.setBrightness(0x20);
  display.clear();

  // open read descriptor at 115200 baud rate
  Serial.begin(115200);

  // call function to update configuration variables
  updateConfig();

  // Acknowledge emitor that data was successfully received
  Serial.print("Ready\n");

  // start temperature sensor
  sensors.begin();

  // display on display degree symbol
  display.setSegments(degreeCelsius);

  //Turn the PID controller on
  lightPID.SetMode(AUTOMATIC);

  //Adjust PID values
  lightPID.SetTunings(Kp, Ki, Kd);
}

// humidity (not really) handler
void ultrasonicRoutine()
{
  // config
  long duration;
  pinMode(trigPin, OUTPUT);
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  pinMode(echoPin, INPUT);
  duration = pulseIn(echoPin, HIGH);
  waterValue = duration * 0.34 / 2;

  // turn off pump if humidity is higher than setted and on otherwise
  if (waterValue > waterSet)
    digitalWrite(in4, HIGH); // pump off
  else
    digitalWrite(in4, LOW); // pump on
}

// temperature handler

int pwmOutput = 0; // by default cooler is turned off
void coolerRoutine()
{
  // get data from the L298N sensor
  sensors.requestTemperatures();
  temperatureValue = sensors.getTempCByIndex(0);

  // apply hysteresis logic
  if (temperatureValue > temperatureSet)
    pwmOutput = 255;
  if (temperatureValue <= (temperatureSet - 3))
    pwmOutput = 0;

  // Send PWM signal to L298N Enable pin and display temperature value
  analogWrite(enA, pwmOutput);
  display.showNumberDec((int)temperatureValue, false, 2, 0);
}

// brightness handler
void lightRoutine()
{
  // Controller implemented using map function
  // lightValue = map(analogRead(pResistorAnalog), 0, 1023, lightSet, 0);
  // analogWrite(lightOut, lightValue);

  // map light value to discret range
  lightValue = map(analogRead(A0), 0, 1024, 0, 255);
  // PID calculation
  lightPID.Compute();
  // Write the lightPWM as calculated by the PID function
  analogWrite(10, lightPWM); // LED is set to digital pwm pin 10
}

// serial transmission routine
void transmitSerial()
{
  // print a value followed by delimiter
  Serial.print((int)lightValue);
  Serial.print("_");
  Serial.print(temperatureValue);
  Serial.print("_");
  Serial.print(waterValue);
  Serial.print("\n");
}

void loop()
{
  // execute each routine
  coolerRoutine();
  ultrasonicRoutine();
  lightRoutine();

  // uncomment for Arduino IDE's serial monitor
  //  if(Serial.available() > 0) {
    transmitSerial();
  //  }

  updateConfig();
}
