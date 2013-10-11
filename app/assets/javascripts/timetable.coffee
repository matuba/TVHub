timeBetween = 4

@createTimeTableTag = ( hour) ->
	tr = $('<tr/>')
	td = $('<td/>')
	small = $('<small/>')
	small.text(("0" + hour).slice(-2))
	td.attr({"class":"timebetween"});
	tr.css("height", "240px")
	small.appendTo(td)
	td.appendTo(tr)
	return tr

@createTimeTable = ( start, tablename) ->
	table = $(tablename)
	table.css("width", "30px")
	table.css("float", "left")
	stop = start + (timeBetween * 60 * 60 * 1000)
	while start < stop
		table.append( @createTimeTableTag( new Date(start).getHours()))
		start = start + (60 * 60 * 1000)

@appendTimeTable = ( tablename) ->
	hour = $(tablename + ' tr:last-child td').text() 
	hour = parseInt(hour, 10)
	startDate = new Date();
	startDate.setHours(hour)
	start = startDate.getTime()
	for i in [0...timeBetween]
		start = start + (60 * 60 * 1000)
		$(tablename).append( @createTimeTableTag( new Date(start).getHours()))

@prependTimeTable = ( tablename) ->
	hour = $(tablename + ' tr:first-child td').text()
	hour = parseInt(hour, 10)
	startDate = new Date();
	startDate.setHours(hour)
	start = startDate.getTime()
	for i in [0...timeBetween]
		start = start - (60 * 60 * 1000)
		$(tablename).prepend( @createTimeTableTag( new Date(start).getHours()))

@dropFirstTimeTable = ( tablename) ->
	for i in [0...timeBetween]
		$(tablename + ' tr:first').remove()

@dropLastTimeTable = ( tablename) ->
	for i in [0...timeBetween]
		$(tablename + ' tr:last').remove()
