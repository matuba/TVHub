class @InformationBar
	constructor: ->
		information = $('#information')
		information.hover ->
			$('#information_on').css("display", "")
			$('#information_off').css("display", "none")
		,->
			$('#information_on').css("display", "none")
			$('#information_off').css("display", "")
			
