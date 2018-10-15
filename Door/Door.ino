#include <SoftwareSerial.h>
#define BT_RX 9
#define BT_TX 8
#define SENSOR 4
#define OPEN 7
#define CLOSE 6
SoftwareSerial BlueTooth(BT_RX, BT_TX);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  BlueTooth.begin(9600);
  pinMode(OPEN, OUTPUT);
  pinMode(CLOSE, OUTPUT);
  pinMode(SENSOR,INPUT);
}


void loop() {
  // put your main code here, to run repeatedly:
  digitalWrite(CLOSE, HIGH); //불을 켬
  while (BlueTooth.available()) {
    //  if(BlueTooth.available()){ //블루투스가 이용가능상태이면
    char data = BlueTooth.read(); // 블루투스로부터 데이터를 받아옴
    //    Serial.write(BlueTooth.read());
    

    if (data == '1') { //만약 입력된 데이터가 1이면
      digitalWrite(OPEN, HIGH); //불을 켬
      digitalWrite(CLOSE, LOW); //불을 끔
      while(digitalRead(SENSOR) != LOW)
      {}
      /*
       * 적외선 센서 코드 추가필요
       */
      BlueTooth.write(100);//기기에 1을 보냄
    }

    if (data == '0') { //입력된 데이터가 0이면
      digitalWrite(OPEN, LOW); //불을 끔
      digitalWrite(CLOSE, HIGH); //불을 켬
    }
  }
}
