#include <OneWire.h>
#include <DallasTemperature.h>
#include <Arduino.h>
#include "TM1637Display.h"
#include <PID_v1.h>

#define ONE_WIRE_BUS 7
#define CLK 2
#define DIO 6
#define pResistor 11
#define pResistorAnalog A0
#define lightOut 10
#define trigPin 9
#define echoPin 8
#define resetPin 13
#define enA 3
#define in1 5
#define in2 4
#define in3 12
#define in4 13

double lightSet ; // will be the desired value
double lightValue; // photo sensor
double lightPWM ; //LED
//PID parameters
double Kp=0, Ki=3, Kd=0; 
PID lightPID(&lightValue, &lightPWM, &lightSet, Kp, Ki, Kd, DIRECT);

const short int isDemoMode = 0;


double buff[3]={0};
int waterValue,
    waterSet = 50,
    loaded = 0, i = 0;
char data;
float temperatureValue, temperatureSet = 30;
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);
TM1637Display display(CLK, DIO);


const uint8_t degreeCelsius[] = {
  SEG_G,                         // -
  SEG_G,                         // -
  SEG_A | SEG_B | SEG_F | SEG_G, // o
  SEG_A | SEG_D | SEG_E | SEG_F  // C
  };

void(* resetFunc) (void) = 0;

void updateConfig() {
  if(isDemoMode)
  {
    lightSet = 50;
    temperatureSet = 20;
    waterSet = 60;    
  }
  else
  {
    while(!loaded)
      if(Serial.available() > 0)
        recieveSerial();
    
    lightSet = buff[0];
    temperatureSet = buff[1];
    waterSet = buff[2]; 
  }
  
}

void setup() {
  pinMode(pResistor, INPUT);
  pinMode(lightOut, OUTPUT);
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input
  pinMode(enA, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
  display.setBrightness(0x20);
  display.clear();
  Serial.begin(115200);
  updateConfig();
  sensors.begin();
  display.setSegments(degreeCelsius);
  
  Serial.print("Ready\n");
}

void ultrasonicRoutine() {
   long duration;
   pinMode(trigPin, OUTPUT);
   digitalWrite(trigPin, LOW);
     
   delayMicroseconds(2);
   digitalWrite(trigPin, HIGH);
   delayMicroseconds(10);
   digitalWrite(trigPin, LOW);
   pinMode(echoPin, INPUT);
   duration = pulseIn(echoPin, HIGH);
   waterValue = duration*0.34/2;

   if(waterValue > waterSet)
    digitalWrite(in4, HIGH);
   else
    digitalWrite(in4, LOW);
    //Turn the PID on
    lightPID.SetMode(AUTOMATIC);
    //Adjust PID values
    lightPID.SetTunings(Kp, Ki, Kd);
}

void coolerRoutine() {
  sensors.requestTemperatures();
  temperatureValue = sensors.getTempCByIndex(0);
  
  int pwmOutput = 0;
  if(temperatureValue > temperatureSet)
    pwmOutput = 255;
  if (temperatureValue <= (temperatureSet - 3))
    pwmOutput = 0;
  analogWrite(enA, pwmOutput); // Send PWM signal to L298N Enable pin
  display.showNumberDec((int)temperatureValue, false, 2, 0);
}

void lightRoutine(){
  // Map controller
//  lightValue = map(analogRead(pResistorAnalog), 0, 1023, lightSet, 0);
//  analogWrite(lightOut, lightValue);

  //PID controller
  lightValue = map(analogRead(A0), 0, 1024, 0, 255);  // photo senor is set on analog pin 5
  //PID calculation
  lightPID.Compute();
  //Write the lightPWM as calculated by the PID function
  analogWrite(10,lightPWM); //LED is set to digital 3 this is a pwm pin. 
}

void recieveSerial() {
  data = Serial.read();
  switch(data){
    case 'f':
      loaded = 1;
      break;
    case 'e':
        i = (i + 1) % 3;
      break;
    default:
        buff[i] *= 10;
        buff[i] += (data - 48);
        break;
  }
}

void transmitSerial() {
  if(isDemoMode)
  {
    //Send data by serial for plotting 
    Serial.print(lightValue);
    Serial.print(" ");
    Serial.print(lightPWM);
    Serial.print(" ");  
    Serial.println(lightSet);
  }
  else
  {
    Serial.print((int) lightValue); Serial.print("_");
    Serial.print(temperatureValue); Serial.print("_");
    Serial.print(waterValue); Serial.print("\n");
  }
}


void loop(){
  coolerRoutine();
  ultrasonicRoutine();
  lightRoutine();

// uncomment for Arduino IDE
//  if(Serial.available() > 0) {
    transmitSerial();
//  }

}
