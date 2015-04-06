#!/usr/bin/python

import informixdb

# connect to the informix database
conn = informixdb.connect('iotdata')
curr = conn.cursor()

# create a timestamp string
dt = datetime.datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S.%f")
data = "Hello, World!"

# create our informix database insert statement
cmd = "insert into test_table values(%s, %s)" % (dt[:-1], data)

# perform the insert
curr.execute(cmd)
curr.execute("commit")
curr.execute("begin work")