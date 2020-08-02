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

address = 'C:/Users/Pranav/AppData/Local/Programs/Python/Python37/Book1.csv'

air = pd.read_csv(address)

air.columns = ['Airport','Terminal','Date','Hour','US_avg_wait_time','US_max_wait_time','other_avg_wait_time','other_max_wait_time','all_avg_wait_time','all_max_wait_time','0-15','16-30','31-45','46-60','61-90','91-120','120 plus','Excluded' , 'Total' , 'Flights', 'Booths']

#print(air.head())

new_air = air.drop(columns = ['Airport','Terminal','Date','Hour','all_avg_wait_time','all_max_wait_time'])
air_data = new_air.values
#print(new_air)
air_data_names = ['Flights','Booths']

y = air.iloc[:,8].values

sb.regplot(x='Flights' , y='Booths', data = air , scatter=True)

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
d4 =0
d5 =0
d6 =0
d7 =0
d8 =0
d9 =0
d10 =0
d11 =0
d12 =0
d13 =0
d14 =0
d15 =0

firebase = firebase.FirebaseApplication('https://smartindia-hackathon-e92f1.firebaseio.com/', None)

result = firebase.get('/Airports/Hourly_Entries/', None)



d1 = result['0-15']
d2 = result['120 plus']
d3 = result['16-30']
d4 = result['31-45']
d5 = result['46-60']
d6 = result['61-90']
d7 = result['91-120']
d8 = result['Booths']
d9 = result['Excluded']
d10 = result['Flights']
d11 = result['Total']
d12 = result['US_avg_wait_time']
d13 = result['US_max_wait_time']
d14 = result['other_avg_wait_time']
d15 = result['other_max_wait_time']



predict_time = [[d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,d11,d12,d13,d14,d15]]
print('Predicted time is ', LogReg.predict(predict_time)[0]-1, ' minutes')














