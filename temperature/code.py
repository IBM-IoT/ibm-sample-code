#!/usr/bin/python

import Adafruit_BBIO.ADC as ADC
import datetime
import time
import informixdb
import sys

# the name of our temperature sensor
sensor_pin='P9_40'

ADC.setup()

# connect to the informix database
conn = informixdb.connect('iotdata')
curr = conn.cursor()

while True:
	
	# read in temperature data from the sensor
	reading = ADC.read(sensor_pin)
	
	# convert temperature reading to standardized units
	millivolts = reading * 1800
	temp_c = (millivolts - 500) / 10
	temp_f = (temp_c * 9/5) + 32
	
	# create a timestamp string
	dt = datetime.datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S.%f")
	pdt = dt[:-1]
	
	# show what data will be stored, and when it happened
	print ('%s|%f|%f|%f (Sending Message)' % (dt[:-1],millivolts, temp_c, temp_f))
	
	# create our informix database insert statement
	cmd = "insert into sensors_vti values(1,'%s',('{temp_f:%f}'::json)::bson)" % (dt[:-1],temp_f)
	
	# show what statement will be executed on the informix database
	print "CMD = %s " %  (cmd)
	sys.stdout.flush()
	
	# perform the insert
	curr.execute(cmd)
	curr.execute("commit")
	curr.execute("begin work")

	time.sleep(.95)
