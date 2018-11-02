#include<SoftwareSerial.h>

#define Led_red 4
#define Led_green 5
#define rx 9
#define tx 8
SoftwareSerial btSerial(tx, rx);
//rx 송신
//tx 수신

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  btSerial.begin(9600);
  pinMode(Led_red, OUTPUT);
  pinMode(Led_green, OUTPUT);
  digitalWrite(Led_red, HIGH);
}//pinmode 수행 방식
//digitalWrite(포트,HIGH/LOW);
//SoftwareSerial class 생성자 (tx,rx)
//Serial.available() <= 버퍼에 읽을게 존재한다면.
//.read() <= byte stream 으로 읽어온다.
//.write() <= byte stream 으로 작성
//delay() <== msec 단위

int bt_num = 0;
void loop() {
  int bt_read;
  // put your main code here, to run repeatedly:
  if (Serial.available())
  {
    btSerial.write(Serial.read());
  }
  if (btSerial.available())
  {
    //byte bt_read = btSerial.read();
    //Serial.write(bt_read);
    //bt_num = atoi(bt_read);
    bt_read = btSerial.read()-'0';
    Serial.write(bt_read);
      if(bt_read == 0)
      {
      if(digitalRead(Led_green) == HIGH)
      {
          digitalWrite(Led_green,LOW);
          digitalWrite(Led_red,HIGH);
       }
       else if(digitalRead(Led_red) == LOW)
          digitalWrite(Led_red,HIGH);
      }
      else if(bt_read == 1)
      {
        digitalWrite(Led_green,HIGH);
        digitalWrite(Led_red,LOW);
        delay(1000);
        bt_read = '0';
      }
      /*else
      {
        for(int k = 0; k<10;k++)
        {
          digitalWrite(Led_red,LOW);
          delay(1000);
          digitalWrite(Led_red,HIGH);
          }
      }*/
  }
}
