import numpy as np
import pandas as pd

from pandas import Series, DataFrame

import scipy
from scipy.stats import spearmanr

from pylab import rcParams
import seaborn as sb
import matplotlib.pyplot as plt

import sklearn
from sklearn.preprocessing import scale
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn import metrics
from sklearn import preprocessing
from sklearn.metrics import classification_report
from sklearn.model_selection import train_test_split

from firebase import firebase
import datetime
from datetime import timedelta
from datetime import date
import time
from time import localtime, strftime




#matplotlib inline
rcParams['figure.figsize'] = 5, 4
sb.set_style('whitegrid')

firebase = firebase.FirebaseApplication('https://smartindia-hackathon-e92f1.firebaseio.com/', None)
result = firebase.get('/Airports/15MinsEntries/', None)
keys = list(result.keys())
k = (list(result[keys[-1]].keys()))
i=1
z=1

while(i<3):
    
        address = 'C:/Users/Pranav/AppData/Local/Programs/Python/Python37/Dummy'+str(z)+ '.csv'

        air = pd.read_csv(address)

        air.columns = ['Airport','Date','Time Interval','Avg_time','Max', 'Passengers' , 'temp', 'Flights']

        #print(air.head())

        new_air = air.drop(columns = ['Airport','Date','Time Interval','Avg_time','temp'])
        air_data = new_air.values
        #print(new_air)



        #air_data_names = ['Flights','Booths']

        y = air.iloc[:,3].values

        #sb.regplot(x='Avg_time' , y='Passengers', data = air , scatter=True)

        #sb.countplot(x='all_avg_wait_time' , data = air, palette = 'hls')

        #plt.show()

        X = scale(air_data)
        X_train, X_test, y_train, y_test= train_test_split(X,y, test_size=0.25, random_state=0)

        LogReg = LogisticRegression(max_iter=500)

        LogReg.fit(X, y)
        #print(LogReg.score(X,y))

        y_pred = LogReg.predict(X)
        #print(classification_report(y,y_pred))
        

        d1 =0
        d2 =0
        d3 =0
        
        result2 = firebase.get('/Airports/15MinsEntries/' + keys[-1] + '/' + k[i], None)
        
        d1 = result2['Max_time']
        d2 = result2['Passengers']
        d3 = result2['Flights']

        #print(d1,d2,d3)

        predict_time = [[10,2,1]]
        print('Predicted time is ', LogReg.predict(predict_time)[0]-1, ' minutes')

        p = LogReg.predict(predict_time)[0]-1

        data = { 'N1' : str(p),
         'N2' : '2'}
        firebase.post('/Airports/Threshold/', data)
        i = i+1

