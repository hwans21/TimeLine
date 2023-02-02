function youtube_find(id) {
	var result;
  	$.ajax({
        data: {'youtubeId': id},
        url: '/manage/find',
        dataType: 'json',
        async: false,
        success: function(data) {
      	    result = data;
        }
  	});
  	return result;
}

function timeline_insert(time,title){
    console.log(time,title);
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'time':time,
            'title':title
            }),
        url: '/timeline/insert',
        headers: {'Content-Type': 'application/json'},
        async: false,
        success: function(data) {
            console.log(data)
        }
    });
}

function timeline_find(start, time){
    console.log(start,time);
    var result;
    $.ajax({
        type:'post',
        data: JSON.stringify({
            'start':start,
            'time':time
        }),
        url: '/timeline/copylist',
        headers: {'Content-Type': 'application/json'},
        async: false,
        success: function(data) {
            console.log(data)
            result = data;
        }
    });
    return result;
}