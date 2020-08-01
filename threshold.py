from firebase import firebase
import datetime
from datetime import timedelta
from datetime import date
import time
from time import localtime, strftime



firebase = firebase.FirebaseApplication('https://smartindia-hackathon-e92f1.firebaseio.com/', None)



cur_time = datetime.datetime.now()

ref_time = cur_time.replace(hour=0,minute=0,second=0,microsecond=0)

limit_time = ref_time + timedelta(hours=2)





while True:

    result = firebase.get('/Airports/FlightSchedule/', None)
    
    pas = 0
    count = 0
    
    if(ref_time.hour < 23):
        for flight in result:

            fly_time = datetime.datetime.strptime(result[flight]['Departure_Time'], '%H:%M')
            fly_time = fly_time.replace(year=cur_time.year,month=cur_time.month,day=cur_time.day)

            if(fly_time <= ref_time + timedelta(hours=2)  and   fly_time >= ref_time + timedelta(hours=1,minutes=30) ):
                pas = pas + (int(result[flight]['Capacity']))*0.6
                count = count + 1
                #print(fly_time)

            if(fly_time <= ref_time + timedelta(hours=1,minutes=30)  and   fly_time >= ref_time + timedelta(hours=1) ):
                pas = pas + (int(result[flight]['Capacity']))*0.3
                count = count + 1
                #print(fly_time)

            if(fly_time <= ref_time + timedelta(hours=1)  and   fly_time >= ref_time + timedelta(minutes=45) ):
                pas = pas + (int(result[flight]['Capacity']))*0.1
                count = count + 1
                #print(fly_time)

        print('Time Slot :',ref_time)
        #print(count)
        print('Estimated Number of Passengers',pas)
        print("\n")
        
        limit_time = limit_time + timedelta(hours=1)
        ref_time = ref_time + timedelta(hours=1)

        
            
              
