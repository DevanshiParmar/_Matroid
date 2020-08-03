from firebase import firebase
import datetime
from datetime import timedelta
from datetime import date
import time
from time import localtime, strftime


def convert(t_seconds, count):
    if(count>0):
        avg_sec = t_seconds/count
        avg_min = avg_sec/60
    else:
        avg_sec=0
        avg_min=0

    return int(avg_sec) , int(avg_min)





while True :
    start_time = time.time()
    firebase = firebase.FirebaseApplication('https://smartindia-hackathon-e92f1.firebaseio.com/', None)
    cur_time = datetime.datetime.now()
    all_data = firebase.get('/Ahmedabad/', None)
    counter_type = list(all_data.keys())

    formatted_date=cur_time.strftime("%m-%d_%H")

        
    for counter_type_i in counter_type:
        print('counter_type_i: '+counter_type_i)

        for counter_no in all_data[counter_type_i].keys():

            print('counter_no: '+counter_no)
            
            for hour_key_i in all_data[counter_type_i][counter_no]['Entries'].keys():
                print('hour_key_i: '+ hour_key_i)
                #print(all_data[counter_type_i][counter_no]['Entries'][hour_key_i])

                if(hour_key_i[:8]==formatted_date):
                    t_seconds = 0
                    count = 0
    
                    for key_i in all_data[counter_type_i][counter_no]['Entries'][hour_key_i].keys():
                        
                        #print('entry time: '+all_data[counter_type_i][counter_no]['Entries'][hour_key_i][key_i]['entry_time'])
                        #print('exit time: '+all_data[counter_type_i][counter_no]['Entries'][hour_key_i][key_i]['exit_time'])


                        entry_t=all_data[counter_type_i][counter_no]['Entries'][hour_key_i][key_i]['entry_time'][11:]
                        exit_t=all_data[counter_type_i][counter_no]['Entries'][hour_key_i][key_i]['exit_time'][11:]
                 
                        dwell_time = datetime.datetime.strptime(exit_t, '%H:%M:%S')- datetime.datetime.strptime(entry_t, '%H:%M:%S')
                        seconds = (dwell_time.total_seconds())
                        print(seconds)
                        t_seconds = t_seconds + seconds
                        count = count + 1
                    avg_sec , avg_min = convert(t_seconds , count)
                    print(avg_sec , count)
                
                    post_data ={ 'avg_dwell' : str(avg_sec) , 'footfall':str(count) ,
                                 'hour':  formatted_date}
                    print(post_data)
                    firebase.post('/Ahmedabad/'+counter_type_i+'/'+counter_no+'/Hourly/'+formatted_date,post_data)


                    #time.sleep(3600)  #to made one hour
    print("--- %s seconds ---" % (time.time() - start_time))
    time.sleep(3600)
                    


                    

##        #result = firebase.get('/Ahmedabad/Boarding_gate/' + all_counter_Type + '/Entries/', None)
##        #print(result['exit_time'])
##        #print(result['entry_time'])
##        #print(str(dwell_time)[2:4])
##        keyId = list(result.keys())
##        #print(result[keyId[-1]])
##        k = (list(result[keyId[-1]].keys()))
##        print(k)
##
##
##        result_2 = firebase.get('/Ahmedabad/Boarding_gate/'+all_counter_Type+'/Entries/'+formatted_date , None)
##        #print(id)
##        t_seconds = 0
##        count = 0
##        
##        try:
##            
##            t_seconds = 0
##            count = 0
##
##            for keyID in result_2:
##                
##                dwell_time = datetime.datetime.strptime\
##                             (result_2[keyID]['exit_time'][11:], '%H:%M:%S')\
##                             - datetime.datetime.strptime(result_2[keyID]['entry_time']\
##                                                          [11:], '%H:%M:%S')
##                seconds = (dwell_time.total_seconds())
##                print(seconds)
##                t_seconds = t_seconds + seconds
##                count = count + 1
##                #print(count)
##
##        except:
##            pass
##
##        avg_sec , avg_min = convert(t_seconds , count)
##        print(avg_sec , count)
##        time.sleep(3600)  #to made one hour
##
##        post_data ={ 'avg_dwell' : str(avg_sec) , 'footfall':str(count) ,
##                     'hour':  formatted_date}
##        print(post_data)
##        #firebase.post('/Ahmedabad/Boarding_gate/'+all_counter_Type+'/Hourly/'+formatted_date,post_data)
##        
##                    
##                    
##                
##
##        
##        
##
