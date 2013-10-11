class @Json
	tablename = ""

	constructor:( @tablename) ->

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

	@getProgrammeHeight:( programme) ->
		timeMin = (programme.stop - programme.start) / (1000 * 60);
		return timeMin * 4

	getLoading:( loading) ->
		if $(@tablename).attr("loading") == "true"
			return true
		return false

	@createListingTableTag:( programme) ->
		height = @getProgrammeHeight( programme)
		tr = $('<tr/>')
		td = $('<td/>')
		small = $('<small/>')
		small.text(@adjustTitle( programme.title, height))

		start = new Date(programme.start)	
		stop = new Date(programme.stop)	
		td.attr({"rel":"popover"});
		td.attr({"data-trigger":"hover"});
		popupTitle = ("0"+start.getHours()).slice(-2) + ":" + ("0"+start.getMinutes()).slice(-2) + "-" + ("0"+stop.getHours()).slice(-2) + ":" + ("0"+stop.getMinutes()).slice(-2)
		td.attr({"data-original-title": popupTitle});
		td.attr({"data-placement":"right"});
		td.attr({"data-html":"true"});
		td.attr({"data-content": "<h5>"+ programme.title + "</h5>" + programme.desc});
		td.attr({"onclick":"return false"});
		td.attr({"class":programme.category});
		td.popover();

		td.css("padding", "0px")
#		td.css("margin", "0px")
#		td.css("border", "0px")

		tr.attr({"id":programme.programmeID});
		tr.css("height", height.toString() + "px")
	
		small.appendTo(td);
		td.appendTo(tr);
		return tr

	createListingTable:( url, start) ->
		table = $(@tablename)
		table.css("width", "140px");
		table.css("float", "left");
		caption = $('<caption/>')
		caption.text('')
		caption.css("height", "0px")
		caption.attr({"loading":"false"});

		table.append(caption)
#		$.getJSON( url, (programmes) => Json.createTrCallBack( programmes, @tablename, start));
		jQuery.ajaxQueue( {url:url, success:(programmes) => Json.createTrCallBack( programmes, @tablename, start)});

	@createTrCallBack:(programmes, tablename, start) ->
		height = (programmes[0].start - start.getTime())/(1000 * 60)
		height = height * 4
		height = $(tablename + ' caption').height() + height
		$(tablename + ' caption').css("height", height.toString() + "px")
		for programme in programmes
			$(tablename).append(@createListingTableTag(programme))

	@appendTrCallBack:(programmes, tablename) ->
		for programme in programmes
			$(tablename).append(@createListingTableTag(programme))

	appendTr:(url) ->
		jQuery.ajaxQueue( {url:url, success:(programmes) => Json.appendTrCallBack( programmes, @tablename)});
#		$.getJSON( url, (programmes) => Json.appendTrCallBack(programmes, @tablename))

	@prependTrCallBack:(programmes, tablename) ->
		i = programmes.length
		while i--
			programme = programmes[i]
			height = $(tablename + ' caption').height() - @getProgrammeHeight( programme)
			$(tablename + ' caption').css("height", height.toString() + "px")
			$(tablename).prepend(@createListingTableTag(programme));
		$(tablename).attr({"loading":false});

	prependTr:( url, height) ->
		$(@tablename).attr({"loading":true});

		height = $(@tablename + ' caption').height() + height
		$(@tablename + ' caption').css("height", height.toString() + "px")

		jQuery.ajaxQueue( {url:url, success:(programmes) => Json.prependTrCallBack( programmes, @tablename)});
#		$.getJSON( url, (programmes) => Json.prependTrCallBack(programmes, @tablename, height))
