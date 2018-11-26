
#include <SoftwareSerial.h>
#define BT_RX 13
#define BT_TX 12
#define OPEN 7
#define CLOSE 6
SoftwareSerial BTSerial(BT_RX, BT_TX); // 소프트웨어 시리얼 (TX,RX)

void setup() {
  Serial.begin(9600);
  Serial.println("Hello!");
  BTSerial.begin(9600);
}

void loop() {
  while (BTSerial.available()) {
    byte data = BTSerial.read();
    Serial.write(data);
  }

  while (Serial.available()) {
    byte data = Serial.read();
    BTSerial.write(data);
  }
}


//#include <SoftwareSerial.h>
//
//SoftwareSerial BlueTooth(BT_RX, BT_TX);
//void setup() {
//  BlueTooth.begin(9600);
//  Serial.begin(9600);
//  pinMode(OPEN, OUTPUT);//RED
//  pinMode(CLOSE, OUTPUT);//BLUE
//  digitalWrite(CLOSE, HIGH);
//}
//
//
//void loop() {
//
//  if (BlueTooth.available()) {
//    char data = BlueTooth.read(); // 블루투스로부터 데이터를 받아옴
//
//    if (data == '1') { //만약 입력된 데이터가 1이면
//      BlueTooth.write(100);//기기에 1을 보냄
//      digitalWrite(OPEN,HIGH);
//      digitalWrite(CLOSE,LOW);
//      delay(1000);
//      digitalWrite(OPEN,LOW);
//      digitalWrite(CLOSE,HIGH);
//    }
//  }
//
//}
