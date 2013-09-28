class @Json
	url = ""

	constructor:(@url) ->

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

	@json2table:(programmes, tablename) ->
		table = $(tablename)

		remainder = (programmes[0].start / (1000 * 60) % 60) * 4
		if remainder > 0
			tr = $('<tr/>')
			tr.css("height", remainder.toString() + "px")
			table.append(tr);
		
		for programme in programmes
			timeMin = (programme.stop - programme.start) / (1000 * 60);
			height = timeMin * 4
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
	
			tr.attr({"id":programme.programmeID});
			tr.css("height", height.toString() + "px")
	
			small.appendTo(td);
			td.appendTo(tr);
			table.append(tr);

	setTable:(tablename) ->
		table = $(tablename)
		table.css("width", "140px");
		table.css("float", "left");
		$.getJSON( @url, (data) -> Json.json2table(data, tablename));

	@appendTrCallBack:(programmes, tablename) ->
		table = $(tablename)
		for programme in programmes
			timeMin = (programme.stop - programme.start) / (1000 * 60);
			height = timeMin * 4
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
	
			tr.attr({"id":programme.programmeID});
			tr.css("height", height.toString() + "px")
	
			small.appendTo(td);
			td.appendTo(tr);
			table.append(tr);

	appendTr:(tablename) -> $.getJSON( @url, (data) -> Json.appendTrCallBack(data, tablename))

	@prependTrCallBack:(programmes, tablename) ->
		table = $(tablename)
		maxHeight = 0
		for programme, i in programmes by -1
			timeMin = (programme.stop - programme.start) / (1000 * 60);
			height = timeMin * 4
			if maxHeight < height
				maxHeight = height
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

			tr.attr({"id":programme.programmeID});
			tr.css("height", height.toString() + "px")

			small.appendTo(td);
			td.appendTo(tr);
			table.prepend(tr);
			
		scrollBy(maxHeight);

	prependTr:(tablename) -> $.getJSON( @url, (data) -> Json.prependTrCallBack(data, tablename))
					