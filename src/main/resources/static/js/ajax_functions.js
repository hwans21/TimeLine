function youtube_find(id) {
	var result;
  	$.ajax({
        data: {'youtubeId': id},
        url: '/youtube_find',
        dataType: 'json',
        async: false,
        success: function(data) {
      	    result = data;
        }
  	});
  	return result;
}