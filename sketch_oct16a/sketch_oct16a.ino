#define SENSOR 4
#define GREAN 7
#define RED 6
void setup() {
  // put your setup code here, to run once:

  pinMode(GREAN,OUTPUT);
  pinMode(RED,OUTPUT);
  pinMode(SENSOR,INPUT);
}//적외선은 pinMode를 통해서 작업한다.
//value 값은 없을때 HIGH 있을때 Low
int val;
void loop() {
  // put your main code here, to run repeatedly:
  val = digitalRead(SENSOR);
  if(val == HIGH){
    digitalWrite(GREAN,HIGH);
  }
  else if(val == LOW)
  {
    digitalWrite(GREAN,LOW);
    digitalWrite(RED,HIGH);
    delay(1000);
    digitalWrite(RED,LOW);
   }
}
