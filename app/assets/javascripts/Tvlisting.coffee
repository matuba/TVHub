class @Tvlisting
	constructor:( @tablename, @titlename, @channelname, @timerange, @programmeheight) ->

	@adjustTitle:(title, height) ->
		if height >= 240
			return title
		else if height >= 120
			return title.substring(0, 40)
		else if height >= 90
			return title.substring(0, 30)
		else if height >= 60
			return title.substring(0, 20)
		else if height >= 30
			return title.substring(0, 10)
		return ""

	@getProgrammeHeight:( start, stop) ->
		return (( stop - start) / (1000 * 60)) * 4;

	getLoading:( loading) ->
		if $(@tablename).attr("loading") == "true"
			return true
		return false

	@createListingTableSpaceTag:( start, stop) ->
		tr = $('<tr/>')
		tr.attr({"start":start});
		tr.attr({"stop":stop});
		height = @getProgrammeHeight( stop, start)
		tr.css("height", height.toString() + "px")
		return tr

	@createListingTableTag:( programme) ->
		height = @getProgrammeHeight( programme.start, programme.stop)
		tr = $('<tr/>')
		td = $('<td/>')
		small = $('<small/>')
		small.text(@adjustTitle( programme.title, height))

		start = new Date(programme.start)	
		stop = new Date(programme.stop)
		days = ["日", "月", "火", "水", "木", "金", "土"]
		day = days[start.getDay()]
		infoTime = (" "+start.getDate()).slice(-2) + "日(" + day + ")" + ("0"+start.getHours()).slice(-2) + ":" + ("0"+start.getMinutes()).slice(-2) + "-" + ("0"+stop.getHours()).slice(-2) + ":" + ("0"+stop.getMinutes()).slice(-2)

		td.attr({"class":programme.category})
		td.hover ->
			$('#infoTime').html("<font color='lightcyan'>" + infoTime + "</font>")
			$('.marquee').html("<font color='lightcyan'>" + programme.title + "</font>" + programme.desc)
			$('.marquee').marquee({
			speed: 10000,
			gap: 150,
			delayBeforeStart: 900,
			direction: 'left',
			duplicated: true,
			pauseOnHover: true
			});

		td.css("padding", "0px")
		tr.attr({"start":programme.start})
		tr.attr({"stop":programme.stop})
		tr.css("height", height.toString() + "px")
	
		small.appendTo(td)
		td.appendTo(tr)
		return tr

	createListingTable:(createtime) ->
		url = getJsonProgrammesURL(createtime, @timerange, "GR", @channelname)

		table = $(@tablename)
		table.css("width", "140px");
		table.css("float", "left");
		caption = $('<caption/>')
		caption.text('')
		caption.css("height", "0px")
		caption.attr({"loading":"false"});
		table.append(caption)
		jQuery.ajaxQueue( {url:url, success:(programmes) => Tvlisting.createTrCallBack( programmes, @tablename, createtime)});

	@createTrCallBack:(programmes, tablename, start) ->
		table = $(tablename)
		if programmes.length is 0
			table.attr({"loading":false});
			return

		height = (programmes[0].start - start.getTime())/(1000 * 60)
		height = height * 4
		height = $(tablename + ' caption').height() + height
		$(tablename + ' caption').css("height", height.toString() + "px")
		for programme in programmes
			table.append(@createListingTableTag(programme))

	@appendTrCallBack:(programmes, tablename) ->
		table = $(tablename)
		if programmes.length is 0
			table.attr({"loading":false});
			return

		tr = $( tablename + " tr:last")
		stop = parseInt( tr.attr("stop"), 10)
		appendArray= []
		if stop isnt programmes[0].start
			appendArray.push( @createListingTableSpaceTag( programmes[0].start, stop))
		appendArray.push(@createListingTableTag(programmes[0]))
		i = 1
		while i <= programmes.length-1
			if programmes[i-1].stop isnt programmes[i].start
				appendArray.push(@createListingTableSpaceTag( programmes[i].start, programmes[i-1].stop))
			appendArray.push(@createListingTableTag(programmes[i]))
			i++
		table.attr({"loading":false});
		table.append( appendArray)

	appendTr:(appendTime) ->
		url = getJsonProgrammesURL(appendTime, @timerange, "GR", @channelname)
		@startTime = new Date()
		$(@tablename).attr({"loading":true});
		jQuery.ajaxQueue( {url:url, success:(programmes) => Tvlisting.appendTrCallBack( programmes, @tablename)});

	@prependTrCallBack:(programmes, tablename) ->
		table = $(tablename)
		if programmes.length is 0
			table.attr({"loading":false});
			return
		height = 0

		tr = $( tablename + " tr:first")
		start = parseInt( tr.attr("start"), 10)
		programmeLast = programmes[programmes.length-1]

		if programmeLast.stop isnt start
			table.prepend( @createListingTableSpaceTag( start, programmeLast.stop))
			height = height + @getProgrammeHeight( programmeLast.stop, start)
		table.prepend(@createListingTableTag( programmeLast))
		height = height + @getProgrammeHeight( programmeLast.start, programmeLast.stop)

		i = programmes.length - 1
		while i--
			if programmes[i].stop isnt programmes[i+1].start
				table.prepend(@createListingTableSpaceTag( programmes[i+1].start, programmes[i].stop))
				height = height + @getProgrammeHeight( programmes[i].stop, programmes[i+1].start)
			height = height + @getProgrammeHeight( programmes[i].start, programmes[i].stop)
			table.prepend(@createListingTableTag( programmes[i]));
		table.attr({"loading":false});
		height = $(tablename + ' caption').height() - height
		$(tablename + ' caption').css("height", height.toString() + "px")

	prependTr:( prependtime) ->
		url = getJsonProgrammesURL(prependtime, @timerange, "GR", @channelname)
		$(@tablename).attr({"loading":true});
		height = $(@tablename + ' caption').height() + @programmeheight
		$(@tablename + ' caption').css("height", height.toString() + "px")
		jQuery.ajaxQueue( {url:url, success:(programmes) => Tvlisting.prependTrCallBack( programmes, @tablename)});
		
	@getChannelNameCallBack:(programmes, tablename) ->

	getChannelName:() ->
		url = getJsonChannelNameURL(@channelname)
		jQuery.ajaxQueue( {url:url, success:(channelName) => $(@titlename).text(channelName)});
		